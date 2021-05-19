package com.yu.payment.service.impl;

import com.yu.payment.config.WeChatConfig;
import com.yu.payment.domain.User;
import com.yu.payment.mapper.UserMapper;
import com.yu.payment.service.IUserService;
import com.yu.payment.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

@Service
public class UserServiceImpl implements IUserService {


    @Autowired
    private WeChatConfig weChatConfig;

    @Autowired
    private UserMapper userMapper;


    /**
     * 用户与微信用来交互数据
     *
     * @param code
     * @return
     * @throws UnsupportedEncodingException
     */
    @Override
    public User saveWeChatUser(String code) throws UnsupportedEncodingException {

        //获取用户授权后的回调地址  获取token的一个地址
        String accessTokenUrl = String.format(WeChatConfig.getOpenAccessTokenUrl(), weChatConfig.getOpenAppid(), code);
        //调用自己封装的方法进行get请求,获取 assess_token
        Map<String, Object> baseMap = HttpUtils.doGet(accessTokenUrl);
        if (baseMap == null || baseMap.isEmpty()) {
            return null;
        }

        String access_token = (String) baseMap.get("access_token");
        String openId = (String) baseMap.get("openId");
        String unionid = (String) baseMap.get("unionid");

        // 先根据openId查询数据库中有没有该用户
        User dbUser = userMapper.findById(openId);
        if (dbUser != null) { //如果有该用户，可以直接返回或更新用户信息
            return dbUser;
        }


        //格式化获取个人信息的url
        String userInfoUrl = String.format(WeChatConfig.getOpenUserInfoUrl(), access_token, openId);
        //通过code 与 access_token 来获取用户信息
        Map<String, Object> userInfoMap = HttpUtils.doGet(userInfoUrl);
        if (userInfoMap == null || userInfoMap.isEmpty())
            return null;
        String nickname = (String) userInfoMap.get("nickname");
        //为了防止乱码，进行重新编码
        nickname = new String(nickname.getBytes("ISO-8859-1"), "UTF-8");
        Double sexTemp = (Double) userInfoMap.get("sex");
        int sex = sexTemp.intValue();
        String city = (String) userInfoMap.get("city");
        String country = (String) userInfoMap.get("country");
        String province = (String) userInfoMap.get("province");
        String headimgurl = (String) userInfoMap.get("headimgurl");
        StringBuilder sb = new StringBuilder(country).append("||").append(province).append("||").append(city);
        String finalAddress = sb.toString();
        //为了防止乱码，进行重新编码
        finalAddress = new String(finalAddress.getBytes("ISO-8859-1"), "UTF-8");

        /**
         *  String openId;
         *  String name;
         *  String headImg;
         *  String phone;
         *  String sign;
         *  Integer sex;
         *  String city;
         *  java.sql.Timestamp createTime;
         */
        //封装用户信息
        User user = new User();
        user.setName(nickname);
        user.setHeadImg(headimgurl);
        user.setCity(finalAddress);
        user.setOpenId(openId);
        user.setSex(sex);
        user.setCreateTime(new Date());

        user = userMapper.save(user);


        return user;
    }
}
