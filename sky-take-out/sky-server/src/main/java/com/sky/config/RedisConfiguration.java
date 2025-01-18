package com.sky.config;

import com.sky.controller.admin.CategoryController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Configuration
public class RedisConfiguration {
    //创建RedisTemplate对象
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
        log.info("创建Redis的模板对象");
        RedisTemplate redisTemplate = new RedisTemplate();
        //设置链接工厂对象
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //设置Redis key的序列化器
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //设置value的序列化器 否则会出现二进制
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
