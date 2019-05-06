package com.jinglitong.wallet.server.controller.console;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.jinglitong.wallet.server.util.MD5Utils;
import com.jinglitong.wallet.server.util.UuidUtil;

@RestController
@RequestMapping("/console")
public class TestController {

    @ResponseBody
    @RequestMapping("/test.json")
    public String  test(@RequestBody String resultStr) throws Exception {
    	
	    //json->bean
	    //Tb student = JSON.parseObject(resultStr, new TypeReference<Tb>() {});
	    //加密
	    Map<String,Object> m = MD5Utils.json2Map(resultStr);
	    String oldKey = (String) m.get("sign");
	    m.remove("sign");
	    String key=MD5Utils.makeSign(JSON.toJSONString(m));
	    System.out.println("加密后"+key);
	    
	    Map result = new HashMap<>();
	    if(key.equals(oldKey)) {
	    	result.put("message", "success");
	    }else {
	    	result.put("message", "fail");
	    }
	    //返回
	    result.put("code", 1);
	    result.put("flowId", UuidUtil.getUUID());
	    
	    return JSON.toJSONString(result);
    }
}
