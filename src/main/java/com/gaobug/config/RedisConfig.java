package com.gaobug.config;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {
   @Value("${spring.cache.redis.time-to-live}")
   private Duration timeToLive;

   public RedisConfig() {
      this.timeToLive = Duration.ZERO;
   }

   @Bean(
      name = {"redisTemplate"}
   )
   public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
      RedisTemplate<String, Object> template = new RedisTemplate();
      template.setConnectionFactory(redisConnectionFactory);
      Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
      objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, DefaultTyping.NON_FINAL, As.WRAPPER_ARRAY);
      jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
      template.setValueSerializer(jackson2JsonRedisSerializer);
      StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
      template.setKeySerializer(stringRedisSerializer);
      template.setKeySerializer(stringRedisSerializer);
      template.setHashKeySerializer(stringRedisSerializer);
      template.setValueSerializer(jackson2JsonRedisSerializer);
      template.setHashValueSerializer(jackson2JsonRedisSerializer);
      template.afterPropertiesSet();
      return template;
   }

   @Bean
   public CacheManager cacheManager(RedisConnectionFactory factory) {
      RedisSerializer<String> redisSerializer = new StringRedisSerializer();
      Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
      objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      objectMapper.registerModule(new JavaTimeModule());
      objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, DefaultTyping.NON_FINAL, As.WRAPPER_ARRAY);
      jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
      RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().entryTtl(this.timeToLive).serializeKeysWith(SerializationPair.fromSerializer(redisSerializer)).serializeValuesWith(SerializationPair.fromSerializer(jackson2JsonRedisSerializer)).disableCachingNullValues();
      RedisCacheManager cacheManager = RedisCacheManager.builder(factory).cacheDefaults(config).build();
      return cacheManager;
   }
}
