package com.jinglitong.wallet.give.mapper;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jinglitong.wallet.api.model.Feedback;
import com.jinglitong.wallet.api.model.view.FeedbackVo;
import com.jinglitong.wallet.give.util.MyMapper;

@Service
public interface FeedbackMapper extends MyMapper<Feedback> {
	
	List<Feedback> getFeedbackList(FeedbackVo vo);
}
