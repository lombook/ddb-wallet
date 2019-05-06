package com.jinglitong.wallet.job.service;

import com.jinglitong.wallet.ddbapi.model.DdbShouldFrozen;
import com.jinglitong.wallet.job.mapper.DdbShouldFrozenMapper;
import com.jinglitong.wallet.job.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Slf4j
@Service
public class BFFrozenService {

    @Autowired
    private DdbShouldFrozenMapper ddbShouldFrozenMapper;

    @Autowired
    private GrowInteService growInteService;

    @Autowired
    private AliCloudMailService aliCloudMailService;

    @Value("${RealaseAdminEmailAddress}")
    private String realaseAdminEmailAddress;

    static  Integer num = 0;

    static  int test = 0;
    public void releaseBf() {
        while(true){
            log.info("开始处理之前的数据");
            Date beforeDate = DateUtils.addDays(new Date(), -1);
            List<DdbShouldFrozen> ddbShouldFrozenList = ddbShouldFrozenMapper.selectByExcutimeBeforeToday(DateUtils.formatDateTime(beforeDate));
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
                        log.info("之前数据处理异常"+e);
                        sendemail();
                        throw new RuntimeException("增长释放数据处理异常，已发送邮件。");
                    }
                }
            }
        }



        //查询当天需要释放的
        log.info("开始处理当天的数据");
        while(true){
            List<DdbShouldFrozen> ddbShouldFrozenList = ddbShouldFrozenMapper.selectByExcutime(DateUtils.getDateTime());
            if(ddbShouldFrozenList.size() == 0){
                break;
            }
            for (DdbShouldFrozen ddb:ddbShouldFrozenList) {
                log.info("处理当天的数据id:"+ddb.getId());
                if(ddb.getLeftAmount() > 0){
                    SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        log.info("======================="+(++num)+"");
                       growInteService.growIntege(ddb);
                       // MessageServiceTaskFactory.newInstance().addNewTask(new AddIntegTask(ddb));
                    }catch (Exception e){
                        log.info("今日数据处理失败"+e);
                        sendemail();
                        throw new RuntimeException("增长释放数据处理异常，已发送邮件。");
                    }
                }
            }
        }
    }

    void  sendemail(){
        String arr[] = realaseAdminEmailAddress.split(",");
        for (int i = 0; i < arr.length; i++) {
            // 向管理员 发邮件
            aliCloudMailService.sendMail(arr[i], "树贝增长释放","增长释放有失败，请查看数据和日志");
        }
    }
}
