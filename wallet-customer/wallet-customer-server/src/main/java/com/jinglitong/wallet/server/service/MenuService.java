/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.jinglitong.wallet.server.service;

import com.jinglitong.wallet.server.mapper.MenuMapper;
import com.jinglitong.wallet.api.model.Menu;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {

    @Autowired
    private MenuMapper menuMapper;

    public Set<String> findMenuCodeByUserId(String adminId) {
        return menuMapper.findMenuCodeByUserId(adminId);
    }

    public Set<String> getAllMenuCode() {
        return menuMapper.getALLMenuCode();
    }


    public HashMap<String, Object> getMenuList() {
        List<Menu> menus = menuMapper.selectAll();
        HashMap<String, Object> map = new HashMap<>();
        map.put("menuList",menus);
        return map;
    }

    public List<Menu> getAllMenus() {
        List<Menu> menus = menuMapper.selectAll();
        return menus;
    }
}
