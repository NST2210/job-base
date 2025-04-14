package com.gtel.api.infrastracture.configuration;

import com.ecwid.consul.v1.ConsulClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.logging.log4j.util.Strings;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.io.IOException;

@EnableRedisRepositories
@Configuration
public class RedisConfig {

    @Autowired
    Environment env;

    @Autowired
    ConsulClient consulClient;

    @Bean
    public RedissonClient createRedissonClient() throws IOException {
        String filename = env.getProperty("redis.config-file");

        if (Strings.isBlank(filename)) {
            filename = "redis_single";
        }
        String value = consulClient.getKVValue(filename).getValue().getDecodedValue();
        Config config = Config.fromYAML(value);
        return Redisson.create(config);
    }

    @Bean("redisTemplateString")
    RedisTemplate<String, String> redisTemplateString(@Autowired RedissonConnectionFactory redissonConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redissonConnectionFactory);

        ObjectMapper om = new  ObjectMapper();
        om.registerModule(new JavaTimeModule());
        Jackson2JsonRedisSerializer<String> jsonRedisSerializer = new Jackson2JsonRedisSerializer<>(String.class);
        redisTemplate.setValueSerializer(jsonRedisSerializer);
        return redisTemplate;
    }

    @Bean
    RedisTemplate<String, Object> redisTemplate(@Autowired RedissonConnectionFactory redissonConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redissonConnectionFactory);

        ObjectMapper om = new  ObjectMapper();
        om.registerModule(new JavaTimeModule());
        Jackson2JsonRedisSerializer jsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(om, Object.class);
        redisTemplate.setValueSerializer(jsonRedisSerializer);
        return redisTemplate;
    }

    @Bean
    public RedissonConnectionFactory redissonConnectionFactory(@Autowired RedissonClient redissonClient) {
        return new RedissonConnectionFactory(redissonClient);
    }

    @Autowired
    @Qualifier("WsEventListener")
    @Lazy
    MessageListener wsMessageListener;

    @Bean("wsMessageListenerAdapter")
    MessageListenerAdapter wsMessageListenerAdapter() {
        return new MessageListenerAdapter(wsMessageListener);
    }

    @Bean("systemTopic")
    ChannelTopic wsTopic(@Value("${redis.topic.ws-broadcast:ws-event}") String topic) {
        return new ChannelTopic(topic);
    }

    @Bean
    RedisMessageListenerContainer userTokenEventRedisContainer(@Autowired RedissonConnectionFactory redissonConnectionFactory,
                                                               @Autowired @Qualifier("systemTopic") ChannelTopic topic,
                                                               @Autowired @Qualifier("wsMessageListenerAdapter") MessageListenerAdapter wsMessageListenerAdapter) {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redissonConnectionFactory);
        container.addMessageListener(wsMessageListenerAdapter, topic);
        return container;
    }


}
