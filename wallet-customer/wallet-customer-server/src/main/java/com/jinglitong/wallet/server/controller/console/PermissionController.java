package com.jinglitong.wallet.server.controller.console;

import com.jinglitong.wallet.server.service.MenuService;
import com.jinglitong.wallet.server.util.JsonUtil;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "/console")
public class PermissionController extends BaseController{

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    MenuService menuService;
    /**
     * 获取权限列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/permissionList.json", method = RequestMethod.POST)
    public Map pandec(String customerId) {
        HashMap<String, Object> map = menuService.getMenuList();
        return JsonUtil.toJsonSuccess("获取权限列表", map);
    }

}