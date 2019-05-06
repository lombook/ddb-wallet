package com.jinglitong.wallet.reportserver.mapper;

import com.jinglitong.wallet.api.model.Menu;
import com.jinglitong.wallet.reportserver.util.MyMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface MenuMapper extends MyMapper<Menu> {
    Set<String> getALLMenuCode();
    Set<String> findMenuCodeByUserId(String adminId);

    List<Menu> getALLMenu();
}