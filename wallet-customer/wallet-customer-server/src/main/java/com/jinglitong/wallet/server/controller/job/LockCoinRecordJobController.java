package com.jinglitong.wallet.server.controller.job;

import com.jinglitong.wallet.api.feign.LockCoinRecordFeignApi;
import com.jinglitong.wallet.api.model.LockCoinRecord;
import com.jinglitong.wallet.api.model.MainChain;
import com.jinglitong.wallet.api.model.SubChain;
import com.jinglitong.wallet.api.model.view.PaymentVO;
import com.jinglitong.wallet.server.service.*;
import com.jinglitong.wallet.server.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequestMapping("lockCoinRecordJob")
@RestController
@Slf4j
public class LockCoinRecordJobController implements LockCoinRecordFeignApi {

    @Resource
    private MainChainService mainChainService;

    @Resource
    private SubChainService subChainService;

    @Resource
    private CustomerAPIService apiService;


    /**
     * 每次查询的条数
     */
    @Value("${batchtransfer.querysize}")
    private int query_per_size = 1;




    @Resource
    private LockCoinRecordService recordService;


    /**
     * 执行任务 确认锁仓是否到账
     */
    @PostMapping("confirmPayStatusFeign")
    @Override
	public void confirmPayStatus() {
		log.info("锁仓状态查询任务开始");
		LockCoinRecord queryRecord = new LockCoinRecord();
		queryRecord.setLockStatus(1); // 查询正在锁的
		int totalNum = recordService.queryLockCoinCount(queryRecord);
		if (totalNum <= 0) {
			log.info("这次没有数据");
			return;
		}
		List<LockCoinRecord> coinRecords = recordService.queryLockCoinRecords(queryRecord, 0, query_per_size);
		do {
			for (LockCoinRecord record : coinRecords) {
				// 查询状态
				SubChain subChain = new SubChain();
				subChain = subChainService.selectByCoinId(record.getCoinId());
				if (null == subChain || StringUtils.isEmpty(subChain.getChainId())) {
					log.error("找不到币：记录id =: " + record.getLockRecordId());
					continue;
				}
				// 查询链信息
				MainChain mainChain = new MainChain();
				mainChain.setChainId(subChain.getChainId());
				mainChain = mainChainService.getOneMainChain(mainChain);
				if (mainChain.getHandleName().indexOf("swt") == -1) {
					record.setLockStatus(7);
					record.setUpdateTime(DateUtils.getDateTime());
					recordService.updateLockCoinRecord(record);
					continue;
				}

				try {
					PaymentVO paymentVO = new PaymentVO();
					paymentVO.setChainId(mainChain.getChainId());
					paymentVO.setWalletId(record.getWalletId());
					paymentVO.setCustId(record.getCustId());
					paymentVO.setPayHash(record.getPayHash());
					Map<String, Object> map = apiService.getTX(paymentVO);

					if ((int) map.get("code") == 0 && (Boolean) ((Map) map.get("data")).get("success")) {
						record.setLockStatus(2); // 锁仓成功
						record.setUpdateTime(DateUtils.getDateTime());
						recordService.updateLockCoinRecord(record);
						log.info("查询结果成功:" + record.getLockRecordId());
					} else {
						log.error("查询结果失败:" + record.getLockRecordId());
						record.setLockStatus(6); // 锁仓失败
						record.setUpdateTime(DateUtils.getDateTime());
						recordService.updateLockCoinRecord(record);
					}

				} catch (Exception e) {
					log.error(e.getMessage());
					e.printStackTrace();
				}

			}
			coinRecords = recordService.queryLockCoinRecords(queryRecord, 0, query_per_size);

		} while (coinRecords.size() > 0);
		log.info("锁仓状态查询任务结束");
	}



    /**
     * 获得ip
     * @param url
     * @return
     */
    private String getIP(String url) {
        //使用正则表达式过滤，
        String re = "((http|ftp|https)://)(([a-zA-Z0-9._-]+)|([0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}))(([a-zA-Z]{2,6})|(:[0-9]{1,4})?)";
        String str = "";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(re);
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        //若url==http://127.0.0.1:9040或www.baidu.com的，正则表达式表示匹配
        if (matcher.matches()) {
            str = url;
        } else {
            String[] split2 = url.split(re);
            if (split2.length > 1) {
                String substring = url.substring(0, url.length() - split2[1].length());
                str = substring;
            } else {
                str = split2[0];
            }
        }
        return str;
    }

}
