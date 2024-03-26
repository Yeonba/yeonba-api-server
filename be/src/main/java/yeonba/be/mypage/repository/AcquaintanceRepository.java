package yeonba.be.mypage.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yeonba.be.mypage.entity.Acquaintance;

@Repository
public interface AcquaintanceRepository extends JpaRepository<Acquaintance, Long> {

    List<Acquaintance> findAllById(long userId);
}
