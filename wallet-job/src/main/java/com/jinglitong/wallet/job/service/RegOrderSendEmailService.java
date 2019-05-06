package com.jinglitong.wallet.job.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.ddbapi.model.DdbOrder;
import com.jinglitong.wallet.job.mapper.CustomerMapper;
import com.jinglitong.wallet.job.mapper.DdbOrderMapper;
import com.jinglitong.wallet.job.util.DateUtils;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

@Service
@Slf4j
public class RegOrderSendEmailService {

	@Autowired
	private CustomerMapper customerMapper;
	
	@Autowired
	private AliCloudMailService aliCloudMailService;
	
	@Autowired
	private DdbOrderMapper ddbOrderMapper; 
	
	
	@Value("${ddb_app_id}")
	private String appId;
	
	@Value("${statistics.address}")
	private String statisticsAddress;
	
	public void sendEamil() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 23:00:00");
    	String now = DateUtils.getDate()+" 23:00:00";
    	String yesTody = sdf.format(DateUtils.addDays(new Date(), -1));
		 //注册总数
		int cusCount =  customerMapper.selectCountByDate(appId,null,null);
		//今天注册
		int todayCusCount =  customerMapper.selectCountByDate(appId,yesTody,now);
		
		//今日订单数  金额
		Map<String, String> map =  ddbOrderMapper.selectCountByDate(yesTody, now);
		
		System.out.println(map.toString());
		//发送邮件
		String arr[] = statisticsAddress.split(",");
		String price = String.valueOf(map.get("price")==null ? 0:map.get("price"));
		for (int i = 0; i < arr.length; i++) {
			aliCloudMailService.sendMail(arr[i], "大地宝注册、订单统计","大地宝注册用户总数："+cusCount+" <br>今日注册用户数："+todayCusCount+""
					+ "<br> 今日订单数："+String.valueOf(map.get("count"))
					+ "<br> 今日订单总额："+price+"元");
			log.info(arr[i]+"邮件发送成功");
		}
		log.info("大地宝注册用户有："+cusCount+",今日注册总数："+todayCusCount+"每日订单数："
		+String.valueOf(map.get("count"))+"今日订单总额："+price+"元");
	}
	
}
