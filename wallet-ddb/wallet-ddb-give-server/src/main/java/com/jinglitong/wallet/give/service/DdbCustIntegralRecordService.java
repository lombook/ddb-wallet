package com.jinglitong.wallet.give.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jinglitong.wallet.give.util.JsonUtil;
import com.jinglitong.wallet.give.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.api.model.view.PageVO;
import com.jinglitong.wallet.give.mapper.DdbCustIntegralRecordMapper;

@Service
@Transactional
public class DdbCustIntegralRecordService {

	@Autowired
	private DdbCustIntegralRecordMapper ddbCustIntegralRecordMapper;
	
	public Map<String,Object> getIntegralRecord(PageVO vo){
		Customer cust = (Customer)SessionUtil.getUserInfo();
		String custId = cust.getCustId();
		if(vo.getPage() != null && vo.getRows() != null) {
			PageHelper.startPage(vo.getPage(), vo.getRows(),"create_time desc");
			
		}
		List<Map<String,Object>> list = ddbCustIntegralRecordMapper.getIntegralRecord(custId);
		for(Map map : list) {
			map.put("baofen", ((Integer)map.get("baofen")).doubleValue()/100+"");
		}
		PageInfo pageinfo = new PageInfo(list);
		HashMap<String, Object> map = new HashMap<>();
		map.put("pageCount", pageinfo.getTotal());
		map.put("recordList", list);
		
		return JsonUtil.toJsonSuccess("查询成功", map);
	}
	
	
	
}
