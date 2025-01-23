package com.sky.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DeleteCacheUtil{
    @Autowired
    private RedisTemplate redisTemplate;

    public void deleteCache(String key){
        redisTemplate.delete(key);
    }
    public void deleteAll(String key){
        Set keys = redisTemplate.keys(key+"*");
        redisTemplate.delete(keys);
    }
}
