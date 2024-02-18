package yeonba.be.util;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class CustomResponse<T> {
    @Schema(
        type = "string",
        description = "요청 처리 상태",
        example = "success"
    )
    private final String status;
    @Schema(
        type = "string",
        description = "처리 상황을 설명하는 메시지",
        example = "정상 처리 되었습니다."
    )
    private final String message;
    @Schema(
        description = "응답 데이터",
        example = "null"
    )
    private final T data;

    public CustomResponse(String message) {
        this.status = "fail";
        this.message = message;
        this.data = null;
    }

    public CustomResponse(T data) {
        this.status = "success";
        this.message = null;
        this.data = data;
    }

    public CustomResponse() {
        this.status = "success";
        this.message = null;
        this.data = null;
    }
}
