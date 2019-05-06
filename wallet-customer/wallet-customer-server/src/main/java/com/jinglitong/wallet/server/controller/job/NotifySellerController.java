package com.jinglitong.wallet.server.controller.job;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.jinglitong.wallet.api.feign.NotifySellerFeignApi;
import com.jinglitong.wallet.api.model.PayInfo;
import com.jinglitong.wallet.api.model.Seller;
import com.jinglitong.wallet.server.common.BatchTransfer.BatchTransferConstants;
import com.jinglitong.wallet.server.service.JingtongWalletService;
import com.jinglitong.wallet.server.service.MainChainService;
import com.jinglitong.wallet.server.service.PayInfoService;
import com.jinglitong.wallet.server.service.SellerService;
import com.jinglitong.wallet.server.util.RSASignature;

import lombok.extern.slf4j.Slf4j;

@RequestMapping("notifySellerJob")
@RestController
@Slf4j
public class NotifySellerController  implements NotifySellerFeignApi{ 
	
    @Resource
    private RestTemplate restTemplate;
    
    @Resource
    private PayInfoService payInfoService;

    @Resource
    private JingtongWalletService jingtongWalletService;

    @Resource
    private SellerService sellerService;

    @Resource
    private MainChainService mainChainService;

    @Value("${payPrivateKey}")
    private String privateKey;
    
    @Value("${payPubKey}")
    private String pubKey;

    /**
     * 每次查询的条数
     */
    @Value("${batchtransfer.querysize}")
    private int query_per_size = 1;

    @PostMapping("excueteTaskFeign")
	public void excueteTask() {
    	log.info("通知任务开始");
		int totalNum = payInfoService.getHavedPayCountInfoByExample();
		if (totalNum <= 0) {
			log.info("这次没有数据，任务结束！");
			return;
		}
		List<PayInfo> payInfos = payInfoService.getHavedPayInfoByExample(0, query_per_size);
		do {
			for (PayInfo info : payInfos) {
				// 查询商家
				Seller seller = new Seller();
				seller.setSellerId(info.getSellerId());
				seller = sellerService.queryOneSeller(seller);
				try {
					String notiryUrl = seller.getAsynNotify();
					HttpHeaders httpHeaders = new HttpHeaders();
					httpHeaders.set(BatchTransferConstants.APP_KEY, BatchTransferConstants.APP_KEY_VALUE);
					String timstamp = System.currentTimeMillis() + "";
					httpHeaders.set(BatchTransferConstants.TIMESTAMP, timstamp);
					String signData = "appkey=" + BatchTransferConstants.APP_KEY_VALUE + ";timestamp=" + timstamp;
					httpHeaders.set(BatchTransferConstants.SIGN, RSASignature.sign(signData.getBytes(), privateKey));

					MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
					httpHeaders.setContentType(type);

					MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
					params.add("orderNo", info.getOrderNo());
					params.add("sellerOrderNo", info.getSellerOrderNo());
					params.add("payStatus", info.getPayStatus() + "");
					HttpEntity requestEntity = new HttpEntity(params, httpHeaders);

					ResponseEntity<String> response = restTemplate.postForEntity(notiryUrl, requestEntity,
							String.class);
					if (response.getBody().equalsIgnoreCase("1")) {
						info.setNoticeTime(null == info.getNoticeTime() ? 1 : info.getNoticeTime() + 1);
						info.setNoticeStatus(1); // 通知成功
					} else {
						info.setNoticeTime(null == info.getNoticeTime() ? 1 : info.getNoticeTime() + 1);
						info.setNoticeStatus(2); // 未收到反馈
					}
					payInfoService.updatePayInfo(info);

				} catch (Exception e) {
					log.error(e.getMessage());
					e.printStackTrace();
					info.setNoticeTime(null == info.getNoticeTime() ? 1 : info.getNoticeTime() + 1);
					info.setNoticeStatus(2); // 未收到反馈
					payInfoService.updatePayInfo(info);
				}
				payInfos = payInfoService.getHavedPayInfoByExample(0, query_per_size);
			}

		} while (payInfos.size() > 0);
		log.info("通知任务结束");
	}

}
