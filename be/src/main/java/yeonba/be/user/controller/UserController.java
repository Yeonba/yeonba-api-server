package yeonba.be.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import yeonba.be.mypage.service.ReportService;
import yeonba.be.user.dto.request.UserQueryRequest;
import yeonba.be.user.dto.request.UserReportRequest;
import yeonba.be.user.dto.response.UserProfileResponse;
import yeonba.be.user.dto.response.UserQueryPageResponse;
import yeonba.be.user.service.BlockService;
import yeonba.be.user.service.FavoriteService;
import yeonba.be.util.CustomResponse;

@Tag(name = "User", description = "사용자 API")
@RestController
@RequiredArgsConstructor
public class UserController {

	private final BlockService blockService;
	private final ReportService reportService;
	private final FavoriteService favoriteService;


	@Operation(
		summary = "이성(다른 사용자) 목록 조회",
		description = "조건에 따라 다른 사용자 프로필 목록을 조회할 수 있습니다."
	)
	@ApiResponse(
		responseCode = "200",
		description = "이성 목록 정상 조회"
	)
	@GetMapping("/users")
	public ResponseEntity<CustomResponse<UserQueryPageResponse>> users(
		@ParameterObject UserQueryRequest request) {

		return ResponseEntity
			.ok()
			.body(new CustomResponse<>());
	}


	@Operation(
		summary = "다른 사용자 프로필 조회",
		description = "다른 사용자의 프로필을 조회할 수 있습니다."
	)
	@ApiResponse(
		responseCode = "200",
		description = "사용자 프로필 정상 조회"
	)
	@GetMapping("/users/{userId}")
	public ResponseEntity<CustomResponse<UserProfileResponse>> profile(
		@Parameter(description = "조회대상 사용자 ID", example = "1")
		@PathVariable long userId) {

		return ResponseEntity
			.ok()
			.body(new CustomResponse<>(
				new UserProfileResponse(
					"존잘남",
					23,
					177,
					"서울시 강남구",
					80,
					"저음",
					"여우상",
					false
				)
			));
	}

	@Operation(summary = "즐겨찾기 등록", description = "다른 사용자를 자신의 즐겨찾기에 등록할 수 있습니다.")
	@ApiResponse(responseCode = "202", description = "즐겨찾기 등록 정상 처리")
	@PostMapping("/favorites/{userId}")
	public ResponseEntity<CustomResponse<Void>> registerFavorite(
		@RequestAttribute("userId") long userId,
		@Parameter(description = "즐겨찾기에 등록될 사용자 ID", example = "1")
		@PathVariable("userId") long favoriteUserId) {

		favoriteService.addFavorite(userId, favoriteUserId);

		return ResponseEntity
			.accepted()
			.body(new CustomResponse<>());
	}

	@Operation(summary = "즐겨찾기 삭제", description = "즐겨찾기에 등록한 사용자를 삭제합니다.")
	@ApiResponse(responseCode = "202", description = "즐겨찾기 삭제 정상 처리")
	@DeleteMapping("/favorites/{userId}")
	public ResponseEntity<CustomResponse<Void>> deleteFavorite(
		@RequestAttribute("userId") long userId,
		@Parameter(description = "즐겨찾기에서 삭제할 사용자 ID", example = "1")
		@PathVariable("userId") long favoriteUserId) {

		favoriteService.deleteFavorite(userId, favoriteUserId);

		return ResponseEntity
			.accepted()
			.body(new CustomResponse<>());
	}

	@Operation(summary = "사용자 신고", description = "다른 사용자를 신고할 수 있습니다.")
	@ApiResponse(responseCode = "202", description = "신고 정상 처리")
	@PostMapping("/users/{userId}/report")
	public ResponseEntity<CustomResponse<Void>> report(
		@RequestAttribute("userId") long userId,
		@Parameter(description = "신고 대상 사용자 ID", example = "1")
		@PathVariable("userId") long reportedUserId,
		@RequestBody UserReportRequest request) {

		reportService.makeReport(
			userId,
			reportedUserId,
			request);

		return ResponseEntity
			.accepted()
			.body(new CustomResponse<>());
	}

	@Operation(summary = "차단하기", description = "다른 사용자를 차단할 수 있습니다.")
	@ApiResponse(responseCode = "202", description = "차단 요청 정상 처리")
	@PostMapping("/users/{userId}/block")
	public ResponseEntity<CustomResponse<Void>> block(
		@RequestAttribute("userId") long userId,
		@Parameter(description = "차단하는 사용자 ID", example = "1")
		@PathVariable("userId") long blockedUserId) {

		blockService.block(userId, blockedUserId);

		return ResponseEntity
			.accepted()
			.body(new CustomResponse<>());
	}
}
