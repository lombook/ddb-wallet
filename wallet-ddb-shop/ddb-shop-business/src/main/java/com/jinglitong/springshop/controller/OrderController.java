package com.jinglitong.springshop.controller;

import com.jinglitong.springshop.IConstants;
import com.jinglitong.springshop.ShopRespose;
import com.jinglitong.springshop.service.OrderService;
import com.jinglitong.springshop.vo.request.OrderRequestVo;
import com.jinglitong.springshop.vo.request.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(value = "订单管理接口",tags = "订单管理接口 API")
@Controller
@RequestMapping(value = "/business/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    /**
     * 获取订单列表
     * @return
     */
    @ApiOperation(value = "订单列表")
    @ResponseBody
    @RequestMapping(value = "/getOrderList", method = RequestMethod.POST)
    public ShopRespose getOrderList(@ApiParam(name = "X-Auth-Token", value = "登录token", required = false) @RequestHeader(name = "X-Auth-Token", required = false, defaultValue = "") String token,
                                       @RequestBody OrderRequestVo orderRequestVo) {
        Map<String,Object> orders = orderService.getOrders(orderRequestVo);
        return new ShopRespose(IConstants.SUCCESS,"查询成功",orders);
    }

    /**
     * 获取用户详情
     * @return
     */
    @ApiOperation(value = "订单详情")
    @ResponseBody
    @RequestMapping(value = "/getOrderDetail", method = RequestMethod.POST)
    public ShopRespose getCustomer(@ApiParam(name = "X-Auth-Token", value = "登录token", required = false) @RequestHeader(name = "X-Auth-Token", required = false, defaultValue = "") String token,
                                   @RequestBody OrderVO orderVO ) {
        Map<String, Object> orderDetail = orderService.getOrderDetailManage(null, orderVO);
        return new ShopRespose(IConstants.SUCCESS,"成功",orderDetail);
    }

}