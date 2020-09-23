package com.gs.api.security.authention;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gs.api.util.JwtTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description TODO 权限拦截器
 * 当访问需要权限校验的URL(URL需要经过配置的) 则会来到此拦截器 在该拦截器中对传来的Token进行校验
 * @Author GuoShao
 * @Date 2020/9/22 21:35
 * @Version 1.0
 **/
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /**
     * 在过滤之前和之后执行的事件
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain chain) throws IOException, ServletException {
        String tokenHeader = request.getHeader(JwtTokenUtil.TOKEN_HEADER);

        //检测 token 是否是我们系统中签发的
        if (!checkIsTokenAuthorizationHeader(tokenHeader)) {
            writeJson(response, "token 不是我们系统签发的");
            return;
        }
        handleTokenExpired(response,request,chain);
        // 若请求头中有token,设置认证信息
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(tokenHeader));
        super.doFilterInternal(request, response, chain);
    }

    /**
     * token过期处理
     */
    private void handleTokenExpired(HttpServletResponse response, HttpServletRequest request, FilterChain filterChain) throws IOException, ServletException {
        // 去掉前缀 获取Token字符串
        String refreshToken = request.getHeader(JwtTokenUtil.TOKEN_HEADER).replace(JwtTokenUtil.TOKEN_PREFIX, "");
        // 用户名称
        String username = JwtTokenUtil.getUsername(refreshToken);
        // 从Token中解密获取用户角色
        String role = JwtTokenUtil.getUserRole(refreshToken);
        // 生成token
        String newToken = JwtTokenUtil.createToken(username, role);
        // 设置请求头
        response.addHeader(JwtTokenUtil.TOKEN_HEADER, newToken);
        // 若请求头中有token,设置认证信息
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(newToken));
        filterChain.doFilter(request,response);
    }

    /**
     * 从token中获取用户信息并新建一个token
     * @param tokenHeader 字符串形式的Token请求头
     * @return 带用户名和密码以及权限的Authentication
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader) {
        // 去掉前缀 获取Token字符串
        String token = tokenHeader.replace(JwtTokenUtil.TOKEN_PREFIX, "");

        // 验证token是否过期
        boolean expiration = JwtTokenUtil.isExpiration(token);
        System.out.println("Token是否过期:"+expiration);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        System.out.println("令牌签发时间：" + sdf.format(JwtTokenUtil.getIssuedAtDateFromToken(token)));
        System.out.println("令牌过期时间：" + sdf.format(JwtTokenUtil.getExpirationDateFromToken(token)));

        // 从Token中解密获取用户名
        String username = JwtTokenUtil.getUsername(token);
        // 从Token中解密获取用户角色
        String role = JwtTokenUtil.getUserRole(token);
        // 将[ROLE_XXX,ROLE_YYY]格式的角色字符串转换为数组
        String[] roles = StringUtils.strip(role, "[]").split(", ");
        Collection<SimpleGrantedAuthority> authorities=new ArrayList<>();
        for (String s:roles){
            authorities.add(new SimpleGrantedAuthority(s));
        }
        if (username != null){
            return new UsernamePasswordAuthenticationToken(username, null,authorities);
        }
        return null;
    }

    /**
     * 写 json 数据给前端
     */
    private void writeJson(HttpServletResponse response, String msg) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        Map<String, String> params = new HashMap<>(4);
        params.put("msg", msg);
        response.getWriter().print(OBJECT_MAPPER.writeValueAsString(params));
    }


    /**
     * 判断是否是系统中登录后签发的token
     */
    private boolean checkIsTokenAuthorizationHeader(String authorizationHeader) {
        if (StringUtils.isBlank(authorizationHeader)) {
            return false;
        }
        if (!StringUtils.startsWith(authorizationHeader, JwtTokenUtil.TOKEN_PREFIX)) {
            return false;
        }
        return true;
    }
}
