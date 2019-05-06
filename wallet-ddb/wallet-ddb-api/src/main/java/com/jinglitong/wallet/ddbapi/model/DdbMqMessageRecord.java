package com.jinglitong.wallet.ddbapi.model;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Data
@Table(name = "ddb_mq_message_record")
public class DdbMqMessageRecord {

    public DdbMqMessageRecord() {
    }

    public DdbMqMessageRecord(String group, String topic, String tag, String msgId, Integer sendType, Integer groupType, String createTime, String data) {
        this.groupName = group;
        this.topic = topic;
        this.tag = tag;
        this.msgId = msgId;
        this.sendType = sendType;
        this.groupType = groupType;
        this.createTime = createTime;
        this.dataBody = data;
        
    }

    public DdbMqMessageRecord(String groupName, String topic, String tag, String msgId, Integer sendType,
			Integer groupType, String createTime, String dataBody, Boolean status) {
		super();
		this.groupName = groupName;
		this.topic = topic;
		this.tag = tag;
		this.msgId = msgId;
		this.sendType = sendType;
		this.groupType = groupType;
		this.createTime = createTime;
		this.dataBody = dataBody;
		this.status = status;
	}

    
	public DdbMqMessageRecord(String flowId, String groupName, String topic, String tag, String msgId, Integer sendType,
			Integer groupType, String createTime, String dataBody, Boolean status) {
		super();
		this.flowId = flowId;
		this.groupName = groupName;
		this.topic = topic;
		this.tag = tag;
		this.msgId = msgId;
		this.sendType = sendType;
		this.groupType = groupType;
		this.createTime = createTime;
		this.dataBody = dataBody;
		this.status = status;
	}


	/**
     * id
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name ="flow_id")
    private String flowId;
    /**
     * ��������/��������
     */
    @Column(name = "group_name")
    private String groupName;

    /**
     * mq-topic
     */
    private String topic;

    /**
     * topic - tag
     */
    private String tag;

    /**
     * mq����id
     */
    @Column(name = "msg_id")
    private String msgId;

    /**
     * ���ͷ�ʽ��1���� 2����ʱ 3 ����
     */
    @Column(name = "send_type")
    private Integer sendType;

    /**
     * ���ͣ�1������ 2������
     */
    @Column(name = "group_type")
    private Integer groupType;

    /**
     * ����ʱ��
     */
    @Column(name = "create_time")
    private String createTime;

    /**
     * mq����
     */
    @Column(name = "data_body")
    private String dataBody;

    @Column(name = "status")
    private Boolean status;
    
    
    
}