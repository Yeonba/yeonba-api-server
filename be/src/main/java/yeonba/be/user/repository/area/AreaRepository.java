package yeonba.be.user.repository.area;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yeonba.be.user.entity.Area;

@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {

  Optional<Area> findByName(String name);
}
