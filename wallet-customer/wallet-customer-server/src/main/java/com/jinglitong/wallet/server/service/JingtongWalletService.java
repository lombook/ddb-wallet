package com.jinglitong.wallet.server.service;

import com.jinglitong.wallet.api.model.walletVo.*;
import com.jinglitong.wallet.server.conf.JingtongWalletProperty;
import com.jinglitong.wallet.server.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class JingtongWalletService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JingtongWalletProperty jingtongWalletProperty;

    /**
     * 余额
     * @param address
     * @return
     */
    public BalancesResponse getBalancesOfWallet(String address){
        address = String.format(jingtongWalletProperty.getUrl()+jingtongWalletProperty.getBalances(),address);
        BalancesResponse result = restTemplate.getForObject(address,BalancesResponse.class);
        return result;
    }

    /**
     * 余额
     * @param address
     * @return
     */
    public BalancesResponse getBalancesOfWallet(String url,String address){
        address = String.format(url+jingtongWalletProperty.getBalances(),address);
        BalancesResponse result = restTemplate.getForObject(address,BalancesResponse.class);
        return result;
    }


    /**
     * 创建钱包
     * @return
     */
    public CreateNewResponse createWallet(){
        String address = jingtongWalletProperty.getUrl()+jingtongWalletProperty.getWalletnew();
        ResponseEntity<CreateNewResponse> result = restTemplate.getForEntity(address,CreateNewResponse.class);
        return result.getBody();
    }

    /**
     * 创建钱包
     * @return
     */
    public CreateNewResponse createWallet(String url){
        String address = url+jingtongWalletProperty.getWalletnew();
        ResponseEntity<CreateNewResponse> result = restTemplate.getForEntity(address,CreateNewResponse.class);
        return result.getBody();
    }


    /**
     *支付
     * @param sourceAddress
     * @param paymentRequest
     */
    public PaymentResponse gotoPayment(String sourceAddress, PaymentRequest paymentRequest){
        String address = String.format(jingtongWalletProperty.getUrl()+jingtongWalletProperty.getGotopayment(),sourceAddress);
        paymentRequest.setClient_id(UuidUtil.getUUID()+System.currentTimeMillis());
        PaymentResponse result = restTemplate.postForObject(address,paymentRequest,PaymentResponse.class);
        return result;
    }

    /**
     *支付
     * @param sourceAddress
     * @param paymentRequest
     */
    public PaymentResponse gotoPayment(String url,String sourceAddress, PaymentRequest paymentRequest){
        String address = String.format(url+jingtongWalletProperty.getGotopayment(),sourceAddress);
        paymentRequest.setClient_id(UuidUtil.getUUID()+System.currentTimeMillis());
        PaymentResponse result = restTemplate.postForObject(address,paymentRequest,PaymentResponse.class);
        return result;
    }

    /**
     *查询交易信息
      * @param address
     * @param tradeHash
     */
    public PaymentInfoResponse getPaymentInfo(String address, String tradeHash){
        address = String.format(jingtongWalletProperty.getUrl()+jingtongWalletProperty.getPaymentinfo(),address,tradeHash);
        PaymentInfoResponse result = restTemplate.getForObject(address,PaymentInfoResponse.class);
        return result;
    }

    /**
     *查询交易信息
     * @param address
     * @param tradeHash
     */
    public PaymentInfoResponse getPaymentInfo(String url,String address,String tradeHash){
        address = String.format(url+jingtongWalletProperty.getPaymentinfo(),address,tradeHash);
        PaymentInfoResponse result = restTemplate.getForObject(address,PaymentInfoResponse.class);
        return result;
    }


    /**
     * 查询第一笔交易
     * @param address
     * @return
     */
    public JingtongTransaction getFirstJingtongTransaction(String address){
        address = String.format(jingtongWalletProperty.getUrl()+jingtongWalletProperty.getRegTime(),address);
        JingtongTransaction result = restTemplate.getForObject(address,JingtongTransaction.class);
        return result;

    }

    /**
     * 查询第一笔交易
     * @param address
     * @return
     */
    public JingtongTransaction getFirstJingtongTransaction(String url,String address){
        address = String.format(url+jingtongWalletProperty.getRegTime(),address);
        JingtongTransaction result = restTemplate.getForObject(address,JingtongTransaction.class);
        return result;
    }
}
