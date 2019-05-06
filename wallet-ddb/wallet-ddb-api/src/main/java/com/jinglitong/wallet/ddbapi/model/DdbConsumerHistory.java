package com.jinglitong.wallet.ddbapi.model;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;
@Data
@Table(name = "ddb_consumer_history")
public class DdbConsumerHistory {

    public DdbConsumerHistory() {
    }
    public DdbConsumerHistory(String msgId, String createTime) {
        this.msgId = msgId;
        this.createTime = createTime;
    }

    /**
     * id
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * mq����id
     */
    @Column(name = "msg_id")
    private String msgId;

    @Column(name = "flow_id")
    private String flowId;

    /**
     * ����ʱ��
     */
    @Column(name = "create_time")
    private String createTime;


}