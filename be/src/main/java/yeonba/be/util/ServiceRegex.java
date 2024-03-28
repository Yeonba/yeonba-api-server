package yeonba.be.util;

public enum ServiceRegex {

    EMAIL("[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"),
    PASSWORD("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~#@!]).{8,20}$"),
    VERIFICATION_CODE("^[A-Za-z0-9]{6}$"),
    PHONE_NUMBER("^010\\d{8}$"),
    BEARER_TOKEN("^Bearer ([A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*)$");

    private final String pattern;

    ServiceRegex(String pattern) {

        this.pattern = pattern;
    }

    public String getPattern() {

        return pattern;
    }
}
