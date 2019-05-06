package com.jinglitong.wallet.server.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.wallet.api.model.MainChain;
import com.jinglitong.wallet.api.model.SubChain;
import com.jinglitong.wallet.api.model.view.BalanceVO;
import com.jinglitong.wallet.api.model.view.MainChainVO;
import com.jinglitong.wallet.server.mapper.MainChainMapper;
import com.jinglitong.wallet.server.mapper.SubChainMapper;
import com.jinglitong.wallet.server.util.DateUtils;
import com.jinglitong.wallet.server.util.UuidUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MainChainService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MainChainMapper mainChainMapper;

    @Autowired
    private SubChainMapper subChainMapper;

    public List<MainChain> getAllChain(BalanceVO vo){
        return mainChainMapper.getChainAll(vo);
    }


    public HashMap<String,Object> getMainChains(MainChainVO mainChainVO) {
        if (mainChainVO.getPage() != null && mainChainVO.getRows() != null) {
            PageHelper.startPage(mainChainVO.getPage(), mainChainVO.getRows());
        }
        List<Map> mainChainList = mainChainMapper.getMainChains(mainChainVO);
        PageInfo pageinfo = new PageInfo(mainChainList);
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageCount",pageinfo.getTotal());
        map.put("appHelps",mainChainList);
        return  map;
    }
    @Transactional
    public Integer createMainChain(MainChainVO mainChainVO) {
        MainChain mainChain = new MainChain();
        String uuid = UuidUtil.getUUID();
        mainChain.setChainId(uuid);
        if(!StringUtils.isEmpty(mainChainVO.getChainCurrency()))
            mainChain.setChainCurrency(mainChainVO.getChainCurrency());
        if(!StringUtils.isEmpty(mainChainVO.getChainCurrencyImg()))
            mainChain.setChainCurrencyImg(mainChainVO.getChainCurrencyImg());
        if(!StringUtils.isEmpty(mainChainVO.getChainDesc()))
            mainChain.setChainDesc(mainChainVO.getChainDesc());
        if(!StringUtils.isEmpty(mainChainVO.getChainInterface()))
            mainChain.setChainInterface(mainChainVO.getChainInterface());
        if(!StringUtils.isEmpty(mainChainVO.getChainName()))
            mainChain.setChainName(mainChainVO.getChainName());
        if(!StringUtils.isEmpty(mainChainVO.getHandleName()))
            mainChain.setHandleName(mainChainVO.getHandleName());
        if(mainChainVO.getState() != null){
            mainChain.setState(mainChainVO.getState());
        }
        if(mainChainVO.getActiveFlag() != null){
            mainChain.setActiveFlag(mainChainVO.getActiveFlag());
        }else {
            mainChain.setActiveFlag(false);
        }
        mainChain.setAppId(mainChainVO.getAppId());
        String dateTime = DateUtils.getDateTime();
        mainChain.setCreateTime(dateTime);
        mainChain.setUpdateTime(dateTime);
        int insert = mainChainMapper.insert(mainChain);
        if(insert > 0){
            SubChain subChain = new SubChain();
            subChain.setCurrency(mainChain.getChainCurrency());
            subChain.setCoinId(UuidUtil.getUUID());
            subChain.setChainId(uuid);
            subChain.setState(true);
            subChain.setBaseChain(true);
            subChain.setCoinName(mainChain.getChainCurrency());
            subChain.setCreateTime(dateTime);
            subChain.setUpdateTime(dateTime);
            subChain.setAppId(mainChainVO.getAppId());
            int insert1 = subChainMapper.insert(subChain);
            if(insert1 < 0){
                throw new RuntimeException("主币创建异常");
            }
        }
        return insert;
    }

    public int  updateMainChain(MainChainVO mainChainVO) {
        MainChain mainChain = new MainChain();
        mainChain.setId(mainChainVO.getId());
        mainChain.setAppId(mainChainVO.getAppId());
        mainChain.setChainId(mainChainVO.getChainId());
        if(!StringUtils.isEmpty(mainChainVO.getChainCurrency()))
            mainChain.setChainCurrency(mainChainVO.getChainCurrency());
        if(!StringUtils.isEmpty(mainChainVO.getChainCurrencyImg()))
            mainChain.setChainCurrencyImg(mainChainVO.getChainCurrencyImg());
        if(!StringUtils.isEmpty(mainChainVO.getChainDesc()))
            mainChain.setChainDesc(mainChainVO.getChainDesc());
        if(!StringUtils.isEmpty(mainChainVO.getChainInterface()))
            mainChain.setChainInterface(mainChainVO.getChainInterface());
        if(!StringUtils.isEmpty(mainChainVO.getChainName()))
            mainChain.setChainName(mainChainVO.getChainName());
        if(!StringUtils.isEmpty(mainChainVO.getHandleName()))
            mainChain.setHandleName(mainChainVO.getHandleName());
        if(mainChainVO.getActiveFlag() != null){
            mainChain.setActiveFlag(mainChainVO.getActiveFlag());
        }
        if(mainChainVO.getState() != null){
            mainChain.setState(mainChainVO.getState());
        }
        mainChain.setUpdateTime(DateUtils.getDateTime());
        int update = mainChainMapper.updateByPrimaryKeySelective(mainChain);
        return update;
    }

    public List<HashMap<String,Object>> getmainChainByAppId(MainChainVO mainChainVO) {
        List<HashMap<String,Object>> list =  mainChainMapper.getmainChainByAppId(mainChainVO);
        return list;
    }
    
    public List<HashMap<String,Object>> gethandleNames() {
        List<HashMap<String,Object>> list =  mainChainMapper.gethandleNames();
        return list;
    }

    public MainChain checkChainName(String chainName) {
        MainChain mainChain = mainChainMapper.checkChainName(chainName);
        return mainChain;
    }
    @Transactional
    public int updateMainChainState(MainChainVO mainChainVO) {
        Integer update =null;
        if(!mainChainVO.getState()){
            update = subChainMapper.updateByMainChainId(mainChainVO.getChainId(),0);
        }
        MainChain mainChain = new MainChain();
        mainChain.setId(mainChainVO.getId());
        if(mainChainVO.getState() != null) {
            mainChain.setState(mainChainVO.getState());
            update = mainChainMapper.updateByPrimaryKeySelective(mainChain);
        }
        return update;


    }

    /**
     * 查询一个mainChain
     * @param mainChain
     * @return
     */
    public MainChain getOneMainChain(MainChain mainChain){
        return mainChainMapper.selectOne(mainChain);
    }
    
    public List getMainChainInfo(boolean state) {
    	List list=mainChainMapper.getMainChainInfo(state);
    	return list;
    }

    public List<MainChain> selectAllMainChain(MainChainVO  mainChainVO) {
        List<MainChain> mainChains = mainChainMapper.selectAllMainChain(mainChainVO);
        return mainChains;
    }

    public MainChain selectById(String id) {
        MainChain mainChain = mainChainMapper.selectByPrimaryKey(id);
        return mainChain;
    }


	public Boolean checksMainChain(MainChainVO mainChainVO) {
	  int i =	mainChainMapper.checksMainChain(mainChainVO);
	  if(i>0) {
		  return false;
	  }
		return true;
	}
}
