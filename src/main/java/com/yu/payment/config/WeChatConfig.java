package com.yu.payment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:application.properties")
public class WeChatConfig {

    /**
     * 微信公众号 应用id
     */
    @Value("${wxpay.appid}")
    private String appid;

    /**
     * 微信公众号 应用密钥
     */
    @Value("${wxpay.appsecret}")
    private String appsecret;

    /**
     * 微信开放平台 应用id
     */
    @Value("${wxopen.appid}")
    private String openAppid;

    /**
     * 微信开放平台 回调路径
     */
    @Value("${wxopen.redirect_url}")
    private String openAppsecret;


    /**
     * 微信开放平台 应用密钥
     */
    @Value("${wxopen.redirect_url}")
    private String openRedirectUrl;

    /**
     * 微信商户id
     */
    @Value("${wxpay.mer_id}")
    private String mchId;

    /**
     * 微信商户 密钥 key
     */
    @Value("${wxpay.key}")
    private String key;

    /**
     * 微信支付回调地址
     */
    @Value("${wxpay.callback}")
    private String payCallBackUrl;

    /**
     * 微信开放平台二维码连接
     */
    private final static String OPEN_QRCODE_URL = "https://open.weixin.qq.com/connect/qrconnect?" +
            "appid=%s&" +
            "redirect_uri=%s&" +
            "response_type=code&" +
            "scope=snsapi_login&" +
            "state=%s#wechat_redirect";

    /**
     * 开放平台
     */
    private final static String OPEN_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=SECRET&code=%s&grant_type=authorization_code";

    //用于获取用户信息的url
    private final static String OPEN_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";

    /**
     * 统一下单地址
     */
    public static final String UNIFIED_ORDER_URL ="https://api.mch.weixin.qq.com/pay/unifiedorder";

    public static String getUnifiedOrderUrl() {
        return UNIFIED_ORDER_URL;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPayCallBackUrl() {
        return payCallBackUrl;
    }

    public void setPayCallBackUrl(String payCallBackUrl) {
        this.payCallBackUrl = payCallBackUrl;
    }

    public static String getOpenAccessTokenUrl() {
        return OPEN_ACCESS_TOKEN_URL;
    }

    public static String getOpenUserInfoUrl() {
        return OPEN_USER_INFO_URL;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret;
    }

    public String getOpenAppid() {
        return openAppid;
    }

    public void setOpenAppid(String openAppid) {
        this.openAppid = openAppid;
    }

    public String getOpenAppsecret() {
        return openAppsecret;
    }

    public void setOpenAppsecret(String openAppsecret) {
        this.openAppsecret = openAppsecret;
    }

    public String getOpenRedirectUrl() {
        return openRedirectUrl;
    }

    public void setOpenRedirectUrl(String openRedirectUrl) {
        this.openRedirectUrl = openRedirectUrl;
    }

    public static String getOpenQrcodeUrl() {
        return OPEN_QRCODE_URL;
    }
}
