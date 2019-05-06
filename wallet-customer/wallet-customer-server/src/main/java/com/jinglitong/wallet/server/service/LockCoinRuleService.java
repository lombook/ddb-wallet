package com.jinglitong.wallet.server.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.wallet.api.model.LockCoinRule;
import com.jinglitong.wallet.api.model.SubChain;
import com.jinglitong.wallet.api.model.logic.LWalletVO;
import com.jinglitong.wallet.api.model.logic.LockCoinRuleChainCoin;
import com.jinglitong.wallet.api.model.view.*;
import com.jinglitong.wallet.server.mapper.LockCoinRuleMapper;
import com.jinglitong.wallet.server.util.UuidUtil;
import com.jinglitong.wallet.server.util.DateUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

@Service
@Transactional
public class LockCoinRuleService {

    @Resource
    private LockCoinRuleMapper lockCoinRuleMapper;
    
    @Autowired
    private SwtChainService swtChainService;

    /**
     * 查看锁仓规则
     * @param lockCoinRuleVo
     * @return
     */
    public HashMap<String, Object> getLockCoinRules(LockCoinRuleVo lockCoinRuleVo) {
        HashMap<String, Object> selectmap = new HashMap<>();
        if (lockCoinRuleVo.getPage() != null && lockCoinRuleVo.getRows() != null) {
            PageHelper.startPage(lockCoinRuleVo.getPage(), lockCoinRuleVo.getRows());
        }

        Weekend<LockCoinRule> weekend = Weekend.of(LockCoinRule.class);
        WeekendCriteria<LockCoinRule, Object> criteria = weekend.weekendCriteria();
        if(null !=lockCoinRuleVo.getRuleStatus()){
            criteria.andEqualTo(LockCoinRule::getRuleStatus, lockCoinRuleVo.getRuleStatus());
            criteria.andEqualTo(LockCoinRule::getAppId, lockCoinRuleVo.getAppId());
        }
        weekend.setOrderByClause(" create_time desc ");


        List<LockCoinRule> rules = lockCoinRuleMapper.selectByExample(weekend);
        PageInfo pageinfo = new PageInfo(rules);
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageCount",pageinfo.getTotal());
        map.put("rules",rules);
        return  map;
    }

    /**
     * 添加锁仓规则
     * @param rule
     * @return
     */
    public Integer addLockCoinRule(LockCoinRule rule){
        rule.setLockRuleId( UuidUtil.getUUID());
        return lockCoinRuleMapper.insert(rule);
    }

    /**
     * 删除锁仓规则
     * @param rule
     * @return
     */
    public Integer deleteLockCoinRule(LockCoinRule rule){
        return lockCoinRuleMapper.delete(rule);
    }

    /**
     * 修改锁仓规则
     * @param rule
     * @return
     */
    public Integer updateLockCoinRule(LockCoinRule rule){
        return lockCoinRuleMapper.updateByPrimaryKey(rule);
    }

    /**
     * 查询规则
     * @param rule
     * @return
     */
    public LockCoinRule getLockCoinRule(LockCoinRule rule){
        return  lockCoinRuleMapper.selectOne(rule);
    }

    public Map<String ,Object> getLockCoinRuleList(LockConRuleVO lockCoinRuleVo) {
        if (lockCoinRuleVo.getPage() != null && lockCoinRuleVo.getRows() != null) {
            PageHelper.startPage(lockCoinRuleVo.getPage(), lockCoinRuleVo.getRows());
        }
        List<LockCoinRuleChainCoin> rules = lockCoinRuleMapper.selectAllList(lockCoinRuleVo);
        PageInfo pageinfo = new PageInfo(rules);
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageCount", pageinfo.getTotal());
        map.put("data", rules);
        return map;
    }

    @Transactional
    public Boolean createLockCoinRuleList(LockCreateConRuleVO lockCoinRuleVo) {
    	Map<String,String> map = new HashMap<String, String>();
    	map.put("chainId", lockCoinRuleVo.getChainId());
    	map.put("coinId", lockCoinRuleVo.getCoinId());
    	map.put("appId",String.valueOf(lockCoinRuleVo.getAppId()));//add appid
    	List<LockCoinRule> list =lockCoinRuleMapper.selectById(map);//一条链一个币，一个总账
    	LWalletVO result = new LWalletVO();
    	String custId ="";
    	if(list.size() > 0){
    		result.setPublicAddress(list.get(0).getAddress());
    		result.setPayPasswd(list.get(0).getSecret());
    		custId=list.get(0).getCustId();
    	}else{
    		WalletVO vo = new WalletVO();
    		custId=UuidUtil.getUUID();
        	vo.setChainId(lockCoinRuleVo.getChainId());
        	vo.setCustId(custId);
        	vo.setPasswd("123456");
        	result =swtChainService.createWallet(vo);
        	if(result.getResCode() != 0){
                throw new RuntimeException("批量创建规则异常:解仓总账地址创建失败");
            }
    	}
    	List<Rule> rulelist = lockCoinRuleVo.getRulelist();
        for (Rule rule: rulelist) {
            LockCoinRule lockCoinRule = new LockCoinRule();
            lockCoinRule.setAppId(lockCoinRuleVo.getAppId());//add appid
            lockCoinRule.setLockRuleId(UuidUtil.getUUID());
            if (!StringUtils.isEmpty(lockCoinRuleVo.getChainId()))
                lockCoinRule.setChainId(lockCoinRuleVo.getChainId());
            if (!StringUtils.isEmpty(lockCoinRuleVo.getCoinId()))
                lockCoinRule.setCoinId(lockCoinRuleVo.getCoinId());
            if (!StringUtils.isEmpty(lockCoinRuleVo.getLockAddress()))
                lockCoinRule.setLockAddress(lockCoinRuleVo.getLockAddress());
            if (!StringUtils.isEmpty(lockCoinRuleVo.getMinLockAmout()))
                lockCoinRule.setMinLockAmout(lockCoinRuleVo.getMinLockAmout());
            if(rule.getRuleStatus() != null)
                lockCoinRule.setRuleStatus(rule.getRuleStatus());
            else
                lockCoinRule.setRuleStatus(false);
            if (rule.getLockDays()!= null)
                lockCoinRule.setLockDays(rule.getLockDays());
            if (rule.getLockRate()!=null)
                lockCoinRule.setLockRate(rule.getLockRate());
            String dateTime = DateUtils.getDateTime();
            lockCoinRule.setCreateTime(dateTime);
            lockCoinRule.setUpdateTime(dateTime);
            lockCoinRule.setAddress(result.getPublicAddress());
            lockCoinRule.setSecret(result.getPayPasswd());
            lockCoinRule.setCustId(custId);
            int insert = lockCoinRuleMapper.insert(lockCoinRule);
            if(insert < 0){
                throw new RuntimeException("批量创建规则异常");
            }
        }
            return  true;
    }
    /**
     * 
     * 功能说明:隐藏操作
     * @param lockCoinRuleVo
     * @return
     */
	public Boolean updateRuleState(LockConRuleVO lockCoinRuleVo) {
		LockCoinRule lockCoinRule = new LockCoinRule();
		if (lockCoinRuleVo.getRuleStatus() != null)
			lockCoinRule.setRuleStatus(lockCoinRuleVo.getRuleStatus());
			lockCoinRule.setUpdateTime(DateUtils.getDateTime());
			Weekend<LockCoinRule> weekend = Weekend.of(LockCoinRule.class);
			WeekendCriteria<LockCoinRule,Object> Criteria =weekend.weekendCriteria();
			Criteria.andEqualTo(LockCoinRule::getLockRuleId, lockCoinRuleVo.getLockRuleId());
			Criteria.andEqualTo(LockCoinRule::getAppId, lockCoinRuleVo.getAppId());// add appid
			int i = lockCoinRuleMapper.updateByExampleSelective(lockCoinRule, weekend);
			if (i > 0)
				return true;
			return false;
	}
}
