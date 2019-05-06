package com.jinglitong.wallet.ddbserver.mapper;

import com.jinglitong.wallet.api.model.Role;
import com.jinglitong.wallet.ddbserver.util.MyMapper;

import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface RoleMapper extends MyMapper<Role> {

    List<Role> getRoles(HashMap<String, Object> selectmap);

    List<Role> selectRoles();
}