package yeonba.be.util;

import java.security.SecureRandom;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.binary.Base64;

/*
	- 32비트(4바이트) 길이 salt 만으로 대부분의 보안적 위협은 충분히 커버 가능
	- base64 인코딩에 따라 아래 로직에서 생성되는 salt 문자열의 길이는 8
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SaltGenerator {

	private static final int SALT_BYTE_LENGTH = 4;

	public static String generateRandomSalt() {

		SecureRandom random = new SecureRandom();

		byte[] salt = new byte[SALT_BYTE_LENGTH];
		random.nextBytes(salt);

		return Base64.encodeBase64String(salt);
	}
}
