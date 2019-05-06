package com.jinglitong.springshop.servcie;

import com.jinglitong.springshop.ErrorEnum;
import com.jinglitong.springshop.IConstants;
import com.jinglitong.springshop.ShopRespose;
import com.jinglitong.springshop.entity.Customer;
import com.jinglitong.springshop.entity.DdbIntegralAccount;
import com.jinglitong.springshop.entity.DdbIntegralWallet;
import com.jinglitong.springshop.entity.PropertieTab;
import com.jinglitong.springshop.jwt.JwtTokenUtils;
import com.jinglitong.springshop.mapper.CustomerMapper;
import com.jinglitong.springshop.mapper.DdbIntegralAccountMapper;
import com.jinglitong.springshop.mapper.DdbIntegralWalletMapper;
import com.jinglitong.springshop.mapper.PropertieTabMapper;
import com.jinglitong.springshop.md5.MD5ShiroUtils;
import com.jinglitong.springshop.service.RedisService;
import com.jinglitong.springshop.utils.AesEncryptUtil;
import com.jinglitong.springshop.utils.UuidUtil;
import com.jinglitong.springshop.vo.LoginVo;
import com.jinglitong.springshop.vo.request.CustRegVO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName LoginService
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/1/9 12:58
 * @Version 1.0
 **/
@Service
public class LoginService {
    
    @Resource
    private CustomerMapper customerMapper;

    @Resource
    private RedisService redisService;

    
    @Autowired
    private DdbIntegralAccountMapper integralAccountMapper;
    
    @Autowired
    private DdbIntegralWalletMapper integralWalletMapper;
    
    @Autowired
    private PropertieTabMapper propertieTabMapper;

    public ShopRespose login(String userName,String password,String clientId,String appId){
        ShopRespose respose = new ShopRespose();
        if(StringUtils.isBlank(userName)){
            return new ShopRespose(ErrorEnum.ERROR_202.getCode(),"用户名为空");
        }
        if(StringUtils.isBlank(password)){
            return new ShopRespose(ErrorEnum.ERROR_202.getCode(),"密码为空");
        }
        if(StringUtils.isBlank(clientId)){
            return new ShopRespose(ErrorEnum.ERROR_202.getCode(),"未获取到客户端信息");
        }
        Customer cus = new Customer();
        cus.setAccount(userName);
        cus.setAppId(appId);
        Customer customer = customerMapper.selectOne(cus);
        if(customer==null){
            return new ShopRespose(ErrorEnum.ERROR_203.getCode(),"用户名或密码错误");
        }
        if(!customer.getState()){
			return new ShopRespose(ErrorEnum.ERROR_208.getCode(),ErrorEnum.ERROR_208.getMsg());
		}
        String salt = customer.getSalt();
        String md5Password = null;
        try {
            //解密密码方式是：先做URLDecoder.decode 再做AesEncryptUtil.aesDecrypt  AES解密；
            //当然在登录的时候需要先做URLEncoder.encode()在做AesEncryptUtil.aesEncrypt()  AES的加密
            md5Password = MD5ShiroUtils.createCustomPwd(AesEncryptUtil.aesDecrypt(URLDecoder.decode(password, "utf-8")), salt);
            System.out.println(AesEncryptUtil.aesDecrypt(URLDecoder.decode(password, "utf-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(md5Password==null||!md5Password.equals(customer.getPassword())){
            return new ShopRespose(ErrorEnum.ERROR_204.getCode(),"用户名或密码错误");
        }
        //jwt 生成token
        String token = JwtTokenUtils.createToken(userName,"ROLE_USER");
        //判断此用户在当前终端是否已经登录
        String md5TokenKey = MD5ShiroUtils.createRedisKey(clientId+token,IConstants.LOGIN_ENCRYPT_SALT);
        if(redisService.hasKey(clientId+userName)){
            //已经登录，删除原token信息
            redisService.delete(MD5ShiroUtils.createRedisKey(clientId+redisService.getValue(clientId+userName),IConstants.LOGIN_ENCRYPT_SALT));
        }
        redisService.setKey(md5TokenKey,token, 1,TimeUnit.DAYS);
        redisService.setKey(clientId+userName,token, 1,TimeUnit.DAYS);
        redisService.setObjectWithTime(userName,customer,1,TimeUnit.DAYS);
        LoginVo loginVo = new LoginVo();
        BeanUtils.copyProperties(customer,loginVo);
        loginVo.setUserName(customer.getAccount());
        loginVo.setToken(token);
        respose.setCode(IConstants.SUCCESS);
        respose.setData(loginVo);
        respose.setMessage("登录成功");
        return respose;
    }
    
    public ShopRespose loginOut(String userName,String clientId) {
    	ShopRespose respose = new ShopRespose();
    	String token = JwtTokenUtils.createToken(userName,"ROLE_USER");
    	String md5TokenKey = MD5ShiroUtils.createRedisKey(clientId+token,IConstants.LOGIN_ENCRYPT_SALT);
    	redisService.delete(md5TokenKey);
    	redisService.delete(clientId+userName);
    	respose.setCode(IConstants.SUCCESS);
        respose.setMessage("登出成功");
        return respose;
    }
    
    public String getInviteCode(){
    	char[] chs = { 'A', 'B', 'C', 'D',  'E', 'F', 'G', 'H', 'I', 'J', 'K','L', 'M', 'N','O' ,'P', 'Q','R','S', 'T', 'U', 'V',
    				'W', 'X', 'Y', 'Z' };
		SecureRandom random = new SecureRandom();
		final char[] value = new char[8];
		for (int i = 0; i < value.length; i++) {
			value[i] = (char) chs[random.nextInt(chs.length)];
		}
		final String code = new String(value);
		//查询数据库
		Customer codeBean = new Customer();
		codeBean.setSelfInvite(code);
		Customer cus=customerMapper.selectOne(codeBean);
		
		if(cus == null){
			return code;
		}else{
			return getInviteCode();
		}

    }
    
  
}
