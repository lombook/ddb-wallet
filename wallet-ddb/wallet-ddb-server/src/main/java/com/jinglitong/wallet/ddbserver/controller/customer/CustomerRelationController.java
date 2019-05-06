package com.jinglitong.wallet.ddbserver.controller.customer;

import static com.jinglitong.wallet.ddbserver.common.ErrorEnum.ERROR_41115;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.ddbapi.feign.NoticeKJFeignApi;
import com.jinglitong.wallet.ddbapi.model.view.FriendsChangeNoticeVo;
import com.jinglitong.wallet.ddbserver.service.CustomerRelationService;
import com.jinglitong.wallet.ddbserver.service.CustomerService;
import com.jinglitong.wallet.ddbserver.util.JsonUtil;
import com.jinglitong.wallet.ddbserver.util.SessionUtil;

import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
@RequestMapping("/friendRelation")
public class CustomerRelationController extends CusBaseController {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

	
    @Autowired
    private CustomerRelationService customerRelationService;
    
    
    
    /**
     * 游戏向钱包后台同步信息：好友增加减少通知
     * 还缺少一些业务？？？
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/customerRelationNotice.json")
    public Map customerRelationNotice(@RequestBody FriendsChangeNoticeVo vo) {
        
        Integer num = customerRelationService.insertcustomerRelation(vo);
        
        if(num > 0) {
        	return JsonUtil.toJsonSuccess("成功",null);
        }else {
        	return JsonUtil.toJsonError(ERROR_41115);
        }

    }
}
