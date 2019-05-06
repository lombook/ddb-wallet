package com.jinglitong.springshop.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
@Data
@ToString
@Table(name = "product_category_brand")
public class ProductCategoryBrand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "product_categories_id")
    private String productCategoriesId;

    @Column(name = "brands_id")
    private String brandsId;

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
     * @return product_categories_id
     */
    public String getProductCategoriesId() {
        return productCategoriesId;
    }

    /**
     * @param productCategoriesId
     */
    public void setProductCategoriesId(String productCategoriesId) {
        this.productCategoriesId = productCategoriesId;
    }

    /**
     * @return brands_id
     */
    public String getBrandsId() {
        return brandsId;
    }

    /**
     * @param brandsId
     */
    public void setBrandsId(String brandsId) {
        this.brandsId = brandsId;
    }
}