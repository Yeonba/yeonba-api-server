package yeonba.be.arrow.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import yeonba.be.arrow.entity.ArrowTransaction;
import yeonba.be.arrow.repository.ArrowCommand;
import yeonba.be.arrow.repository.ArrowQuery;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.UserException;
import yeonba.be.fixture.UserFixtureFactory;
import yeonba.be.user.entity.User;
import yeonba.be.user.repository.user.UserQuery;

@ExtendWith(MockitoExtension.class)
class ArrowServiceTest {

    @Mock
    private ArrowQuery arrowQuery;

    @Mock
    private UserQuery userQuery;

    @Mock
    private ArrowCommand arrowCommand;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private ArrowService arrowService;

    @Nested
    @DisplayName("사용자 고유 번호, 출석체크 일시를 입력받아 ")
    class dailyCheckTest {

        private long userId = 1L;
        private User dailyCheckUser;
        private LocalDateTime dailyCheckAt;

        @BeforeEach
        void setUp() {
            dailyCheckUser = UserFixtureFactory.builder().build().getFixture();
            dailyCheckAt = LocalDateTime.now();
        }

        @DisplayName("출석체크를 통해 사용자에게 화살을 10개 지급한다.")
        @Test
        void success() {

            // given
            int userArrow = dailyCheckUser.getArrow();
            given(userQuery.findById(userId)).willReturn(dailyCheckUser);

            // when
            boolean result = arrowService.dailyCheck(userId, dailyCheckAt);

            // then
            assertAll(
                () -> assertThat(result).isTrue(),
                () -> then(arrowCommand).should(times(1)).save(any(ArrowTransaction.class)),
                () -> assertThat(dailyCheckUser.getArrow()).isEqualTo(userArrow + 10),
                () -> assertThat(dailyCheckUser.getLastAccessedAt()).isEqualTo(dailyCheckAt)
            );
        }

        @DisplayName("이미 출석 체크한 사용자일 경우 출석 체크하지 않는다.")
        @Test
        void alreadyDailyChecked() {

            // given
            int userArrow = dailyCheckUser.getArrow();
            LocalDateTime lastAccessedAt = dailyCheckAt.plusMinutes(10);
            dailyCheckUser.updateLastAccessedAt(lastAccessedAt);
            given(userQuery.findById(userId)).willReturn(dailyCheckUser);

            // when
            boolean result = arrowService.dailyCheck(userId, dailyCheckAt);

            // then
            assertAll(
                () -> assertThat(result).isFalse(),
                () -> assertThat(dailyCheckUser.getArrow()).isEqualTo(userArrow),
                () -> then(userQuery).should(times(1)).findById(userId),
                () -> then(arrowCommand).should(never()).save(any(ArrowTransaction.class)),
                () -> assertThat(dailyCheckUser.getLastAccessedAt()).isEqualTo(dailyCheckAt)
            );
        }

        @DisplayName("출석체크하려는 사용자 고유번호가 존재하지 않을 경우 예외를 발생시킨다.")
        @Test
        void nonExistUser() {

            // given
            long nonExistUserId = 2L;
            given(userQuery.findById(nonExistUserId))
                .willThrow(new GeneralException(UserException.USER_NOT_FOUND));

            // when & then
            assertAll(
                () -> assertThatThrownBy(
                    () -> arrowService.dailyCheck(nonExistUserId, dailyCheckAt))
                    .isInstanceOf(GeneralException.class)
                    .extracting("exception")
                    .isEqualTo(UserException.USER_NOT_FOUND),
                () -> then(userQuery).should(times(1)).findById(nonExistUserId)
            );
        }

        @DisplayName("휴면 상태 사용자일 경우 예외를 발생시킨다.")
        @Test
        void inactiveUser() {

            // given
            dailyCheckUser.changeInactiveStatus(true);
            given(userQuery.findById(userId)).willReturn(dailyCheckUser);

            // when & then
            assertAll(
                () -> assertThatThrownBy(() -> arrowService.dailyCheck(userId, dailyCheckAt))
                    .isInstanceOf(GeneralException.class)
                    .extracting("exception")
                    .isEqualTo(UserException.INACTIVE_USER),
                () -> then(userQuery).should(times(1)).findById(userId)
            );
        }
    }
}
