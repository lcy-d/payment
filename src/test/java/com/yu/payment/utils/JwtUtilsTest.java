package com.yu.payment.utils;

import com.yu.payment.domain.User;
import io.jsonwebtoken.Claims;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;


public class JwtUtilsTest {

    @Test
    public void TestGeneToken() {
        User user = new User();
        user.setId(9);
        user.setName("jack");
        user.setHeadImg("www.baidu.com");
        String token = JwtUtils.geneToken(user);
        System.out.println(token);
        //eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ4ZGNsYXNzIiwiaWQiOjksIm5hbWUiOiJqYWNrIiwiaW1nIjoid3d3LmJhaWR1LmNvbSIsImlhdCI6MTYxODQ0NzA0OCwiZXhwIjoxNjE4NTMzNDQ4fQ.LBh-WpNm_ZtXcVAJDA27de8XmSVkCHRtya6lRIwSPS4
        //eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ4ZGNsYXNzIiwiaWQiOjksIm5hbWUiOiJqYWNrIiwiaW1nIjoid3d3LmJhaWR1LmNvbSIsImlhdCI6MTYxODQ0NzA2OSwiZXhwIjoxNjE4NTMzNDY5fQ.Ga2wQcLuCO7cpplG2b9nxqhV393zVR3zVhwWLayOZxs
    }


    @Test
    public void TestCheckToken() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ4ZGNsYXNzIiwiaWQiOjksIm5hbWUiOiJqYWNrIiwiaW1nIjoid3d3LmJhaWR1LmNvbSIsImlhdCI6MTYxODQ0NzA2OSwiZXhwIjoxNjE4NTMzNDY5fQ.Ga2wQcLuCO7cpplG2b9nxqhV393zVR3zVhwWLayOZxs";
        Claims claims = JwtUtils.checkToken(token);


    }
}
