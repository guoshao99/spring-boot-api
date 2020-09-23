package com.gs.api.util;

import com.gs.api.shop.entity.User;
import io.jsonwebtoken.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @Description TODO
 * @Author GuoShao
 * @Date 2020/9/22 21:24
 * @Version 1.0
 **/
public class JwtTokenUtil {
    // Token请求头
    public static final String TOKEN_HEADER = "Authorization";
    // Token
    public static final String TOKEN_PREFIX = "access_token ";

    public static final String REFRESH_TOKEN = "refresh_token ";
    // 过期时间
    public static final long EXPIRITION = 100 * 1000;
    //7*24*3600*1000;
    // 应用密钥
    public static final String APPSECRET_KEY = "asd4546546ewq1dasdqw84612";
    // 角色权限声明
    private static final String ROLE_CLAIMS = "role";

    /**
     * 生成Token
     */
    public static String createToken(String subject,String role) {
        Map<String,Object> map = new HashMap<>();
        map.put(ROLE_CLAIMS, role);
        String token = Jwts
                .builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(subject)
                .setClaims(map)
                .claim("username",subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRITION))
                .signWith(SignatureAlgorithm.HS256, APPSECRET_KEY).compact();
        return token;
    }

    /**
     * 获取Token中的信息 ，解析token
     */
    public static Claims getTokenClaim(String token) {
        try {
            final Claims claims = Jwts.parser().setSigningKey(APPSECRET_KEY).parseClaimsJws(token).getBody();
            return claims;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取Token失效时间
     */
    public static Date getExpirationDateFromToken(String token) {
        return getTokenClaim(token).getExpiration();
    }

    /**
     * 获取jwt发布时间
     */
    public static Date getIssuedAtDateFromToken(String token) {
        return getTokenClaim(token).getIssuedAt();
    }

    /**
     * 从Token中获取username
     */
    public static String getUsername(String token){
        Claims claims = getTokenClaim(token);
        return claims.get("username").toString();
    }

    /**
     * 从Token中获取用户角色
     */
    public static String getUserRole(String token){
        Claims claims = getTokenClaim(token);
        return claims.get("role").toString();
    }

    /**
     * 校验Token是否过期
     */
    public static boolean isExpiration(String token){
        try {
            Claims claims = getTokenClaim(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 刷新令牌
     * @param token 原令牌
     * @return 新令牌
     */
    /*public String refreshToken(String token) {
        String refreshedToken;
        try {
            Claims claims = getTokenClaim(token);
            claims.put("created", new Date());
            refreshedToken = aa(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }*/


    /**
     * 验证令牌
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        User user = (User) userDetails;
        String username = getTokenClaim(token).getSubject();
        return (username.equals(user.getUsername()) && !isExpiration(token));
    }

}
