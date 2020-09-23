package com.gs.api.shop.dao;

import com.gs.api.core.BaseDao;
import com.gs.api.shop.entity.Role;
import com.gs.api.shop.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Description TODO 角色
 * @Author GuoShao
 * @Date 2020/9/21 18:39
 * @Version 1.0
 **/
@Mapper
public interface RoleDao extends BaseDao<Role> {

    List<Role>  selectRole(Map map);
}
