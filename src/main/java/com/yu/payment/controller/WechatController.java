package com.yu.payment.controller;

import com.yu.payment.config.WeChatConfig;
import com.yu.payment.domain.JsonData;
import com.yu.payment.domain.User;
import com.yu.payment.domain.VideoOrder;
import com.yu.payment.mapper.VideoOrderMapper;
import com.yu.payment.service.IUserService;
import com.yu.payment.service.IVideoOrderService;
import com.yu.payment.utils.JwtUtils;
import com.yu.payment.utils.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.*;

@Controller
@RequestMapping("/api/v1/wechat")
public class WechatController {

    //引用微信配置类
    @Autowired
    private WeChatConfig weChatConfig;

    //引用微信配置类
    @Autowired
    private IUserService userService;

    //调用微信订单服务层
    @Autowired
    private IVideoOrderService videoOrderService;

    /**
     * 拼装微信扫一扫登陆的 URL
     *
     * @param accessPage
     * @return
     */
    @GetMapping("/login_url")
    @ResponseBody
    public JsonData loginUrl(@RequestParam(value = "access_page", required = true) String accessPage) throws UnsupportedEncodingException {
        // 返回code时，使用的地址
        String redirectUrl = weChatConfig.getOpenRedirectUrl(); //获取微信开放平台重定向地址
        String callbackUrl = URLEncoder.encode(redirectUrl, "GBK");//将字符串的重定向地址 编码 为回调地址
        String format = String.format(weChatConfig.getOpenQrcodeUrl(), weChatConfig.getAppid(), callbackUrl, accessPage);//利用string的format函数将预定模型的回调url进行替换
        return JsonData.buildSuccess(format);
    }


    /**
     * 用户确认授权后，回调函数
     *
     * @param code
     * @param state
     * @param response
     */
    @GetMapping("/user/callback")
    public void weChatUserCallback(@RequestParam(value = "code", required = true) String code,
                                   String state, HttpServletResponse response) throws IOException {

        //此时应该检查 微信给我们的code ，下一步好使用code去和微信申请需要的数据
        User user = userService.saveWeChatUser(code);
        //点击登陆之后重新跳转网页
        if (user != null) {
            String token = JwtUtils.geneToken(user);
            //注意名称的编码问题，小心乱码，可以使用url 重新编码
            //注意跳转问题，查看是否需要 + http:// ，如果不加，则为站内跳转，可能会出现问题，如404
            response.sendRedirect(state + "?token=" + token + "&username=" + URLEncoder.encode(user.getName(), "UTF-8") + "&head_img=" + user.getHeadImg());
        }
    }

    /**
     * 微信支付 通知回调url,接受微信传给我们的数据
     *
     * @param request
     * @param response
     */
    @RequestMapping("/order/callback")
    public void weChatUserCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {

        ServletInputStream is = request.getInputStream();
        //Buffered是包装设计模式,性能更高
        BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuffer sb = new StringBuffer();
        String line;
        //逐行读取请求的数据
        while ((line = in.readLine()) != null) {
            sb.append(line);
        }
        in.close();
        is.close();
        //将读取到的数据转化为map
        Map<String, String> callBackMap = WXPayUtil.xmlToMap(sb.toString());
        //验证有没有数据传回来
        if (callBackMap != null) {
            SortedMap<String, String> sortedMap = WXPayUtil.getSortedMap(callBackMap);
            //校验签名是否正确
            if (WXPayUtil.isCorrectSign(sortedMap, weChatConfig.getKey())) {
                //校验交易标识是否正确  ，交易是否成功需要查看result_code来判断
                if ("SUCCESS".equals(sortedMap.get("result_code"))) {
                    //支付成功
                    VideoOrder dbVideoOrder = videoOrderService.findByOutTradeNo(sortedMap.get("out_trade_no"));
                    if (dbVideoOrder != null && dbVideoOrder.getState() == 0) { //如果订单存在，且没有支付过
                        VideoOrder videoOrder = new VideoOrder();
                        videoOrder.setOpenid(sortedMap.get("openid"));
                        videoOrder.setNotifyTime((Timestamp) new Date());
                        videoOrder.setDel(0);
                        videoOrder.setState(1);
                        int isFlag = videoOrderService.updateVideoOderByOutTradeNo(videoOrder);
                        if (isFlag == 1) {
                            //交易成功,需要通知微信订单处理成功
                           /* Map<String, String> map = new HashMap<>();
                            map.put("return_code","SUCCESS");
                            String result = WXPayUtil.mapToXml(map);
                            response.getOutputStream().print(result);*/
                            response.setContentType("text/xml");
                            response.getWriter().println("SUCCESS");
                        }
                    }
                }
            }
        }
        //其余都处理失败
        response.setContentType("text/xml");
        response.getWriter().println("FAIL");
    }

}
