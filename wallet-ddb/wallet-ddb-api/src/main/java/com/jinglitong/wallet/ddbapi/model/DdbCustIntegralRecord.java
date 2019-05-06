package com.jinglitong.wallet.ddbapi.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
@Data
@Table(name = "ddb_cust_integral_record")
public class DdbCustIntegralRecord {
    /**
     * id
     */
    @Id
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
     * ���ֵ
     */
    private Integer amount;

    /**
     * ��������id
     */
    @Column(name = "inte_zid")
    private String inteZid;

    /**
     * ������
     */
    @Column(name = "real_name")
    private String realName;

    /**
     * ����������
     */
    @Column(name = "real_cname")
    private String realCname;

    /**
     * 1:�����˽��� 2:�������� 3:�ɶ������ 4:����MEI 5�������ͷ�6:�ڿ����ת��
     */
    @Column(name = "cost_type")
    private Integer costType;

    /**
     * ����ʱʹ��
     */
    private String hash;

    /**
     * ��Ʒ����id
     */
    @Column(name = "flow_id")
    private String flowId;

    /**
     * ��ע
     */
    private String remark;

    @Column(name = "integ_name")
    private String integName;

    @Column(name = "integ_cname")
    private String integCname;

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
     * '操作类型：1、增加 2、减少
     */
    @Column(name = "type")
    private Integer type;
    
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
     * ��ȡ���ֵ
     *
     * @return amount - ���ֵ
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * ���ý��ֵ
     *
     * @param amount ���ֵ
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
     * ��ȡ��������id
     *
     * @return inte_zid - ��������id
     */
    public String getInteZid() {
        return inteZid;
    }

    /**
     * ���û�������id
     *
     * @param inteZid ��������id
     */
    public void setInteZid(String inteZid) {
        this.inteZid = inteZid;
    }

    /**
     * ��ȡ������
     *
     * @return real_name - ������
     */
    public String getRealName() {
        return realName;
    }

    /**
     * ���û�����
     *
     * @param realName ������
     */
    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * ��ȡ����������
     *
     * @return real_cname - ����������
     */
    public String getRealCname() {
        return realCname;
    }

    /**
     * ���û���������
     *
     * @param realCname ����������
     */
    public void setRealCname(String realCname) {
        this.realCname = realCname;
    }

    /**
     * ��ȡ1:�����˽��� 2:�������� 3:�ɶ������ 4:����MEI 5�������ͷ�6:�ڿ����ת��
     *
     * @return cost_type - 1:�����˽��� 2:�������� 3:�ɶ������ 4:����MEI 5�������ͷ�6:�ڿ����ת��
     */
    public Integer getCostType() {
        return costType;
    }

    /**
     * ����1:�����˽��� 2:�������� 3:�ɶ������ 4:����MEI 5�������ͷ�6:�ڿ����ת��
     *
     * @param costType 1:�����˽��� 2:�������� 3:�ɶ������ 4:����MEI 5�������ͷ�6:�ڿ����ת��
     */
    public void setCostType(Integer costType) {
        this.costType = costType;
    }

    /**
     * ��ȡ����ʱʹ��
     *
     * @return hash - ����ʱʹ��
     */
    public String getHash() {
        return hash;
    }

    /**
     * ���ö���ʱʹ��
     *
     * @param hash ����ʱʹ��
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * ��ȡ��Ʒ����id
     *
     * @return flow_id - ��Ʒ����id
     */
    public String getFlowId() {
        return flowId;
    }

    /**
     * ������Ʒ����id
     *
     * @param flowId ��Ʒ����id
     */
    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    /**
     * ��ȡ��ע
     *
     * @return remark - ��ע
     */
    public String getRemark() {
        return remark;
    }

    /**
     * ���ñ�ע
     *
     * @param remark ��ע
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "DdbCustIntegralRecord{" +
                "id=" + id +
                ", zid='" + zid + '\'' +
                ", custId='" + custId + '\'' +
                ", amount=" + amount +
                ", inteZid='" + inteZid + '\'' +
                ", realName='" + realName + '\'' +
                ", realCname='" + realCname + '\'' +
                ", costType=" + costType +
                ", hash='" + hash + '\'' +
                ", flowId='" + flowId + '\'' +
                ", remark='" + remark + '\'' +
                ", integName='" + integName + '\'' +
                ", integCname='" + integCname + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", type=" + type +
                '}';
    }
}