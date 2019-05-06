package com.jinglitong.springshop.mapper;

import com.jinglitong.springshop.entity.Business;
import com.jinglitong.springshop.utils.MyMapper;
import com.jinglitong.springshop.vo.BusiStoreVO;
import com.jinglitong.springshop.vo.SupplierVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BusinessMapper extends MyMapper<Business> {
    List<SupplierVO> listBusinessAndSupplier(@Param("bZid") String bZid);

    List<SupplierVO> listSelectiveBusiness(BusiStoreVO bsvo);
}