package yeonba.be.user.repository.animal;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.UserException;
import yeonba.be.user.entity.Animal;

@Component
@RequiredArgsConstructor
public class AnimalQuery {

    private final AnimalRepository animalRepository;

    public Animal findByName(String name) {

        return animalRepository.findByName(name)
            .orElseThrow(() -> new GeneralException(UserException.ANIMAL_NOT_FOUND));
    }

    public List<Animal> findAll() {

        return animalRepository.findAll();
    }
}
