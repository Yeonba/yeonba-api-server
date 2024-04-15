package yeonba.be.util;

public enum GlobalValidationRegex {

	EMAIL("[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"),
	PASSWORD("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~#@!]).{8,20}$");

	private final String pattern;

	GlobalValidationRegex(String pattern) {
		this.pattern = pattern;
	}

	public String getPattern() {

		return pattern;
	}
}
