package com.jinglitong.springshop.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.jetbrains.annotations.NotNull;

/**
 * @ClassName SkuItemAddCartParam
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/1/8 15:39
 * @Version 1.0
 **/
@ApiModel(value = "具体商品")
public class SkuItemAddCartParam {
    //@NotNull 表示前端必传值，否则在BindingResult.hasErrors()有错

    @NotNull
    @ApiModelProperty(value = "业务主键", name = "zid")
    private String zid;
    @ApiModelProperty(value = "名称", name = "name")
    @NotNull
    private String name;
    @ApiModelProperty(value = "库存ID", name = "storeId")
    @NotNull
    private String storeId;
    @ApiModelProperty(value = "sn", name = "sn")
    private String sn;
    @ApiModelProperty(value = "brandId", name = "brandId")
    private Integer brandId;
    @NotNull
    @ApiModelProperty(value = "商品分类ID", name = "productCategoryId")
    private Integer productCategoryId;
    @NotNull
    @ApiModelProperty(value = "分类项", name = "specificationitems")
    private String specificationitems;

    public String getZid() {
        return zid;
    }

    public void setZid(String zid) {
        this.zid = zid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Integer productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public String getSpecificationitems() {
        return specificationitems;
    }

    public void setSpecificationitems(String specificationitems) {
        this.specificationitems = specificationitems;
    }
}
