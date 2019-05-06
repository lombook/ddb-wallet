package com.jinglitong.wallet.server.mapper;

import java.util.List;

import com.jinglitong.wallet.api.model.Area;
import com.jinglitong.wallet.server.util.MyMapper;
import org.springframework.stereotype.Component;

@Component
public interface AreaMapper extends MyMapper<Area> {


    List<Area> getprovinces();
    List<Area> getcities(String provinceId);
    
    
    


}
