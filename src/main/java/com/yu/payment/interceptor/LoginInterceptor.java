package com.yu.payment.interceptor;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.deploy.net.HttpResponse;
import com.yu.payment.domain.JsonData;
import com.yu.payment.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginInterceptor implements HandlerInterceptor {


    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //看看请求头中有没有该token
        String token = request.getHeader("token");
        //如果token没找到，再去请求参数中看下
        if (token == null) {
            token = request.getParameter("token");
        }
        //如果找到token ，代表找到了，进行验证操作
        if (token != null) {
            //先进行解密，
            Claims claims = JwtUtils.checkToken(token);
            Integer id = (Integer) claims.get("id");
            String name = (String) claims.get("name");
            String img = (String) claims.get("img");
            //再将数据放到request 域中
            request.setAttribute("user_id", id);
            request.setAttribute("name", name);
            request.setAttribute("img", img);

            //代表认证成功
            return true;
        }

        //如果没有进入到if 代表认证失败，最好有失败理由。
        sendErrorMessage(response, JsonData.buildError("请登录"));
        return false;
    }

    public void sendErrorMessage(HttpServletResponse response, Object obj) throws IOException {
        response.setContentType("application/json;charset = UTF-8");
        PrintWriter writer = response.getWriter();
        String errorMessage = objectMapper.writeValueAsString(obj);
        writer.write(errorMessage);
        writer.close();
        writer.flush();

    }
}
