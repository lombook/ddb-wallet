package com.jinglitong.springshop.servcie;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.springshop.entity.Customer;
import com.jinglitong.springshop.entity.DdbCustIntegralRecord;
import com.jinglitong.springshop.entity.DdbIntegralWallet;
import com.jinglitong.springshop.entity.Orders;
import com.jinglitong.springshop.mapper.DdbCustIntegralRecordMapper;
import com.jinglitong.springshop.mapper.DdbIntegralWalletMapper;
import com.jinglitong.springshop.mapper.OrdersMapper;
import com.jinglitong.springshop.vo.request.InteCustRecordVo;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class IntegralCustRecordService {
	@Autowired
    private DdbCustIntegralRecordMapper integralCustRecordMapper;
	
	@Autowired
    private OrdersMapper ordersMapper;
	
	@Autowired
    private DdbIntegralWalletMapper integralWalletMapper;
	
	@Value("${integral.cust.record}")
	private String inte_record_for_order;
	
	public HashMap<String, Object> getRecords(InteCustRecordVo vo,String custId) {
		// TODO Auto-generated method stub

		if (vo.getPageNum() != null && vo.getPageSize() != null) {
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        }
		
		// DdbCustIntegralRecord 记录
		String integname = vo.getRealname();

		DdbCustIntegralRecord ddbCustIntegralRecord = new DdbCustIntegralRecord();
		ddbCustIntegralRecord.setIntegName(integname);
		ddbCustIntegralRecord.setCustId(custId);
		List<DdbCustIntegralRecord> list = integralCustRecordMapper.getAssetsRecord(ddbCustIntegralRecord);
		List<InteCustRecordVo> listVo = getListVo(list);
		DdbIntegralWallet wallet = new DdbIntegralWallet();
		wallet.setCustId(custId);
		wallet.setInteName(integname);
		wallet = integralWalletMapper.selectOne(wallet);
		PageInfo pageinfo = new PageInfo(list);
		HashMap<String, Object> map = new HashMap<>();
		map.put("pageCount", pageinfo.getTotal());
		map.put("userAssetsRecord", listVo);
		map.put("userAssets", (wallet == null)? 0:new BigDecimal(wallet.getAmount()).divide(new BigDecimal(100)));
		return map;
	}
	
	private List<InteCustRecordVo> getListVo(List<DdbCustIntegralRecord> list) {
		// TODO Auto-generated method stub
		List<InteCustRecordVo> listVo = new ArrayList<InteCustRecordVo>();
		if (list == null || list.size() == 0) {
			return listVo;
			// return null;
		}
		String inteRecordForOrder = inte_record_for_order;
		String[] recordForOrderList = inteRecordForOrder.split(",");
		for (DdbCustIntegralRecord ddbCustIntegralRecord : list) {
			InteCustRecordVo ddbUseAssetsRecordVo = new InteCustRecordVo();			
			// 订单的请求ID
			String flowId = ddbCustIntegralRecord.getFlowId();
			String realName = ddbCustIntegralRecord.getRealName();// sb_dingdan,mb_dingdan,sb_fenqi
			
			// 由于需求原因  ，  所以在此处根据flow_id查询订单号。而不是写关联查询的sql。
//			if("sb_dingdan".equals(realName) || "mb_dingdan".equals(realName) || "sb_fenqi".equals(realName)) {
//				// 查询订单
//				flowId = queryOrderID(flowId);
//			}else {
//				flowId= "";
//			}
			String shopTrad = "";
			for(String str : recordForOrderList) {
				if(str.equals(realName)) {
					shopTrad = queryOrderID(flowId);
					break;
				}
			}
				
			Integer amount = ddbCustIntegralRecord.getAmount() == null ? 0 : ddbCustIntegralRecord.getAmount();
			BigDecimal amountVo = new BigDecimal(amount).divide(new BigDecimal("100"));

			ddbUseAssetsRecordVo.setAmount(amountVo.toString());
			ddbUseAssetsRecordVo.setRealname(ddbCustIntegralRecord.getRealName());

			ddbUseAssetsRecordVo.setCreateTime(ddbCustIntegralRecord.getCreateTime());
			ddbUseAssetsRecordVo.setOrderNum(shopTrad);
			ddbUseAssetsRecordVo.setInteType(ddbCustIntegralRecord.getType());
			listVo.add(ddbUseAssetsRecordVo);

		}
		return listVo;
	}
	
	private String queryOrderID(String flowId) {
		// TODO Auto-generated method stub

		Orders ddbOrder = new Orders();
		ddbOrder.setZid(flowId);
		
		try{
			ddbOrder = ordersMapper.selectOne(ddbOrder);
			return ddbOrder.getSn();// 订单
		}catch(Exception e){
			throw new RuntimeException("不存在对应的订单");			
		}
		
	}

}
