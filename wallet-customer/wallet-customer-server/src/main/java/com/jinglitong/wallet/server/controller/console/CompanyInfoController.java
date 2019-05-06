package com.jinglitong.wallet.server.controller.console;


import com.jinglitong.wallet.api.model.Admin;
import com.jinglitong.wallet.api.model.CompanyInfo;
import com.jinglitong.wallet.api.model.view.CompanyInfoVo;
import com.jinglitong.wallet.server.common.ErrorEnum;
import com.jinglitong.wallet.server.service.CompanyInfoService;
import com.jinglitong.wallet.server.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping(value = "console/companyInfo")

@Slf4j
public class CompanyInfoController extends BaseController{

    @Resource
    CompanyInfoService companyInfoService;

    /**
     * 公司列表
     * @param companyInfoVo
     * @return
     */
    @RequestMapping("list")
    public Map queryCompanyInfoByPage(@RequestBody CompanyInfoVo companyInfoVo, @RequestHeader HttpHeaders httpHeaders){
        log.info("appId: "+httpHeaders.get("appId"));
        return JsonUtil.toJsonSuccess("公司列表", companyInfoService.queryCompanyInfoByPage(companyInfoVo));
    }


    /**
     * 新增公司信息
     * @param companyInfo
     * @return
     */
    @RequestMapping("add")
    public Map addCompanyInfo(@RequestBody CompanyInfo companyInfo){
        Admin admin = (Admin) SecurityUtils.getSubject().getPrincipal();
        companyInfo.setCreatedUser(admin.getUsername());
        companyInfo.setUpdatedUser(admin.getUsername());
        int m = companyInfoService.insertCompanyInfo(companyInfo);
        if(m > 0){
            return JsonUtil.toJsonSuccess("新增成功", m);
        }else{
            return JsonUtil.toJsonError(ErrorEnum.ERROR_36003);
        }

    }


    /**
     * 更新公司信息
     * @param companyInfo
     * @return
     */
    @RequestMapping("update")
    public Map updateCompanyInfo(@RequestBody  CompanyInfo companyInfo){
        Admin admin = (Admin) SecurityUtils.getSubject().getPrincipal();
        companyInfo.setUpdatedUser(admin.getUsername());
        CompanyInfo companyInfo1 = companyInfoService.SelectCompanyInfoById(companyInfo.getId());
        if(companyInfo1 != null){
            if(!companyInfo.getCompanyName().equals(companyInfo1.getCompanyName())){
                Boolean m = companyInfoService.checkCompanyName(companyInfo);
                if(!m){
                    return JsonUtil.toJsonSuccess("公司名不能重复", false);
                }
            }
        }
        int m = companyInfoService.updateCompanyInfo(companyInfo);
        if(m > 0){
            return JsonUtil.toJsonSuccess("更新成功", m);
        }else{
            return JsonUtil.toJsonError(ErrorEnum.ERROR_36003);
        }

    }

    /**
     * 获得公司信息
     * @param companyInfo
     * @return
     */
    @RequestMapping("getCompanyInfo")
    public Map getCompanyInfo(@RequestBody CompanyInfo companyInfo){
        return JsonUtil.toJsonSuccess("获得公司信息成功", companyInfoService.getOneCompanyInfo(companyInfo));
    }


    /**
     * 删除公司信息
     * @param companyInfo
     * @return
     */
    /*@RequestMapping("deleteCompanyInfo")
        public Map deleteCompanyInfo(@RequestBody  CompanyInfo companyInfo){
            int m = companyInfoService.deleteCompanyInfo(companyInfo);
            if(m > 0){
                return JsonUtil.toJsonSuccess("删除公司信息成功", m);
            }else{
                return JsonUtil.toJsonError(ErrorEnum.ERROR_36003);
            }

    }*/

    @RequestMapping("checkCompanyName")
    public Map check(@RequestBody  CompanyInfo companyInfo){
        Boolean m = companyInfoService.checkCompanyName(companyInfo);
        if(m){
            return JsonUtil.toJsonSuccess("公司名可用", true);
        }else{
            return JsonUtil.toJsonSuccess("公司名不能重复", false);
        }

    }

    /**
     * 公司下拉框
     * @return
     */
    @RequestMapping("getList")
    public Map getCompanyList(){
        return JsonUtil.toJsonSuccess("公司列表", companyInfoService.queryCompany());

    }
}
