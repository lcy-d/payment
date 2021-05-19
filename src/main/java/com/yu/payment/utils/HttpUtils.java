package com.yu.payment.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpsConfigurator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 用来封装 http get 与 post
 * 用来接口直接的相互调用
 */
public class HttpUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static Map<String, Object> map = new HashMap<>();

    /**
     * 与其他api 接口 对接的 get方法
     * 用于扫码登陆
     *
     * @param url
     * @return
     */
    public static Map<String, Object> doGet(String url) {
        HttpClient client = HttpClients.createDefault(); //相当于创建了一个http的客户端
        RequestConfig httpRequestConfig = RequestConfig.custom().setConnectTimeout(5000) //设置建立连接时间
                .setConnectionRequestTimeout(5000) //设置请求超时时间
                .setSocketTimeout(5000) //读取等待时间
                .setRedirectsEnabled(true) // 开启重定向
                .build(); //构建对象

        HttpGet httpGet = new HttpGet(url); //建立get 请求
        httpGet.setConfig(httpRequestConfig); //配置get 相关配置

        try {
            HttpResponse response = client.execute(httpGet); //发送 get 请求
            if (response.getStatusLine().getStatusCode() == 200) { // 如果返回状态码为 200 则 代表请求成功

                HttpEntity entity = response.getEntity();//获取返回请求的结果
                String resultJson = entity.toString(); //将请求结果转化为 string

                Map map = objectMapper.readValue(resultJson, HttpUtils.map.getClass()); //利用json工具，解析json，并将结果放入到 map中
            }
            return map;
        } catch (Exception e) {

            System.out.println(e.getMessage());
        }
        return null;
    }


    /**
     * 与其他api 接口 对接的 post方法
     * 用于微信支付
     *
     * @param url
     * @param data
     * @return
     */
    public static String doPost(String url, String data) {
        HttpClient httpClient = HttpClients.createDefault();

        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000) //设置建立连接时间
                .setConnectionRequestTimeout(5000) //设置请求超时时间
                .setSocketTimeout(5000) //读取等待时间
                .setRedirectsEnabled(true) // 开启重定向
                .build();//构建对象

        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        httpPost.setHeader("Content-Type", "test/html;charset=UTF-8");



        if (data != null && data instanceof String) { //校验参数
            StringEntity stringEntity = new StringEntity(data, "UTF-8");
            httpPost.setEntity(stringEntity);//给请求体添加数据
        }
        try {
            HttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                String result = response.getEntity().toString();
                return result;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
