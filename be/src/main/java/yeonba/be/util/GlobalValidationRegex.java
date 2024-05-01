package yeonba.be.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GlobalValidationRegex {

    PASSWORD("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~#@!]).{8,20}$"),
    VERIFICATION_CODE("^[A-Za-z0-9]{6}$"),
    PHONE_NUMBER("^010\\d{8}$");

    private final String pattern;
}
