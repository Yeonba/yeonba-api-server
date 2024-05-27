package yeonba.be.chatting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import yeonba.be.chatting.dto.request.ChatPublishRequest;

@Service
@RequiredArgsConstructor
public class RedisChattingPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(ChannelTopic topic, ChatPublishRequest request) {

        redisTemplate.convertAndSend(topic.getTopic(), request);
    }
}
