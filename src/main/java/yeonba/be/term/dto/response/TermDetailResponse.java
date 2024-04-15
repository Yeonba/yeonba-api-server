package yeonba.be.term.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TermDetailResponse {

  @Schema(type = "string", description = "제목", example = "이용 약관")
  private String title;

  @Schema(type = "string", description = "내용", example = """
      제1조 (목적)
                
      본 약관은 회사가 제공하는 인터넷 관련 서비스(이하 "서비스"라 함)를 이용함에 있어
      회사와 회원 간의 권리 의무 및 책임사항을 규정함을 목적으로 합니다.
                
      제2조 (정의)
                
      "회사"란 서비스를 제공하는 주체를 의미합니다.
      "회원"이라 함은 회사와 서비스 이용계약을 체결하고 회사가 제공하는 서비스를 이용하는 자를 말합니다.
      "이용자"란 회원 및 비회원을 모두 포함한 서비스를 이용하는 자를 의미합니다.
                
      제3조 (약관의 효력 및 변경)
                
      본 약관은 서비스 홈페이지에 게시함으로써 효력을 발생합니다.
      회사는 필요한 경우 본 약관을 변경할 수 있으며, 변경된 약관은 서비스 홈페이지에 공지함으로써 효력을 발생합니다.
                                    
      // 이하 필요 내용 ...
      """)
  private String content;

  @Schema(type = "string", description = "등록 일시", example = "2010-10-02")
  private LocalDate registeredAt;
}
