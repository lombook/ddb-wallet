package com.jinglitong.springshop.controller;

import com.github.pagehelper.PageInfo;
import com.jinglitong.springshop.IConstants;
import com.jinglitong.springshop.ShopRespose;
import com.jinglitong.springshop.entity.Product;
import com.jinglitong.springshop.entity.SkuPrice;
import com.jinglitong.springshop.servcie.ProductService;
import com.jinglitong.springshop.vo.ProductVo;
import com.jinglitong.springshop.vo.request.SkuAddCartParam;
import com.jinglitong.springshop.vo.response.ProductDetailVo;
import com.jinglitong.springshop.vo.response.ProductListVo;
import com.jinglitong.springshop.vo.response.ProductResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

/**
 * @ClassName ProductController
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/1/7 17:34
 * @Version 1.0
 **/
@RestController
@RequestMapping("/product")
@Api(value = "商品 API",tags = "商品 API")
public class ProductController {
    @Autowired
    private ProductService productService;

    @ApiOperation(value = "添加购物车")
    @RequestMapping(value = "/addShoppingCart", method = RequestMethod.POST)
    public ShopRespose addShoppingCart(@ApiParam(name = "X-Auth-Token", value = "登录token", required = false)@RequestHeader(name = "X-Auth-Token", required = false, defaultValue = "") String token,
                                       @RequestBody @Valid SkuAddCartParam param, BindingResult result){
        ShopRespose respose = new ShopRespose<>();
        if (result.hasErrors()) {
            respose.setMessage(IConstants.FAILED_MSG);
            respose.setData(result);
            return respose;
        }
        //TODO
        respose.setMessage(IConstants.SUCCESS_MSG);
        respose.setCode(IConstants.SUCCESS);
        return respose;
    }

    @ApiOperation(value = "初始化商品页")
    @PostMapping(value = "/getProductList")
    public ShopRespose getProductList(@RequestBody @Valid ProductVo product){
        ShopRespose respose = new ShopRespose<>();
        PageInfo<ProductResponseVo> info = productService.selectProductsOrder(product);
        respose.setMessage(IConstants.SUCCESS_MSG);
        respose.setData(info);
        respose.setCode(IConstants.SUCCESS);
        return respose;
    }

    @ApiOperation(value = "商品详情")
    @RequestMapping(value ="/getProductDetailById" , method = RequestMethod.POST)
    public ShopRespose getProductDetail(@RequestBody ProductListVo vop) {
    	ShopRespose respose = new ShopRespose<>();
    	ProductDetailVo vo = productService.selectProductDetails(vop.getId());
        respose.setMessage(IConstants.SUCCESS_MSG);
        respose.setData(vo);
        respose.setCode(IConstants.SUCCESS);
        return respose;
    }

    @ApiOperation(value = "获取商品价格")
    @RequestMapping(value ="/getProductPrice" , method = RequestMethod.POST)
    public ShopRespose getProductPrice(@RequestBody Map<String, String> map) {
    	ShopRespose respose = new ShopRespose<>();
    	List<SkuPrice> list = productService.getproductPrice(map.get("skuId"));
        respose.setMessage(IConstants.SUCCESS_MSG);
        respose.setData(list);
        respose.setCode(IConstants.SUCCESS);
        return respose;
    }
    
}
