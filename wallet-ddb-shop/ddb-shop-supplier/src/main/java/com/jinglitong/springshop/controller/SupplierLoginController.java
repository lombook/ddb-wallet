package com.jinglitong.springshop.controller;

import com.jinglitong.springshop.ErrorEnum;
import com.jinglitong.springshop.IConstants;
import com.jinglitong.springshop.ShopRespose;
import com.jinglitong.springshop.service.SupplierLoginService;
import com.jinglitong.springshop.vo.request.LoginParamVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @ClassName SupplierLoginController
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/4/8 11:31
 * @Version 1.0
 **/
@RestController
@RequestMapping()
public class SupplierLoginController {
    @Autowired
    private SupplierLoginService supplierLoginService;

    @ApiOperation(value = "商家登录")
    @RequestMapping( value= "/supplier/login", method = RequestMethod.POST)
    public ShopRespose login(@ApiParam(name = "clientId", value = "客户端Id", required = true)@RequestHeader(name = "clientId", required = true,  defaultValue = "")String clientId,
                             @RequestBody @Valid LoginParamVo vo) {
        if (vo == null) {
            return new ShopRespose(IConstants.FAILED, ErrorEnum.ERROR_209.getMsg());
        }
        return supplierLoginService.login(vo.getUserName(),vo.getPassword(),clientId);
    }
}
