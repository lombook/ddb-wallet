package com.jinglitong.springshop.vo;

import com.jinglitong.springshop.vo.request.PageVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName ProductVo
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/1/7 17:54
 * @Version 1.0
 **/
@ApiModel(value = "商品")
@Data
public class ProductVo extends PageVo {
    @ApiModelProperty(value = "自增主键", name = "id")
    private Integer id;
    @ApiModelProperty(value = "业务主键", name = "zid")
    private String zid;
    @ApiModelProperty(value = "标题", name = "caption")
    private String caption;
    @ApiModelProperty(value = "图片", name = "image")
    private String image;
    @ApiModelProperty(value = "", name = "islist")
    private Boolean islist;
    @ApiModelProperty(value = "", name = "memo")
    private String memo;
    @ApiModelProperty(value = "名称", name = "name")
    private String name;
    @ApiModelProperty(value = "库存ID", name = "storeId")
    private String storeId;
    @ApiModelProperty(value = "sn", name = "sn")
    private String sn;
    @ApiModelProperty(value = "brandId", name = "brandId")
    private Integer brandId;
    /**
     * 基准价，人民币的标的价格
     */
    @ApiModelProperty(value = "基准价", name = "benchmarkprice")
    private BigDecimal benchmarkprice;
    @ApiModelProperty(value = "创建时间", name = "createdTime")
    private Date createdTime;
    @ApiModelProperty(value = "更新时间", name = "updatedTime")
    private Date updatedTime;
    @ApiModelProperty(value = "商品说明", name = "introduction")
    private String introduction;
    @ApiModelProperty(value = "商品图片", name = "productimages")
    private String productimages;
    @ApiModelProperty(value = "分类项", name = "specificationitems")
    private String specificationitems;
    @ApiModelProperty(value = "销量", name = "sellNum")
    private Integer sellNum;
    @ApiModelProperty(value = "关键字", name = "keyword")
    private String keyword;
    @ApiModelProperty(value = "商品分类ID", name = "productCategoryId")
    private String productCategoryId;
    @ApiModelProperty(value = "销量排序", name = "sellNumOrder")
    private Boolean sellNumOrder = false;
    @ApiModelProperty(value = "销量排序类型", name = "sellNumOrderType")
    private String sellNumOrderType = "DESC";
    @ApiModelProperty(value = "基准价排序", name = "benchmarkpriceOrder")
    private Boolean benchmarkpriceOrder = false;
    @ApiModelProperty(value = "基准价排序类型", name = "benchmarkpriceOrderType")
    private String benchmarkpriceOrderType = "DESC";
    @ApiModelProperty(value = "币种类型", name = "currency")
    private String currency = "";
    private String supportIntegral;//是否支持积分支付
}
