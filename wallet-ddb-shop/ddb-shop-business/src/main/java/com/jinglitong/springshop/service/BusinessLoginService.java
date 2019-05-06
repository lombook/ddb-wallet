package com.jinglitong.springshop.service;

import com.jinglitong.springshop.ErrorEnum;
import com.jinglitong.springshop.IConstants;
import com.jinglitong.springshop.ShopRespose;
import com.jinglitong.springshop.entity.Business;
import com.jinglitong.springshop.entity.admin.ShopAdmin;
import com.jinglitong.springshop.jwt.JwtTokenUtils;
import com.jinglitong.springshop.mapper.BusinessMapper;
import com.jinglitong.springshop.mapper.admin.ShopAdminMapper;
import com.jinglitong.springshop.md5.MD5ShiroUtils;
import com.jinglitong.springshop.utils.AesEncryptUtil;
import com.jinglitong.springshop.vo.BusinessVo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName BusinessLoginService
 * @Description 商铺管理员登录系统
 * @Author zili.zong
 * @Date 2019/1/14 14:32
 * @Version 1.0
 **/
@Service
public class BusinessLoginService {
    @Resource
    private ShopAdminMapper adminMapper;

    @Resource
    private BusinessMapper businessMapper;
    @Resource
    private RedisService redisService;
    
    @Resource
    private ShopAdminMapper ShopAdminMapper;

    public ShopRespose login(String userName, String password,String clientId){
        if(StringUtils.isBlank(userName)){
            return new ShopRespose(ErrorEnum.ERROR_202.getCode(),"用户名为空");
        }
        if(StringUtils.isBlank(password)){
            return new ShopRespose(ErrorEnum.ERROR_202.getCode(),"密码为空");
        }
        if(StringUtils.isBlank(clientId)){
            return new ShopRespose(ErrorEnum.ERROR_202.getCode(),"未获取到客户端信息");
        }
        ShopAdmin queryParam = new ShopAdmin();
        queryParam.setUserName(userName);
        ShopAdmin admin = adminMapper.selectOne(queryParam);
        if(admin==null){
            return new ShopRespose(ErrorEnum.ERROR_203.getCode(),"用户不存在");
        }
        String md5Password = null;
        try {
            //解密密码方式是：先做URLDecoder.decode 再做AesEncryptUtil.aesDecrypt  AES解密；
            //当然在登录的时候需要先做URLEncoder.encode()在做AesEncryptUtil.aesEncrypt()  AES的加密
            md5Password = MD5ShiroUtils.createBusinessPwd(AesEncryptUtil.aesDecrypt(URLDecoder.decode(password,"utf-8")),admin.getSalt());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(md5Password==null||!md5Password.equals(admin.getPassword())){
            return new ShopRespose(ErrorEnum.ERROR_204.getCode(),"密码不正确");
        }
        //jwt 生成token
        String token = JwtTokenUtils.createToken(userName,"ROLE_ADMIN");
        BusinessVo vo = new BusinessVo();
        BeanUtils.copyProperties(admin,vo);
        vo.setToken(token);
        vo.setUsername(admin.getUserName());
        vo.setEmail(admin.getEmail());
        //判断此用户在当前终端是否已经登录
        String md5TokenKey = MD5ShiroUtils.createRedisKey(clientId+token,IConstants.LOGIN_ENCRYPT_SALT);
        if(redisService.hasKey(clientId+userName)){
            //已经登录，删除原token信息
            redisService.delete(MD5ShiroUtils.createRedisKey(clientId+redisService.getValue(clientId+userName),IConstants.LOGIN_ENCRYPT_SALT));
        }
        redisService.setKey(md5TokenKey,token, 1,TimeUnit.DAYS);
        redisService.setKey(clientId+userName,token, 1,TimeUnit.DAYS);
        redisService.setObjectWithTime(IConstants.BUSINESS_LOGIN_KEY_HEADER_FIXED+userName,admin,1,TimeUnit.DAYS);
        return new ShopRespose(IConstants.SUCCESS,IConstants.SUCCESS_MSG,vo);
    }

    public ShopAdmin getBusinessInfo(String userName){
    	ShopAdmin record = new ShopAdmin();
    	record.setUserName(userName);
    	record = ShopAdminMapper.selectOne(record);
    	/*Business business = (Business) redisService.getObject(userName);
        BusinessVo businessVo = new BusinessVo();
        BeanUtils.copyProperties(business,businessVo);*/
        return record;
    }
}
