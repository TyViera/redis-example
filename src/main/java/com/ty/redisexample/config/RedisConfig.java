package com.ty.redisexample.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ty.redisexample.model.Event;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
//Important class
public class RedisConfig {

  @Bean
  @Primary
  public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
    return new LettuceConnectionFactory("localhost", 6379);
  }

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper()
        .findAndRegisterModules()
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  @Bean
  public ReactiveRedisTemplate<String, Event> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory, ObjectMapper objectMapper) {
    StringRedisSerializer keySerializer = new StringRedisSerializer();
    Jackson2JsonRedisSerializer<Event> valueSerializer = new Jackson2JsonRedisSerializer<>(Event.class);
    valueSerializer.setObjectMapper(objectMapper);
    RedisSerializationContext.RedisSerializationContextBuilder<String, Event> builder = RedisSerializationContext.newSerializationContext(keySerializer);

    RedisSerializationContext<String, Event> context = builder
        .value(valueSerializer)
        .hashKey(keySerializer)
        .hashValue(valueSerializer)
        .build();
    return new ReactiveRedisTemplate<>(factory, context);
  }

}
