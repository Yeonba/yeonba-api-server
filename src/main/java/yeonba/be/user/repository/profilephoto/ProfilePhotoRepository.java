package yeonba.be.user.repository.profilephoto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yeonba.be.user.entity.ProfilePhoto;

@Repository
public interface ProfilePhotoRepository extends JpaRepository<ProfilePhoto, Long> {

}
