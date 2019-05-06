package com.jinglitong.wallet.give.service;

import com.jinglitong.wallet.ddbapi.model.DdbRequireHistory;
import com.jinglitong.wallet.give.mapper.DdbRequireHistoryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
