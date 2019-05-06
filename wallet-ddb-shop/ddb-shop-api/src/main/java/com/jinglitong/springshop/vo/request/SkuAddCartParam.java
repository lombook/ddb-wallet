package com.jinglitong.springshop.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @ClassName SkuAddCartParam
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/1/8 15:36
 * @Version 1.0
 **/
@ApiModel(value = "要添加购物车的商品")
public class SkuAddCartParam {
    //@NotNull 表示前端必传值，否则在BindingResult.hasErrors()有错
    @NotNull
    @ApiModelProperty(value = "添加的商品列表")
    private List<SkuItemAddCartParam> skuItemAddCartParamList;

    public List<SkuItemAddCartParam> getSkuItemAddCartParamList() {
        return skuItemAddCartParamList;
    }

    public void setSkuItemAddCartParamList(List<SkuItemAddCartParam> skuItemAddCartParamList) {
        this.skuItemAddCartParamList = skuItemAddCartParamList;
    }
}
