package com.gtel.api.infrastracture.repository.redis.publisher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

@Slf4j
public class RedisMessagePublisher {
    RedisTemplate<String, Object> redisTemplate;

    ChannelTopic channelTopic;

    public RedisMessagePublisher(RedisTemplate<String, Object> redisTemplate, ChannelTopic channelTopic) {
        this.redisTemplate = redisTemplate;
        this.channelTopic = channelTopic;
    }

    public void publish(Object message) {
        redisTemplate.convertAndSend(channelTopic.getTopic(), message);
    }
}
