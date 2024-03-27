package yeonba.be.user.service;

import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yeonba.be.login.dto.request.UserJoinRequest;
import yeonba.be.login.dto.response.UserJoinResponse;
import yeonba.be.user.entity.User;
import yeonba.be.user.event.ProfilePhotoDeletionEvent;
import yeonba.be.util.JwtUtil;

@Service
@RequiredArgsConstructor
public class JoinService {

	private final ApplicationEventPublisher eventPublisher;
	private final UserService userService;

	private final JwtUtil jwtUtil;

	/*
	회원 가입 비즈니스 로직 과정
	1. 사용자 엔티티 저장
	2. 프로필 사진 엔티티 저장
	3. 사용자 선호조건 저장
	4. access token 및 refresh token 발급
	5. 사용자 refresh token 업데이트
 	*/
	@Transactional
	public UserJoinResponse join(UserJoinRequest request) {
		User user = userService.saveUser(request);
		userService.saveProfilePhotos(user, request);

		// 트랜잭션 롤백시 업로드된 프로필 사진 삭제를 위해 이벤트 발행
		ProfilePhotoDeletionEvent event = new ProfilePhotoDeletionEvent(
			user.getProfilePhotos());
		eventPublisher.publishEvent(event);

		userService.saveUserPreference(user, request);

		// access token, refresh token 발급
		Date issuedAt = new Date();
		String accessToken = jwtUtil.generateAccessToken(user, issuedAt);
		String refreshToken = jwtUtil.generateRefreshToken(user, issuedAt);

		// 사용자 refresh token 업데이트
		user.updateRefreshToken(refreshToken);

		return new UserJoinResponse(accessToken, refreshToken);
	}
}
