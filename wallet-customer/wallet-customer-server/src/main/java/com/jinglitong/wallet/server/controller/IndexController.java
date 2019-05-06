package com.jinglitong.wallet.server.controller;

import com.jinglitong.wallet.server.common.ErrorEnum;
import com.jinglitong.wallet.server.common.storage.AliCloudMailService;
import com.jinglitong.wallet.server.service.AdminService;
import com.jinglitong.wallet.server.service.FrozenReleaseService;
import com.jinglitong.wallet.api.model.Admin;
import com.jinglitong.wallet.api.model.Menu;
import com.jinglitong.wallet.api.model.Role;
import com.jinglitong.wallet.server.service.MenuService;
import com.jinglitong.wallet.server.service.RoleService;
import com.jinglitong.wallet.server.util.JsonUtil;
import com.jinglitong.wallet.server.util.SessionUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class IndexController {
    @Autowired
    RoleService roleService;
    @Autowired
    AdminService adminService;
    @Autowired
    MenuService menuService;
    @Autowired
    FrozenReleaseService frozenReleaseService;

    @Autowired
    AliCloudMailService aliCloudMailService;
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @RequestMapping(value = "/")
    public void index(HttpServletResponse response) {
        try {
           // aliCloudMailService.sendMail("fanyy@dabland.cn","测试","123456");
            response.sendRedirect("/static/index.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @ResponseBody
    @RequestMapping(value = "/checkLgoin.json", method = RequestMethod.GET)
    public Map checkLgoin() {
        Admin admin = (Admin) SessionUtil.getUserInfo();
        if(admin == null){
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31421);
        }else{
            if(admin.getIsSystem()==true){
                List<Menu> menus = menuService.getAllMenus();
                return JsonUtil.toJsonSuccess("用户已登录", menus);
            }
            List<Role> roles = adminService.getRoles(admin.getAdminId());
            List<Menu> menus = roleService.getMenus(roles);
            return JsonUtil.toJsonSuccess("用户已登录", menus);
        }
    }
    @ResponseBody
    @RequestMapping(value = "/data.json", method = RequestMethod.POST)
    public Map data(HttpServletRequest request) {
        String type = request.getParameter("data");
        HashMap<String, Object> map = new HashMap<>();
        map.put("code",0);
        return map;

    }


}
