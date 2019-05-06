package com.jinglitong.wallet.server.controller.customer;

import com.jinglitong.wallet.server.common.ErrorEnum;
import com.jinglitong.wallet.api.model.logic.FirstinStall;
import com.jinglitong.wallet.server.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/customer/firstLogin/api")
public class FirstLoginAPIController extends CusBaseController{
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${isEnable}")
    private int isEnable;

    @Value("${web_ip}")
    private String webIp;

    @Autowired
    private RestTemplate restTemplate;


    /**
     * 第一次打开app时
     * @param firstinStall
     */
    @ResponseBody
    @RequestMapping(value = "/firstLogin.json",method = RequestMethod.POST)
    public Map firstLogin(@RequestBody FirstinStall firstinStall) {
        if (isEnable == 1) {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
                ResponseEntity<String> postForEntity = restTemplate.postForEntity(webIp + "/firstinstall/open",firstinStall,String.class);//传递实体
                String result = postForEntity.getBody();
                if (result.equals("success")) {
                    return JsonUtil.toJsonSuccess(result, 0);
                } else {
                    return JsonUtil.toJsonError(ErrorEnum.ERROR_36001.getCode(), ErrorEnum.ERROR_36001.getMsg());
                }
            } catch (Exception e) {
                e.printStackTrace();
                return JsonUtil.toJsonError(ErrorEnum.ERROR_36001.getCode(), ErrorEnum.ERROR_36001.getMsg());
            }
        }
         return JsonUtil.toJsonSuccess("success",0);
    }


}
