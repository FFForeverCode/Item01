package com.sky.Aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.DataValidationEvaluator;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 自定义切面实现公共字段自动填充
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /**
     * 1.定义切入点
     * 2.横向逻辑
     * 3.织入
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void pointCut(){}

    //TODO:注解+反射 需要理解熟练 类似：AOP实现日志功能
    @Around("pointCut()")
    public Object autoFill(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("公共字段填充...");
        //1.确定操作类型：获取注解，根据注解的value确定
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();//方法签名
        AutoFill annotation = methodSignature.getMethod().getAnnotation(AutoFill.class);//获取方法的注解
        OperationType value = annotation.value();//注解的值
        //2.获取传入的参数，实体类
        Object entity = joinPoint.getArgs()[0];
        if(entity==null){
            log.info("无参数...");
        }else{
            //准备填充的数据
            LocalDateTime now = LocalDateTime.now();
            Long currentId = BaseContext.getCurrentId();
            //通过反射获取当前方法的实体类的方法
            Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
            if(value == OperationType.INSERT) {
                log.info("插入操作填充...");
                //通过反射获取当前的实体类对象的方法
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                //通过反射对属性进行赋值
                setCreateTime.invoke(entity, now);
                setCreateUser.invoke(entity, currentId);
            }
            setUpdateTime.invoke(entity,now);
            setUpdateUser.invoke(entity,currentId);
        }

        return joinPoint.proceed();//执行，并返回结果

    }

}
