package yeonba.be.mypage.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import yeonba.be.exception.AcquaintanceException;
import yeonba.be.exception.GeneralException;
import yeonba.be.mypage.entity.Acquaintance;

@Component
@RequiredArgsConstructor
public class AcquaintanceCommand {

    private final AcquaintanceRepository acquaintanceRepository;

    public void saveAll(List<Acquaintance> acquaintances) {

        try {
            acquaintanceRepository.saveAll(acquaintances);
        } catch (DataIntegrityViolationException e) {
            throw new GeneralException(AcquaintanceException.ALREADY_EXIST_PHONENUMBER);
        }
    }

    public void deleteAllByUserId(long userId) {

        acquaintanceRepository.deleteAllByUserId(userId);
    }
}
