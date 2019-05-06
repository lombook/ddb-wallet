/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.jinglitong.wallet.ddbserver.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.wallet.ddbserver.mapper.MenuMapper;
import com.jinglitong.wallet.ddbserver.mapper.RoleMapper;
import com.jinglitong.wallet.ddbserver.mapper.RoleMenuMapper;
import com.jinglitong.wallet.ddbserver.util.DateUtils;
import com.jinglitong.wallet.ddbserver.util.UuidUtil;
import com.jinglitong.wallet.api.model.Menu;
import com.jinglitong.wallet.api.model.Role;
import com.jinglitong.wallet.api.model.RoleMenu;
import com.jinglitong.wallet.api.model.view.PageVO;
import com.jinglitong.wallet.api.model.view.RoleVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private MenuMapper menuMapper;


    public HashMap<String,Object> getRoleList(PageVO page) {
        if (page.getPage() != null && page.getRows() != null) {
            PageHelper.startPage(page.getPage(), page.getRows());
        }
        List<Role> roles = roleMapper.selectRoles();
        PageInfo pageinfo = new PageInfo(roles);
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageCount",pageinfo.getTotal());
        map.put("roles",roles);
        return map;
    }
    @Transactional
    public int crestedrole(RoleVO roleVO) {
        String menus = roleVO.getMenus();
        int insert = 0;
        Role role = new Role();
        String uuid = UuidUtil.getUUID();
        role.setRoleId(uuid);
        role.setEnable(roleVO.getEnable());
        role.setRoleDesc(roleVO.getRoleDesc());
        role.setRoleName(roleVO.getRoleName());
        String dateTime = DateUtils.getDateTime();
        role.setCreatedTime(dateTime);
        role.setUpdatedTime(dateTime);
        try{
            insert = roleMapper.insert(role);
            if(menus != null && menus != ""){
                String[] split = menus.split(",");
                for ( String menuId: split) {
                    RoleMenu roleMenu = new RoleMenu();
                    roleMenu.setRoleId(uuid);
                    roleMenu.setMenuId(menuId);
                    insert = roleMenuMapper.insert(roleMenu);
                }
            }

        }catch (Exception e){
            throw  new RuntimeException("运行时插入异常");
        }
        return insert;
    }


    public HashMap<String,Object> getRoleById(String roleId) {
        Role role= roleMapper.selectByPrimaryKey(roleId);
        if(role != null){
            List<Menu> menus = roleMenuMapper.getByRoleId(role.getRoleId());
            role.setMenus(menus);
        }
        List<Menu> allMenu = menuMapper.selectAll();
        HashMap<String, Object> map = new HashMap<>();
        map.put("role",role);
        map.put("allMenu",allMenu);
        return map;
    }

    @Transactional
    public int updateRole(RoleVO roleVO) {
        String menus = roleVO.getMenus();
        int update = 0;
        Role role = new Role();
        role.setRoleId(roleVO.getRoleId());
        role.setEnable(roleVO.getEnable());
        role.setRoleDesc(roleVO.getRoleDesc());
        role.setRoleName(roleVO.getRoleName());
        role.setUpdatedTime(DateUtils.getDateTime());
        try{
            update = roleMapper.updateByPrimaryKey(role);
            int delete = roleMenuMapper.deleteByRoleId(role.getRoleId());
            if(menus != null && menus != ""){
                String[] split = menus.split(",");
                for ( String menuId: split) {
                    RoleMenu roleMenu = new RoleMenu();
                    roleMenu.setRoleId(role.getRoleId());
                    roleMenu.setMenuId(menuId);
                    update = roleMenuMapper.insert(roleMenu);
                }
            }

        }catch (Exception e){
            throw  new RuntimeException("运行时插入异常");
        }
        return update;
    }


    public int updateRoleEnable(String roleId, Boolean enable) {
        Role role = new Role();
        role.setRoleId(roleId);
        role.setEnable(enable);
        int update = roleMapper.updateByPrimaryKeySelective(role);
     return update;
    }


    public List<Role> getAllRole() {
       return  roleMapper.selectAll();
    }

    public List<Menu> getMenus(List<Role> roles) {
        List<Menu> menus = new ArrayList<>();
        for (Role role:roles) {
            List<Menu> byRoleId = roleMenuMapper.getByRoleId(role.getRoleId());
            if(byRoleId.size() > 0){
                for (Menu menu : byRoleId) {
                    menus.add(menu);
                }
            }
        }
       return menus;
    }
}
