package yeonba.be.arrow.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import yeonba.be.arrow.service.ArrowService;
import yeonba.be.support.ControllerTestSupport;

@WebMvcTest(ArrowController.class)
class ArrowControllerTest extends ControllerTestSupport {

    @Autowired
    private ArrowController arrowController;

    @MockBean
    private ArrowService arrowService;

    @Override
    protected Object controller() {

        return arrowController;
    }

    @Nested
    @DisplayName("사용자 고유 번호를 요청해 ")
    class dailyCheckTest {

        private long userId = 1L;

        @DisplayName("출석 체크를 할 수 있다.")
        @Test
        void dailCheck() throws Exception {

            // given
            given(arrowService.dailyCheck(eq(userId), any(LocalDateTime.class))).willReturn(true);

            // when & then
            mockMvc.perform(post("/daily-check")
                    .requestAttr("userId", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.data").isEmpty());
        }

        @DisplayName("이미 출석 체크한 경우 출석 체크할 수 없다.")
        @Test
        void alreadyDailyChecked() throws Exception {

            // given
            given(arrowService.dailyCheck(eq(userId), any(LocalDateTime.class))).willReturn(false);

            // when & then
            mockMvc.perform(post("/daily-check")
                    .requestAttr("userId", userId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("이미 출석 체크한 사용자입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
        }
    }
}
