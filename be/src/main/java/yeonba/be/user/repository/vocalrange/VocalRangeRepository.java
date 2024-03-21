package yeonba.be.user.repository.vocalrange;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yeonba.be.user.entity.VocalRange;

@Repository
public interface VocalRangeRepository extends JpaRepository<VocalRange, Long> {

  Optional<VocalRange> findByClassification(String classification);
}
