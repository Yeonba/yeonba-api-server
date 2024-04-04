package yeonba.be.user.repository.profilephoto;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yeonba.be.user.entity.ProfilePhoto;

@Component
@RequiredArgsConstructor
public class ProfilePhotoCommand {

    private final ProfilePhotoRepository profilePhotoRepository;

    public List<ProfilePhoto> save(List<ProfilePhoto> profilePhotos) {

        return profilePhotoRepository.saveAll(profilePhotos);
    }
}
