package com.jinglitong.wallet.reportserver.mapper;

import com.jinglitong.wallet.api.model.AdminRole;
import com.jinglitong.wallet.api.model.Role;
import com.jinglitong.wallet.reportserver.util.MyMapper;
import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminRoleMapper extends MyMapper<AdminRole> {

    List<Role> getRoleByAdminId(String admin_id);
    @Delete("DELETE FROM admin_role WHERE admin_id = #{adminId}")
    void deleteByAdminId(String adminId);
}