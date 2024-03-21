package yeonba.be.exception;

import org.springframework.http.HttpStatus;

public enum JoinException implements BaseException {

	PASSWORD_CONFIRMATION_NOT_MATCH(
		HttpStatus.BAD_REQUEST,
      "비밀번호 확인 값이 비밀번호와 일치하지 않습니다."),

	VOCAL_RANGE_NOT_FOUND(
		HttpStatus.BAD_REQUEST,
      "존재하지 않는 음역대입니다."),

	ANIMAL_NOT_FOUND(
		HttpStatus.BAD_REQUEST,
      "존재하지 않는 동물상입니다."),

	AREA_NOT_FOUND(
		HttpStatus.BAD_REQUEST,
      "존재하지 않는 지역입니다.");


	private final HttpStatus httpStatus;
	private final String reason;

	JoinException(HttpStatus httpStatus, String reason) {
		this.httpStatus = httpStatus;
		this.reason = reason;
	}

	@Override
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	@Override
	public String getReason() {
		return reason;
	}
}
