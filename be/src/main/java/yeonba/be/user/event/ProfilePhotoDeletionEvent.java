package yeonba.be.user.event;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import yeonba.be.user.entity.ProfilePhoto;

@Getter
@RequiredArgsConstructor
public class ProfilePhotoDeletionEvent {

    private final List<ProfilePhoto> profilePhotos;
}