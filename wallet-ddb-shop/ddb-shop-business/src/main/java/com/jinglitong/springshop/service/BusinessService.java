package com.jinglitong.springshop.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.springshop.ErrorEnum;
import com.jinglitong.springshop.IConstants;
import com.jinglitong.springshop.ShopRespose;
import com.jinglitong.springshop.entity.Business;
import com.jinglitong.springshop.mapper.BusinessMapper;
import com.jinglitong.springshop.md5.MD5ShiroUtils;
import com.jinglitong.springshop.utils.AesEncryptUtil;
import com.jinglitong.springshop.utils.UuidUtil;
import com.jinglitong.springshop.vo.BusiStoreVO;
import com.jinglitong.springshop.vo.BusiVO;
import com.jinglitong.springshop.vo.SupplierVO;
import com.jinglitong.springshop.vo.request.PageVo;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

/**
 * @ClassName BusinessService
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/4/8 14:45
 * @Version 1.0
 **/
@Service
public class BusinessService extends BaseService<Business> {

    @Resource
    private BusinessMapper businessMapper;

    public ShopRespose registerBusiness(BusiVO vo){
        if(!vo.getPassword().equals(vo.getPassword2())){
            return new ShopRespose(ErrorEnum.ERROR_5212.getCode(),ErrorEnum.ERROR_5212.getMsg());
        }
        String salt = UuidUtil.getUUID();
        String zid = UuidUtil.getUUID();
        String md5Password = null;
        Business queryParam = new Business();
        queryParam.setUsername(vo.getUsername());
        Business business = businessMapper.selectOne(queryParam);
        if(business!=null){
            return new ShopRespose(ErrorEnum.ERROR_5206.getCode(),ErrorEnum.ERROR_5206.getMsg());
        }
        try {
            //解密密码方式是：先做URLDecoder.decode 再做AesEncryptUtil.aesDecrypt  AES解密；
            //当然在登录的时候需要先做URLEncoder.encode()在做AesEncryptUtil.aesEncrypt()  AES的加密
            md5Password = MD5ShiroUtils.createBusinessPwd(AesEncryptUtil.aesDecrypt(URLDecoder.decode(vo.getPassword(),"utf-8")),salt);
        } catch (Exception e) {
            return new ShopRespose(ErrorEnum.ERROR_5301.getCode(),ErrorEnum.ERROR_5301.getMsg());
        }
        business = new Business();
        BeanUtils.copyProperties(vo,business);
        business.setPassword(md5Password);
        business.setSalt(salt);
        business.setZid(zid);
        business.setBalance(BigDecimal.ZERO);
        business.setFrozenfund(BigDecimal.ZERO);
        business.setCreatedTime(new Date(System.currentTimeMillis()));
        business.setMobile("1");
        businessMapper.insert(business);
        return new ShopRespose(IConstants.SUCCESS,IConstants.SUCCESS_MSG);
    }

    public ShopRespose listBusiness(PageVo vo){
    	if(vo != null && vo.getPageNum()!= null && vo.getPageSize() != null){
			PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
		}
        List<SupplierVO> vos = businessMapper.listBusinessAndSupplier(null);
        return new ShopRespose(IConstants.SUCCESS,IConstants.SUCCESS_MSG,new PageInfo<SupplierVO>(vos));
    }

    public ShopRespose getBusinessById(String bZid) {
        return new ShopRespose(IConstants.SUCCESS,IConstants.SUCCESS_MSG,businessMapper.listBusinessAndSupplier(bZid));
    }

    public ShopRespose updateState(Integer state, String bZid) {
        Example example = new Example(Business.class);
        example.createCriteria().andEqualTo("zid",bZid);
        Business updateParam = new Business();
        updateParam.setState(state);
        return new ShopRespose(IConstants.SUCCESS,IConstants.SUCCESS_MSG,businessMapper.updateByExampleSelective(updateParam,example));
    }

    public ShopRespose listSelectiveBusiness(BusiStoreVO bsvo) {
        if(bsvo != null && bsvo.getPageNum()!= null && bsvo.getPageSize() != null){
            PageHelper.startPage(bsvo.getPageNum(), bsvo.getPageSize());
        }
        List<SupplierVO> vos = businessMapper.listSelectiveBusiness(bsvo);
        return new ShopRespose(IConstants.SUCCESS,IConstants.SUCCESS_MSG,new PageInfo<SupplierVO>(vos));
    }
}
