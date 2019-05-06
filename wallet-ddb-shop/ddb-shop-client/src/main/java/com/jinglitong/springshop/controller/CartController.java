package com.jinglitong.springshop.controller;

import com.github.pagehelper.PageInfo;
import com.jinglitong.springshop.ErrorEnum;
import com.jinglitong.springshop.IConstants;
import com.jinglitong.springshop.ShopRespose;
import com.jinglitong.springshop.jwt.JwtTokenUtils;
import com.jinglitong.springshop.servcie.CartService;
import com.jinglitong.springshop.servcie.CustomerService;
import com.jinglitong.springshop.service.RedisService;
import com.jinglitong.springshop.vo.LoginVo;
import com.jinglitong.springshop.vo.request.CartVo;
import com.jinglitong.springshop.vo.response.CartResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @ClassName ProductController
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/1/7 17:34
 * @Version 1.0
 **/
@RestController
@RequestMapping("/cart")
@Api(value = "购物车 API",tags = "购物车 API")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private RedisService redisService;


    @ApiOperation(value = "获取购物列表")
    @PostMapping(value = "/getCartList")
    public ShopRespose getProductList(@ApiParam(name = "X-Auth-Token", value = "登录token", required = false)@RequestHeader(name = "X-Auth-Token", required = false, defaultValue = "") String token,
                                      @ApiParam(name = "clientId", value = "客户端Id", required = true)@RequestHeader(name = "clientId", required = true,  defaultValue = "")String clientId,
                                      @RequestBody @Valid CartVo cart
                                      ){
        PageInfo<CartResponseVo> resultData = new PageInfo<>();
        if(StringUtils.isNotEmpty(token)){
            LoginVo info = customerService.getCustomerInfo(JwtTokenUtils.getUsername(token));
            //info.setZid("514f3aa1256243c4a9dbab84cb8f9a8e");
            cart.setClientId("");
            //用户已登录状态查询购物车
            cart.setCustId(info.getZid());
            cart.setPageSize(Integer.MAX_VALUE);
            resultData = cartService.getCustomerCart(cart);
        }else {
            //用户未登录状态查询购物车
            if(StringUtils.isEmpty(cart.getClientId())){
                cart.setClientId(clientId);
            }
            if(StringUtils.isNotEmpty(cart.getClientId())){
                resultData = cartService.getCustomerCart(cart);
            }else {
                return new ShopRespose(ErrorEnum.ERROR_3001);
            }
        }
        return new ShopRespose(IConstants.SUCCESS,"成功",resultData);
    }

    @ApiOperation(value = "增加购物项")
    @PostMapping(value = "/addCartItem")
    public ShopRespose addCartItem(@ApiParam(name = "X-Auth-Token", value = "登录token", required = false)@RequestHeader(name = "X-Auth-Token", required = false, defaultValue = "") String token,
                                   @ApiParam(name = "clientId", value = "客户端Id", required = true)@RequestHeader(name = "clientId", required = true,  defaultValue = "")String clientId,
                                   @RequestBody @Valid CartVo cart
    ){
        Integer flag = 1;
        if(StringUtils.isNotEmpty(token) && StringUtils.isNotEmpty(cart.getSkuId())){
            LoginVo info = customerService.getCustomerInfo(JwtTokenUtils.getUsername(token));
            //info.setZid("514f3aa1256243c4a9dbab84cb8f9a8e");
            //用户已登录状态
            cart.setClientId("");
            cart.setCustId(info.getZid());
             flag = cartService.addCartItem(cart);
        }else {
            //用户未登录状态
            if(StringUtils.isEmpty(cart.getClientId())){
                cart.setClientId(clientId);
            }
            if(StringUtils.isNotEmpty(cart.getClientId()) && StringUtils.isNotEmpty(cart.getSkuId())){
                flag = cartService.addCartItem(cart);
            }else {
                return new ShopRespose(ErrorEnum.ERROR_3001);
            }
        }
        //1成功 2数量超过限制 3sku不存在 4购物车商品总数超过限制 5购物车不存在 6购物项不存在
        if(flag == 1){
            return new ShopRespose(IConstants.SUCCESS,"成功",flag);
        }else if (flag == 2){
            return new ShopRespose(ErrorEnum.ERROR_3002);
        }else if (flag == 3){
            return new ShopRespose(ErrorEnum.ERROR_3003);
        }else if (flag == 4){
            return new ShopRespose(ErrorEnum.ERROR_3004);
        }else if (flag == 5){
            return new ShopRespose(ErrorEnum.ERROR_3005);
        }else {
            return new ShopRespose(ErrorEnum.ERROR_3006);
        }
    }

    @ApiOperation(value = "删除购物项")
    @PostMapping(value = "/deleteCartItem")
    public ShopRespose deleteCartItem(@ApiParam(name = "X-Auth-Token", value = "登录token", required = false)@RequestHeader(name = "X-Auth-Token", required = false, defaultValue = "") String token,
                                      @ApiParam(name = "clientId", value = "客户端Id", required = true)@RequestHeader(name = "clientId", required = true,  defaultValue = "")String clientId,
                                      @RequestBody @Valid CartVo cart
    ){
        Integer flag ;
        if(StringUtils.isNotEmpty(token) && cart.getSkuPriceIdList().size() > 0){
            LoginVo info = customerService.getCustomerInfo(JwtTokenUtils.getUsername(token));
            //info.setZid("514f3aa1256243c4a9dbab84cb8f9a8e");
            //用户已登录状态查询购物车
            cart.setCustId(info.getZid());
            cart.setClientId("");
            flag = cartService.deleteCartItem(cart);
        }else {
            //用户未登录状态查询购物车
            if(StringUtils.isEmpty(cart.getClientId())){
                cart.setClientId(clientId);
            }
            if(StringUtils.isNotEmpty(cart.getClientId()) && cart.getSkuPriceIdList().size() > 0){
                flag = cartService.deleteCartItem(cart);
            }else {
                return new ShopRespose(ErrorEnum.ERROR_3001);
            }
        }
//1成功 2数量超过限制 3sku不存在 4购物车商品总数超过限制 5购物车不存在 6购物项不存在
        if(flag == 1){
            return new ShopRespose(IConstants.SUCCESS,"成功",flag);
        }else if (flag == 2){
            return new ShopRespose(ErrorEnum.ERROR_3002);
        }else if (flag == 3){
            return new ShopRespose(ErrorEnum.ERROR_3003);
        }else if (flag == 4){
            return new ShopRespose(ErrorEnum.ERROR_3004);
        }else if (flag == 5){
            return new ShopRespose(ErrorEnum.ERROR_3005);
        }else {
            return new ShopRespose(ErrorEnum.ERROR_3006);
        }}


    @ApiOperation(value = "增减购物项数量")
    @PostMapping(value = "/updateCartItemNum")
    public ShopRespose updateCartItemNum(@ApiParam(name = "X-Auth-Token", value = "登录token", required = false)@RequestHeader(name = "X-Auth-Token", required = false, defaultValue = "") String token,
                                         @ApiParam(name = "clientId", value = "客户端Id", required = true)@RequestHeader(name = "clientId", required = true,  defaultValue = "")String clientId,
                                         @RequestBody @Valid CartVo cart
    ){
        Integer flag ;
        if(StringUtils.isNotEmpty(token)){
            LoginVo info = customerService.getCustomerInfo(JwtTokenUtils.getUsername(token));
            //info.setZid("514f3aa1256243c4a9dbab84cb8f9a8e");
            //用户已登录状态查询购物车
            cart.setClientId("");
            cart.setCustId(info.getZid());
            flag = cartService.updateCartItemNum(cart);
        }else {
            //用户未登录状态查询购物车
            if(StringUtils.isEmpty(cart.getClientId())){
                cart.setClientId(clientId);
            }
            if(StringUtils.isNotEmpty(cart.getClientId())){
                flag = cartService.updateCartItemNum(cart);
            }else {
                return new ShopRespose(ErrorEnum.ERROR_3001);
            }
        }
//1成功 2数量超过限制 3sku不存在 4购物车商品总数超过限制 5购物车不存在 6购物项不存在
        if(flag == 1){
            return new ShopRespose(IConstants.SUCCESS,"成功",flag);
        }else if (flag == 2){
            return new ShopRespose(ErrorEnum.ERROR_3002);
        }else if (flag == 3){
            return new ShopRespose(ErrorEnum.ERROR_3003);
        }else if (flag == 4){
            return new ShopRespose(ErrorEnum.ERROR_3004);
        }else if (flag == 5){
            return new ShopRespose(ErrorEnum.ERROR_3005);
        }else {
            return new ShopRespose(ErrorEnum.ERROR_3006);
        }  }

    @ApiOperation(value = "批量增加购物项")
    @PostMapping(value = "/addCartItemList")
    public ShopRespose addCartItemList(@ApiParam(name = "X-Auth-Token", value = "登录token", required = false)@RequestHeader(name = "X-Auth-Token", required = false, defaultValue = "") String token,
                                   @ApiParam(name = "clientId", value = "客户端Id", required = true)@RequestHeader(name = "clientId", required = true,  defaultValue = "")String clientId,
                                   @RequestBody @Valid CartVo cart
    ){
        Integer flag = 1;
        if(StringUtils.isNotEmpty(token)){
            LoginVo info = customerService.getCustomerInfo(JwtTokenUtils.getUsername(token));
            //info.setZid("514f3aa1256243c4a9dbab84cb8f9a8e");
            //用户已登录状态
            cart.setClientId("");
            cart.setCustId(info.getZid());
            flag = cartService.addCartItem(cart);
        }else {
            //用户未登录状态
            if(StringUtils.isEmpty(cart.getClientId())){
                cart.setClientId(clientId);
            }
            if(StringUtils.isNotEmpty(cart.getClientId()) && StringUtils.isNotEmpty(cart.getSkuId())){
                flag = cartService.addCartItem(cart);
            }else {
                return new ShopRespose(ErrorEnum.ERROR_3001);
            }
        }
        //1成功 2数量超过限制 3sku不存在 4购物车商品总数超过限制 5购物车不存在 6购物项不存在
        if(flag == 1){
            return new ShopRespose(IConstants.SUCCESS,"成功",flag);
        }else if (flag == 2){
            return new ShopRespose(ErrorEnum.ERROR_3002);
        }else if (flag == 3){
            return new ShopRespose(ErrorEnum.ERROR_3003);
        }else if (flag == 4){
            return new ShopRespose(ErrorEnum.ERROR_3004);
        }else if (flag == 5){
            return new ShopRespose(ErrorEnum.ERROR_3005);
        }else {
            return new ShopRespose(ErrorEnum.ERROR_3006);
        }
    }
}
