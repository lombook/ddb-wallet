package com.jinglitong.wallet.server.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.wallet.api.model.MainChain;
import com.jinglitong.wallet.api.model.SubChain;
import com.jinglitong.wallet.api.model.view.SubChainVO;
import com.jinglitong.wallet.server.mapper.SubChainMapper;
import com.jinglitong.wallet.server.util.DateUtils;
import com.jinglitong.wallet.server.util.UuidUtil;
import com.jinglitong.wallet.server.mapper.MainChainMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

/**
 * Created by fan on 2018/5/22.
 */
@Service
@Transactional
public class SubChainService {

    @Autowired
    SubChainMapper subChainMapper;
    @Autowired
    MainChainMapper mainChainMapper;


    public HashMap<String, Object> getSubChains(SubChainVO subChainVO) {
        if (subChainVO.getPage() != null && subChainVO.getRows() != null) {
            PageHelper.startPage(subChainVO.getPage(), subChainVO.getRows());
        }
        List<Map> SubChainList = subChainMapper.getSubChains(subChainVO);
        PageInfo pageinfo = new PageInfo(SubChainList);
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageCount", pageinfo.getTotal());
        map.put("appHelps", SubChainList);
        return map;
    }

    public int createSubChains(SubChainVO subChainVO) {
        SubChain subChain = new SubChain();
        subChain.setAppId(subChainVO.getAppId());
        subChain.setCoinId(UuidUtil.getUUID());
        if (!StringUtils.isEmpty(subChainVO.getChainId()))
            subChain.setChainId(subChainVO.getChainId());
        if (subChain.getState() != null)
            subChain.setState(subChainVO.getState());
        else
            subChain.setState(true);
        subChain.setBaseChain(false);
        if (!StringUtils.isEmpty(subChainVO.getCurrency()))
            subChain.setCurrency(subChainVO.getCurrency());
        if (!StringUtils.isEmpty(subChainVO.getCoinRemark()))
            subChain.setCoinRemark(subChainVO.getCoinRemark());
        if (!StringUtils.isEmpty(subChainVO.getTokenAddress()))
            subChain.setTokenAddress(subChainVO.getTokenAddress());
        String dateTime = DateUtils.getDateTime();
        if (!StringUtils.isEmpty(subChainVO.getCoinImg()))
            subChain.setCoinImg(subChainVO.getCoinImg());
        if (!StringUtils.isEmpty(subChainVO.getCoinName()))
            subChain.setCoinName(subChainVO.getCoinName());
        subChain.setUpdateTime(dateTime);
        subChain.setCreateTime(dateTime);
        int insert = subChainMapper.insert(subChain);
        return insert;
    }
    @Transactional
    public int updateSubChain(SubChainVO subChainVO) {
        SubChain subChain = new SubChain();
        subChain.setId(subChainVO.getId());
        subChain.setAppId(subChainVO.getAppId());
        subChain.setCoinId(subChainVO.getCoinId());
        if (!StringUtils.isEmpty(subChainVO.getChainId()))
            subChain.setChainId(subChainVO.getChainId());
        if (subChain.getState() != null)
            subChain.setState(subChainVO.getState());
        if (!StringUtils.isEmpty(subChainVO.getCurrency()))
            subChain.setCurrency(subChainVO.getCurrency());
        if (!StringUtils.isEmpty(subChainVO.getCoinRemark()))
            subChain.setCoinRemark(subChainVO.getCoinRemark());
        if (!StringUtils.isEmpty(subChainVO.getTokenAddress()))
            subChain.setTokenAddress(subChainVO.getTokenAddress());
        else
            subChain.setTokenAddress("");
        if (!StringUtils.isEmpty(subChainVO.getCoinImg()))
            subChain.setCoinImg(subChainVO.getCoinImg());
        if (!StringUtils.isEmpty(subChainVO.getCoinName()))
            subChain.setCoinName(subChainVO.getCoinName());
        if(subChainVO.getState() != null)
            subChain.setState(subChainVO.getState());
        subChain.setUpdateTime(DateUtils.getDateTime());
        int update = subChainMapper.updateByPrimaryKeySelective(subChain);
        if(!StringUtils.isEmpty(subChainVO.getCurrency()) || subChain.getBaseChain() == true){
            MainChain mainChain = new MainChain();
            mainChain.setChainId(subChain.getChainId());
            mainChain.setChainCurrency(subChain.getCurrency());
            mainChain.setUpdateTime(DateUtils.getDateTime());
            int i = mainChainMapper.updateByPrimaryKeySelective(mainChain);
            if(i < 0){
                throw  new RuntimeException("链主币种修改失败");
            }
        }
        return  update;
    }

    public int updateSubChainState(SubChainVO subChainVO) {
        SubChain subChain = new SubChain();
        subChain.setId(subChainVO.getId());
        if(subChainVO.getState() != null)
           subChain.setState(subChainVO.getState());
        int update = subChainMapper.updateByPrimaryKeySelective(subChain);
        return update;

    }

    public SubChain selectById(Integer id) {
        SubChain subChain = subChainMapper.selectByPrimaryKey(id);
        return subChain;
    }

    public Boolean checkCurrency(String currency,String appId,String chainId) {
        Weekend<SubChain> weekend = Weekend.of(SubChain.class);
        WeekendCriteria<SubChain, Object> criteria = weekend.weekendCriteria();
        criteria.andEqualTo(SubChain::getCurrency,currency);
        criteria.andEqualTo(SubChain::getAppId,appId);
        criteria.andEqualTo(SubChain::getChainId,chainId);
        List<SubChain> subChains = subChainMapper.selectByExample(weekend);
        if(subChains.size() == 0){
            return true;
        }else {
            return false;
        }
    }

    public List<SubChain> getChainCoins(SubChainVO subChainVO) {
         List<SubChain> list = subChainMapper.selectByChainId(subChainVO);
         return list;
    }

    public List<SubChain> geAllSubChain() {
        List<SubChain> list = subChainMapper.selectAll();
        return list;
    }

    public List<SubChain> selectByChianIdAndState(String chainId, int i) {
        Weekend<SubChain> weekend = Weekend.of(SubChain.class);
        WeekendCriteria<SubChain, Object> criteria = weekend.weekendCriteria();
        criteria.andEqualTo(SubChain::getChainId, chainId);
        criteria.andEqualTo(SubChain::getState, i);
        List<SubChain> list = subChainMapper.selectByExample(weekend);
        return list;
    }

    public SubChain selectByCoinId(String coinId) {
        return subChainMapper.selectByCoinId(coinId);
    }
}
