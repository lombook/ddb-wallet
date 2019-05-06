package com.jinglitong.wallet.ddbapi.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "ddb_order_analysis_detail")
public class DdbOrderAnalysisDetail {
    /**
     * id
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * ҵ��id
     */
    @Column(name = "flow_id")
    private String flowId;

    /**
     * ���id
     */
    @Column(name = "rule_id")
    private String ruleId;

    /**
     * ���
     */
    private Integer price;

    /**
     * ���ͱ���
     */
    @Column(name = "gift_baofen")
    private Integer giftBaofen;

    /**
     * �û�id
     */
    @Column(name = "cust_id")
    private String custId;

    /**
     * һ��������id
     */
    @Column(name = "invite_id")
    private String inviteId;

    /**
     * Ӧ����һ�������˱���
     */
    @Column(name = "invite_baofen")
    private Integer inviteBaofen;


    /**
     * ���������˱���
     */
    @Column(name = "level")
    private Integer level;

    /**
     * ����ʱ��
     */
    @Column(name = "create_time")
    private String createTime;

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
     * @return flow_id - ҵ��id
     */
    public String getFlowId() {
        return flowId;
    }

    /**
     * ����ҵ��id
     *
     * @param flowId ҵ��id
     */
    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    /**
     * ��ȡ���id
     *
     * @return rule_id - ���id
     */
    public String getRuleId() {
        return ruleId;
    }

    /**
     * ���ù��id
     *
     * @param ruleId ���id
     */
    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    /**
     * ��ȡ���
     *
     * @return price - ���
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * ���ý��
     *
     * @param price ���
     */
    public void setPrice(Integer price) {
        this.price = price;
    }

    /**
     * ��ȡ���ͱ���
     *
     * @return gift_baofen - ���ͱ���
     */
    public Integer getGiftBaofen() {
        return giftBaofen;
    }

    /**
     * �������ͱ���
     *
     * @param giftBaofen ���ͱ���
     */
    public void setGiftBaofen(Integer giftBaofen) {
        this.giftBaofen = giftBaofen;
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
     * ��ȡһ��������id
     *
     * @return level_one_id - һ��������id
     */
   
    /**
     * ��ȡ����ʱ��
     *
     * @return create_time - ����ʱ��
     */
    public String getCreateTime() {
        return createTime;
    }

    public String getInviteId() {
		return inviteId;
	}

	public void setInviteId(String inviteId) {
		this.inviteId = inviteId;
	}

	public Integer getInviteBaofen() {
		return inviteBaofen;
	}

	public void setInviteBaofen(Integer inviteBaofen) {
		this.inviteBaofen = inviteBaofen;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	/**
     * ���ô���ʱ��
     *
     * @param createTime ����ʱ��
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}