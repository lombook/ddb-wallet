package com.jinglitong.wallet.ddbserver.mapper;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jinglitong.wallet.api.model.FeedBackSub;
import com.jinglitong.wallet.api.model.view.FeedbackVo;
import com.jinglitong.wallet.ddbserver.util.MyMapper;

@Service
public interface FeedbackSubMapper extends MyMapper<FeedBackSub> {

	 List<FeedBackSub> getFeedbackList(FeedbackVo vo);
}
