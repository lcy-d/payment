package com.yu.payment.service.impl;

import com.yu.payment.config.WeChatConfig;
import com.yu.payment.domain.User;
import com.yu.payment.domain.Video;
import com.yu.payment.domain.VideoOrder;
import com.yu.payment.dto.VideoOrderDto;
import com.yu.payment.mapper.UserMapper;
import com.yu.payment.mapper.VideoMapper;
import com.yu.payment.mapper.VideoOrderMapper;
import com.yu.payment.service.IVideoOrderService;
import com.yu.payment.utils.CommonUtils;
import com.yu.payment.utils.HttpUtils;
import com.yu.payment.utils.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Service
public class VideoOrderServiceImpl implements IVideoOrderService {

    @Autowired
    private VideoOrderMapper videoOrderMapper;
    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatConfig weChatConfig;


    /**
     * 下订单
     *
     * @param videoOrderDto
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public String saveOrder(VideoOrderDto videoOrderDto) throws Exception {


        //查找视频信息
        Video dbVideo = videoMapper.findById(videoOrderDto.getVideoId());
        //查找用户信息
        User dbUser = userMapper.findById(videoOrderDto.getUserId().toString());

        //生成订单
        VideoOrder videoOrder = new VideoOrder();

        videoOrder.setIp(videoOrderDto.getIp());
        videoOrder.setVideoId(dbVideo.getId());
        videoOrder.setVideoImg(dbVideo.getCoverImg());
        videoOrder.setTotalFee(dbVideo.getPrice());
        videoOrder.setVideoTitle(dbVideo.getTitle());

        videoOrder.setState(0);
        videoOrder.setDel(0);
        videoOrder.setCreateTime(new Date());
        videoOrder.setOutTradeNo(CommonUtils.generateUUID());

        videoOrder.setOpenid(dbUser.getOpenId());
        videoOrder.setNickname(dbUser.getName());
        videoOrder.setHeadImg(dbUser.getHeadImg());
        videoOrder.setUserId(dbUser.getId());

        //想数据库插入数据
        videoOrderMapper.insert(videoOrder);

        //生成签名
        String codeUrl = this.unifiedOrder(videoOrder);
        //统一下单
        //获取 codeUrl
        //生成二维码
        return null;
    }

    /**
     * 调用微信接口，用于统一下单
     *
     * @param videoOrder
     * @return
     */
    public String unifiedOrder(VideoOrder videoOrder) throws Exception {

        //先生成签名

        // 需要一个SortedMap

        SortedMap<String, String> params = new TreeMap<>();
        params.put("appid", weChatConfig.getAppid());
        params.put("mch_id", weChatConfig.getMchId());
        params.put("nonce_str", CommonUtils.generateUUID());
        params.put("body", "123");
        params.put("out_trade_no", videoOrder.getOutTradeNo());
        params.put("total_fee", videoOrder.getTotalFee().toString());
        params.put("spbill_create_ip", videoOrder.getIp());
        //设置 通知url
        params.put("notify_url", weChatConfig.getPayCallBackUrl());
        params.put("trade_type", "NATIVE");

        //sign签名 key   xdclasss20182018xdclass2018x018d
//        String sign = WXPayUtil.createSign(params, weChatConfig.getKey());
        String sign = WXPayUtil.createSign(params,weChatConfig.getKey());
//        String payXml1= WXPayUtil.mapToXml(params);

        params.put("sign", sign);
        //map转为xml
        String payXml = WXPayUtil.mapToXml(params);
/*        boolean correctSign = WXPayUtil.isCorrectSign(params, weChatConfig.getKey());
        System.out.println(payXml);*/

        // 再统一下单
        String orderStr = HttpUtils.doPost(WeChatConfig.getUnifiedOrderUrl(), payXml);
        if (orderStr == null) {
            return null;
        }
        Map<String, String> orderMap = WXPayUtil.xmlToMap(orderStr);
        if (orderMap != null) {
            return orderMap.get("code_url");
        }

        return "";
    }

    @Override
    public VideoOrder findByOutTradeNo(String outTradeNo) {
        return videoOrderMapper.findByOutTradeNo(outTradeNo);
    }

    @Override
    public int updateVideoOderByOutTradeNo(VideoOrder videoOrder) {
        return videoOrderMapper.updateVideoOderByOutTradeNo(videoOrder);
    }
}
