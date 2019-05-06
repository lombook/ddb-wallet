package com.jinglitong.wallet.ddbserver.mapper;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jinglitong.wallet.api.model.Feedback;
import com.jinglitong.wallet.api.model.view.FeedbackVo;
import com.jinglitong.wallet.ddbserver.util.MyMapper;

@Service
public interface FeedbackMapper extends MyMapper<Feedback> {
	
	List<Feedback> getFeedbackList(FeedbackVo vo);
}
