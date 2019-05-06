package com.jinglitong.springshop.service;

import com.jinglitong.springshop.ErrorEnum;
import com.jinglitong.springshop.IConstants;
import com.jinglitong.springshop.ShopRespose;
import com.jinglitong.springshop.entity.Business;
import com.jinglitong.springshop.jwt.JwtTokenUtils;
import com.jinglitong.springshop.mapper.BusinessMapper;
import com.jinglitong.springshop.md5.MD5ShiroUtils;
import com.jinglitong.springshop.utils.AesEncryptUtil;
import com.jinglitong.springshop.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName LoginService
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/1/9 12:58
 * @Version 1.0
 **/
@Service
public class SupplierLoginService {

    @Resource
    private BusinessMapper businessMapper;
    @Resource
    private RedisService redisService;


    public ShopRespose login(String userName, String password, String clientId){
        ShopRespose respose = new ShopRespose();
        if(StringUtils.isBlank(userName)){
            return new ShopRespose(ErrorEnum.ERROR_203.getCode(),ErrorEnum.ERROR_203.getMsg());
        }
        if(StringUtils.isBlank(password)){
            return new ShopRespose(ErrorEnum.ERROR_204.getCode(),ErrorEnum.ERROR_204.getMsg());
        }
        if(StringUtils.isBlank(clientId)){
            return new ShopRespose(ErrorEnum.ERROR_207.getCode(),ErrorEnum.ERROR_207.getMsg());
        }
        Business bus = new Business();
        bus.setUsername(userName);
        Business business = businessMapper.selectOne(bus);
        if(business==null){
            return new ShopRespose(ErrorEnum.ERROR_209.getCode(),ErrorEnum.ERROR_209.getMsg());
        }
        if(business.getState()==2){
            return new ShopRespose(ErrorEnum.ERROR_210.getCode(),ErrorEnum.ERROR_210.getMsg());
        }
        String salt = business.getSalt();
        String md5Password = null;
        try {
            //解密密码方式是：先做URLDecoder.decode 再做AesEncryptUtil.aesDecrypt  AES解密；
            //当然在登录的时候需要先做URLEncoder.encode()在做AesEncryptUtil.aesEncrypt()  AES的加密
            md5Password = MD5ShiroUtils.createBusinessPwd(AesEncryptUtil.aesDecrypt(URLDecoder.decode(password,"utf-8")),business.getSalt());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(md5Password==null||!md5Password.equals(business.getPassword())){
            return new ShopRespose(ErrorEnum.ERROR_209.getCode(),ErrorEnum.ERROR_209.getMsg());
        }
        //jwt 生成token
        String token = JwtTokenUtils.createToken(userName,"ROLE_USER");
        //判断此用户在当前终端是否已经登录
        String md5TokenKey = MD5ShiroUtils.createRedisKey(clientId+token, IConstants.LOGIN_ENCRYPT_SALT);
        if(redisService.hasKey(clientId+userName)){
            //已经登录，删除原token信息
            redisService.delete(MD5ShiroUtils.createRedisKey(clientId+redisService.getValue(clientId+userName),IConstants.LOGIN_ENCRYPT_SALT));
        }
        redisService.setKey(md5TokenKey,token, 1,TimeUnit.DAYS);
        redisService.setKey(clientId+userName,token, 1,TimeUnit.DAYS);
        redisService.setObjectWithTime(userName,business,1,TimeUnit.DAYS);
        LoginVo loginVo = new LoginVo();
        BeanUtils.copyProperties(business,loginVo);
        loginVo.setUserName(business.getUsername());
        loginVo.setToken(token);
        respose.setCode(IConstants.SUCCESS);
        respose.setData(loginVo);
        respose.setMessage(IConstants.SUCCESS_MSG);
        return respose;
    }


    public Business getBusinessInfo(String userName){
    	Business record = new Business();
    	record.setUsername(userName);
    	record = businessMapper.selectOne(record);
        return record;
    }
}
