package yeonba.be.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yeonba.be.login.dto.request.UserJoinRequest;
import yeonba.be.login.dto.response.UserJoinResponse;
import yeonba.be.user.entity.User;
import yeonba.be.user.event.ProfilePhotoDeletionEvent;

@Service
@RequiredArgsConstructor
public class JoinService {

	private final ApplicationEventPublisher eventPublisher;
	private final UserService userService;

	/*
	  회원 가입 비즈니스 로직 과정
	  1. 사용자 엔티티 저장
	  2. 프로필 사진 엔티티 저장
	  3. 사용자 선호조건 저장
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

		return new UserJoinResponse("access_token", "refresh_token");
	}
}
