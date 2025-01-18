package com.sky.controller.user;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userShopController")
@RequestMapping("/user")
@Slf4j
@Api(tags = "用户商铺接口")
@SuppressWarnings("all")
public class ShopController {

    @Autowired
    RedisTemplate redisTemplate;
    public static final String KEY = "SHOP_STATUS";
    @GetMapping("/status")
    @ApiOperation("获取营业状态")
    public Result getStatus(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Integer status = Integer.parseInt((String)valueOperations.get(KEY));
        log.info("用户端获取营业状态:{}",status);
        return Result.success(status);
    }
}
