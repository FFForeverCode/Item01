package com.sky.Aspect;

import com.sky.dto.DishDTO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
//清理缓存
@Aspect
@Slf4j
@Component
public class DeleteCacheAspect {
    /**
     * 由于redis中以categoryId为key，缓存菜品数据，
     * 当菜品修改时，缓存需要清理。
     * 因此可采用AOP切面实现缓存清理功能，
     * 使得业务代码与功能性代码分离
     */

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 1.切入点
     * 2.横向逻辑
     * 3.植入
     */
    @Pointcut("@annotation(com.sky.annotation.DeleteCache)")
    public void pointCut(){
    }

    @Around("pointCut()")
    public Object deleteCathe(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 获取方法参数
        Object[] args = proceedingJoinPoint.getArgs();

        // 检查参数是否存在
        if (args == null || args.length == 0 || args[0] == null) {
            log.info("无有效参数，直接执行原方法");
            return proceedingJoinPoint.proceed();
        }

        Object arg = args[0]; // 获取第一个参数
        Object result = proceedingJoinPoint.proceed(); // 执行原方法

        try {
            // 处理 DishDTO 类型参数
            if (arg instanceof DishDTO) {
                // 通过反射获取 categoryId 字段
                Field field = arg.getClass().getDeclaredField("categoryId");
                field.setAccessible(true); // 设置字段为可访问
                Object categoryId = field.get(arg);

                if (categoryId != null) {
                    String key = "dish_" + categoryId; // 拼接缓存 key
                    redisTemplate.delete(key); // 删除缓存
                    log.info("DishDTO 缓存 key [{}] 已删除", key);
                } else {
                    log.warn("DishDTO 的 categoryId 为 null，未执行缓存删除");
                }
            }
            // 处理 Integer 类型参数
            else if (arg instanceof Integer) {
                String key = "dish_" + arg; // 拼接缓存 key
                redisTemplate.delete(key); // 删除缓存
                log.info("Integer 缓存 key [{}] 已删除", key);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error("处理参数时发生反射异常: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("缓存删除时发生异常: {}", e.getMessage(), e);
        }

        return result;
    }
}
