package com.jinglitong.wallet.server.service;

import com.googlecode.jsonrpc4j.JsonRpcClientException;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.jinglitong.wallet.server.common.ErrorEnum;
import com.jinglitong.wallet.server.mapper.MainChainMapper;
import com.jinglitong.wallet.api.model.MainChain;
import com.jinglitong.wallet.api.model.logic.LBallanceVO;
import com.jinglitong.wallet.api.model.logic.LPayResVO;
import com.jinglitong.wallet.api.model.logic.LPaymentVO;
import com.jinglitong.wallet.api.model.logic.LWalletVO;
import com.jinglitong.wallet.api.model.view.BalanceVO;
import com.jinglitong.wallet.api.model.view.WalletVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service("ethChainService")
public class EthChainService implements ChainService{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String HEAD_CONTENT_TYPE = "application/json;charset=UTF-8";

    private final String CLASS_NAME = this.getClass().getSimpleName();

    private final String ETH_GAS_KEY = "ETH_GAS_KEY";

    @Autowired
    private MainChainMapper chainMapper;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public LWalletVO createWallet(WalletVO vo) {
        LWalletVO resVO = new LWalletVO();
        resVO.setResCode(21301);
        MainChain chain = chainMapper.getMainChainById(vo.getChainId());
        if (null == chain){
            resVO.setResCode(ErrorEnum.ERROR_21399.getCode());
            resVO.setMessage(ErrorEnum.ERROR_21399.getMsg());
            return resVO;
        }

        JsonRpcHttpClient client = null;
        try {
            client = new JsonRpcHttpClient(
                    new URL(chain.getChainInterface()));
        } catch (Exception e) {
            logger.error(CLASS_NAME + "error:",e);
            return resVO;
        }
        client.setContentType(HEAD_CONTENT_TYPE);
        //生成钱包的id
        Map<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("custid",vo.getCustId());
        hashMap.put("passwd",vo.getPasswd());
        Map res = null;
        try {
            res = client.invoke("createAccount", hashMap, Map.class);
        } catch (JsonRpcClientException clientException) {
            resVO.setResCode(clientException.getCode());
            resVO.setMessage(clientException.getMessage());
            return resVO;
        } catch (Throwable throwable) {
            resVO.setMessage("内部错误");
            return resVO;
        }
        resVO.setPayPasswd((String)res.get("secret"));
        resVO.setPublicAddress((String)res.get("address"));
        resVO.setResCode(0);
        return resVO;
    }

    @Override
    public LBallanceVO getBallance(BalanceVO vo) {
        LBallanceVO resVO = new LBallanceVO();
        resVO.setChainId(vo.getChainId());
        resVO.setResCode(-1);
        MainChain chain = chainMapper.getMainChainById(vo.getChainId());
        if (null == chain){
            resVO.setResCode(ErrorEnum.ERROR_21399.getCode());
            resVO.setMessage(ErrorEnum.ERROR_21399.getMsg());
            return resVO;
        }
        JsonRpcHttpClient client = null;
        try {
            client = new JsonRpcHttpClient(
                    new URL(chain.getChainInterface()));
        } catch (Exception e) {
            logger.error(CLASS_NAME + "error:",e);
            return resVO;
        }
        client.setContentType(HEAD_CONTENT_TYPE);
        Map<String,Object> hashMap = new HashMap<String,Object>();
        hashMap.put("address",vo.getAddress());
        if (vo.getTokens().size() > 0) {
            hashMap.put("tokens",vo.getTokens());
        } else {
            hashMap.put("tokens","");
        }
        Map<String,Object> json  = new HashMap<String,Object>();
        List list = null;
        try {
            list = client.invoke("getBalance", hashMap, List.class);
            resVO.setDataList(list);
            resVO.setResCode(0);
            resVO.setMessage("成功");
            return resVO;
        } catch (JsonRpcClientException clientException) {
            resVO.setResCode(clientException.getCode());
            resVO.setMessage(clientException.getMessage());
            return resVO;
        } catch (Throwable throwable) {
            resVO.setMessage("内部错误");
            return resVO;
        }
    }

    @Override
    public LWalletVO importWallet(WalletVO vo) {
        LWalletVO resVO = new LWalletVO();
        resVO.setResCode(21321);
        MainChain chain = chainMapper.getMainChainById(vo.getChainId());
        if (null == chain){
            resVO.setResCode(ErrorEnum.ERROR_21399.getCode());
            resVO.setMessage(ErrorEnum.ERROR_21399.getMsg());
            return resVO;
        }

        JsonRpcHttpClient client = null;
        try {
            client = new JsonRpcHttpClient(
                    new URL(chain.getChainInterface()));
        } catch (Exception e) {
            logger.error(CLASS_NAME + "error:",e);
            return resVO;
        }
        client.setContentType(HEAD_CONTENT_TYPE);

        Map<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("custid",vo.getCustId());
        hashMap.put("passwd",vo.getPasswd());
        hashMap.put("secret",vo.getSecret());
        hashMap.put("mnemonic",vo.getMnemonic());
        Map res = null;
        try {
            res = client.invoke("importWallet", hashMap, Map.class);
        } catch (JsonRpcClientException clientException) {
            resVO.setResCode(clientException.getCode());
            resVO.setMessage(clientException.getMessage());
            return resVO;
        } catch (Throwable throwable) {
            resVO.setMessage("内部错误");
            return resVO;
        }
        resVO.setPayPasswd((String)res.get("secret"));
        resVO.setPublicAddress((String)res.get("address"));
        resVO.setResCode(0);
        return resVO;
    }

    @Override
    public void payment(LPaymentVO resVO,String chainType) {
        resVO.setResCode(21341);
        MainChain chain = chainMapper.getMainChainById(chainType);
        if (null == chain){
            resVO.setResCode(ErrorEnum.ERROR_21399.getCode());
            resVO.setMessage(ErrorEnum.ERROR_21399.getMsg());
            return;
        }
        JsonRpcHttpClient client = null;
        try {
            client = new JsonRpcHttpClient(
                    new URL(chain.getChainInterface()));
        } catch (Exception e) {
            logger.error(CLASS_NAME + "error:",e);
            return;
        }
        client.setContentType(HEAD_CONTENT_TYPE);

        Map<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("destAddress", resVO.getDestAddress());
        hashMap.put("fromAddress", resVO.getFromAddress());
        hashMap.put("tokenAddress", resVO.getCurrency());
        hashMap.put("amount", resVO.getAmount());
        hashMap.put("tokenAddress", resVO.getCurrency());
        hashMap.put("gasPrice", resVO.getGasPrice());
        hashMap.put("gasLimit", resVO.getGas());
        hashMap.put("memo", resVO.getMemo());
        hashMap.put("custid", resVO.getCustid());
        hashMap.put("ppwd", resVO.getPpwd());
        hashMap.put("secretkey", resVO.getSecretkey());
        hashMap.put("uuid", resVO.getUuid());

        String res = null;
        try {
            res = client.invoke("payment", hashMap, String.class);
            resVO.setPaymentHash(res);
            resVO.setResCode(0);
        } catch (JsonRpcClientException clientException) {
            resVO.setResCode(clientException.getCode());
            resVO.setMessage(clientException.getMessage());
            return;
        } catch (Throwable throwable) {
            resVO.setMessage("内部错误");
            return;
        }
    }

    @Override
    public LBallanceVO getPayMents(BalanceVO vo) {
        LBallanceVO resVO = new LBallanceVO();
        resVO.setResCode(21351);
        MainChain chain = chainMapper.getMainChainById(vo.getChainId());
        if (null == chain){
            resVO.setResCode(ErrorEnum.ERROR_21399.getCode());
            resVO.setMessage(ErrorEnum.ERROR_21399.getMsg());
            return resVO;
        }

        JsonRpcHttpClient client = null;
        try {
            client = new JsonRpcHttpClient(
                    new URL(chain.getChainInterface()));
        } catch (Exception e) {
            logger.error(CLASS_NAME + "error:",e);
            return resVO;
        }
        client.setContentType(HEAD_CONTENT_TYPE);
        Map<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("address",vo.getAddress());
        hashMap.put("page_num",vo.getPageNum());
        Map<String,Object> json  = new HashMap<String,Object>();
        Map map = null;
        try {
            map = client.invoke("getpayments", hashMap, Map.class);
            resVO.setDataMap(map);
            resVO.setResCode(0);
            resVO.setMessage("成功");
            return resVO;
        } catch (JsonRpcClientException clientException) {
            resVO.setResCode(clientException.getCode());
            resVO.setMessage(clientException.getMessage());
            return resVO;
        } catch (Throwable throwable) {
            resVO.setMessage("内部错误");
            return resVO;
        }
    }

    @Override
    public LWalletVO modifyPasswd(WalletVO vo) {
        LWalletVO resVO = new LWalletVO();
        resVO.setResCode(21361);
        MainChain chain = chainMapper.getMainChainById(vo.getChainId());
        if (null == chain){
            resVO.setResCode(ErrorEnum.ERROR_21399.getCode());
            resVO.setMessage(ErrorEnum.ERROR_21399.getMsg());
            return resVO;
        }

        JsonRpcHttpClient client = null;
        try {
            client = new JsonRpcHttpClient(
                    new URL(chain.getChainInterface()));
        } catch (Exception e) {
            logger.error(CLASS_NAME + "error:",e);
            return resVO;
        }
        client.setContentType(HEAD_CONTENT_TYPE);

        Map<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("oldpasswd",vo.getOldpasswd());
        hashMap.put("newpasswd",vo.getPasswd());
        hashMap.put("secret",vo.getSecret());
        hashMap.put("custid",vo.getCustId());
        hashMap.put("address",vo.getAddress());
        String res = null;
        try {
            res = client.invoke("modifyPasswd", hashMap, String.class);
            resVO.setPayPasswd(res);
        } catch (JsonRpcClientException clientException) {
            resVO.setResCode(clientException.getCode());
            resVO.setMessage(clientException.getMessage());
            return resVO;
        } catch (Throwable throwable) {
            resVO.setMessage("内部错误");
            return resVO;
        }
        resVO.setResCode(0);
        return resVO;
    }

    @Override
    public LWalletVO forgetPasswd(WalletVO vo) {
        LWalletVO resVO = new LWalletVO();
        resVO.setResCode(21371);
        MainChain chain = chainMapper.getMainChainById(vo.getChainId());
        if (null == chain){
            resVO.setResCode(ErrorEnum.ERROR_21399.getCode());
            resVO.setMessage(ErrorEnum.ERROR_21399.getMsg());
            return resVO;
        }

        JsonRpcHttpClient client = null;
        try {
            client = new JsonRpcHttpClient(
                    new URL(chain.getChainInterface()));
        } catch (Exception e) {
            logger.error(CLASS_NAME + "error:",e);
            return resVO;
        }
        client.setContentType(HEAD_CONTENT_TYPE);

        Map<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("passwd",vo.getPasswd());
        hashMap.put("secret",vo.getSecret());
        hashMap.put("custid",vo.getCustId());
        hashMap.put("address",vo.getAddress());
        String res = null;
        try {
            res = client.invoke("forgetPasswd", hashMap, String.class);
            resVO.setPayPasswd(res);
        } catch (JsonRpcClientException clientException) {
            resVO.setResCode(clientException.getCode());
            resVO.setMessage(clientException.getMessage());
            return resVO;
        } catch (Throwable throwable) {
            resVO.setMessage("内部错误");
            return resVO;
        }
        resVO.setResCode(0);
        return resVO;
    }

    @Override
    public LWalletVO exportWallet(WalletVO vo) {
        LWalletVO resVO = new LWalletVO();
        resVO.setResCode(21381);
        MainChain chain = chainMapper.getMainChainById(vo.getChainId());
        if (null == chain){
            resVO.setResCode(ErrorEnum.ERROR_21399.getCode());
            resVO.setMessage(ErrorEnum.ERROR_21399.getMsg());
            return resVO;
        }

        JsonRpcHttpClient client = null;
        try {
            client = new JsonRpcHttpClient(
                    new URL(chain.getChainInterface()));
        } catch (Exception e) {
            logger.error(CLASS_NAME + "error:",e);
            return resVO;
        }
        client.setContentType(HEAD_CONTENT_TYPE);

        Map<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("passwd",vo.getPasswd());
        hashMap.put("secret",vo.getSecret());
        hashMap.put("custid",vo.getCustId());
        String res = null;
        try {
            res = client.invoke("exportWallet", hashMap, String.class);
            resVO.setSecretKey(res);
        } catch (JsonRpcClientException clientException) {
            resVO.setResCode(clientException.getCode());
            resVO.setMessage(clientException.getMessage());
            return resVO;
        } catch (Throwable throwable) {
            resVO.setMessage("内部错误");
            return resVO;
        }
        resVO.setResCode(0);
        return resVO;
    }

    @Override
    public LBallanceVO getPasPrice(WalletVO vo) {
        LBallanceVO resVO = new LBallanceVO();
        resVO.setResCode(21303);
        try {
            Map<String, Object> defaultGas = (Map<String, Object>) redisTemplate.opsForValue().get(ETH_GAS_KEY);
            if(null != defaultGas) {
                resVO.setDataMap(defaultGas);
                resVO.setResCode(0);
                resVO.setMessage("成功");
                return resVO;
            }
        } catch (Exception e) {
            logger.error("redis get gas error:",e);
        }
        MainChain chain = chainMapper.getMainChainById(vo.getChainId());
        if (null == chain){
            resVO.setResCode(ErrorEnum.ERROR_21399.getCode());
            resVO.setMessage(ErrorEnum.ERROR_21399.getMsg());
            return resVO;
        }

        JsonRpcHttpClient client = null;
        try {
            client = new JsonRpcHttpClient(
                    new URL(chain.getChainInterface()));
        } catch (Exception e) {
            logger.error(CLASS_NAME + "error:",e);
            return resVO;
        }

        client.setContentType(HEAD_CONTENT_TYPE);
        Map<String,Object> json  = new HashMap<String,Object>();
        Map map = null;
        try {
            map = client.invoke("gasPrice", null, Map.class);
            redisTemplate.opsForValue().set(ETH_GAS_KEY,map,1, TimeUnit.MINUTES);
            resVO.setDataMap(map);
            resVO.setResCode(0);
            resVO.setMessage("成功");
            return resVO;
        } catch (JsonRpcClientException clientException) {
            resVO.setResCode(clientException.getCode());
            resVO.setMessage(clientException.getMessage());
            return resVO;
        } catch (Throwable throwable) {
            resVO.setMessage("内部错误");
            return resVO;
        }
    }

    @Override
    public void getTx(LPayResVO vo) {
    }
}
