package com.jinglitong.wallet.ddbserver.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.wallet.ddbapi.model.DdbCustIntegralWallet;
import com.jinglitong.wallet.ddbapi.model.DdbShouldFrozen;
import com.jinglitong.wallet.ddbserver.mapper.DdbCustIntegralWalletMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbShouldFrozenMapper;
import com.jinglitong.wallet.ddbserver.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.orderbyhelper.OrderByHelper;

import javax.persistence.criteria.CriteriaBuilder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class BFFrozenService {

    @Autowired
    private DdbShouldFrozenMapper ddbShouldFrozenMapper;

    @Autowired
    private GrowInteService growInteService;

    static  int test = 0;
    public void releaseBf() {
        while(true){
            log.info("开始处理之前的数据");
            List<DdbShouldFrozen> ddbShouldFrozenList = ddbShouldFrozenMapper.selectByExcutimeBeforeToday();
            if(ddbShouldFrozenList.size() == 0){
                break;
            }
            for (DdbShouldFrozen ddbshouFrozen:ddbShouldFrozenList) {
                log.info("处理之前的数据id:"+ddbshouFrozen.getId());
                if(ddbshouFrozen.getLeftAmount() > 0){
                    SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        Date excuteTime=sDateFormat.parse(ddbshouFrozen.getExcuteTime());
                        //转账
                        growInteService.frozenTransfer(excuteTime,ddbshouFrozen);
                    }catch (Exception e){
                        log.info("日期装换失败"+e);
                    }

                }
            }
        }



        //查询当天需要释放的
        log.info("开始处理当天的数据");
        List<DdbShouldFrozen> ddbShouldFrozenList = ddbShouldFrozenMapper.selectByExcutime();
        for (DdbShouldFrozen ddb:ddbShouldFrozenList) {
            log.info("处理当天的数据id:"+ddb.getId());
            if(ddb.getLeftAmount() > 0){
                SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    growInteService.growIntege(ddb);
                }catch (Exception e){
                    log.info("日期装换失败"+e);
                }
            }
        }
    }

}
