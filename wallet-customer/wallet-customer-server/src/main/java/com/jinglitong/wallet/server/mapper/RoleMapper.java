package com.jinglitong.wallet.server.mapper;

import com.jinglitong.wallet.api.model.Role;
import com.jinglitong.wallet.server.util.MyMapper;

import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface RoleMapper extends MyMapper<Role> {

    List<Role> getRoles(HashMap<String, Object> selectmap);

    List<Role> selectRoles();
}