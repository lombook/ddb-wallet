package com.jinglitong.springshop.controller;

import com.github.pagehelper.PageInfo;
import com.jinglitong.springshop.IConstants;
import com.jinglitong.springshop.ShopRespose;
import com.jinglitong.springshop.entity.ProductCategory;
import com.jinglitong.springshop.servcie.ProductCategoryService;
import com.jinglitong.springshop.vo.ProductVo;
import com.jinglitong.springshop.vo.response.ProductResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author fyy
 * @create 2019-01-09-15:34}
 */
@RestController
@RequestMapping("/product")
@Api(value = "商品分类 API",tags = "商品分类 API")
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @ApiOperation(value = "获取分类列表")
    @PostMapping(value = "/getProductCategoryList")
    public ShopRespose getProductList(){
        List<ProductCategory> info =  productCategoryService.sellectBy2Grade();
        return new ShopRespose(IConstants.SUCCESS,"查询成功",info);
    }
}
