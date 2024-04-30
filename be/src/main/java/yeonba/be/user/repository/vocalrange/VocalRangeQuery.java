package yeonba.be.user.repository.vocalrange;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.UserException;
import yeonba.be.user.entity.VocalRange;

@Component
@RequiredArgsConstructor
public class VocalRangeQuery {

    private final VocalRangeRepository vocalRangeRepository;

    public VocalRange findByClassification(String classification) {

        return vocalRangeRepository.findByClassification(classification)
            .orElseThrow(() -> new GeneralException(UserException.VOCAL_RANGE_NOT_FOUND));
    }

    public List<VocalRange> findAll() {

        return vocalRangeRepository.findAll();
    }
}
