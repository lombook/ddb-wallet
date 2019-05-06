package com.jinglitong.wallet.server.controller.console;

import com.jinglitong.wallet.server.common.ErrorEnum;
import com.jinglitong.wallet.api.model.PayInfo;
import com.jinglitong.wallet.api.model.view.PayInfoVo;
import com.jinglitong.wallet.server.service.PayInfoService;
import com.jinglitong.wallet.server.util.JsonUtil;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("console/payInfo")
public class PayInfoController extends BaseController{

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private PayInfoService payInfoService;


    /**
     * 支付信息 分页信息
     * @param payInfo
     * @return
     */
    @RequestMapping("infos")
    public Map<String,Object> getPayInfosByPage(@RequestBody  PayInfoVo payInfo){
        return JsonUtil.toJsonSuccess("查询成功",payInfoService.queryPayInfoCanPay(payInfo.getOrderNo()));
    }
    /**
     * 后台查询支付信息
     * @param payInfo
     * @return
     */
    @RequestMapping("infoList")
    public Map<String,Object> getPayInfos(@RequestBody  PayInfoVo payInfo){
    	setGlobalAdminAppId(payInfo);
       HashMap map = payInfoService.queryPayInfos(payInfo);
        return  JsonUtil.toJsonSuccess("查询成功",map);
    }



    @GetMapping("/orderInfo/{orderNo}.json")
    public Map<String,Object> getOrderInfo(@PathVariable("orderNo") String orderNo){

        //订单是否存在
        PayInfo info = new PayInfo();
        info.setOrderNo(orderNo);
        info = payInfoService.queryOnePayInfo(info);
        if(null ==info || StringUtils.isEmpty(info.getSellerOrderNo()) || null == info.getSellerId())
        {
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31451);
        }

        return JsonUtil.toJsonSuccess("订单信息",info);
    }


    /**
     * 用户支付列表 分页信息
     * @return
     */
    @RequestMapping("payInfos.json")
    public Map<String,Object> getPayInfosByPage2(@RequestBody PayInfoVo payInfoVo){

        payInfoVo.setCustId("126");//当前用户
        Map<String, Object> stringObjectMap = payInfoService.queryPayInoByPage(payInfoVo);
        return JsonUtil.toJsonSuccess("查询成功",stringObjectMap);
    }


//    /**
//     * 支付订单
//     * @param payCoinVo
//     * @return
//     */
//    @RequestMapping(value = "payOrder",method = RequestMethod.POST)
//    public Map<String,Object> payOrder(PayCoinVo payCoinVo){
//        //验证用户
//        Customer customer = (Customer) SessionUtil.getUserInfo();
//        if(null ==customer || StringUtils.isEmpty(customer.getCustId()))
//        {
//            return JsonUtil.toJsonError(ErrorEnum.ERROR_21005);
//        }
//
//        //订单是否存在
//        PayInfo info = new PayInfo();
//        info.setOrderNo(payCoinVo.getOrderNo());
//        info = payInfoService.queryOnePayInfo(info);
//        if(null ==info || StringUtils.isEmpty(info.getSellerOrderNo()) || null == info.getSellerId())
//        {
//            return JsonUtil.toJsonError(ErrorEnum.ERROR_31451);
//        }
//        //金额不匹配
//        if(info.getAmount().compareTo(new BigDecimal(payCoinVo.getAmount()))==0){
//            return JsonUtil.toJsonError(ErrorEnum.ERROR_31453);
//        }
//        PaymentVO paymentVO = new PaymentVO();
//        paymentVO.setCustId(customer.getCustId());
//
//        return payInfoService.sendPayInfo(info,paymentVO);
//    }

    @RequestMapping(value = "test",method = RequestMethod.POST)
    public String  test(@RequestBody Map map, @RequestHeader HttpHeaders headers){

        logger.info("测试支付");
        return "1";
    }
}
