package com.jinglitong.wallet.give.mapper;

import com.jinglitong.wallet.api.model.Menu;
import com.jinglitong.wallet.give.util.MyMapper;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public interface MenuMapper extends MyMapper<Menu> {
    Set<String> getALLMenuCode();
    Set<String> findMenuCodeByUserId(String adminId);

    List<Menu> getALLMenu();
}