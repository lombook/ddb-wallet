package com.jinglitong.springshop.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import javax.persistence.*;
@Data
@ToString
public class Specification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 权重排序
     */
    private Integer orders;

    /**
     * 名字
     */
    private String name;

    /**
     * 所属产品类型
     */
    @Column(name = "product_category_id")
    private String productCategoryId;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;

    /**
     * 具体值
     */
    private String options;

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
     * 获取权重排序
     *
     * @return orders - 权重排序
     */
    public Integer getOrders() {
        return orders;
    }

    /**
     * 设置权重排序
     *
     * @param orders 权重排序
     */
    public void setOrders(Integer orders) {
        this.orders = orders;
    }

    /**
     * 获取名字
     *
     * @return name - 名字
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名字
     *
     * @param name 名字
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取所属产品类型
     *
     * @return product_category_id - 所属产品类型
     */
    public String getProductCategoryId() {
        return productCategoryId;
    }

    /**
     * 设置所属产品类型
     *
     * @param productCategoryId 所属产品类型
     */
    public void setProductCategoryId(String productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    /**
     * @return created_time
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * @param createdTime
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * @return updated_time
     */
    public Date getUpdatedTime() {
        return updatedTime;
    }

    /**
     * @param updatedTime
     */
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    /**
     * 获取具体值
     *
     * @return options - 具体值
     */
    public String getOptions() {
        return options;
    }

    /**
     * 设置具体值
     *
     * @param options 具体值
     */
    public void setOptions(String options) {
        this.options = options == null ? null : options.trim();
    }
}