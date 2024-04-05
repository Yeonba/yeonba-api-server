package yeonba.be.util;

public enum GlobalValidationRegex {

    BEARER_TOKEN("^Bearer ([A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*)$"),
    EMAIL("[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"),
    PASSWORD("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~#@!]).{8,20}$"),
    PHONE_NUMBER("^010\\d{8}$"),
    VERIFICATION_CODE("^[A-Za-z0-9]{6}$");

    private final String pattern;

    GlobalValidationRegex(String pattern) {

        this.pattern = pattern;
    }

    public String getPattern() {

        return pattern;
    }
}
