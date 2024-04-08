package yeonba.be.util;

import java.security.SecureRandom;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.binary.Base64;

/*
- 32바이트 길이의 salt만으로 대부분의 보안적 위협은 커버 가능
- 이하 로직을 통해 생성되는 salt 문자열의 길이는 약 44자
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SaltGenerator {

    private static final int SALT_BYTE_LENGTH = 32;

    public static String generateRandomSalt() {

        SecureRandom random = new SecureRandom();

        byte[] salt = new byte[SALT_BYTE_LENGTH];
        random.nextBytes(salt);

        return Base64.encodeBase64String(salt);
    }
}
