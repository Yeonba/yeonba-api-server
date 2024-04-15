package yeonba.be.term.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import yeonba.be.term.dto.response.TermDetailResponse;
import yeonba.be.util.CustomResponse;

@Tag(name = "Term", description = "약관 API")
@RestController
public class TermController {

  @Operation(
      summary = "전체 약관 조회 API",
      description = "전체 약관을 조회할 수 있습니다."
  )
  @ApiResponse(
      responseCode = "200",
      description = "전체 약관 조회 성공"
  )
  @GetMapping("/terms")
  public ResponseEntity<CustomResponse<List<TermDetailResponse>>> allTerms() {
    List<TermDetailResponse> response = List.of(
        new TermDetailResponse(
            "이용 약관",
            "약관 내용,약관 내용,약관 내용,약관 내용",
            LocalDate.of(2012, 10, 1)
        ),
        new TermDetailResponse(
            "개인 정보 처리 방침",
            "약관 내용, 약관 내용, 약관 내용",
            LocalDate.of(2013, 10, 1)
        )
    );

    return ResponseEntity
        .ok()
        .body(new CustomResponse<>(response));
  }
}
