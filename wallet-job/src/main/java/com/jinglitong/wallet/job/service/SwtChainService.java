package com.jinglitong.wallet.job.service;

import com.googlecode.jsonrpc4j.JsonRpcClientException;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.jinglitong.wallet.api.model.InvestAccount;
import com.jinglitong.wallet.api.model.MainChain;
import com.jinglitong.wallet.api.model.logic.LBallanceVO;
import com.jinglitong.wallet.api.model.logic.LPayResVO;
import com.jinglitong.wallet.api.model.logic.LPaymentVO;
import com.jinglitong.wallet.api.model.logic.LWalletVO;
import com.jinglitong.wallet.api.model.view.BalanceVO;
import com.jinglitong.wallet.api.model.view.WalletVO;
import com.jinglitong.wallet.job.common.ErrorEnum;
import com.jinglitong.wallet.job.mapper.InvestAccountMapper;
import com.jinglitong.wallet.job.mapper.MainChainMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("swtChainService")
public class SwtChainService implements ChainService{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String HEAD_CONTENT_TYPE = "application/json;charset=UTF-8";

    private final String CLASS_NAME = this.getClass().getSimpleName();

    @Autowired
    private MainChainMapper chainMapper;

    @Autowired
    private InvestAccountMapper investAccountMapper;

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
        InvestAccount ia = null;
        if(null != chain.getActiveFlag() && chain.getActiveFlag() == true) {
            InvestAccount temp = new InvestAccount();
            temp.setChainId(chain.getChainId());
            temp.setStatus(true);
            List<InvestAccount> ias = investAccountMapper.select(temp);
            if(ias.size() > 0) {
                ia = ias.get(0);
            }
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
        if(null != ia) {
            hashMap.put("boss_currency",ia.getCurrency());
            hashMap.put("boss_address",ia.getInvestAddress());
            hashMap.put("boss_secret",ia.getInvestSecret());
            hashMap.put("boss_activeNum",ia.getActiveNum());
            hashMap.put("need_boss","1");
        }else{
            hashMap.put("need_boss","0");
        }
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
        Map<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("address",vo.getAddress());
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
    //导入钱包w
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
        resVO.setPayPasswd((String)res.get("secret"));//加密后的秘钥
        resVO.setPublicAddress((String)res.get("address"));//公钥地址
        resVO.setResCode(0);
        return resVO;
    }
    //支付fyy
    @Override
    public void payment(LPaymentVO resVO,String chainType) {
        resVO.setResCode(21341);
        MainChain chain = chainMapper.getMainChainById(chainType);//链ID
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
        hashMap.put("destAddress", resVO.getDestAddress());//目标地址
        hashMap.put("fromAddress", resVO.getFromAddress());//总账户
        hashMap.put("amount", resVO.getAmount());//金额
        hashMap.put("currency", resVO.getCurrency());//币种
        hashMap.put("issuer", resVO.getIssuer());//银冠（subchain）
        hashMap.put("memo", resVO.getMemo());
        hashMap.put("custid", resVO.getCustid());//salt
        hashMap.put("ppwd", resVO.getPpwd());//payword 123456
        hashMap.put("secretkey", resVO.getSecretkey());//秘钥
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
        hashMap.put("marker",vo.getMarker());
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
        return null;
    }

    //查Hash结果（lianID，hash）
    @Override
    public void getTx(LPayResVO resVO) {
        resVO.setResCode(21352);
        MainChain chain = chainMapper.getMainChainById(resVO.getChainId());
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
        hashMap.put("hash", resVO.getTxHash());

        Map res = null;
        try {
            res = client.invoke("getTx", hashMap, Map.class);
            resVO.setResMap(res);
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
}

