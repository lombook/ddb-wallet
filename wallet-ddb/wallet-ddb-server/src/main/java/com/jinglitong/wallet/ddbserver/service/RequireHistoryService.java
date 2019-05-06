package com.jinglitong.wallet.ddbserver.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.wallet.api.model.Admin;
import com.jinglitong.wallet.api.model.AppWallet;
import com.jinglitong.wallet.api.model.view.AppWalletVo;
import com.jinglitong.wallet.api.model.view.PropertieVO;
import com.jinglitong.wallet.ddbapi.model.DdbRequireHistory;
import com.jinglitong.wallet.ddbserver.mapper.AppWalletMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbRequireHistoryMapper;
import com.jinglitong.wallet.ddbserver.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.orderbyhelper.OrderByHelper;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional
public class RequireHistoryService {

    @Autowired
    public DdbRequireHistoryMapper ddbRequireHistoryMapper;


    public Boolean checkFlowId(String flowId) {
        DdbRequireHistory ddbRequireHistory = ddbRequireHistoryMapper.selectByFlowId(flowId);
        if(ddbRequireHistory != null){
            return false;
        }else {
            return  true;
        }
    }

    public Boolean ddbRequireHistoryInsert(String flowId,String date){
        DdbRequireHistory ddbRequireHistory = new DdbRequireHistory();
        ddbRequireHistory.setCreateTime(date);
        ddbRequireHistory.setFlowId(flowId);
        int insert = ddbRequireHistoryMapper.insert(ddbRequireHistory);
        if(insert >0){
            return true;
        }else{
            return false;
       }
    }
}
