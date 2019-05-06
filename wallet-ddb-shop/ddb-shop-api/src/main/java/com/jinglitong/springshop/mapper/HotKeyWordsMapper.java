package com.jinglitong.springshop.mapper;

import com.jinglitong.springshop.entity.HotKeyWords;
import com.jinglitong.springshop.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HotKeyWordsMapper extends MyMapper<HotKeyWords> {
    List<HotKeyWords> selectTopNum(@Param("num") String num);
}