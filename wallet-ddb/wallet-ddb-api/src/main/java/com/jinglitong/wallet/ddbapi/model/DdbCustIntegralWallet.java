package com.jinglitong.wallet.ddbapi.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "ddb_cust_integral_wallet")
public class DdbCustIntegralWallet {
    /**
     * id
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * ҵ��id
     */
    private String zid;

    /**
     * �û�id
     */
    @Column(name = "cust_id")
    private String custId;

    /**
     * ����ֵ
     */
    private Integer baofen;

    /**
     * ���ᱦ��ֵ
     */
    @Column(name = "freez_baofen")
    private Integer freezBaofen;

    /**
     * ����ʱ��
     */
    @Column(name = "create_time")
    private String createTime;

    /**
     * ����ʱ��
     */
    @Column(name = "update_time")
    private String updateTime;

    /**
     * ��ȡid
     *
     * @return id - id
     */
    public Integer getId() {
        return id;
    }

    /**
     * ����id
     *
     * @param id id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * ��ȡҵ��id
     *
     * @return zid - ҵ��id
     */
    public String getZid() {
        return zid;
    }

    /**
     * ����ҵ��id
     *
     * @param zid ҵ��id
     */
    public void setZid(String zid) {
        this.zid = zid;
    }

    /**
     * ��ȡ�û�id
     *
     * @return cust_id - �û�id
     */
    public String getCustId() {
        return custId;
    }

    /**
     * �����û�id
     *
     * @param custId �û�id
     */
    public void setCustId(String custId) {
        this.custId = custId;
    }

    /**
     * ��ȡ����ֵ
     *
     * @return baofen - ����ֵ
     */
    public Integer getBaofen() {
        return baofen;
    }

    /**
     * ���ñ���ֵ
     *
     * @param baofen ����ֵ
     */
    public void setBaofen(Integer baofen) {
        this.baofen = baofen;
    }

    /**
     * ��ȡ���ᱦ��ֵ
     *
     * @return freez_baofen - ���ᱦ��ֵ
     */
    public Integer getFreezBaofen() {
        return freezBaofen;
    }

    /**
     * ���ö��ᱦ��ֵ
     *
     * @param freezBaofen ���ᱦ��ֵ
     */
    public void setFreezBaofen(Integer freezBaofen) {
        this.freezBaofen = freezBaofen;
    }

    /**
     * ��ȡ����ʱ��
     *
     * @return create_time - ����ʱ��
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * ���ô���ʱ��
     *
     * @param createTime ����ʱ��
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * ��ȡ����ʱ��
     *
     * @return update_time - ����ʱ��
     */
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * ���ø���ʱ��
     *
     * @param updateTime ����ʱ��
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}