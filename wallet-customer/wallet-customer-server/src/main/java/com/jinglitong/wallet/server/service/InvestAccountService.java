package com.jinglitong.wallet.server.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jinglitong.wallet.server.mapper.InvestAccountMapper;
import com.jinglitong.wallet.server.mapper.MainChainMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.wallet.api.model.InvestAccount;
import com.jinglitong.wallet.api.model.view.InvestAccountVO;
import com.jinglitong.wallet.api.model.view.MainChainVO;
import com.jinglitong.wallet.server.util.DateUtils;
import com.jinglitong.wallet.server.util.UuidUtil;

import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import com.jinglitong.wallet.server.util.AesECBUtils;

import static com.jinglitong.wallet.server.common.ErrorEnum.ERROR_31460;

@Service
@Transactional
public class InvestAccountService {
	
	@Autowired
	private InvestAccountMapper investAccountMapper;
	@Autowired
	private MainChainMapper mainChainMapper;
	@Value("${aes.secrect}")
	private String  secretKey ;
	
	
	
	
	public int addInvestAccount(InvestAccountVO vo) throws Exception {
		boolean state=vo.getState();
		InvestAccount ia=new InvestAccount();
		String chainId=vo.getChainId();
		ia.setChainId(chainId);
		ia.setStatus(true);
		List<InvestAccount> list = investAccountMapper.select(ia);
		if(state) {
			if(list.size()>0) {
				throw new Exception(ERROR_31460.getCode()+"");
			}
		}
		ia.setInvestId(UuidUtil.getUUID());
		ia.setActiveNum(vo.getActiveNum());
		ia.setCurrency(vo.getCurrency());
		ia.setInvestAddress(vo.getInvestAddress());
		ia.setStatus(vo.getState());
		ia.setInvestName(vo.getInvestName());
		
		//私钥加密
		String secret=null;
		if(vo.getInvestSecret()!=null && !"".equals(vo.getInvestSecret())) {
			secret=AesECBUtils.encrypt(vo.getInvestSecret().trim(), secretKey);
		}
		ia.setInvestSecret(secret);
		String now=DateUtils.getDateTime();
		ia.setCreateTime(now);
		ia.setUpdateTime(now);
		ia.setAppId(vo.getAppId());
		int i=investAccountMapper.insert(ia);
		
		MainChainVO chainVo =new MainChainVO();
		chainVo.setChainId(vo.getChainId());
		if(list.size()==0) {
			chainVo.setActiveFlag(vo.getState());
		}else {//已有true数据
			chainVo.setActiveFlag(true);
		}
		mainChainMapper.updateByChainId(chainVo);
		return i;
	}
	
	public int updInvestAccount(InvestAccountVO vo) throws Exception {
		boolean state=vo.getState();
		InvestAccount ia=new InvestAccount();
		String chainId=vo.getChainId();
		ia.setChainId(chainId);
		ia.setStatus(true);
		List<InvestAccount> list = investAccountMapper.select(ia);
		if(state) {
			if(list.size()>0) {
				InvestAccount account=list.get(0);
				if(!account.getInvestId().equals(vo.getInvestId())) {
					throw new Exception(ERROR_31460.getCode()+"");
				}
			}
		}
		ia.setInvestId(vo.getInvestId());
		ia.setActiveNum(vo.getActiveNum());
		ia.setCurrency(vo.getCurrency());
		ia.setInvestAddress(vo.getInvestAddress());
		ia.setStatus(vo.getState());
		ia.setInvestName(vo.getInvestName());
		
		//私钥加密
		String secret=null;
		if(vo.getInvestSecret()!=null && !"".equals(vo.getInvestSecret())) {
			secret=AesECBUtils.encrypt(vo.getInvestSecret().trim(), secretKey);
		}
		ia.setInvestSecret(secret);
		String now=DateUtils.getDateTime();
		ia.setUpdateTime(now);
		ia.setAppId(vo.getAppId());
		Weekend<InvestAccount>weekend = Weekend.of(InvestAccount.class);
		WeekendCriteria<InvestAccount, Object> Criteria =weekend.weekendCriteria();
		Criteria.andEqualTo(InvestAccount::getInvestId, vo.getInvestId());
		int i=investAccountMapper.updateByExampleSelective(ia, weekend);
		
		MainChainVO chainVo =new MainChainVO();
		chainVo.setChainId(vo.getChainId());
		
		if(list.size() > 0) {//有true数据
			if(vo.getState()) {//如果设置为true，全改为true。
				chainVo.setActiveFlag(true);
			}else {//false
				if(vo.getInvestId().equals(list.get(0).getInvestId())) {//一对一关系，修改的当前的
					chainVo.setActiveFlag(vo.getState());
				}else {//一对多  一个0 一个1
					chainVo.setActiveFlag(true);
				}
			}
		}else {//无true数据
			chainVo.setActiveFlag(vo.getState());
		}
		mainChainMapper.updateByChainId(chainVo);
		return i;
	}
	
	public Map<String , Object> qryInvestAccounts(InvestAccountVO vo){
		if (vo.getPage() != null && vo.getRows() != null) {
            PageHelper.startPage(vo.getPage(), vo.getRows());
        }
		 List<InvestAccount> list= investAccountMapper.qryInvestAccounts(vo);
        PageInfo pageinfo = new PageInfo(list);
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageCount",pageinfo.getTotal());
        map.put("investAccounts",list);
        return  map;
	}

}
