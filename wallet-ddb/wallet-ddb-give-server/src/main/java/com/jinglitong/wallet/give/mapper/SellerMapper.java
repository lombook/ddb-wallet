package com.jinglitong.wallet.give.mapper;

import java.util.List;

import com.jinglitong.wallet.api.model.Seller;
import com.jinglitong.wallet.api.model.view.SellerVo;
import com.jinglitong.wallet.give.util.MyMapper;

public interface SellerMapper extends MyMapper<Seller> {
	
	List<Seller> selectAllByApp(SellerVo SellerVo);
}