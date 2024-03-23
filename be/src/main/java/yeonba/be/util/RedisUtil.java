package yeonba.be.util;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisUtil {

	private final RedisTemplate<String, Object> redisTemplate;

	public void putData(String key, String value, long expiredTimeMinutes) {
		redisTemplate.opsForValue()
			.set(key, value, expiredTimeMinutes, TimeUnit.MINUTES);
	}

	public Optional<Object> getData(String key) {
		Object value = redisTemplate.opsForValue().get(key);

		return Optional.ofNullable(value);
	}

	public void deleteData(String key) {
		redisTemplate.delete(key);
	}
}
