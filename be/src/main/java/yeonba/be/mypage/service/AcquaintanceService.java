package yeonba.be.mypage.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yeonba.be.mypage.dto.request.UserUpdateUnwantedAcquaintancesRequest;
import yeonba.be.mypage.entity.Acquaintance;
import yeonba.be.mypage.repository.AcquaintanceCommand;
import yeonba.be.user.repository.UserQuery;

@Service
@RequiredArgsConstructor
public class AcquaintanceService {

    private final UserQuery userQuery;
    private final AcquaintanceCommand acquaintanceCommand;

    @Transactional
    public void updateOrSaveUnwantedAcquaintances(long userId, UserUpdateUnwantedAcquaintancesRequest request) {

        // 요청한 사용자가 유효한 사용자인지 검증
        userQuery.findById(userId);

        // 기존 만나고 싶지 않은 지인 목록 삭제
        acquaintanceCommand.deleteAllByUserId(userId);

        // 새로운 만나고 싶지 않은 지인 목록 저장
        List<Acquaintance> acquaintances = request.getAcquaintances()
            .stream()
            .map(acquaintance -> new Acquaintance(userId, acquaintance.getName(),
                acquaintance.getPhoneNumber()))
            .distinct()
            .collect(Collectors.toList());

        acquaintanceCommand.saveAll(acquaintances);
    }
}
