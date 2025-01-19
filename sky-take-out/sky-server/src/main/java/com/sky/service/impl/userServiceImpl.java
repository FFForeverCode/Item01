package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.userService;
import com.sky.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class userServiceImpl implements userService {


    @Autowired
    private  WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;

    private static String LOG_IN = "https://api.weixin.qq.com/sns/jscode2session";



    /**
     * 获取小程序的code，加上自身的Secret，ID
     * 向微信服务端发送请求来获取OpenID等必要数据
     * 通过HttpClient工具包来发送请求并获取数据
     * @param userLoginDTO
     * @return
     */
    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        //调用微信接口服务，获得当前用户的OpenId
        String openId = getOpenId(userLoginDTO);
        //若OpenId为空则登陆失败，抛出异常
        if(openId == null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        //判断是否为新用户
        User user = userMapper.getByOpenId(openId);
        //若为新用户则注册
        if(user == null){
            user = User.builder()
                    .openid(openId)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }

        return user;
    }

    /**
     * 通过客户端工具包，发送code获取openid
     * @param userLoginDTO
     * @return
     */
    private String getOpenId(UserLoginDTO userLoginDTO) {
        Map<String,String>map = new HashMap<>();//存储请求参数
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code", userLoginDTO.getCode());
        map.put("grant_type","authorization_code");
        String json = HttpClientUtil.doGet(LOG_IN,map);//使用HttpClient发送请求，获取结果
        JSONObject jsonObject = JSON.parseObject(json);
        return jsonObject.getString("openid");
    }
}
