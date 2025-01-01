package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class OssConfiguration {
    /**
     * 配置阿里云文件上传工具类，将阿里云OSS属性注入到工具类中，然后将工具类
     * 创建为Bean，置于IOC容器中
     * @param aliOssProperties
     * @return
     */
    @Bean
    @ConditionalOnMissingBean//当没有该对象时创建，保证唯一，避免多次创建
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties){

        log.info("开始创建阿里云文件上传工具类对象,{}",aliOssProperties);
        return new AliOssUtil(
                aliOssProperties.getEndpoint(),
                aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(),
                aliOssProperties.getBucketName()
        );
    }
}
