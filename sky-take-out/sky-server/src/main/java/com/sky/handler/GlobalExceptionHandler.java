package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 * 可以捕获全局出现的异常
 * 捕获了异常后,控制台就不会出现一大堆信息#Error....
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }
    /**
     * 处理执行sql语句的异常
     */
    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException exception){
        //例如:
        // 异常信息:(表内数据重复)
        // Duplicate entry 'zhangsan' for key 'employee.idx_username'
        //获取异常信息
        String message = exception.getMessage();
        //根据异常信息,判断该异常的类型
        //管理员重复的异常
        if(message.contains("Duplicate entry")){
            //根据空格将字符串分隔为数组
            //例如:ni hao a -> s[0]=ni s[1]=hao s[2] = a
           String[]split = message.split(" ");
           String username = split[2];
           String msg = username+"已经存在!";
           log.info("异常信息:{}",msg);//将错误信息打印到日志上
           return Result.error(msg);//给前端返回携带异常信息的 Result类
        }else{
            //异常信息工具类,内部都是异常信息常量
            //未知错误
            return Result.error(MessageConstant.UNKNOWN_ERROR);//返回给前端
        }
    }


}
