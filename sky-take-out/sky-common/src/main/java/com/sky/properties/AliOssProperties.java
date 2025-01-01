package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里OSS配置属性类
 * 将属性类封装成对象
 */
@Component
@ConfigurationProperties(prefix = "sky.alioss")//配置文件 与其绑定注入信息
@Data
public class AliOssProperties {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

}
