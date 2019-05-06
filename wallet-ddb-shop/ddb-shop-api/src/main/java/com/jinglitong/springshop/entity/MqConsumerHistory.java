package com.jinglitong.springshop.entity;

import java.util.Date;
import javax.persistence.*;


import lombok.Data;

@Data
@Table(name = "mq_consumer_history")
public class MqConsumerHistory {
	  public MqConsumerHistory() {
	    }
	    public MqConsumerHistory(String msgId, Date createTime) {
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
	    private Date createTime;

		private String tag;
}