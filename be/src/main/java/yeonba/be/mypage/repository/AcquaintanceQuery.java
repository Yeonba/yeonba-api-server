package yeonba.be.mypage.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.mypage.entity.Acquaintance;

@Component
@RequiredArgsConstructor
public class AcquaintanceQuery {

    private final AcquaintanceRepository acquaintanceRepository;

    public List<Acquaintance> findAllById(long userId) {

        return acquaintanceRepository.findAllById(userId);
    }
}
