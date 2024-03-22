package yeonba.be.util;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import yeonba.be.user.entity.User;

@Service
@RequiredArgsConstructor
public class S3Service {

	private final String ALLOWED_PROFILE_PHOTO_EXTENSION_REGEX = "^(.+)\\.(jpg|jpeg|png)$";

	private final S3Client s3Client;

	@Value("${S3_BUCKET_NAME}")
	private String bucketName;

	/*
		프로필 사진 업로드는 다음 과정을 거쳐 이뤄진다.
		1. 사진 파일들 확장자 검증
		2. 파일 식별을 위한 키 생성, profilephoto/{userId}-{photo idx} 형식
		3. 사진 파일 업로드 요청 생성 및 업로드 수행
	 */
	public List<String> uploadProfilePhotos(List<MultipartFile> profilePhotos, User user) {

		if (!validateProfilePhotosExtensions(profilePhotos)) {
			throw new IllegalArgumentException("jpg, jpeg, png 확장자 형식의 파일만 허용됩니다.");
		}

		List<String> uploadedProfilePhotosUrls = new ArrayList<>();
		long userId = user.getId();

		for (int profilePhotoIdx = 0; profilePhotoIdx < profilePhotos.size(); profilePhotoIdx++) {

			MultipartFile profilePhoto = profilePhotos.get(profilePhotoIdx);
			String key = String.format("profilephoto/%d-%d", userId, profilePhotoIdx);

			PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(bucketName)
				.key(key)
				.contentDisposition("inline")
				.contentType(profilePhoto.getContentType())
				.build();

			try {
				s3Client.putObject(putObjectRequest,
					RequestBody.fromInputStream(profilePhoto.getInputStream(),
						profilePhoto.getSize()));
				uploadedProfilePhotosUrls.add(key);
			} catch (Exception e) {
				throw new IllegalStateException(
					String.format("Failed to upload file : %s", profilePhoto.getOriginalFilename())
					, e);
			}
		}

		return uploadedProfilePhotosUrls;
	}

	private boolean validateProfilePhotosExtensions(List<MultipartFile> profilePhotos) {

		return profilePhotos.stream()
			.allMatch(profilePhoto -> profilePhoto.getOriginalFilename()
				.matches(ALLOWED_PROFILE_PHOTO_EXTENSION_REGEX));
	}
}
