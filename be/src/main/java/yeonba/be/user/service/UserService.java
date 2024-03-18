package yeonba.be.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yeonba.be.arrow.repository.ArrowQuery;
import yeonba.be.user.dto.response.UserProfileResponse;
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

        boolean isAlreadySentArrow = arrowQuery.isArrowTransactionExist(user, targetUser);

        return new UserProfileResponse(
            targetUser.getProfilePhotoUrls(),
            targetUser.getGender(),
            targetUser.getNickname(),
            targetUser.getArrow(),
            targetUser.getAge(),
            targetUser.getHeight(),
            targetUser.getArea().getName(),
            targetUser.getPhotoSyncRate(),
            targetUser.getVocalRange().getClassification(),
            targetUser.getAnimal().getName(),
            isAlreadySentArrow);
    }
}
