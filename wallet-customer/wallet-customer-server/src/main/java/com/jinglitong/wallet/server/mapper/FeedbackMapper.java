package com.jinglitong.wallet.server.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jinglitong.wallet.api.model.Feedback;
import com.jinglitong.wallet.api.model.view.FeedbackVo;
import com.jinglitong.wallet.server.util.MyMapper;

@Service
public interface FeedbackMapper extends MyMapper<Feedback> {
	
	List<Feedback> getFeedbackList(FeedbackVo vo);
	/**
	 * 获得某个用户的反馈列表，这些反馈已经得到客服人员的回复
	 * @param cust_id cust_id
	 * @return
	 */
	List<Map<String,Object>> selectAlreadyRepliedFeedbackList(String cust_id,String appId);
	
	/**
	 * 获得反馈详情
	 * @param cust_id cust_id
	 * @param feedback_id feedback_id
	 * @return
	 */
	Map<String,Object> selectFeedbackDetail(String cust_id,String feedback_id,String appId);
	
	/**
	 * 更新用户是否查看过客服回复信息状态 
	 * @param cust_id cust_id
	 * @param feedback_id feedback_id
	 * @param haveseen haveseen
	 */
	void upFeedbackStatus(String cust_id,String feedback_id,int haveseen,String appId);
	
	/**
	 * 获得某个用户未查看的客服回复信息数目
	 * @param cust_id cust_id
	 * @param app_id app_id
	 * @return
	 */
	Integer selectUnReadFeedbackNum(String cust_id,String app_id);
}
