package com.jinglitong.wallet.server.controller.console;

import com.jinglitong.wallet.server.common.ErrorEnum;
import com.jinglitong.wallet.api.model.Role;
import com.jinglitong.wallet.api.model.view.PageVO;
import com.jinglitong.wallet.api.model.view.RoleVO;
import com.jinglitong.wallet.server.service.RoleService;
import com.jinglitong.wallet.server.util.JsonUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "/console")
public class RoleController extends BaseController{

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RoleService roleService;
    /**
     * 获取角色列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/roleList.json", method = RequestMethod.POST)
    public Map roleList(PageVO page) {
        HashMap<String, Object> map = roleService.getRoleList(page);
        return JsonUtil.toJsonSuccess("获取角色列表", map);
    }
    /**
     * 创建角色
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/crestedrole.json", method = RequestMethod.POST)
    public Map crestedrole(@RequestBody RoleVO roleVO) {
        int insert= roleService.crestedrole(roleVO);
        if(insert > 0){
            return JsonUtil.toJsonSuccess("创建角色成功", insert);
        }else{
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31416);
        }
    }
    /**
     * 获取角色
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getRole.json", method = RequestMethod.POST)
    public Map getRole(@RequestBody RoleVO roleVO) {
        if(StringUtils.isEmpty(roleVO.getRoleId()))
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31417);
        HashMap<String, Object> map = roleService.getRoleById(roleVO.getRoleId());
            return JsonUtil.toJsonSuccess("获取角色成功", map);
    }
    /**
     * 获取角色
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAllRole.json", method = RequestMethod.POST)
    public Map getAllRole() {
        List<Role> roles= roleService.getAllRole();
            return JsonUtil.toJsonSuccess("获取角色成功", roles);
    }
    /**
     * 修改角色
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateRole.json", method = RequestMethod.POST)
    public Map updateRole(@RequestBody RoleVO roleVO) {
        int update= roleService.updateRole(roleVO);
        if(update > 0){
            return JsonUtil.toJsonSuccess("修改角色成功", update);
        }else{
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31418);
        }
    }

    /**
     * 启用或冻结role
     * @param roleId
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/updateRoleEnable.json", method = RequestMethod.POST)
    public Map memberState(String roleId,Boolean enable) {
        if(roleId == null || enable == null){
            return JsonUtil.toJsonError(ErrorEnum.ERROR_21101);
        }
        int i = roleService.updateRoleEnable(roleId, enable);
        if(i > 0){
            return JsonUtil.toJsonSuccess("修改状态成功", i);
        }else{
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31419);
        }
    }
}