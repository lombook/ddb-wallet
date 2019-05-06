package com.jinglitong.springshop.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import javax.persistence.*;
@Data
@ToString
@Table(name = "cart_items")
public class CartItems {

    public CartItems(String zid, String cartId, Integer quantity, String skuId, String productId, Date createdTime, Date updatedTime) {
        this.zid = zid;
        this.cartId = cartId;
        this.quantity = quantity;
        this.skuId = skuId;
        this.productId = productId;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
    }
	public CartItems(String zid, String cartId, Integer quantity, String skuId, String productId, String skuPriceId,
			Date createdTime, Date updatedTime) {
		super();
		this.zid = zid;
		this.cartId = cartId;
		this.quantity = quantity;
		this.skuId = skuId;
		this.productId = productId;
		this.skuPriceId = skuPriceId;
		this.createdTime = createdTime;
		this.updatedTime = updatedTime;
	}
    public CartItems() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String zid;

    /**
     * 购物车Id
     */
    @Column(name = "cart_id")
    private String cartId;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * sku的zid
     */
    @Column(name = "sku_id")
    private String skuId;

    /**
     * 商品的Zid
     */
    @Column(name = "product_id")
    private String productId;
    
    @Column(name = "sku_price_id")
    private String skuPriceId;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;

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
     * @return zid
     */
    public String getZid() {
        return zid;
    }

    /**
     * @param zid
     */
    public void setZid(String zid) {
        this.zid = zid == null ? null : zid.trim();
    }

    /**
     * 获取购物车Id
     *
     * @return cart_id - 购物车Id
     */
    public String getCartId() {
        return cartId;
    }

    /**
     * 设置购物车Id
     *
     * @param cartId 购物车Id
     */
    public void setCartId(String cartId) {
        this.cartId = cartId == null ? null : cartId.trim();
    }

    /**
     * 获取数量
     *
     * @return quantity - 数量
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * 设置数量
     *
     * @param quantity 数量
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * 获取sku的zid
     *
     * @return sku_id - sku的zid
     */
    public String getSkuId() {
        return skuId;
    }

    /**
     * 设置sku的zid
     *
     * @param skuId sku的zid
     */
    public void setSkuId(String skuId) {
        this.skuId = skuId == null ? null : skuId.trim();
    }

    /**
     * 获取商品的Zid
     *
     * @return product_id - 商品的Zid
     */
    public String getProductId() {
        return productId;
    }

    /**
     * 设置商品的Zid
     *
     * @param productId 商品的Zid
     */
    public void setProductId(String productId) {
        this.productId = productId == null ? null : productId.trim();
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
}