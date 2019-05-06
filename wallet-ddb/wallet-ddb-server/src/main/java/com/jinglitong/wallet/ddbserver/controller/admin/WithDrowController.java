package com.jinglitong.wallet.ddbserver.controller.admin;

import com.jinglitong.wallet.api.model.Admin;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralWithdrawRecord;
import com.jinglitong.wallet.ddbapi.model.view.IntegralWithdrawApplyVo;
import com.jinglitong.wallet.ddbapi.model.view.IntegralwithdrawCustomerApplyVo;
import com.jinglitong.wallet.ddbserver.controller.customer.CusBaseController;
import com.jinglitong.wallet.ddbserver.service.DdbWithDrowStatusService;
import com.jinglitong.wallet.ddbserver.service.WithdrawApplyInfoService;
import com.jinglitong.wallet.ddbserver.util.JsonUtil;
import com.jinglitong.wallet.ddbserver.util.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.jinglitong.wallet.ddbserver.common.ErrorEnum.ERROR_1;
import static com.jinglitong.wallet.ddbserver.common.ErrorEnum.ERROR_21011;
import static com.jinglitong.wallet.ddbserver.common.ErrorEnum.ERROR_411201;

@RestController
@RequestMapping("/console/withdrow")
@Slf4j
public class WithDrowController extends CusBaseController{

    @Autowired
    private DdbWithDrowStatusService ddbWithDrowStatusService;
    @Autowired
    private WithdrawApplyInfoService withdrawApplyInfoService;
    
    
    /**
     * 查询提现申请列表接口
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getWithdrawApplyInfo.json", method = RequestMethod.POST)
    public Map getWithdrawApplyInfo(@RequestBody IntegralWithdrawApplyVo vo ) {



    	HashMap<String, Object> map = withdrawApplyInfoService.getwithdrawApplyInfo(vo);
        return JsonUtil.toJsonSuccess("查询成功", map);


    }
    
    /**
     * 查看用户申请信息
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getWithdrawDetailInfo.json", method = RequestMethod.POST)
    public Map getWithdrawDetailInfo(@RequestBody IntegralWithdrawApplyVo integralWithdrawApplyVo ) {



    	IntegralwithdrawCustomerApplyVo vo = withdrawApplyInfoService.getwithdrawDetailInfo(integralWithdrawApplyVo.getZid());
        return JsonUtil.toJsonSuccess("查询成功", vo);


    }
    
    /**
     * verify pass or not pass
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/audit.json", method = RequestMethod.POST)
    public Map drawStatus(@RequestBody DdbIntegralWithdrawRecord withdrawRecord ) {

        //verify the login
        Admin admin =(Admin) SessionUtil.getUserInfo();
        if(admin == null){
            JsonUtil.toJsonError(ERROR_21011,"管理员未登陆");
        }
        // if the audit is approved
        //pass
        if(withdrawRecord.getApplyStatus() == 1){
           Map<String,String> result = ddbWithDrowStatusService.doPass(withdrawRecord,admin);
           if("success".equals(result.get("status"))){
               return JsonUtil.toJsonSuccess("success");
           }else {
               JsonUtil.toJsonError(ERROR_1,"sql异常");
           }
        }else if( withdrawRecord.getApplyStatus() == 2 ){
            //not pass
            Map<String,String> result = ddbWithDrowStatusService.doNotPass(withdrawRecord,admin);
            if("success".equals(result.get("status"))){
                return JsonUtil.toJsonSuccess("success");
            }else {
                JsonUtil.toJsonError(ERROR_1,"sql异常");
            }
        }else {
            return JsonUtil.toJsonError(ERROR_411201,"参数有误");
        }
        return JsonUtil.toJsonSuccess("审核成功");

    }


    /**
     * withdrow success or fail
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/apply.json", method = RequestMethod.POST)
    public Map getcities(@RequestBody DdbIntegralWithdrawRecord withdrawRecord ) {

        //verify the login
        Admin admin =(Admin) SessionUtil.getUserInfo();
        if(admin == null){
            JsonUtil.toJsonError(ERROR_21011,"管理员未登陆");
        }
        // if the withdrow is approved
        if(withdrawRecord.getDrawStatus()  ==1){
            //success
            Map<String,String> result =   ddbWithDrowStatusService.doSuccess(withdrawRecord,admin);
            if("success".equals(result.get("status"))){
                return JsonUtil.toJsonSuccess("success");
            }else {
                JsonUtil.toJsonError(ERROR_1,"sql异常");
            }
        }else if(withdrawRecord.getDrawStatus()  ==2){
            //fail
            Map<String,String> result =   ddbWithDrowStatusService.doFail(withdrawRecord,admin);
            if("success".equals(result.get("status"))){
                return JsonUtil.toJsonSuccess("success");
            }else {
                JsonUtil.toJsonError(ERROR_1,"sql异常");
            }
        }else {
            return JsonUtil.toJsonError(ERROR_411201,"参数有误");
        }

        return JsonUtil.toJsonSuccess("提现成功");

    }

}
