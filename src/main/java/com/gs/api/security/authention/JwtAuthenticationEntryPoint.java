package com.gs.api.security.authention;

import com.alibaba.fastjson.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * @Description TODO 自定义类 用于进行匿名用户访问资源时无权限的处理
 * @Author GuoShao
 * @Date 2020/9/22 21:37
 * @Version 1.0
 **/
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint  {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/javascript;charset=utf-8");
        //处理对应的业务
        response.getWriter().print(JSONObject.toJSONString("您未登录，没有访问权限"));
    }
}
