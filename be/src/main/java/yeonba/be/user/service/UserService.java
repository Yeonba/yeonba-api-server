package yeonba.be.user.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yeonba.be.arrow.repository.ArrowQuery;
import yeonba.be.user.dto.response.UserProfileResponse;
import yeonba.be.user.entity.ProfilePhoto;
import yeonba.be.user.entity.User;
import yeonba.be.user.repository.UserQuery;

@Service
@RequiredArgsConstructor
public class UserService {


    private final ArrowQuery arrowQuery;
    private final UserQuery userQuery;


    @Transactional(readOnly = true)
    public UserProfileResponse getTargetUserProfile(long userId, long targetUserId) {

        User user = userQuery.findById(userId);
        User targetUser = userQuery.findById(targetUserId);

        List<String> profilePhotosUrls = targetUser
            .getProfilePhotos().stream()
            .map(ProfilePhoto::getPhotoUrl)
            .toList();

        boolean isAlreadySentArrow = arrowQuery.isArrowTransactionExist(user, targetUser);

        return new UserProfileResponse(
            profilePhotosUrls,
            targetUser.getNickname(),
            targetUser.getArrow(),
            targetUser.getAge(),
            targetUser.getHeight(),
            targetUser.getArea().getName(),
            targetUser.getPhotoSyncRate(),
            targetUser.getDrinkingHabit(),
            targetUser.getSmokingHabit(),
            targetUser.getVocalRange().getClassification(),
            targetUser.getAnimal().getName(),
            isAlreadySentArrow);
    }
}
