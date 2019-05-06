package com.jinglitong.wallet.server.controller.console;

import com.jinglitong.wallet.server.common.ErrorEnum;
import com.jinglitong.wallet.server.common.LoginEnum;
import com.jinglitong.wallet.server.common.shiro.CustomerAuthenticationToken;
import com.jinglitong.wallet.server.service.AdminService;
import com.jinglitong.wallet.server.util.JsonUtil;
import com.jinglitong.wallet.server.util.SessionUtil;
import com.jinglitong.wallet.api.model.Admin;
import com.jinglitong.wallet.api.model.view.AdminRegVO;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.jinglitong.wallet.server.common.ErrorEnum.ERROR_37006;


@Controller
@RequestMapping(value = "/console")
public class AdminController extends BaseController{



    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    AdminService adminService;

    @ResponseBody
    @RequestMapping(value = "/login.json", method = RequestMethod.POST)
    public Map loginPost(@RequestBody @Valid AdminRegVO admin, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return JsonUtil.toJsonError(ERROR_37006);
        }
        String username = admin.getUsername();
        CustomerAuthenticationToken token = new CustomerAuthenticationToken(admin.getUsername(), admin.getPasswd(),admin.getAppId(), false);
        token.setLoginType(LoginEnum.ADMIN.toString());
        //获取当前的Subject
        Subject currentUser = SecurityUtils.getSubject();
        Admin byUsername = adminService.findByUsername(admin.getUsername(),admin.getAppId());
        if(byUsername != null){
            if(byUsername.getState() == null || byUsername.getState() == false){
                return JsonUtil.toJsonError(ErrorEnum.ERROR_21013);
            }
        }
        try {
            logger.info("对用户[" + username + "]进行登录验证..验证开始");
            currentUser.login(token);
            logger.info("对用户[" + username + "]进行登录验证..验证通过");
        } catch (UnknownAccountException uae) {
            logger.info("对用户[" + username + "]进行登录验证..验证未通过,未知账户");
            return JsonUtil.toJsonError(ErrorEnum.ERROR_21011);
        } catch (IncorrectCredentialsException ice) {
            logger.info("对用户[" + username + "]进行登录验证..验证未通过,错误的凭证");
            return JsonUtil.toJsonError(ErrorEnum.ERROR_21015);
        } catch (LockedAccountException lae) {
            logger.info("对用户[" + username + "]进行登录验证..验证未通过,账户已锁定");
            return JsonUtil.toJsonError(ErrorEnum.ERROR_21013);
        } catch (ExcessiveAttemptsException eae) {
            logger.info("对用户[" + username + "]进行登录验证..验证未通过,错误次数过多");
            return JsonUtil.toJsonError(ErrorEnum.ERROR_21014);
        } catch (AuthenticationException ae) {
            logger.info("对用户[" + username + "]进行登录验证..验证未通过,堆栈轨迹如下");
            return JsonUtil.toJsonError(ErrorEnum.ERROR_21012);
        }

        PrincipalCollection subject = SessionUtil.getSubject().getPrincipals();
        //验证是否登录成功
        if (currentUser.isAuthenticated()) {
            Session session = SecurityUtils.getSubject().getSession();
            session.setAttribute("loginType", LoginEnum.ADMIN.toString());
            logger.info("用户[" + username + "]登录认证通过");
            Admin user = adminService.SelectByUsername(admin.getUsername());
            user.setPassword("helloworld,jinglitong!");
            user.setSalt("helloworld,jinglitong!");
            return JsonUtil.toJsonSuccess("成功", user);
        } else {
            token.clear();
            return JsonUtil.toJsonError(ErrorEnum.ERROR_21016);
        }
    }

    @RequiresPermissions("system:admin")
    @ResponseBody
    @RequestMapping(value = "/sys.json")
    public Map sys() {
            return JsonUtil.toJsonSuccess("ok",null);
    }



    /**
     * 退出
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/logout.json")
    public Map logout() {
        try {
            if(SecurityUtils.getSubject() != null)
                SecurityUtils.getSubject().logout();
        }catch (Exception e){
            logger.error("登出错误",e);
            return JsonUtil.toJsonError(ErrorEnum.ERROR_21016);
        }
        return JsonUtil.toJsonSuccess("登出成功",null);
    }


    @ResponseBody
    @RequestMapping(value = "/accounterr")
    public Map accounterr() {
        return JsonUtil.toJsonError(ErrorEnum.ERROR_21099);
    }


}
