package yeonba.be.mypage.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.mypage.entity.Acquaintance;

@Component
@RequiredArgsConstructor
public class AcquaintanceCommand {

    private final AcquaintanceRepository acquaintanceRepository;

    public void saveAll(List<Acquaintance> acquaintances) {

        acquaintanceRepository.saveAll(acquaintances);
    }

    public void deleteAllByUserId(long userId) {

        acquaintanceRepository.deleteAllByUserId(userId);
    }
}
