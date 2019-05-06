package com.jinglitong.springshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.jinglitong.springshop.IConstants;
import com.jinglitong.springshop.ShopRespose;
import com.jinglitong.springshop.entity.Business;
import com.jinglitong.springshop.entity.Deliverycorp;
import com.jinglitong.springshop.jwt.JwtTokenUtils;
import com.jinglitong.springshop.service.SupplierLoginService;
import com.jinglitong.springshop.service.SupplierOrderService;
import com.jinglitong.springshop.vo.response.OrderDeliverVo;
import com.jinglitong.springshop.vo.response.SupplierOrderVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/supplier")
@Api(value = "供应商订单列表",tags = "web商品 API")
public class SupplierOrderController {

    @Autowired
    private SupplierLoginService supplierLoginService;
    @Autowired
	private SupplierOrderService supplierOrderService;
	
    /**
     * 
     * 功能说明:待发货 和 已发货列表
     * @param token
     * @param vo
     * @return
     */
	@ApiOperation(value = "订单列表")
    @RequestMapping(value ="/selectOrderBySuppl" , method = RequestMethod.POST)
	public ShopRespose selectOrderBySuppl(@ApiParam(name = "token", value = "登录token", required = true)@RequestHeader(name = "token", required = true, defaultValue = "") String token,@RequestBody SupplierOrderVo vo) {
		ShopRespose respose = new ShopRespose<>();
		Business business = supplierLoginService.getBusinessInfo(JwtTokenUtils.getUsername(token));
		vo.setBusinessZid(business.getZid());
		PageInfo<SupplierOrderVo> info =supplierOrderService.selectOrderBySuppl(vo);
		respose.setMessage(IConstants.SUCCESS_MSG);
        respose.setData(info);
        respose.setCode(IConstants.SUCCESS);
		return respose;
	}
	
    /**
     * 
     * 功能说明:待发货 和 已发货列表
     * @param token
     * @param vo
     * @return
     */
	@ApiOperation(value = "订单列表")
    @RequestMapping(value ="/selectCompleteOrderBySuppl" , method = RequestMethod.POST)
	public ShopRespose selectCompleteOrderBySuppl(@ApiParam(name = "token", value = "登录token", required = true)@RequestHeader(name = "token", required = true, defaultValue = "") String token,@RequestBody SupplierOrderVo vo) {
		ShopRespose respose = new ShopRespose<>();
		Business business = supplierLoginService.getBusinessInfo(JwtTokenUtils.getUsername(token));
		vo.setBusinessZid(business.getZid());
		PageInfo<SupplierOrderVo> info =supplierOrderService.selectCompleteOrderBySuppl(vo);
		respose.setMessage(IConstants.SUCCESS_MSG);
        respose.setData(info);
        respose.setCode(IConstants.SUCCESS);
		return respose;
	}
	
	@ApiOperation(value = "添加物流")
    @RequestMapping(value ="/addDeliverycorp" , method = RequestMethod.POST)
	public ShopRespose addDeliverycorp(@RequestBody OrderDeliverVo vo) {
		ShopRespose respose = new ShopRespose<>();
		respose = supplierOrderService.addDeliverycorp(vo);
		 return respose;
	}
	
	@ApiOperation(value = "物流列表")
    @RequestMapping(value ="/getDeliverycorp" , method = RequestMethod.POST)
	public ShopRespose getDeliverycorp() {
		ShopRespose respose = new ShopRespose<>();
		List<Deliverycorp> info =supplierOrderService.getDeliverycorp();
		respose.setMessage(IConstants.SUCCESS_MSG);
        respose.setData(info);
        respose.setCode(IConstants.SUCCESS);
		return respose;
	}
	
	
	
}
