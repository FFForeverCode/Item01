package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "店铺接口")
@Slf4j
@SuppressWarnings("all")
public class ShopController {

    @Autowired
    RedisTemplate redisTemplate;
    public static final String KEY = "SHOP_STATUS";
    @PutMapping("/{status}")
    @ApiOperation("设置店铺的营业状态")
    public Result setStatus(@PathVariable int status){
        log.info("设置店铺的营业状态为:{}",status==1?"营业中":"打烊中");
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(KEY,String.valueOf(status));
        return Result.success();
    }

    @GetMapping("/status")
    @ApiOperation("获取店铺的营业状态")
    public Result<Integer>getStatus(){
        Integer status = Integer.parseInt((String) redisTemplate.opsForValue().get(KEY));//类型转换
        log.info("获取店铺的营业状态:{}",status);
        return Result.success(status);
    }
}
