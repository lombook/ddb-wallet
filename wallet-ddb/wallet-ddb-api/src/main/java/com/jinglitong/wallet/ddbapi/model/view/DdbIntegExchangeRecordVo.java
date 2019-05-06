package com.jinglitong.wallet.ddbapi.model.view;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import com.jinglitong.wallet.api.model.view.PageVO;

public class DdbIntegExchangeRecordVo extends PageVO{


    /**
     * id
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * zid
     */
    private String zid;

    /**
     * �û�id
     */
    @Column(name = "cust_id")
    private String custId;

    /**
     * ��Ȩ�һ�����id
     */
    @Column(name = "se_rule_id")
    private String seRuleId;

    /**
     * ����id
     */
    @Column(name = "inte_zid")
    private String inteZid;

    /**
     * ������
     */
    private Integer amount;

    /**
     * ���ֳ�����
     */
    @Column(name = "real_name")
    private String realName;

    /**
     * ���ֳ���������
     */
    @Column(name = "real_cname")
    private String realCname;

    /**
     * ����������
     */
    @Column(name = "integ_cname")
    private String integCname;

    /**
     * ������
     */
    @Column(name = "integ_name")
    private String integName;

    /**
     * ����ʱ��
     */
    @Column(name = "create_time")
    private Timestamp createTime;

    /**
     * ����ʱ��
     */
    @Column(name = "update_time")
    private Timestamp updateTime;

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
     * ��ȡzid
     *
     * @return zid - zid
     */
    public String getZid() {
        return zid;
    }

    /**
     * ����zid
     *
     * @param zid zid
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
     * ��ȡ��Ȩ�һ�����id
     *
     * @return se_rule_id - ��Ȩ�һ�����id
     */
    public String getSeRuleId() {
        return seRuleId;
    }

    /**
     * ���ù�Ȩ�һ�����id
     *
     * @param seRuleId ��Ȩ�һ�����id
     */
    public void setSeRuleId(String seRuleId) {
        this.seRuleId = seRuleId;
    }

    /**
     * ��ȡ����id
     *
     * @return inte_zid - ����id
     */
    public String getInteZid() {
        return inteZid;
    }

    /**
     * ��������id
     *
     * @param inteZid ����id
     */
    public void setInteZid(String inteZid) {
        this.inteZid = inteZid;
    }

    /**
     * ��ȡ������
     *
     * @return amount - ������
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * ���û�����
     *
     * @param amount ������
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
     * ��ȡ���ֳ�����
     *
     * @return real_name - ���ֳ�����
     */
    public String getRealName() {
        return realName;
    }

    /**
     * ���û��ֳ�����
     *
     * @param realName ���ֳ�����
     */
    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * ��ȡ���ֳ���������
     *
     * @return real_cname - ���ֳ���������
     */
    public String getRealCname() {
        return realCname;
    }

    /**
     * ���û��ֳ���������
     *
     * @param realCname ���ֳ���������
     */
    public void setRealCname(String realCname) {
        this.realCname = realCname;
    }

    /**
     * ��ȡ����������
     *
     * @return integ_cname - ����������
     */
    public String getIntegCname() {
        return integCname;
    }

    /**
     * ���û���������
     *
     * @param integCname ����������
     */
    public void setIntegCname(String integCname) {
        this.integCname = integCname;
    }

    /**
     * ��ȡ������
     *
     * @return integ_name - ������
     */
    public String getIntegName() {
        return integName;
    }

    /**
     * ���û�����
     *
     * @param integName ������
     */
    public void setIntegName(String integName) {
        this.integName = integName;
    }

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

 

}
