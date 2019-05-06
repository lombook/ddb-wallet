package com.jinglitong.wallet.reportserver.mapper;

import com.jinglitong.wallet.api.model.Admin;
import com.jinglitong.wallet.api.model.view.AdminSelVO;
import com.jinglitong.wallet.reportserver.util.MyMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminMapper extends MyMapper<Admin> {
    List<Admin> getAdminList(AdminSelVO admin);

    Admin selectByUsername(String username);

    Admin selectByAdminId(String adminId);
}