package com.jinglitong.wallet.ddbapi.model;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;
@Data
@Table(name = "ddb_shoreholder")
public class DdbShoreholder {
    private Integer id;

    /**
     * ҵ��id
     */
    @Column(name = "z_id")
    private String zId;

    /**
     * �û�id
     */
    @Column(name = "cust_id")
    private String custId;

    /**
     * 1:�´��ɶ�  2:�ϻ�ɶ�  3:��ҵ�ɶ�  4:�����ɶ�
     */
    @Column(name = "level_define")
    private String levelDefine;

    /**
     * ���Ѿ���ֵ
     */
    @Column(name = "sum_amount")
    private Integer sumAmount;

    /**
     * �������ֵ
     */
    @Column(name = "seed_amount")
    private Integer seedAmount;

    @Column(name = "base_stone")
    private Integer baseStone;

    /**
     * ����ʱ��
     */
    @Column(name = "create_time")
    private String createTime;

    /**
     * �޸�ʱ��
     */
    @Column(name = "update_time")
    private String updateTime;

    @Column(name = "team_sum_amount")
    private Long teamSumAmount;

    @Column(name = "team_member_levels_cnt")
    private String teamMemberLevelsCnt;

    /**
     * 自己购买特定产品数量
     */
    @Column(name = "own_special_num")
    private Integer ownSpecialNum;
    /**
     * 直接下级购买指定数量
     */
    @Column(name = "dsub_special_num")
    private Integer dsubSpecialNum;

}