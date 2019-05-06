package com.jinglitong.wallet.give.mapper;

import java.util.List;

import com.jinglitong.wallet.give.util.MyMapper;
import org.springframework.stereotype.Service;

import com.jinglitong.wallet.api.model.FeedBackSub;
import com.jinglitong.wallet.api.model.view.FeedbackVo;

@Service
public interface FeedbackSubMapper extends MyMapper<FeedBackSub> {

	 List<FeedBackSub> getFeedbackList(FeedbackVo vo);
}
