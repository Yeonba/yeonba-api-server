package yeonba.be.mypage.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncryptor {

    public String encrypt(String password, String salt) {

        String encryptedPassword = "";

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update((password + salt).getBytes());
            byte[] saltedPassword = md.digest();

            StringBuilder sb = new StringBuilder();
            for (byte b : saltedPassword) {
                sb.append(String.format("%02x", b));
            }

            encryptedPassword = sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("암호화 알고리즘이 존재하지 않습니다.");
        }

        return encryptedPassword;
    }
}
