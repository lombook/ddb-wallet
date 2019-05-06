package com.jinglitong.wallet.ddbserver.mapper;

import java.util.HashMap;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.jinglitong.wallet.api.model.DeviceInfo;
import com.jinglitong.wallet.ddbserver.util.MyMapper;

@Service
public interface DeviceInfoMapper extends MyMapper<DeviceInfo> {

	HashMap<String, Object> getAccountInfo(@Param("account")String account,@Param("appId")String appId);
}
