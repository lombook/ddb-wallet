package com.jinglitong.wallet.ddbserver.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.api.model.logic.ReleaseCurrencyVo;
import com.jinglitong.wallet.ddbapi.model.DdbCustIntegralRecord;
import com.jinglitong.wallet.ddbapi.model.DdbCustOwnTree;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralWallet;
import com.jinglitong.wallet.ddbapi.model.DdbOrder;
import com.jinglitong.wallet.ddbapi.model.Orders;
import com.jinglitong.wallet.ddbapi.model.view.DdbUseAssetsRecordVo;
import com.jinglitong.wallet.ddbapi.model.view.DdbUserAssetVo;
import com.jinglitong.wallet.ddbserver.mapper.*;
import com.jinglitong.wallet.ddbserver.util.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DdbUserAssetsService {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private CustomerMapper customerMapper;

	@Autowired
	private DdbIntegralWalletMapper ddbIntegralWalletMapper;
	@Autowired
	private FrozenReleaseRuleMapper frozenReleaseRuleMapper;

	@Autowired
	private DdbCustIntegralRecordMapper ddbCustIntegralRecordMapper;
	
	@Autowired
	private DdbOrderMapper ddbOrderMapper;
	@Autowired
	private OrdersMapper ordersMapper;
	@Autowired
	private DdbCustOwnTreeMapper ddbCustOwnTreeMapper;
	
	@Value("${inte_record_for_ddborder}")
	private String inte_record_for_ddborder;
	
	@Value("${inte_record_for_shoporder}")
	private String inte_record_for_shoporder;

	public DdbUserAssetVo getUserAssetAmount(Customer customer) {
		// TODO Auto-generated method stub
		String custId = customer.getCustId();
		DdbIntegralWallet ddbIntegralWallet = new DdbIntegralWallet();
		ddbIntegralWallet.setCustId(custId);
		List<DdbIntegralWallet> assetsList = ddbIntegralWalletMapper.select(ddbIntegralWallet);
		DdbUserAssetVo vo = new DdbUserAssetVo();
		for (DdbIntegralWallet assets : assetsList) {

			String inteRealName = assets.getInteName() == null ? "" : assets.getInteName();
			// Integer amount = assets.getAmount()==null?0:(assets.getAmount()/100);//
			// 以分为单位,需要转换为元
			Long amount = assets.getAmount() == null ? 0 : assets.getAmount();
			BigDecimal amountVo = new BigDecimal(amount).divide(new BigDecimal("100"));

			// 根据类型 判断 去赋值
			if ("baofen_all".equals(inteRealName)) {// 宝分 baofen_all
				vo.setIntegralType(inteRealName);
				vo.setIntegralAmount(amountVo.toString());

			} else if ("shubei_all".equals(inteRealName)) {// 树贝shubei_all
				vo.setBayTreeAmount(amountVo.toString());
				vo.setBayTreeType(inteRealName);

			} else if ("miaobei_all".equals(inteRealName)) {// 苗贝miaobei_all
				vo.setMiaoBayType(inteRealName);
				vo.setMiaoBayAmount(amountVo.toString());
			} else if ("xianbei_all".equals(inteRealName)) {// 现贝 xianbei_all
				vo.setXianBayAmount(amountVo.toString());
				vo.setXianBayType(inteRealName);

			} else if ("wubei_all".equals(inteRealName)) { // 物贝 wubei_all
				vo.setWuBayAmount(amountVo.toString());
				vo.setWuBayType(inteRealName);

			} else if ("jinbei_all".equals(inteRealName)) {
				vo.setJinBayAmount(amountVo.toString());
				vo.setJinBayType(inteRealName);
			} else if ("hezuo_all".equals(inteRealName)) {// 合作社股权
				vo.setCooperativeAmount(amountVo.toString());
				vo.setCooperativeType(inteRealName);
			} else if ("yumiao_all".equals(inteRealName)) {// 育苗基地股权
				vo.setBreedType(inteRealName);
				vo.setBreedAmount(amountVo.toString());
			} else if ("gufen_all".equals(inteRealName)) {// 育苗基地股权
				vo.setGuFenType(inteRealName);
				vo.setGuFenNum(amountVo.toString());
			}

		}
		//  由于不上线  所以数据表中没有对应数据  返回null，应app端要求：  赋予默认值 保证不为null  
		// 现贝
//		vo.setXianBayAmount("0");
//		vo.setXianBayType("");
		// 物贝
//		vo.setWuBayAmount("0");
//		vo.setWuBayType("");

		return vo;
	}

	public Map<String, Object> getReleaseAmount(ReleaseCurrencyVo vo) {
		Customer customer = customerMapper.selectByUsername(vo.getAccount(), vo.getAppId());
		List<ReleaseCurrencyVo> data = new ArrayList<ReleaseCurrencyVo>();
		data = frozenReleaseRuleMapper.selectBy(customer.getCustId(), vo.getWalletId(), vo.getAppId());
		List<ReleaseCurrencyVo> da = new ArrayList<ReleaseCurrencyVo>();
		for (ReleaseCurrencyVo rs : data) {
			Boolean flag = true;
			if (da.size() != 0) {
				for (ReleaseCurrencyVo ta : da) {
					if (ta.getCurrency().equals(rs.getCurrency())) {
						BigDecimal bigDecimal = new BigDecimal(ta.getSum());
						BigDecimal bigDecimal1 = new BigDecimal(rs.getSum());
						BigDecimal add = bigDecimal1.add(bigDecimal);
						ta.setSum(add.toPlainString());
						flag = false;
					}
				}
				if (flag) {
					ReleaseCurrencyVo releaseCurrencyVo = new ReleaseCurrencyVo();
					releaseCurrencyVo.setCurrency(rs.getCurrency());
					releaseCurrencyVo.setSum(rs.getSum());
					da.add(releaseCurrencyVo);
				}
			} else {
				ReleaseCurrencyVo releaseCurrencyVo = new ReleaseCurrencyVo();
				releaseCurrencyVo.setCurrency(rs.getCurrency());
				releaseCurrencyVo.setSum(rs.getSum());
				da.add(releaseCurrencyVo);
			}

		}
		HashMap<String, Object> map = new HashMap<>();
		if (data == null || data.size() == 0)
			map.put("data", da);
		else
			map.put("data", da);
		return map;
	}

	public HashMap<String, Object> getRecords(DdbUseAssetsRecordVo vo) {
		// TODO Auto-generated method stub

		if (vo.getPage() != null && vo.getRows() != null) {
			PageHelper.startPage(vo.getPage(), vo.getRows());
		}

		Customer customer = (Customer) SessionUtil.getUserInfo();
		// DdbCustIntegralRecord 记录
		String integname = vo.getRealname();

		DdbCustIntegralRecord ddbCustIntegralRecord = new DdbCustIntegralRecord();
		ddbCustIntegralRecord.setIntegName(integname);
		ddbCustIntegralRecord.setCustId(customer.getCustId());
		List<DdbCustIntegralRecord> list = ddbCustIntegralRecordMapper.getAssetsRecord(ddbCustIntegralRecord);
		List<DdbUseAssetsRecordVo> listVo = getListVo(list);
		PageInfo pageinfo = new PageInfo(list);
		HashMap<String, Object> map = new HashMap<>();

		map.put("pageCount", pageinfo.getTotal());
		map.put("userAssetsRecord", listVo);

		return map;
	}

	private List<DdbUseAssetsRecordVo> getListVo(List<DdbCustIntegralRecord> list) {
		// TODO Auto-generated method stub
		List<DdbUseAssetsRecordVo> listVo = new ArrayList<DdbUseAssetsRecordVo>();
		if (list == null || list.size() == 0) {
			return listVo;
			// return null;
		}
		String inteRecordForDdbOrder = inte_record_for_ddborder;
		String[] recordForDdbOrderList = inteRecordForDdbOrder.split(",");
		
		String inteRecordForShopOrder = inte_record_for_shoporder;
		String[] recordForShopOrderList = inteRecordForShopOrder.split(",");
		
		for (DdbCustIntegralRecord ddbCustIntegralRecord : list) {
			DdbUseAssetsRecordVo ddbUseAssetsRecordVo = new DdbUseAssetsRecordVo();
			
			// 订单的请求ID
			String flowId = ddbCustIntegralRecord.getFlowId();
			logger.info(ddbCustIntegralRecord.toString());
            logger.info("==============交易记录flowId："+flowId);
			String realName = ddbCustIntegralRecord.getRealName();// sb_dingdan,mb_dingdan,sb_fenqi
			
			// 由于需求原因  ，  所以在此处根据flow_id查询订单号。而不是写关联查询的sql。
//			if("sb_dingdan".equals(realName) || "mb_dingdan".equals(realName) || "sb_fenqi".equals(realName)) {
//				// 查询订单
//				flowId = queryOrderID(flowId);
//			}else {
//				flowId= "";
//			}
			String shopTrad = "";
			for(String str : recordForDdbOrderList) {
				if(str.equals(realName)) {
					shopTrad = queryOrderID(flowId,"ddb");
					logger.info("===========shopTrod:"+shopTrad);
					break;
				}else {
					shopTrad= "";
				}
			}
			if(shopTrad.equals("")){
				for(String str : recordForShopOrderList) {
					if(str.equals(realName)) {
						shopTrad = queryOrderID(flowId,"shop");
						logger.info("===========shopTrod:"+shopTrad);
						break;
					}else {
						shopTrad= "";
					}
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

	private String queryOrderID(String flowId,String type) {
		// TODO Auto-generated method stub

		if(type.equals("ddb")){
			DdbOrder ddbOrder = new DdbOrder();
			ddbOrder.setFlowId(flowId);
			DdbOrder selectOne = ddbOrderMapper.selectOne(ddbOrder);
			String shopTrade = selectOne.getShopTrade();// 订单
			return shopTrade;
		}else if(type.equals("shop")){
			Orders ddbOrder = new Orders();
			ddbOrder.setZid(flowId);
			Orders selectOne = ordersMapper.selectOne(ddbOrder);
			String shopTrade = selectOne.getSn();// 订单
			return shopTrade;
		}
		return null;
		
	}

	public Integer getTreeNum() {
		// TODO Auto-generated method stub
		 // 调用查询积分的方法
        Customer customer = (Customer) SessionUtil.getUserInfo();
        DdbCustOwnTree ddbCustOwnTree = new DdbCustOwnTree();
        ddbCustOwnTree.setCustId(customer.getCustId());
        ddbCustOwnTree = ddbCustOwnTreeMapper.selectOne(ddbCustOwnTree);
        Integer treeNum = 0;
        if(ddbCustOwnTree != null) {
        	treeNum = ddbCustOwnTree.getTreeNum();
        }
        
        
        return treeNum;
		
	}
	
	
	

}
