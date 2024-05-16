package yeonba.be.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserQueryPageResponse {

    @Schema(
        type = "array",
        description = "조회된 이성(사용자) 목록")
    private List<UserQueryResponse> users;

    @Schema(
        type = "number",
        description = "조회된 전체 페이지 수",
        example = "10")
    private int totalPage;

    @Schema(
        type = "number",
        description = "조회된 전체 데이터 수",
        example = "1000")
    private long totalElements;

    @Schema(
        type = "boolean",
        description = "첫 페이지 여부",
        example = "true")
    @JsonProperty("isFirst")
    private boolean first;

    @Schema(
        type = "boolean",
        description = "마지막 페이지 여부",
        example = "false")
    @JsonProperty("isLast")
    private boolean last;

    public static UserQueryPageResponse from(Page<UserQueryResponse> page) {

        return new UserQueryPageResponse(
            page.getContent(),
            page.getTotalPages(),
            page.getTotalElements(),
            page.isFirst(),
            page.isLast());
    }
}
