package com.jinglitong.springshop.controller;

import com.jinglitong.springshop.IConstants;
import com.jinglitong.springshop.ShopRespose;
import com.jinglitong.springshop.service.BusinessService;
import com.jinglitong.springshop.vo.BusiStoreVO;
import com.jinglitong.springshop.vo.BusiVO;
import com.jinglitong.springshop.vo.request.PageVo;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * @ClassName BusinessController
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/4/8 15:17
 * @Version 1.0
 **/
@RestController
@RequestMapping("/business")
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public ShopRespose registerBusiness(@RequestBody @Valid BusiVO vo, BindingResult result){
        if (result.hasErrors()) {
            return ShopRespose.errorMessage(result);
        }
        return businessService.registerBusiness(vo);
    }

    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public ShopRespose list(@RequestBody  PageVo vo){
        return businessService.listBusiness(vo);
    }

    @RequestMapping(value = "/getBusiness",method =RequestMethod.POST )
    public ShopRespose getBusiness(@RequestBody Map map){

        if(!map.containsKey("bZid")){
            return new ShopRespose(IConstants.FAILED,IConstants.FAILED_MSG,"bZid is null");
        }
        return businessService.getBusinessById((String) map.get("bZid"));
    }

    @RequestMapping(value = "/updateState",method = RequestMethod.POST)
    public ShopRespose updateState(@RequestBody Map map){
        if(!map.containsKey("state")||!map.containsKey("bZid")){
            return new ShopRespose(IConstants.FAILED,IConstants.FAILED_MSG,"state or bZid is null");
        }
        return businessService.updateState(Integer.parseInt((String)map.get("state")),(String) map.get("bZid"));
    }
    @RequestMapping(value = "/listSelectiveBusiness",method =RequestMethod.POST )
    public ShopRespose listSelectiveBusiness(@RequestBody BusiStoreVO bsvo){
        return businessService.listSelectiveBusiness(bsvo);
    }
}
