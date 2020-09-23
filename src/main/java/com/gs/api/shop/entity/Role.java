package com.gs.api.shop.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Description TODO
 * @Author GuoShao
 * @Date 2020/9/22 21:27
 * @Version 1.0
 **/

@Data
@Table(name= "t_role")
public class Role {
    @Id
    private Integer id;
    private String username;
    private String name;

}
