package com.gs.api.shop.controller;

import com.gs.api.core.Result;
import com.gs.api.shop.entity.User;
import com.gs.api.shop.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author GuoShao
 * @Date 2020/9/21 18:47
 * @Version 1.0
 **/


@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserDao weightDao;

    @RequestMapping("/list")
    public Result get(){
        List<User> weights = weightDao.selectAll();
        return Result.success(weights);
    }

    @GetMapping("/login")
    public Result login(@RequestParam String username, @RequestParam String password)  {
        Map map = new HashMap();
        map.put("username",username);
        map.put("password",password);
        User user = weightDao.login(map);
        return Result.success(user);
    }

    @GetMapping("/data")
    private String data() {
        return "This is data.";
    }

}
