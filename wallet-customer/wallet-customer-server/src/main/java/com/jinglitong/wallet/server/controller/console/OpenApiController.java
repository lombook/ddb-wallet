package com.jinglitong.wallet.server.controller.console;

import com.jinglitong.wallet.api.model.*;
import com.jinglitong.wallet.server.common.BatchTransfer.BatchTransferConstants;
import com.jinglitong.wallet.server.common.ErrorEnum;
import com.jinglitong.wallet.server.service.*;
import com.jinglitong.wallet.server.util.RSASignature;
import com.jinglitong.wallet.server.util.UuidUtil;
import com.jinglitong.wallet.server.service.*;
import com.jinglitong.wallet.api.model.*;
import com.jinglitong.wallet.server.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("openapi")
public class OpenApiController extends BaseController{
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private PayInfoService payInfoService;

    @Resource
    private IdWorkerService idWorkerService;

    @Resource
    private SellerService sellerService;

    @Resource
    private SellerCoinInfoService coinInfoService;

    @Resource
    private SubChainService subChainService;


    @Resource
    private RestTemplate restTemplate;

    @Value("${payPrivateKey}")
    private String privateKey;
    @Value("${payPubKey}")
    private String pubKey;








    /**
     * 创建订单
     * @return
     */
    @RequestMapping(value = "createOrder",method = RequestMethod.POST)
    public Map<String,Object> createPayOrder(@RequestBody @Valid  PayInfo payInfo,BindingResult bindingResult, @RequestHeader HttpHeaders headers){
        try{
            if(bindingResult.hasErrors()){
                return JsonUtil.toJsonError(ErrorEnum.ERROR_26001);
            }
            if(null == payInfo || BigDecimal.ZERO.compareTo(payInfo.getAmount())==1 ){
                return JsonUtil.toJsonError(ErrorEnum.ERROR_31456);
            }


            Seller seller = new Seller();
            seller.setAccessKey(headers.get(BatchTransferConstants.APP_KEY).get(0));
            seller = sellerService.queryOneSeller(seller);
            if(null == seller || null == seller.getId()){
                return JsonUtil.toJsonError(ErrorEnum.ERROR_31448);
            }
            payInfo.setSellerName(seller.getShortName());


            SellerCoinInfo coinInfo = new SellerCoinInfo();
            coinInfo.setSellerId(seller.getSellerId());
            coinInfo.setCoinCode(payInfo.getCoinCode());
            coinInfo = coinInfoService.queryOneSellerCoinInfo(coinInfo);
            if(null == coinInfo || StringUtils.isEmpty(coinInfo.getChainCurrency())){
                return JsonUtil.toJsonError(ErrorEnum.ERROR_31457);
            }
            //链
            payInfo.setChainType(coinInfo.getChainCurrency());
            payInfo.setChainId(coinInfo.getChainId());
            //币
            payInfo.setCoinType(coinInfo.getCoinCurrency());
            payInfo.setCoinId(coinInfo.getCoinId());
            //收款地址
            payInfo.setReceiveAddress(coinInfo.getSellerAddress());
            payInfo.setSellerId(coinInfo.getSellerId());
            payInfo.setOrderNo(idWorkerService.nextId()+"");

            SubChain subChain = subChainService.selectByCoinId(coinInfo.getCoinId());
            payInfo.setIssuer(subChain.getTokenAddress());
            
            payInfo.setzId(UuidUtil.getUUID());
            payInfo.setAppId(seller.getAppId());
            if(payInfoService.addPayInfo(payInfo)){
                try{

                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.set(BatchTransferConstants.APP_KEY,BatchTransferConstants.APP_KEY_VALUE);
                    String timstamp = System.currentTimeMillis()+"";
                    httpHeaders.set(BatchTransferConstants.TIMESTAMP,timstamp);
                    String signData = "appkey="+BatchTransferConstants.APP_KEY_VALUE+";timestamp="+timstamp;
                    httpHeaders.set(BatchTransferConstants.SIGN, RSASignature.sign(signData.getBytes(),privateKey));

                    MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
                    httpHeaders.setContentType(type);



                    MultiValueMap<String, String> params= new LinkedMultiValueMap<>();
                    params.add("orderNo",payInfo.getOrderNo());
                    params.add("sellerOrderNo",payInfo.getSellerOrderNo());
                    params.add("payStatus","0");
                    HttpEntity requestEntity = new HttpEntity(params, httpHeaders);

                    ResponseEntity<String> response = restTemplate.postForEntity(seller.getSynNotify(),requestEntity, String.class);

                }catch (Exception e){
                    logger.info("支付同步回调");
                    e.printStackTrace();
                }

                return JsonUtil.toJsonSuccess("新增订单信息成功",payInfo.getOrderNo());
            }else {
                return JsonUtil.toJsonError(ErrorEnum.ERROR_31440);
            }
        }catch (Exception e){
            logger.error("创建订单失败："+e.getMessage());
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31452);
        }

    }

    /**
     * 查询订单
     * @return
     */
    @RequestMapping("queryPayOrder")
    public Map<String,Object> queryPayOrder(@RequestParam("orderNo") String orderNo, @RequestHeader HttpHeaders headers){

        Seller seller = new Seller();
        seller.setAccessKey(headers.get(BatchTransferConstants.APP_KEY).get(0));
        seller = sellerService.queryOneSeller(seller);
        if(null == seller || null == seller.getId()){
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31448);
        }
        PayInfo payInfo = new PayInfo();
        payInfo.setSellerId(seller.getSellerId());
        payInfo.setOrderNo(orderNo);
        payInfo = payInfoService.queryOnePayInfo(payInfo);
        if(null ==payInfo || StringUtils.isEmpty(payInfo.getSellerOrderNo()) || null == payInfo.getSellerId())
        {
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31451);
        }
        if(null != payInfo && payInfo.getId()>0){
            return JsonUtil.toJsonSuccess("订单信息",payInfo);
        }else {
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31451);
        }
    }

    /**
     * 查询商家支付币信息
     * @param headers
     * @return
     */
    @RequestMapping("coinCodes")
    public Map<String,Object> getCoinCodes(@RequestHeader HttpHeaders headers){
        Seller seller = new Seller();
        seller.setAccessKey(headers.get(BatchTransferConstants.APP_KEY).get(0));
        seller = sellerService.queryOneSeller(seller);
        if(null == seller || null == seller.getId()){
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31448);
        }

        SellerCoinInfo coinInfo = new SellerCoinInfo();
        coinInfo.setSellerId(seller.getSellerId());
        List<SellerCoinInfo> coinInfos =  coinInfoService.querySellerCoinInfos(coinInfo);
        List<MiniSellerCoin> miniSellerCoins = new ArrayList<>();
        for (SellerCoinInfo coinInfo1:coinInfos){
            miniSellerCoins.add(SellerCoinInfo.getMiniSellerCoin(coinInfo1));
        }
        return JsonUtil.toJsonSuccess("订单信息",miniSellerCoins);
    }

}
