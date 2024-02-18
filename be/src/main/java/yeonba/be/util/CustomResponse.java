package yeonba.be.util;

import lombok.Getter;

@Getter
public class CustomResponse<T> {
    private final String status;

    private final String message;

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
