package com.jinglitong.wallet.server.mapper;

import java.util.List;

import com.jinglitong.wallet.api.model.Seller;
import com.jinglitong.wallet.api.model.view.SellerVo;
import com.jinglitong.wallet.server.util.MyMapper;

public interface SellerMapper extends MyMapper<Seller> {
	
	List<Seller> selectAllByApp(SellerVo SellerVo);
}