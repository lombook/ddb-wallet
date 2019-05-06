package com.jinglitong.springshop.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
@Data
@ToString
@Table(name = "categories_spec_group")
public class CategoriesSpecGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 产品分类
     */
    @Column(name = "product_categories_id")
    private Integer productCategoriesId;

    /**
     * 规格组名
     */
    @Column(name = "spec_name")
    private String specName;

    /**
     * 显示顺序
     */
    private Byte orders;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取产品分类
     *
     * @return product_categories_id - 产品分类
     */
    public Integer getProductCategoriesId() {
        return productCategoriesId;
    }

    /**
     * 设置产品分类
     *
     * @param productCategoriesId 产品分类
     */
    public void setProductCategoriesId(Integer productCategoriesId) {
        this.productCategoriesId = productCategoriesId;
    }

    /**
     * 获取规格组名
     *
     * @return spec_name - 规格组名
     */
    public String getSpecName() {
        return specName;
    }

    /**
     * 设置规格组名
     *
     * @param specName 规格组名
     */
    public void setSpecName(String specName) {
        this.specName = specName == null ? null : specName.trim();
    }

    /**
     * 获取显示顺序
     *
     * @return orders - 显示顺序
     */
    public Byte getOrders() {
        return orders;
    }

    /**
     * 设置显示顺序
     *
     * @param orders 显示顺序
     */
    public void setOrders(Byte orders) {
        this.orders = orders;
    }
}