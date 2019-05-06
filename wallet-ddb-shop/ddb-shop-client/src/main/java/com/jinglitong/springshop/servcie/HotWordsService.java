package com.jinglitong.springshop.servcie;

import com.jinglitong.springshop.entity.HotKeyWords;
import com.jinglitong.springshop.mapper.HotKeyWordsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author fyy
 * @create 2019-01-10-12:33}
 */
@Service
@Slf4j
public class HotWordsService {

    @Autowired
    private HotKeyWordsMapper hotKeyWordsMapper;

    public List<HotKeyWords> getHotWordsList(String num) {
       return hotKeyWordsMapper.selectTopNum(num);
    }
}
