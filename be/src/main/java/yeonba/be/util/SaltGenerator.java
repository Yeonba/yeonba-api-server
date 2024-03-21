package yeonba.be.util;

import java.security.SecureRandom;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.binary.Base64;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SaltGenerator {

  private static final int MIN_SALT_LENGTH = 16;
  private static final int MAX_SALT_LENGTH = 256;

  public static String generateRandomSalt(){
    SecureRandom random = new SecureRandom();
    int length = random.nextInt(MIN_SALT_LENGTH, MAX_SALT_LENGTH);

    byte[] salt = new byte[length];
    random.nextBytes(salt);

    return Base64.encodeBase64String(salt);
  }
}
