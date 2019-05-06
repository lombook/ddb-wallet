package com.jinglitong.wallet.ddbserver.mapper;

import com.jinglitong.wallet.api.model.ProtocolManagement;
import com.jinglitong.wallet.ddbserver.util.MyMapper;

import java.util.List;
import java.util.Map;

public interface ProtocolManagementMapper extends MyMapper<ProtocolManagement> {

    List<ProtocolManagement> protocolManagementList();

    Integer getWalletProCount(Integer id);

    List<Map> protocolManagementSelect();
}
