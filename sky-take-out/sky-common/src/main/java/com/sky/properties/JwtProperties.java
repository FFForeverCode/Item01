package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//jwt属性类
//扫描sky.jwt配置文件,对应配置生成
@Component
@ConfigurationProperties(prefix = "sky.jwt")
@Data
public class JwtProperties {

    /**
     * 管理端员工生成jwt令牌相关配置
     */
    /**
     * 密钥
     */
    private String adminSecretKey;
    /**
     * 令牌生效时间
     */
    private long adminTtl;
    /**
     * 令牌名称
     */
    private String adminTokenName;

    /**
     * 用户端微信用户生成jwt令牌相关配置
     */
    private String userSecretKey;
    private long userTtl;
    private String userTokenName;

}
