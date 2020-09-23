package com.gs.api.security.config;

import com.gs.api.security.authention.JwtAuthenticationEntryPoint;
import com.gs.api.security.authention.JwtAuthenticationFilter;
import com.gs.api.security.authention.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("userDetailsServiceImpl")
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 此处需要添加passwordEncoder(new BCryptPasswordEncoder()); 否则会报错
        //Spring security 5.0中新增了多种加密方式,也改变了默认的密码格式.
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    /**
     * 安全配置
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 跨域共享
        http.cors()
                .and()
                // 跨域伪造请求限制无效
                .csrf().disable()
                .authorizeRequests()
                .antMatchers().permitAll() //都可以访问
                // 访问/data需要ADMIN角色
                .antMatchers("/api/data").hasRole("ADMIN")
                .and()
                // 添加JWT登录拦截器
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                // 添加JWT鉴权拦截器
                .addFilter(new JwtAuthorizationFilter(authenticationManager()))
                .sessionManagement()
                // 设置Session的创建策略为：Spring Security永不创建HttpSession 不使用HttpSession来获取SecurityContext
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 异常处理
                .exceptionHandling()
                // 匿名用户访问无权限资源时的异常
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint());
    }

    /**
     * 跨域配置
     * @return 基于URL的跨域配置信息
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 注册跨域配置
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}