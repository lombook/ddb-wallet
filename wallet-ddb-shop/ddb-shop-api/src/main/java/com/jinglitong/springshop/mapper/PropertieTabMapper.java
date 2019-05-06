package com.jinglitong.springshop.mapper;


import com.jinglitong.springshop.entity.PropertieTab;
import com.jinglitong.springshop.utils.MyMapper;
import com.jinglitong.springshop.vo.request.PropertieTabVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PropertieTabMapper extends MyMapper<PropertieTab> {
    @Select("SELECT * FROM `propertie_tab` WHERE group_name = #{groupname} ")
    @Results({
            @Result(property = "groupName",  column = "group_name"),
            @Result(property = "groupKey", column = "group_key"),
            @Result(property = "groupValue", column = "group_value"),
            @Result(property = "keyDesc", column = "key_desc")
    })
    List<PropertieTab> selectByGroup(String groupname);


    PropertieTab checkPropertie(String groupKey, String groupName);

    @Delete("delete from propertie_tab where id=#{id} ")
    int deleteByid(String id);

    List<PropertieTab> filterAboutManagement(PropertieTabVO propertieTabVO);
}