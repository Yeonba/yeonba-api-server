package yeonba.be.mypage.service;

import java.util.ArrayList;
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
import yeonba.be.mypage.dto.request.UserUpdateProfileRequest;
import yeonba.be.mypage.dto.response.UserProfileDetailResponse;
import yeonba.be.mypage.dto.response.UserSimpleProfileResponse;
import yeonba.be.user.entity.User;
import yeonba.be.user.repository.UserQuery;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final S3Client s3Client;
    private final UserQuery userQuery;

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

        // TODO: 비밀번호 암호화 후 비교
        String encryptedOldPassword = request.getOldPassword();

        if (!StringUtils.equals(user.getEncryptedPassword(), encryptedOldPassword)) {
            throw new IllegalArgumentException("기존 비밀번호가 틀렸습니다.");
        }

        if (!StringUtils.equals(request.getNewPassword(), request.getNewPasswordConfirmation())) {
            throw new IllegalArgumentException("새 비밀번호와 새 비밀번호 확인 값이 일치하지 않습니다.");
        }

        // TODO: 암호화 후 비밀번호 변경
        String encryptedNewPassword = request.getNewPassword();

        user.changePassword(encryptedNewPassword);
    }

    public void updateProfilePhotos(List<MultipartFile> profilePhotos, long userId) {

        User user = userQuery.findById(userId);

        // TODO: 같은 사용자에 대해서는 같은 경로에 항상 저장되도록 할 것인지 회의 후 결정
        List<String> profilePhotoUrls = uploadProfilePhotos(profilePhotos);
    }

    private List<String> uploadProfilePhotos(List<MultipartFile> profilePhotos) {

        List<String> fileNames = new ArrayList<>();

        // TODO: 회의 후 확장자 제한 로직 추가, 확장자 검증 후 업로드 시작
        // validateFileExtension(profilePhoto);

        for (MultipartFile profilePhoto : profilePhotos) {

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(profilePhoto.getOriginalFilename())
                .contentDisposition("inline")
                .contentType(profilePhoto.getContentType())
                .build();

            try {
                s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(profilePhoto.getInputStream(),
                        profilePhoto.getSize()));

                fileNames.add(profilePhoto.getOriginalFilename());
            } catch (Exception e) {
                throw new IllegalStateException(
                    "Failed to upload file: " + profilePhoto.getOriginalFilename(), e);
            }
        }

        return fileNames;
    }
}
