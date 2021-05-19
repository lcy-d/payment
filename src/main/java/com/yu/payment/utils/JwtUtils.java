package com.yu.payment.utils;

import com.yu.payment.domain.User;
import io.jsonwebtoken.*;

import java.util.Date;

public class JwtUtils {

    private final static String SUBJECT = "xdclass"; //发行者
    private final static long EXPIRA = 1000 * 60 * 60 * 24;  //过期时长
    private final static String APPSECRET = "xd666";

    /**
     * JWT 加密生成token
     *
     * @param user
     * @return
     */
    public static String geneToken(User user) {
        if (user == null || user.getId() == null || user.getName() == null || user.getHeadImg() == null) {
            return null;
        }
        String token = Jwts.builder().setSubject(SUBJECT)
                .claim("id", user.getId())
                .claim("name", user.getName())
                .claim("img", user.getHeadImg())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRA))
                .signWith(SignatureAlgorithm.HS256, APPSECRET).compact();
        return token;
    }

    /**
     * 解密 token
     *
     * @param token
     * @return
     */
    public static Claims checkToken(String token) {
        //入口数据校验
        if (token == null) {
            return null;
        }

        Claims body = null;
        try {
            //告诉 Jwts 要解析        设置解析签名              解析token         获得 token体
            body = Jwts.parser().setSigningKey(APPSECRET).parseClaimsJws(token).getBody();
        } catch (Exception e) {
        }
        return body;
    }

}
