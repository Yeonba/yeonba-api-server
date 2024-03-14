package yeonba.be.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VerificationCodeGenerator {

  private static final int CODE_LENGTH = 6;

  public static String generateVerificationCode() {

    return RandomStringUtils.random(CODE_LENGTH, 0, 0, true, true);
  }
}
