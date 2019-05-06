package com.jinglitong.wallet.server.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.wallet.server.mapper.SellerCoinInfoMapper;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import com.jinglitong.wallet.api.model.SellerCoinInfo;
import com.jinglitong.wallet.api.model.view.SellerCoinInfoVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SellerCoinInfoService {

    @Resource
    private SellerCoinInfoMapper coinInfoMapper;

    /**
     * 商家币信息
     * @param coinInfo
     * @return
     */
    public Map<String,Object> querySellerCoinInfoByPage(SellerCoinInfoVo coinInfo){

        if (coinInfo.getPage() != null && coinInfo.getRows() != null) {
            PageHelper.startPage(coinInfo.getPage(), coinInfo.getRows());
        }
        List<SellerCoinInfo> coinInfos = coinInfoMapper.selectBySellerId(coinInfo);
        PageInfo pageinfo = new PageInfo(coinInfos);
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageCount",pageinfo.getTotal());
        map.put("coinInfos",coinInfos);
        return  map;
    }

    /**
     * 新增商家币信息
     * @param sellerCoinInfo
     * @return
     */
    public boolean addSellerCoinInfo(SellerCoinInfo sellerCoinInfo){
        return coinInfoMapper.insertSelective(sellerCoinInfo)>0;
    }

    /**
     * 修改商家币信息
     * @param sellerCoinInfo
     * @return
     */
    public boolean updateSellerCoinInfo(SellerCoinInfo sellerCoinInfo){
    	Weekend<SellerCoinInfo> weekend = Weekend.of(SellerCoinInfo.class);
    	WeekendCriteria<SellerCoinInfo,Object>criteria =weekend.weekendCriteria();
    	criteria.andEqualTo("zId", sellerCoinInfo.getzId());
        return coinInfoMapper.updateByExampleSelective(sellerCoinInfo,weekend)>0;
    }

    /**
     * 删除商家币信息
     * @param sellerCoinInfo
     * @return
     */
    public boolean deleteSellerCoinInfo(SellerCoinInfo sellerCoinInfo){
        return coinInfoMapper.delete(sellerCoinInfo)>0;
    }

    /**
     * 查询一个商家币信息
     * @param sellerCoinInfo
     * @return
     */
    public SellerCoinInfo queryOneSellerCoinInfo(SellerCoinInfo sellerCoinInfo){
        return coinInfoMapper.selectOne(sellerCoinInfo);
    }


    /**
     * 查询商家币信息
     * @param sellerCoinInfo
     * @return
     */
    public List<SellerCoinInfo> querySellerCoinInfos(SellerCoinInfo sellerCoinInfo){
        return coinInfoMapper.select(sellerCoinInfo);
    }



}
