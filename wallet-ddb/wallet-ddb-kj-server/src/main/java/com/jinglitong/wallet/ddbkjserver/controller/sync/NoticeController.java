package com.jinglitong.wallet.ddbkjserver.controller.sync;

import com.jinglitong.wallet.ddbapi.model.DdbRewardProcessSeq;
import com.jinglitong.wallet.ddbapi.model.view.KJNotice;
import com.jinglitong.wallet.ddbkjserver.common.ConstantDict;
import com.jinglitong.wallet.ddbkjserver.mapper.DdbRewardProcessSeqMapper;
import com.jinglitong.wallet.ddbkjserver.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;


@RestController
@Slf4j
@RequestMapping("/kjnotice")
public class NoticeController {
	@Autowired
	private DdbRewardProcessSeqMapper ddbRewardProcessSeqMapper;
 
    @ResponseBody
    @RequestMapping(value = "/order.json")
    public Map order(@RequestBody KJNotice notice) {
        log.info("get order require : {}",notice.toString());
        try{
            DdbRewardProcessSeq ddbRewardProcessSeq = null;
            if(notice.getType().equals("1")) {
            	/*
            	 * 新增分红订单流水表记录
            	 * 	分红对象类型=1订单
            	 * 	分红对象ZID=对应订单表flowid
            	 *  状态=0未处理
            	 *  创建时间=系统当前时间
            	 */
            	ddbRewardProcessSeq = new DdbRewardProcessSeq();
            	ddbRewardProcessSeq.setType(1);
            	ddbRewardProcessSeq.setOrderZid(notice.getFlowId());
            	ddbRewardProcessSeq.setState(false);
            	ddbRewardProcessSeq.setCreateTime(new Date());
            	DdbRewardProcessSeq histiory = new DdbRewardProcessSeq();
            	histiory.setOrderZid(ddbRewardProcessSeq.getOrderZid());
            	int count = ddbRewardProcessSeqMapper.selectCount(histiory);
            	if(count == 0) {
            		ddbRewardProcessSeqMapper.insert(ddbRewardProcessSeq);
            	}else {
            		log.info("ddbRewardProcessSeq already existed {}",ddbRewardProcessSeq.toString());
            	}
            	
        	}else if(notice.getType().equals("2")) {
            	/*
            	 * 新增分红订单流水表记录
            	 * 	分红对象类型=2育苗
            	 * 	分红对象ZID=育苗基地购买记录表zid
            	 *  状态=0未处理
            	 *  创建时间=系统当前时间
            	 */
        		ddbRewardProcessSeq = new DdbRewardProcessSeq();
            	ddbRewardProcessSeq.setType(2);
            	ddbRewardProcessSeq.setOrderZid(notice.getFlowId());
            	ddbRewardProcessSeq.setState(false);
            	ddbRewardProcessSeq.setCreateTime(new Date());
            	DdbRewardProcessSeq histiory = new DdbRewardProcessSeq();
            	histiory.setOrderZid(ddbRewardProcessSeq.getOrderZid());
            	int count = ddbRewardProcessSeqMapper.selectCount(histiory);
            	if(count == 0) {
            		ddbRewardProcessSeqMapper.insert(ddbRewardProcessSeq);
            	}else {
            		log.info("ddbRewardProcessSeq already existed {}",ddbRewardProcessSeq.toString());
            	}
            	
            	
            }else if(notice.getType().equals("3")){
                //nothing
            }
            ConstantDict.ORDER_CONTROL.add(notice);
        } catch (Exception e){
            log.error("put ConstantDict.ORDER_CONTROL full",e);
            return  JsonUtil.toJsonError(-1,"error");
        }
        return JsonUtil.toJsonSuccess("ok",null);
    }
}
