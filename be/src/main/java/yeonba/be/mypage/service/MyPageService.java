package yeonba.be.mypage.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import yeonba.be.mypage.dto.request.UserChangePasswordRequest;
import yeonba.be.mypage.dto.request.UserDormantRequest;
import yeonba.be.mypage.dto.request.UserUpdateProfileRequest;
import yeonba.be.mypage.dto.response.BlockedUserResponse;
import yeonba.be.mypage.dto.response.BlockedUsersResponse;
import yeonba.be.mypage.dto.response.UserProfileDetailResponse;
import yeonba.be.mypage.dto.response.UserSimpleProfileResponse;
import yeonba.be.mypage.util.PasswordEncryptor;
import yeonba.be.user.entity.Block;
import yeonba.be.user.entity.User;
import yeonba.be.user.repository.BlockQuery;
import yeonba.be.user.repository.UserQuery;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final S3Client s3Client;
    private final UserQuery userQuery;
    private final BlockQuery blockQuery;
    private final PasswordEncryptor passwordEncryptor;

    @Value("${S3_BUCKET_NAME}")
    private String bucketName;

    @Transactional(readOnly = true)
    public UserSimpleProfileResponse getSimpleProfile(long userId) {

        User user = userQuery.findById(userId);

        return new UserSimpleProfileResponse(
            user.getName(),
            user.getRepresentativeProfilePhoto(),
            user.getArrow()
        );
    }

    @Transactional(readOnly = true)
    public UserProfileDetailResponse getProfileDetail(long userId) {

        User user = userQuery.findById(userId);

        return new UserProfileDetailResponse(user);
    }

    @Transactional
    public void updateProfile(UserUpdateProfileRequest request, long userId) {

        User validatedUser = userQuery.findById(userId);

        // TODO: 선호 조건 테이블 생성 후 로직 추가

        // validatedUser.updateProfile(request);
    }

    @Transactional
    public void changePassword(UserChangePasswordRequest request, long userId) {

        User user = userQuery.findById(userId);

        String encryptedOldPassword = passwordEncryptor
            .encrypt(request.getOldPassword(), user.getSalt());

        comparePasswords(request, user, encryptedOldPassword);

        String encryptedNewPassword = passwordEncryptor
            .encrypt(request.getNewPassword(), user.getSalt());

        user.changePassword(encryptedNewPassword);
    }

    public void updateProfilePhotos(List<MultipartFile> profilePhotos, MultipartFile realTimePhoto,
        long userId) {

        User user = userQuery.findById(userId);

        // TODO: AI server 연동 후 얼굴 인식 로직 추가
        // TODO: 사용자마다 정해전 경로에 파일을 업로드 하기 때문에 회원 가입 시 파일을 저장할 경로를 만들어야 함.
        uploadProfilePhotos(profilePhotos, user);
    }

    public List<BlockedUserResponse> getBlockedUsers(long userId) {

        User user = userQuery.findById(userId);

        List<Block> blocksByUser = blockQuery.findBlocksByUser(user);

        return blocksByUser.stream()
            .map(block -> new BlockedUserResponse(
                block.getBlockedUser())
            )
            .toList();
    }

    @Transactional
    public void changeDormantStatus(long userId, UserDormantRequest request) {

        User user = userQuery.findById(userId);
        user.changeInactiveStatus(request.isStatus());
    }

    /**
     * 사용자마다 정해진 profile photo url에 파일을 업로드한다.
     */
    private void uploadProfilePhotos(List<MultipartFile> profilePhotos, User user) {

        List<String> fileNames = user.getProfilePhotoUrls();

        // TODO: 회의 후 확장자 제한 로직 추가, 확장자 검증 후 업로드 시작
        // validateFileExtension(profilePhoto);

        for (int profilePhotoIdx = 0; profilePhotoIdx < profilePhotos.size(); profilePhotoIdx++) {

            MultipartFile profilePhoto = profilePhotos.get(profilePhotoIdx);

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileNames.get(profilePhotoIdx))
                .contentDisposition("inline")
                .contentType(profilePhoto.getContentType())
                .build();

            try {
                s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(profilePhoto.getInputStream(),
                        profilePhoto.getSize()));

            } catch (Exception e) {
                throw new IllegalStateException(
                    "Failed to upload file: " + profilePhoto.getOriginalFilename(), e);
            }
        }
    }

    /**
     * 기존 비밀번호가 올바른지 검증 새 비밀번호와 새 비밀번호 확인 값이 일치하는지 검증
     */
    private void comparePasswords(UserChangePasswordRequest request,
        User user,
        String encryptedOldPassword) {

        if (!user.getEncryptedPassword().equalsIgnoreCase(encryptedOldPassword)) {
            throw new IllegalArgumentException("기존 비밀번호가 틀렸습니다.");
        }

        if (!StringUtils.equals(request.getNewPassword(), request.getNewPasswordConfirmation())) {
            throw new IllegalArgumentException("새 비밀번호와 새 비밀번호 확인 값이 일치하지 않습니다.");
        }
    }
}
