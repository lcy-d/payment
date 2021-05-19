package com.yu.payment.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.yu.payment.domain.JsonData;
import com.yu.payment.domain.VideoOrder;
import com.yu.payment.dto.VideoOrderDto;
import com.yu.payment.service.IVideoOrderService;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 订单接口
 */
@RestController
//@RequestMapping("/user/api/v1/order")
@RequestMapping("/api/v1/order")
public class OrderController {

    private Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
    private Logger datalogger = (Logger) LoggerFactory.getLogger("dataLogger");

    @Autowired
    private IVideoOrderService videoOrderService;

    /**
     * 用户下单
     *
     * @param videoId
     * @param request
     * @return
     */
    @GetMapping("/add")
    public void saveOrder(@RequestParam(value = "video_id", required = true) Integer videoId,
                          HttpServletRequest request, HttpServletResponse response) throws Exception {

//        String userId = request.getParameter("user_id");
//        String ipAddr = IpUtils.getIpAddr(request);
        //用于测试阶段的数据
        String ipAddr = "192.168.202.101";
        Integer userId = 1;

        VideoOrderDto videoOrderDto = new VideoOrderDto();
        videoOrderDto.setUserId(userId);
        videoOrderDto.setVideoId(videoId);
        videoOrderDto.setIp(ipAddr);


        String codeUrl = videoOrderService.saveOrder(videoOrderDto);
        //如果回调地址为空，则抛出空指针异常
        if (codeUrl == null) {
            throw new NullPointerException();
        }

        try {
            //生成二维码
            Map<EncodeHintType, Object> hints = new HashMap<>();
            //设置二维码纠错等级
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            //设置编码类型
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            //设置二维码的内容 形状 以及长 宽 和二维码的纠错等级与编码
            BitMatrix bitMatrix = new MultiFormatWriter().encode(codeUrl, BarcodeFormat.QR_CODE, 400, 400, hints);
            //获取输出流
            ServletOutputStream os = response.getOutputStream();
            //使用谷歌第三方对象输出 二维码，
            MatrixToImageWriter.writeToStream(bitMatrix, "png", os);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
