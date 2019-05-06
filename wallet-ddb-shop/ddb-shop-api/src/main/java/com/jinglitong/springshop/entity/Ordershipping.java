package com.jinglitong.springshop.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

public class Ordershipping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String zid;

    private String address;

    private String area;

    private String consignee;

    /**
     * 物流名
     */
    @Column(name = "deliveryCorp")
    private String deliverycorp;

    @Column(name = "deliveryCorpCode")
    private String deliverycorpcode;

    @Column(name = "deliveryCorpUrl")
    private String deliverycorpurl;

    private BigDecimal freight;

    private String memo;

    private String phone;

    @Column(name = "shippingMethod")
    private String shippingmethod;

    private String sn;

    /**
     * 物流单号
     */
    @Column(name = "trackingNo")
    private String trackingno;

    @Column(name = "zipCode")
    private String zipcode;

    /**
     * 订单id
     */
    @Column(name = "order_id")
    private String orderId;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "update_time")
    private Date updateTime;
    
    @Column(name ="image_url")
    private String imageUrl;
    
    @Column(name ="longitude")
    private String longitude;
    
    @Column(name ="latitude")
    private String latitude;
    
    @Column(name ="coordinate_add")
    private String coordinateAdd;

    
    public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getCoordinateAdd() {
		return coordinateAdd;
	}

	public void setCoordinateAdd(String coordinateAdd) {
		this.coordinateAdd = coordinateAdd;
	}

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
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * @return area
     */
    public String getArea() {
        return area;
    }

    /**
     * @param area
     */
    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    /**
     * @return consignee
     */
    public String getConsignee() {
        return consignee;
    }

    /**
     * @param consignee
     */
    public void setConsignee(String consignee) {
        this.consignee = consignee == null ? null : consignee.trim();
    }

    /**
     * 获取物流名
     *
     * @return deliveryCorp - 物流名
     */
    public String getDeliverycorp() {
        return deliverycorp;
    }

    /**
     * 设置物流名
     *
     * @param deliverycorp 物流名
     */
    public void setDeliverycorp(String deliverycorp) {
        this.deliverycorp = deliverycorp == null ? null : deliverycorp.trim();
    }

    /**
     * @return deliveryCorpCode
     */
    public String getDeliverycorpcode() {
        return deliverycorpcode;
    }

    /**
     * @param deliverycorpcode
     */
    public void setDeliverycorpcode(String deliverycorpcode) {
        this.deliverycorpcode = deliverycorpcode == null ? null : deliverycorpcode.trim();
    }

    /**
     * @return deliveryCorpUrl
     */
    public String getDeliverycorpurl() {
        return deliverycorpurl;
    }

    /**
     * @param deliverycorpurl
     */
    public void setDeliverycorpurl(String deliverycorpurl) {
        this.deliverycorpurl = deliverycorpurl == null ? null : deliverycorpurl.trim();
    }

    /**
     * @return freight
     */
    public BigDecimal getFreight() {
        return freight;
    }

    /**
     * @param freight
     */
    public void setFreight(BigDecimal freight) {
        this.freight = freight;
    }

    /**
     * @return memo
     */
    public String getMemo() {
        return memo;
    }

    /**
     * @param memo
     */
    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    /**
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * @return shippingMethod
     */
    public String getShippingmethod() {
        return shippingmethod;
    }

    /**
     * @param shippingmethod
     */
    public void setShippingmethod(String shippingmethod) {
        this.shippingmethod = shippingmethod == null ? null : shippingmethod.trim();
    }

    /**
     * @return sn
     */
    public String getSn() {
        return sn;
    }

    /**
     * @param sn
     */
    public void setSn(String sn) {
        this.sn = sn == null ? null : sn.trim();
    }

    /**
     * 获取物流单号
     *
     * @return trackingNo - 物流单号
     */
    public String getTrackingno() {
        return trackingno;
    }

    /**
     * 设置物流单号
     *
     * @param trackingno 物流单号
     */
    public void setTrackingno(String trackingno) {
        this.trackingno = trackingno == null ? null : trackingno.trim();
    }

    /**
     * @return zipCode
     */
    public String getZipcode() {
        return zipcode;
    }

    /**
     * @param zipcode
     */
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode == null ? null : zipcode.trim();
    }

    /**
     * 获取订单id
     *
     * @return order_id - 订单id
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * 设置订单id
     *
     * @param orderId 订单id
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
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
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}