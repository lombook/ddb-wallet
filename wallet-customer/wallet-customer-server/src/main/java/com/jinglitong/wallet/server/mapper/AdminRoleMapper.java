package com.jinglitong.wallet.server.mapper;

import com.jinglitong.wallet.api.model.AdminRole;
import com.jinglitong.wallet.api.model.Role;
import com.jinglitong.wallet.server.util.MyMapper;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Service;

@Service
public interface AdminRoleMapper extends MyMapper<AdminRole> {

    List<Role> getRoleByAdminId(String admin_id);
    @Delete("DELETE FROM admin_role WHERE admin_id = #{adminId}")
    void deleteByAdminId(String adminId);
}