package yeonba.be.user.repository.animal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.exception.GeneralException;
import yeonba.be.exception.LoginException;
import yeonba.be.user.entity.Animal;

@Component
@RequiredArgsConstructor
public class AnimalQuery {

  private final AnimalRepository animalRepository;

  public Animal findByName(String name) {

    return animalRepository.findByName(name)
        .orElseThrow(() -> new GeneralException(LoginException.ANIMAL_NOT_FOUND));
  }
}
