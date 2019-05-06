package com.jinglitong.wallet.ddbserver.mapper;

import java.util.List;

import com.jinglitong.wallet.api.model.Area;
import com.jinglitong.wallet.ddbserver.util.MyMapper;
import org.springframework.stereotype.Component;

@Component
public interface AreaMapper extends MyMapper<Area> {


    List<Area> getprovinces();
    List<Area> getcities(String provinceId);
    
    
    


}
