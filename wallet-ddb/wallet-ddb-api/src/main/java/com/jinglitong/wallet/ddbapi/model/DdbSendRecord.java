package com.jinglitong.wallet.ddbapi.model;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Data
@Table(name = "ddb_send_record")
public class DdbSendRecord {

    public DdbSendRecord() {
    }

    public DdbSendRecord(String flowId, String address, Integer type, String retryExcuseTime, Integer retryTimes, String createTime, String body) {
        this.flowId = flowId;
        this.address = address;
        this.type = type;
        this.retryExcuseTime = retryExcuseTime;
        this.retryTimes = retryTimes;
        this.createTime = createTime;
        this.body = body;
    }

    /**
     * id
     */
   @Id
    private Integer id;

    /**
     * flowid
     */
    @Column(name = "flow_id")
    private String flowId;

    /**
     * ���͵�ַ
     */
    private String address;

    /**
     * ���ͣ�1���ɹ� 2��ʧ��
     */
    private Integer type;

    @Column(name = "retry_excuse_time")
    private String retryExcuseTime;

    @Column(name = "trtry_times")
    private Integer retryTimes;
    /**
     * ����ʱ��
     */
    @Column(name = "create_time")
    private String createTime;

    /**
     * ��������
     */
    private String body;


}