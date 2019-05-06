package com.jinglitong.wallet.server.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.wallet.server.mapper.SellerMapper;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import com.jinglitong.wallet.api.model.Seller;
import com.jinglitong.wallet.api.model.view.SellerVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SellerService {

    @Resource
    private SellerMapper sellerMapper;

    /**
     * 商家信息
     * @param sellerVo
     * @return
     */
    public Map<String,Object> querySellerByPage(SellerVo sellerVo){
        if (sellerVo.getPage() != null && sellerVo.getRows() != null) {
            PageHelper.startPage(sellerVo.getPage(), sellerVo.getRows(),"create_time DESC");
        }
        List<Seller> sellers = sellerMapper.selectAllByApp(sellerVo);
        PageInfo pageinfo = new PageInfo(sellers);
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageCount",pageinfo.getTotal());
        map.put("sellers",sellers);
        return  map;
    }

    /**
     * 新增商家信息
     * @param seller
     * @return
     */
    public boolean addSeller(Seller seller){
        return sellerMapper.insertSelective(seller)>0;
    }

    /**
     * 修改商家信息
     * @param seller
     * @return
     */
    public boolean updateSeller(Seller seller){
    	Weekend<Seller> weekend = Weekend.of(Seller.class);
    	WeekendCriteria<Seller, Object> criteria =weekend.weekendCriteria();
    	criteria.andEqualTo(Seller::getSellerId, seller.getSellerId());
        return sellerMapper.updateByExampleSelective(seller, weekend)>0;
    }

    /**
     * 删除商家信息
     * @param seller
     * @return
     */
    public boolean deleteSeller(Seller seller){
        return sellerMapper.delete(seller)>0;
    }

    /**
     * 查询商家信息
     * @param seller
     * @return
     */
    public Seller queryOneSeller(Seller seller){
        return sellerMapper.selectOne(seller);
    }


    public boolean updateSellerState(Seller seller) {//要把物理id改为逻辑id
    	Weekend<Seller> weekend = Weekend.of(Seller.class);
    	WeekendCriteria<Seller, Object> criteria =weekend.weekendCriteria();
    	criteria.andEqualTo(Seller::getSellerId, seller.getSellerId());
        int i = sellerMapper.updateByExampleSelective(seller, weekend);
        if(i>0)
            return true;
        return false;

    }
}
