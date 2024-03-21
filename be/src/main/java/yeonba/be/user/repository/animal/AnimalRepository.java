package yeonba.be.user.repository.animal;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yeonba.be.user.entity.Animal;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

  Optional<Animal> findByName(String name);
}
