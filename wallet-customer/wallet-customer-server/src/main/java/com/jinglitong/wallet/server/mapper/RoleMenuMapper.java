package com.jinglitong.wallet.server.mapper;

import com.jinglitong.wallet.api.model.Menu;
import com.jinglitong.wallet.api.model.RoleMenu;
import com.jinglitong.wallet.server.util.MyMapper;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Service;

@Service
public interface RoleMenuMapper extends MyMapper<RoleMenu> {
    List<Menu> getByRoleId(String roleId);
    @Delete("DELETE FROM role_menu WHERE role_id = #{roleId} ")
    int deleteByRoleId(String roleId);
}