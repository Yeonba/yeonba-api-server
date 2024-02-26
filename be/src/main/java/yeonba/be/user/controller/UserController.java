package yeonba.be.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import yeonba.be.user.dto.request.UserReportRequest;
import yeonba.be.user.dto.response.UserArrowsResponse;
import yeonba.be.user.dto.response.UserProfileResponse;
import yeonba.be.util.CustomResponse;

@Tag(name = "User", description = "사용자 API")
@RestController
public class UserController {

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

  @Operation(
      summary = "사용자 화살 개수 조회",
      description = "사용자 화살 개수를 조회할 수 있습니다."
  )
  @ApiResponse(
      responseCode = "200",
      description = "사용자 화살 개수 정상 조회"
  )
  @GetMapping("/users/arrows")
  public ResponseEntity<CustomResponse<UserArrowsResponse>> arrows() {

    return ResponseEntity
        .ok()
        .body(new CustomResponse<>(new UserArrowsResponse(10)));
  }

  @Operation(
      summary = "출석 체크",
      description = "출석 체크를 통해 사용자가 10개의 화살을 획득할 수 있습니다."
  )
  @ApiResponse(
      responseCode = "200",
      description = "정상적으로 출석 체크 완료"
  )
  @PostMapping("/daily-check")
  public ResponseEntity<CustomResponse<Void>> dailyCheck() {

    return ResponseEntity
        .ok()
        .body(new CustomResponse<>());
  }

  @Operation(
      summary = "사용자 신고",
      description = "다른 사용자를 신고할 수 있습니다."
  )
  @ApiResponse(
      responseCode = "204",
      description = "신고 정상 처리"
  )
  @PostMapping("/users/{userId}/report")
  public ResponseEntity<CustomResponse<Void>> report(
      @Parameter(description = "신고 대상 사용자 ID", example = "1")
      @PathVariable long userId,
      @RequestBody UserReportRequest request) {

    return ResponseEntity
        .ok()
        .body(new CustomResponse<>());
  }

  @Operation(
      summary = "화살 보내기",
      description = "다른 사용자에게 화살을 보낼 수 있습니다."
  )
  @ApiResponse(
      responseCode = "204",
      description = "화살 전송 정상 처리"
  )
  @PostMapping("/users/{userId}/arrow")
  public ResponseEntity<CustomResponse<Void>> sendArrow(
      @Parameter(description = "화살 받는 사용자 ID", example = "1")
      @PathVariable long userId) {

    return ResponseEntity
        .accepted()
        .body(new CustomResponse<>());
  }

  @Operation(
      summary = "차단하기",
      description = "다른 사용자를 차단할 수 있습니다."
  )
  @ApiResponse(
      responseCode = "204",
      description = "차단 요청 정상 처리"
  )
  @PostMapping("/users/{userId}/block")
  public ResponseEntity<CustomResponse<Void>> block(
      @Parameter(description = "차단하는 사용자 ID", example = "1")
      @PathVariable long userId) {

    return ResponseEntity
        .accepted()
        .body(new CustomResponse<>());
  }


  @Operation(
      summary = "화살 충전",
      description = "광고 시청시 화살 5개를 충전할 수 있습니다."
  )
  @ApiResponse(
      responseCode = "204",
      description = "화살 충전 정상 처리"
  )
  @PostMapping("/users/arrows")
  public ResponseEntity<CustomResponse<Void>> earnArrows() {

    return ResponseEntity
        .accepted()
        .body(new CustomResponse<>());
  }
}
