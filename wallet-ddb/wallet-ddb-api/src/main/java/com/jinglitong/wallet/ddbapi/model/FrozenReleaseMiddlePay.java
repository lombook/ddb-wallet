package com.jinglitong.wallet.ddbapi.model;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Data
@Table(name = "frozen_release_middle_pay")
public class FrozenReleaseMiddlePay {

    public FrozenReleaseMiddlePay() {
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String zid;

    /**
     * ת���˻�
     */
    @Column(name = "to_address")
    private String toAddress;

    /**
     * ת���˻�
     */
    @Column(name = "from_address")
    private String fromAddress;

    /**
     * ת�˽��
     */
    private String amount;

    @Column(name = "pre_send_amount")
    private String preSendAmount;

    @Column(name = "suf_send_amount")
    private String sufSendAmount;

    @Column(name = "source_amount")
    private String sourceAmount;

    private Integer proportion;



    /**
     * ����
     */
    @Column(name = "coin_name")
    private String coinName;

    /**
     * ִ��ʱ��
     */
    @Column(name = "execut_time")
    private String executTime;

    /**
     * ����
     */
    @Column(name = "rule_id")
    private String ruleId;

    /**
     * Ǯ��
     */
    @Column(name = "wallet_id")
    private String walletId;

    /**
     * ֪ͨ
     */
    private String memos;

    /**
     * ��ϣֵ
     */
    @Column(name = "trade_hash")
    private String tradeHash;

    /**
     * ״̬:0Ԥ����1:��ʼת��2��ת�˽���
     */
    private Integer status;

    /**
     * ת�˵�����issuer
     */
    @Column(name = "token_address")
    private String tokenAddress;

    /**
     * ת�˵���ID
     */
    @Column(name = "chain_id")
    private String chainId;

    /**
     * frozen_release_excel_source�������
     */
    @Column(name = "source_id")
    private String sourceId;

    /**
     * frozen_release_detail_rule�������
     */
    @Column(name = "detail_id")
    private String detailId;

    /**
     * ����ʱ��
     */
    @Column(name = "create_time")
    private String createTime;

    @Column(name = "update_time")
    private String updateTime;


}