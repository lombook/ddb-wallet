package com.jinglitong.wallet.ddbserver.service;

import com.alibaba.fastjson.JSON;
import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.common.utils.StringUtils;
import com.jinglitong.wallet.ddbapi.feign.NoticeKJFeignApi;
import com.jinglitong.wallet.ddbapi.model.*;
import com.jinglitong.wallet.ddbapi.model.view.KJNotice;
import com.jinglitong.wallet.ddbserver.common.storage.AliCloudMailService;
import com.jinglitong.wallet.ddbserver.mapper.*;
import com.jinglitong.wallet.ddbserver.util.AliMQServiceUtil;
import com.jinglitong.wallet.ddbserver.util.DateUtils;
import com.jinglitong.wallet.ddbserver.util.UuidUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Service
public class OrderService {
	
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private DdbOrderMapper ddbOrderMapper;
    @Autowired
    private SplitOrderService splitOrderService;

	/*@Autowired
	private DdbMqMessageRecordMapper ddbMqMessageRecordMapper; 
    @Autowired
    private RequireHistoryService requireHistoryService;
	
	@Autowired
	private DdbGoodsRuleMapper ddbGoodsRuleMapper;
	
	@Autowired
	private DdbActiveGiveRuleMapper DdbActiveGiveRuleMapper;
	
	@Autowired
	private DdbRewardRuleMapper ddbRewardRuleMapper;

	@Autowired
	private OrderTreeService orderTreeService;*/
    
    public boolean orderHandle(Map<String, Object> map) {
        String  flowId = (String)map.get("flowId");
        String  userId = (String)map.get("userId");
        Customer customer = customerMapper.selectByCustId(userId);
        if(customer == null){
            log.info("没有该用户信息custId："+userId);
            return false;
        }
        //排重入库，订单插入ddbOrder表和订单树的保存
        DdbOrder ddbOrder = splitOrderService.insertDdbOrder(map,flowId);
		
		// 修改订单状态
		ddbOrderMapper.updateStateByFlowId(1, ddbOrder.getFlowId());

		return true;
    }

}
