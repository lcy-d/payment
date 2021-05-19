package com.yu.payment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yu.payment.config.WeChatConfig;
import com.yu.payment.domain.JsonData;
import com.yu.payment.domain.Video;
import com.yu.payment.mapper.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.List;

@RestController
public class TestController {

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private WeChatConfig weChatConfig;

    @RequestMapping("/test")
    public String test() {
        return "test009";
    }

    @RequestMapping("/test_config")
    public JsonData testConfig() {
        return JsonData.buildSuccess(weChatConfig.getAppid());
    }

    @RequestMapping("/test_db")
    public String testDb() throws JsonProcessingException {
        List<Video> all = videoMapper.findAll();
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(all);
        return s;
    }


}
