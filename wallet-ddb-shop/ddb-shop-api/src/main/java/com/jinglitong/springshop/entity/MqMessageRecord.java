package com.jinglitong.springshop.entity;

import java.util.Date;

import javax.persistence.*;

import lombok.Data;

@Data
@Table(name = "mq_message_record")
public class MqMessageRecord {
	 public MqMessageRecord() {
	    }

	    public MqMessageRecord(String group, String topic, String tag, String msgId, Integer sendType, Integer groupType, Date createTime, String data) {
	        this.groupName = group;
	        this.topic = topic;
	        this.tag = tag;
	        this.msgId = msgId;
	        this.sendType = sendType;
	        this.groupType = groupType;
	        this.createTime = createTime;
	        this.dataBody = data;
	        
	    }

	    public MqMessageRecord(String groupName, String topic, String tag, String msgId, Integer sendType,
				Integer groupType, Date createTime, String dataBody, Boolean status) {
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

	    
		public MqMessageRecord(String flowId, String groupName, String topic, String tag, String msgId, Integer sendType,
				Integer groupType, Date createTime, String dataBody, Boolean status) {
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
		@Id
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
	    private Date createTime;

	    /**
	     * mq����
	     */
	    @Column(name = "data_body")
	    private String dataBody;

	    @Column(name = "status")
	    private Boolean status;
}