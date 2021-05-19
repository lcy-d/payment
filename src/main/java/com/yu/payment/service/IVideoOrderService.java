package com.yu.payment.service;

import com.yu.payment.domain.VideoOrder;
import com.yu.payment.dto.VideoOrderDto;
import org.apache.ibatis.annotations.Param;

public interface IVideoOrderService {

    String saveOrder(VideoOrderDto videoOrderDto) throws Exception;

    VideoOrder findByOutTradeNo(String outTradeNo);

    int updateVideoOderByOutTradeNo(VideoOrder videoOrder);
}
