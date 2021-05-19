package com.yu.payment.service;

import com.yu.payment.domain.User;

import java.io.UnsupportedEncodingException;

/**
 * 用户业务接口类
 */
public interface IUserService {

    /**
     * 根据code 去获取用户token，保存用户信息
     * @param code
     * @return
     */
    User saveWeChatUser(String code) throws UnsupportedEncodingException;
}
