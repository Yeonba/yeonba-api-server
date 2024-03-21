package yeonba.be.user.repository.profilephoto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.user.entity.ProfilePhoto;

@Component
@RequiredArgsConstructor
public class ProfilePhotoCommand {

  private final ProfilePhotoRepository profilePhotoRepository;

  public ProfilePhoto save(ProfilePhoto profilePhoto) {

    return profilePhotoRepository.save(profilePhoto);
  }
}
