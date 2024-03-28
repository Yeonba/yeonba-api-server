package yeonba.be.user.repository.vocalrange;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.JoinException;
import yeonba.be.user.entity.VocalRange;

@Component
@RequiredArgsConstructor
public class VocalRangeQuery {

    private final VocalRangeRepository vocalRangeRepository;

    public VocalRange findBy(String classification) {

        return vocalRangeRepository.findByClassification(classification)
            .orElseThrow(() -> new GeneralException(JoinException.VOCAL_RANGE_NOT_FOUND));
    }
}
