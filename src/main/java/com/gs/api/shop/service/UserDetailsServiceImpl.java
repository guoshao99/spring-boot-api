package com.gs.api.shop.service;

import com.gs.api.shop.dao.RoleDao;
import com.gs.api.shop.dao.UserDao;
import com.gs.api.shop.entity.Role;
import com.gs.api.shop.entity.User;
import com.gs.api.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Description TODO 配置UserDetailsService的实现类 用于加载用户信息
 * @Author GuoShao
 * @Date 2020/9/22 21:26
 * @Version 1.0
 **/
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || "".equals(username)) {
            throw new RuntimeException("用户不能为空");
        }
        // 调用方法查询用户
        User user = userDao.selectByName(username);

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        // 存放权限
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        Map map = new HashMap();
        map.put("username",username);
        List<Role> roles = roleDao.selectRole(map);
        // 查询权限
        for (Role role:roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getName()));
        }
        return new org.springframework.security.core.userdetails
                .User(username,new BCryptPasswordEncoder().encode(user.getPassword()),authorities);
    }


}
