package yeonba.be.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.RandomStringUtils;

public class TemporaryPasswordGenerator {

	private static final int MIN_LENGTH = 8;

	private static final String DIGITS = "0123456789";
	private static final String LOWER_CASES = "abcdefghijklmnopqrstuvwxyz";
	private static final String SPECIAL_CHARS = "~#@!";
	private static final String UPPER_CASES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static String generatePassword() {

		// 각 카테고리에서 최소 한 문자씩 선택
		StringBuilder password = new StringBuilder();
		password.append(RandomStringUtils.random(1, LOWER_CASES))
			.append(RandomStringUtils.random(1, UPPER_CASES))
			.append(RandomStringUtils.random(1, DIGITS))
			.append(RandomStringUtils.random(1, SPECIAL_CHARS));

		// 모든 가능한 문자를 포함하는 문자열
		String allPossibleChars = DIGITS.concat(LOWER_CASES)
			.concat(SPECIAL_CHARS)
			.concat(UPPER_CASES);

		// 나머지 길이 채우기
		password.append(RandomStringUtils.random(MIN_LENGTH - 4, allPossibleChars));

		// 문자열 섞기
		List<Character> passwordChars = new ArrayList<>(
			password.chars()
				.mapToObj(c -> (char) c)
				.toList());
		Collections.shuffle(passwordChars);

		// 최종 임시 비밀번호 생성 및 반환

		return passwordChars.stream()
			.map(String::valueOf)
			.collect(Collectors.joining());
	}
}
