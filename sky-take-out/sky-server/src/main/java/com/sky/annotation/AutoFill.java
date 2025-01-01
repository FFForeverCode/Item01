package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动填充注解
 */
@Target(ElementType.METHOD)//用到方法上
@Retention(RetentionPolicy.RUNTIME)//在执行时生效
public @interface AutoFill {
    //枚举类 操作类型：update、insert
    OperationType value();
}
