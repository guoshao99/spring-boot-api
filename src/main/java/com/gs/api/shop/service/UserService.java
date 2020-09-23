package com.gs.api.shop.service;

import com.gs.api.shop.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @Description TODO 重写security的认证方法 自定义查询数据库对比验证
 * @Author GuoShao
 * @Date 2020/9/22 20:31
 * @Version 1.0
 **/
@Service
public class UserService   {

    @Autowired
    UserDao userDao;


}
