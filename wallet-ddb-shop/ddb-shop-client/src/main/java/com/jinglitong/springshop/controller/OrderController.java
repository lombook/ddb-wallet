package com.jinglitong.springshop.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.jinglitong.springshop.ErrorEnum;
import com.jinglitong.springshop.IConstants;
import com.jinglitong.springshop.ShopRespose;
import com.jinglitong.springshop.entity.DdbIntegralWallet;
import com.jinglitong.springshop.entity.Receiver;
import com.jinglitong.springshop.jwt.JwtTokenUtils;
import com.jinglitong.springshop.mapper.DdbIntegralWalletMapper;
import com.jinglitong.springshop.servcie.CartService;
import com.jinglitong.springshop.servcie.CustomerService;
import com.jinglitong.springshop.servcie.OrderService;
import com.jinglitong.springshop.servcie.ReceiverService;
import com.jinglitong.springshop.vo.LoginVo;
import com.jinglitong.springshop.vo.request.AppNSelVO;
import com.jinglitong.springshop.vo.request.CalculateCartVo;
import com.jinglitong.springshop.vo.request.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fyy
 * @create 2019-01-21-10:16}
 * 订单处理
 */

@RestController
@RequestMapping("/order")
@Api(value = "订单 API", tags = "订单 API")
public class OrderController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ReceiverService receiverService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private DdbIntegralWalletMapper integralWalletMapper;
    
    @ApiOperation(value = " 购物车结算")
    @RequestMapping(value = "/calculateCart", method = RequestMethod.POST)
    public ShopRespose getNotices(@ApiParam(name = "X-Auth-Token", value = "登录token", required = false) @RequestHeader(name = "X-Auth-Token", required = false, defaultValue = "") String token,
                                  @RequestBody CalculateCartVo calculateCartVo) {
        LoginVo info = customerService.getCustomerInfo(JwtTokenUtils.getUsername(token));
        HashMap resultMap = new HashMap();
        if (calculateCartVo.getCartItemsList() != null && calculateCartVo.getCartItemsList().size() > 0) {
        	
            List<Map<String, Object>> skuList = cartService.calculateCart(calculateCartVo, resultMap);
           //如果不是同一币种，报错
            String currencyCode = "";
            if(skuList.size() == calculateCartVo.getCartItemsList().size()){
           	 currencyCode  = skuList.get(0).get("currencyCode").toString();
                for (Map<String,Object> sku: skuList) {
                    String  currencyResult = sku.get("currencyCode").toString();
                    if(!currencyCode.equals(currencyResult)){
                    	return new ShopRespose(ErrorEnum.ERROR_3103);
                    }
                }
            }
        } else {
            return new ShopRespose(ErrorEnum.ERROR_3008);
        }
        Receiver receiver = receiverService.selectByDefault(info.getZid());
        
        DdbIntegralWallet wallet = new DdbIntegralWallet();
        wallet.setCustId(info.getZid());
        wallet.setInteName("wubei_all");
        wallet = integralWalletMapper.selectOne(wallet);
        resultMap.put("wubei_all", wallet == null ? 0 : new BigDecimal(wallet.getAmount()).divide(new BigDecimal(100)));
        wallet = new DdbIntegralWallet();
        wallet.setCustId(info.getZid());
        wallet.setInteName("xianbei_all");
        wallet = integralWalletMapper.selectOne(wallet);
        resultMap.put("xianbei_all", wallet == null ? 0 : new BigDecimal(wallet.getAmount()).divide(new BigDecimal(100)));           
        resultMap.put("receiver", receiver);
        return new ShopRespose(IConstants.SUCCESS, "成功", resultMap);
    }

    @ApiOperation(value = "生成订单")
    @RequestMapping(value = "/createOrder", method = RequestMethod.POST)
    public ShopRespose createOrder(@ApiParam(name = "X-Auth-Token", value = "登录token", required = false) @RequestHeader(name = "X-Auth-Token", required = false, defaultValue = "") String token,
                                   @RequestBody CalculateCartVo calculateCartVo) {
        LoginVo info = customerService.getCustomerInfo(JwtTokenUtils.getUsername(token));
        if (calculateCartVo.getCartItemsList() != null && calculateCartVo.getCartItemsList().size() > 0 && calculateCartVo.getReceiver() != null) {          
        	try{
        		return orderService.createOrder(calculateCartVo,info);
        	}catch(Exception e){
        		String res = e.getMessage();
        		if(!StringUtil.isEmpty(res) && res.contains("code")){
        			Map jsStr = JSONObject.parseObject(res);
            		ShopRespose s = new ShopRespose();
            		s.setCode((Integer)(jsStr.get("code")));
            		s.setMessage((String)jsStr.get("msg"));
            		return s;
        		}
        		e.printStackTrace();
        		return new ShopRespose(ErrorEnum.ERROR_3117);	
        	}
        } else {
            return new ShopRespose(ErrorEnum.ERROR_3008);
        }
    }

    @ApiOperation(value = "获取订单列表")
    @RequestMapping(value = "/getOrderList", method = RequestMethod.POST)
    public ShopRespose getOrderList(@ApiParam(name = "X-Auth-Token", value = "登录token", required = false) @RequestHeader(name = "X-Auth-Token", required = false, defaultValue = "") String token,
                                   @RequestBody OrderVO orderVO) {
        LoginVo info = customerService.getCustomerInfo(JwtTokenUtils.getUsername(token));
        Map<String,Object> resultMap = orderService.getOrderList(info.getZid(),orderVO);
        return new ShopRespose(IConstants.SUCCESS, "成功", resultMap);
    }

    @ApiOperation(value = "获取订单详情")
    @RequestMapping(value = "/getOrderDetail", method = RequestMethod.POST)
    public ShopRespose getOrderDetail(@ApiParam(name = "X-Auth-Token", value = "登录token", required = false) @RequestHeader(name = "X-Auth-Token", required = false, defaultValue = "") String token,
                                    @RequestBody OrderVO orderVO) {
        LoginVo info = customerService.getCustomerInfo(JwtTokenUtils.getUsername(token));
        Map<String,Object> resultMap = orderService.getOrderDetail(info.getZid(),orderVO);
        return new ShopRespose(IConstants.SUCCESS, "成功", resultMap);
    }

    @ApiOperation(value = "取消/完成订单")
    @RequestMapping(value = "/cancelOrder", method = RequestMethod.POST)
    public ShopRespose cancelOrder(@ApiParam(name = "X-Auth-Token", value = "登录token", required = false) @RequestHeader(name = "X-Auth-Token", required = false, defaultValue = "") String token,
                                      @RequestBody OrderVO orderVO) {
        LoginVo info = customerService.getCustomerInfo(JwtTokenUtils.getUsername(token));
        Map<String,Object> resultMap = orderService.updateOrderStatus(info.getZid(),orderVO);
        if(resultMap.get("updateNum").equals(0)){
            return new ShopRespose(ErrorEnum.ERROR_3101);
        }
        return new ShopRespose(IConstants.SUCCESS, "成功", resultMap);
    }


}
