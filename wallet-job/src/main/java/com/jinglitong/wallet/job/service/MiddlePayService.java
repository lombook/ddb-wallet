package com.jinglitong.wallet.job.service;


import com.jinglitong.wallet.api.model.*;
import com.jinglitong.wallet.api.model.logic.LPayResVO;
import com.jinglitong.wallet.api.model.logic.LPaymentVO;
import com.jinglitong.wallet.ddbapi.model.FrozenReleaseMiddlePay;
import com.jinglitong.wallet.job.common.spring.ApplicationContextHelper;
import com.jinglitong.wallet.job.mapper.*;
import com.jinglitong.wallet.job.util.DateUtils;
import com.jinglitong.wallet.job.util.UuidUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class MiddlePayService {

    @Autowired
    private TransactionalService transactionalService;

    @Autowired
    private FrozenReleaseDetailRuleMapper frozenReleaseDetailRuleMapper;

    @Autowired
    private FrozenReleaseMiddlePayMapper frozenReleaseMiddlePayMapper;

    @Autowired
    private FrozenReleaseRuleMapper frozenReleaseRuleMapper;

    /**
     * 预生成转账数据
     * @return
     */
   public Boolean createMiddelPayRecord(){
       String dateTime = DateUtils.getDateTime();
       log.info("===========生成转账中间表开始==============");
       while (true){
           List<FrozenReleaseDetailRule> list = frozenReleaseDetailRuleMapper.selectTodayRules();
           if(list.size()>0){
               for (FrozenReleaseDetailRule detailRule : list) {
                   FrozenReleaseRule rule = detailRule.getRule();
                   //修改规则正在执行状态
                   Integer frStatus = rule.getFrStatus();
                   if(frStatus == 0){
                       FrozenReleaseRule frozenReleaseRule = new FrozenReleaseRule();
                       frozenReleaseRule.setRuleId(rule.getRuleId());
                       frozenReleaseRule.setFrStatus(1);
                       int i = frozenReleaseRuleMapper.updateByRuleIdSelective(frozenReleaseRule);
                       if(i <= 0){
                           log.error("大规则修改状态失败1"+rule.getRuleId());
                       }
                   }

                   Integer detailStatus = detailRule.getDetailStatus();
                   if(detailStatus == 0){
                       FrozenReleaseDetailRule detailRuleUpdate = new FrozenReleaseDetailRule();
                       detailRuleUpdate.setDetailId(detailRule.getDetailId());
                       detailRuleUpdate.setDetailStatus(1);
                       int i = frozenReleaseDetailRuleMapper.updateByDetailIdSelective(detailRuleUpdate);
                       if(i <= 0){
                           log.error("小规则修改状态失败1"+detailRule.getDetailId());
                       }
                   }
                   transactionalService.saveMiddlePay(detailRule);
               }
           }else {
               break;
           }
       }
       log.info("===========生成转账中间表结束==============");
       return true;
   }
}
