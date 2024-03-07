package yeonba.be.arrow.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;
import yeonba.be.arrow.dto.UserArrowsResponse;
import yeonba.be.arrow.service.ArrowService;
import yeonba.be.user.entity.User;
import yeonba.be.util.CustomResponse;

@Tag(name = "Arrow", description = "화살 관련 API")
@RestController
@RequiredArgsConstructor
public class ArrowController {

  private final ArrowService arrowService;

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

  @Operation(summary = "출석 체크", description = "출석 체크를 통해 사용자가 10개의 화살을 획득할 수 있습니다.")
  @ApiResponse(
      responseCode = "202",
      description = "출석 체크 정상 처리"
  )
  @PostMapping("/daily-check")
  public ResponseEntity<CustomResponse<Void>> dailyCheck(
      @RequestAttribute("user") User user) {

    arrowService.dailyCheck(user);

    return ResponseEntity
        .accepted()
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
