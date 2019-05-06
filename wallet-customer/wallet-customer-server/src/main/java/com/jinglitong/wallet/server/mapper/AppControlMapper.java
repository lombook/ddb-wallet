package com.jinglitong.wallet.server.mapper;


import com.jinglitong.wallet.api.model.AppControl;
import com.jinglitong.wallet.api.model.view.AppConSelVO;
import com.jinglitong.wallet.server.util.MyMapper;
import java.util.List;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

@Service
public interface AppControlMapper extends MyMapper<AppControl> {
    List<AppConSelVO> getAppControls(AppConSelVO appConSelVO);
    @Select("SELECT MAX(app_version_code) FROM app_control WHERE app_type =#{appType}")
    Integer getVersionCount(String appType);
}