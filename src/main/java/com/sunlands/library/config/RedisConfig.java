package com.sunlands.library.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : hulin
 * @date : 2018/6/12 14:48
 * @description : 配置redis，主要是有效时间设置
 */
@Configuration
public class RedisConfig {
    @Bean
    public CacheManager cacheManager(@SuppressWarnings("rawtypes") RedisTemplate redisTemplate) {
        RedisCacheManager cacheManager= new RedisCacheManager(redisTemplate);
        //单位秒？
        cacheManager.setDefaultExpiration(60);
        //可以为每一个缓存设置过期时间
        Map<String,Long> expiresMap=new HashMap<>(16);
        expiresMap.put("book",100L);
        cacheManager.setExpires(expiresMap);
        return cacheManager;
    }

}
