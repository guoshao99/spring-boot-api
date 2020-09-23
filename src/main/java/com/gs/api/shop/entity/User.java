package com.gs.api.shop.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Description TODO
 * @Author GuoShao
 * @Date 2020/9/21 18:31
 * @Version 1.0
 **/
@Data
@Table(name= "t_user")
public class User {

    @Id
    private String id;
    private String username;
    private String password;

}
