package com.jinglitong.wallet.ddbserver.controller.sync;

import com.alibaba.fastjson.JSON;
import com.jinglitong.wallet.ddbserver.common.ErrorEnum;
import com.jinglitong.wallet.ddbserver.mapper.CustomerMapper;
import com.jinglitong.wallet.ddbserver.service.DdbIntegralWalletService;
import com.jinglitong.wallet.ddbserver.service.OrderService;
import com.jinglitong.wallet.ddbserver.service.RequireHistoryService;
import com.jinglitong.wallet.ddbserver.util.DateUtils;
import com.jinglitong.wallet.ddbserver.util.JsonUtil;
import com.jinglitong.wallet.ddbserver.util.MD5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/sync")
public class SyncController{

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RequireHistoryService requireHistoryService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private DdbIntegralWalletService ddbIntegralWalletService;

    @Autowired
    private CustomerMapper customerMapper;



    /**
     * MEEI信息同步
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/meidata.json")
    public Map MeiData(@RequestBody String mei,HttpServletRequest request) {
        logger.info("MEI同步"+mei);
        String flowId = "";
        HashMap<String, String> map = new HashMap<>();
        try{
            Map<String, Object> m = MD5Utils.json2Map(mei);//json 转map
            String oldKey = (String) m.get("sign");
            m.remove("sign");//map去掉sign
            flowId = (String) m.get("flowId");
            String createTime = (String)m.get("createTime");
            String date = DateUtils.getUnixTime(createTime);
            String key = MD5Utils.makeSign(JSON.toJSONString(m));//对参数加密
           // if(oldKey.equals(key)) {//比较加密串是否一致
            if(true) {//比较加密串是否一致
                map.put("flowIId",flowId);
                Boolean aBoolean = requireHistoryService.checkFlowId(flowId);
               // Boolean aBoolean = true;
                if(aBoolean){
                   // ddbAssetDemandService.SynMEI(date,m);//存储请求
                    ddbIntegralWalletService.receiveBf(m,date);
                }else {
                    logger.info("BF"+ ErrorEnum.ERROR_41002.getMsg());
                    return JsonUtil.toJsonSynError(ErrorEnum.ERROR_41002,flowId);
                }
            }else {
                logger.info("BF"+ErrorEnum.ERROR_41001.getMsg());
                return JsonUtil.toJsonSynError(ErrorEnum.ERROR_41001,flowId);
            }
        }catch (Exception e){
            logger.info("解密异常"+e);
            logger.error("收取BF解密异常",ErrorEnum.ERROR_41001.getMsg());
            return JsonUtil.toJsonSynError(ErrorEnum.ERROR_41001,flowId);
        }

        return JsonUtil.toJsonSynSuccess("success",flowId);
    }
    



    /**
     * 商城订单请求
     * @param result
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value ="/getBfInfo.json", method = RequestMethod.POST)
    public Map orderInfo(@RequestBody String result) throws Exception {
        String flowId="";
        boolean res = false;
        try {
            logger.info("订单参数request={}",result);
            boolean flag = MD5Utils.toSign(result);//校验
            Map<String ,Object> map =  MD5Utils.json2Map(result);
            flowId = (String)map.get("flowId");
            if(!flag) {
                return JsonUtil.toJsonSynError( ErrorEnum.ERROR_41001,flowId);
            }
            Boolean aBoolean = requireHistoryService.checkFlowId(flowId);//排重
            //Boolean aBoolean = true;
            if(aBoolean){
                //Service处理订单开始
                res = orderService.orderHandle(map);
                if (!res){
                    return JsonUtil.toJsonSynError(ErrorEnum.ERROR_41007,flowId);
                }
            }else {
                logger.info("宝分："+flowId+ ErrorEnum.ERROR_41002.getMsg());
               // return JsonUtil.toJsonSynError(ErrorEnum.ERROR_41002,flowId);
                return JsonUtil.toJsonSynSuccess("已处理的flowId：",flowId);
            }
        }catch (Exception e) {
            logger.error("订单处理异常,e=",e);
            return JsonUtil.toJsonSynError(ErrorEnum.ERROR_41003,flowId);
        }
        if(res) {
            return JsonUtil.toJsonSynSuccess("成功",flowId);
        }else {
            return JsonUtil.toJsonSynError(ErrorEnum.ERROR_41003,flowId);
        }

    }

   /* *//**
     * 触发生成订单
     *//*
    @ResponseBody
    @RequestMapping(value ="/createNewOrder.json", method = RequestMethod.POST)
    public Map createnewOrder(@RequestBody Customer cust) throws Exception {
       // orderService.createNewOrder();
        List<Customer> customers = customerMapper.selectAll();
        for(int i=0;i<10000;i++){
            Map<String , Object> param = new HashMap<>();
            param.put("flowId", "fyyT"+cust.getId()+i);
            param.put("ruleId", cust.getAccount());
            param.put("shopTrade", "132");
            param.put("productNum", "1");
            param.put("price", "200");
            String custId = customers.get((int) Math.round(Math.random() * 50)).getCustId();
            param.put("userId",custId );
            param.put("nonceStr", Math.round(Math.random()*20));
            orderService.orderHandle(param);
        }
        return JsonUtil.toJsonSuccess("true");
    }

    *//**
     * 数据测试
     *//*
    @ResponseBody
    @RequestMapping(value ="/test", method = RequestMethod.POST)
    public Map test(@RequestBody String cust) throws Exception {
        orderService.IntegeAddMinu();
        return JsonUtil.toJsonSuccess("true");
    }*//**
     * 数据测试
     *//*
    @ResponseBody
    @RequestMapping(value ="/test1", method = RequestMethod.POST)
    public Map test1(@RequestBody String cust) throws Exception {
        orderService.IntegeAddMinu();
        return JsonUtil.toJsonSuccess("true");
    }*//**
     * 数据测试
     *//*
    @ResponseBody
    @RequestMapping(value ="/test2", method = RequestMethod.POST)
    public Map test2(@RequestBody String cust) throws Exception {
        orderService.IntegeAddMinu();
        return JsonUtil.toJsonSuccess("true");
    }*/
}
