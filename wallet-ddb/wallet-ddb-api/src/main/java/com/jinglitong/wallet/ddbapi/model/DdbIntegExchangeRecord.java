package com.jinglitong.wallet.ddbapi.model;

import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.*;

@Table(name = "ddb_integ_exchange_record")
public class DdbIntegExchangeRecord {
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
    @Column(name = "ie_rule_id")
    private String ieRuleId;

    /**
     * ����id
     */
    @Column(name = "inte_zid")
    private String inteZid;

    /**
     * ������
     */
    private Integer amount;
    
    @Column(name ="ex_amount")
    private Integer exAmount;
    @Column(name ="charge_amount")
    private Integer chargeAmount;

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

    
    @Column(name ="type")
    private Integer type;
    
    
    @Column(name ="ex_integ_cname")
    private String exIntegCname;
    
    @Column(name ="ex_integ_name")
    private String exIntegName;
    
    @Column(name ="charge_integ_cname")
    private String chargeIntegCname;
    
    @Column(name ="charge_integ_name")
    private String chargeIntegName;
    
    
    

    public Integer getChargeAmount() {
		return chargeAmount;
	}

	public void setChargeAmount(Integer chargeAmount) {
		this.chargeAmount = chargeAmount;
	}

	public String getIeRuleId() {
		return ieRuleId;
	}

	public void setIeRuleId(String ieRuleId) {
		this.ieRuleId = ieRuleId;
	}

	public Integer getExAmount() {
		return exAmount;
	}

	public void setExAmount(Integer exAmount) {
		this.exAmount = exAmount;
	}

	public String getExIntegCname() {
		return exIntegCname;
	}

	public void setExIntegCname(String exIntegCname) {
		this.exIntegCname = exIntegCname;
	}

	public String getExIntegName() {
		return exIntegName;
	}

	public void setExIntegName(String exIntegName) {
		this.exIntegName = exIntegName;
	}

	public String getChargeIntegCname() {
		return chargeIntegCname;
	}

	public void setChargeIntegCname(String chargeIntegCname) {
		this.chargeIntegCname = chargeIntegCname;
	}

	public String getChargeIntegName() {
		return chargeIntegName;
	}

	public void setChargeIntegName(String chargeIntegName) {
		this.chargeIntegName = chargeIntegName;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
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