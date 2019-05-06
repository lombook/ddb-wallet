package com.jinglitong.wallet.server.service;

import com.jinglitong.wallet.api.model.*;
import com.jinglitong.wallet.api.model.logic.*;
import com.jinglitong.wallet.server.common.spring.ApplicationContextHelper;
import com.jinglitong.wallet.server.mapper.*;

import com.jinglitong.wallet.api.model.view.BalanceVO;
import com.jinglitong.wallet.api.model.view.PaymentVO;
import com.jinglitong.wallet.api.model.view.WalletCoinVO;
import com.jinglitong.wallet.api.model.view.WalletVO;
import com.jinglitong.wallet.server.util.DateUtils;
import com.jinglitong.wallet.server.util.JsonUtil;
import com.jinglitong.wallet.server.util.UuidUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.*;
import java.util.concurrent.*;

import static com.jinglitong.wallet.server.common.ErrorEnum.*;

@Service
@Transactional
public class CustomerAPIService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WalletMapper walletMapper;

    @Autowired
    private SubChainMapper subChainMapper;

    @Autowired
    private CustomerOperationMapper operationMapper;

    @Autowired
    private MainChainMapper mainChainMapper;

    @Autowired
    private WalletCoinMapper walletCoinMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${sms.export.prefix}")
    private String smsExportPrefix;


    /**
     * 创建钱包统一接口
     * @param vo
     * @return
     */
    @Transactional
    public Map<String,Object> createWallet(WalletVO vo) {
        ChainService service = getChainService(vo.getChainId(),vo.getAppId());
        String uudi = UuidUtil.getUUID();
        LWalletVO res = service.createWallet(vo);
        if (res.getResCode().equals(0) || res.getResCode().equals(10001)){
            //生成新钱包
            String now = DateUtils.getDateTime();
            Wallet wallet = new Wallet();
            wallet.setCustId(vo.getCustId());
            wallet.setChainId(vo.getChainId());
            wallet.setCreatedTime(now);
            wallet.setUpdatedTime(now);
            wallet.setWalletId(uudi);
            wallet.setPayPasswd(res.getPayPasswd());
            wallet.setPublicKey(res.getPublicAddress());
            wallet.setPrompt(vo.getPrompt());
            wallet.setWalletName(vo.getWalletName());
            wallet.setAppId(vo.getAppId());
            try {
                if (walletMapper.insertSelective(wallet) > 0){
                    defaultWalletCoin(wallet);
                    return JsonUtil.toJsonSuccess("新建钱包成功");
                }
            } catch (Exception e) {
                logger.error("DB ERROR:",e);
            }
        } else {
            return JsonUtil.toJsonError(res.getResCode(),res.getMessage());
        }
        return JsonUtil.toJsonError(ERROR_21301);
    }

    public Map<String,Object> balances(BalanceVO vo) {
        ChainService service = getChainService(vo.getChainId(),vo.getAppId());
        LBallanceVO res = service.getBallance(vo);
        if(res.getResCode().equals(0)){
            return JsonUtil.toJsonSuccess("查询成功",res.getDataList());
        } else {
            return JsonUtil.toJsonError(res.getResCode(),res.getMessage());
        }
    }

    public Map<String,Object> importWallet(WalletVO vo) {
        ChainService service = getChainService(vo.getChainId(),vo.getAppId());
        LWalletVO res = service.importWallet(vo);
        String uudi = UuidUtil.getUUID();
        if (res.getResCode().equals(0)){
            //生成新钱包
            String now = DateUtils.getDateTime();
            Wallet wallet = new Wallet();
            wallet.setCustId(vo.getCustId());
            wallet.setChainId(vo.getChainId());
            wallet.setCreatedTime(now);
            wallet.setUpdatedTime(now);
            wallet.setWalletId(uudi);
            wallet.setPayPasswd(res.getPayPasswd());
            wallet.setPublicKey(res.getPublicAddress());
            wallet.setWalletName(vo.getWalletName());
            wallet.setPrompt(vo.getPrompt());
            wallet.setAppId(vo.getAppId());
            try {
                if (walletMapper.insertSelective(wallet) > 0)
                    defaultWalletCoin(wallet);
                return JsonUtil.toJsonSuccess("导入钱包成功");
            } catch (Exception e) {
                logger.error("DB ERROR:",e);
            }
        } else {
            return JsonUtil.toJsonError(res.getResCode(),res.getMessage());
        }
        return JsonUtil.toJsonError(ERROR_21321);
    }

    public Map<String,Object> payment(PaymentVO vo) {
        Wallet wallet = getCheckOutWallet(vo.getCustId(),vo.getWalletId());
        if (wallet == null) {
            return JsonUtil.toJsonError(ERROR_21331);
        }

        ChainService service = getChainService(wallet.getChainId());
        LPaymentVO lvo = new LPaymentVO();
        lvo.setAmount(vo.getAmount());
        lvo.setCurrency(vo.getCurrency().toUpperCase());
        lvo.setCustid(vo.getCustId());
        lvo.setDestAddress(vo.getDestAddress());
        lvo.setFromAddress(wallet.getPublicKey());
        lvo.setMemo(vo.getMemo());
        lvo.setPpwd(vo.getPasswd());
        lvo.setSecretkey(wallet.getPayPasswd());
        lvo.setIssuer(vo.getIssuer());
        lvo.setUuid(UuidUtil.getUUID());
        lvo.setGasPrice(vo.getGasPrice());
        lvo.setGas(vo.getGasLimit());
        service.payment(lvo, wallet.getChainId());

        String now = DateUtils.getDateTime();
        CustomerOperation operationBean = new CustomerOperation();
        operationBean.setCustId(wallet.getCustId());
        operationBean.setAmount(vo.getAmount());
        operationBean.setChainId(wallet.getChainId());
        operationBean.setCreatedTime(now);
        operationBean.setUpdatedTime(now);
        operationBean.setSendAddress(wallet.getPublicKey());
        operationBean.setRevAddress(vo.getDestAddress());
        operationBean.setTx(lvo.getPaymentHash());
        operationBean.setOperationId(lvo.getUuid());
        operationBean.setCurrency(vo.getCurrency());
        operationBean.setAppId(vo.getAppId());
        if (lvo.getResCode().equals(0)){
            operationBean.setStatus(true);
            operationMapper.insertSelective(operationBean);
            return JsonUtil.toJsonSuccess("转账请求已发送",lvo.getPaymentHash());
        } else {
            operationBean.setStatus(false);
            operationMapper.insertSelective(operationBean);
            return JsonUtil.toJsonError(lvo.getResCode(),lvo.getMessage());
        }
    }

    public Map<String,Object> getPayments(PaymentVO vo) {
        /*
        Wallet wallet = getCheckOutWallet(vo.getCustId(),vo.getWalletId());
        if (wallet == null) {
            return JsonUtil.toJsonError(ERROR_21331);
        }
        ChainService service = getChainService(wallet.getChainId());
        BalanceVO bal = new BalanceVO();
        bal.setAddress(wallet.getPublicKey());
        bal.setChainId(wallet.getChainId());
        bal.setPageNum(vo.getPageNum());
        if (!StringUtils.isEmpty(vo.getMarker()))
            bal.setMarker(vo.getMarker());
        LBallanceVO res = service.getPayMents(bal);
        if(res.getResCode().equals(0)){
            List<Object> data = (List)res.getDataMap().get("data");
            Iterator<Object> coinIter = data.iterator();
            SubChain chain = new SubChain();
            chain.setChainId(vo.getChainId());
            List<SubChain> subCoin = subChainMapper.select(chain);
            Map<String,SubChain> subMap = new HashMap<>();
            for(SubChain tempBean : subCoin){
                subMap.put(tempBean.getCurrency(),tempBean);
            }
            while (coinIter.hasNext()) {
                LinkedHashMap<String,Object> map = (LinkedHashMap<String,Object>)coinIter.next();
                if(null != map) {
                    Map<String,String> amountMap = (Map<String, String>) map.get("amount");
                    String currency = amountMap.get("currency");
                    if (null != currency && subMap.containsKey(currency)) {
                        SubChain tempBean = subMap.get(currency);
                        amountMap.put("currency", tempBean.getCoinName());
                    }
                } else {
                    coinIter.remove();
                }
            }
            return JsonUtil.toJsonSuccess("查询成功",res.getDataMap());
        } else {
            return JsonUtil.toJsonError(res.getResCode(),res.getMessage());
        }

        */
        Wallet wallet = getCheckOutWallet(vo.getCustId(),vo.getWalletId());
        if (wallet == null) {
            return JsonUtil.toJsonError(ERROR_21331);
        }
        ChainService service = getChainService(wallet.getChainId(),vo.getAppId());
        BalanceVO bal = new BalanceVO();
        bal.setAddress(wallet.getPublicKey());
        bal.setChainId(wallet.getChainId());
        bal.setPageNum(vo.getPageNum());
        if (!StringUtils.isEmpty(vo.getMarker())) {
            bal.setMarker(vo.getMarker());
        }
        List<SubChain> subChains = walletCoinMapper.getOwnCoinByWalletId(vo.getWalletId(),vo.getAppId());
        Map<String,String> tokensMap = new HashMap<>();
        if(service.getClass().getSimpleName().equals("EthChainService")) {
            for(SubChain chainBean : subChains) {
                if(!StringUtils.isEmpty(chainBean.getTokenAddress())){
                    tokensMap.put(chainBean.getTokenAddress().toLowerCase(),chainBean.getCurrency());
                }
            }
        }
        bal.setAppId(vo.getAppId());
        LBallanceVO res = service.getPayMents(bal);
        if(res.getResCode().equals(0)){
            List<Object> data = (List)res.getDataMap().get("data");
            Iterator<Object> coinIter = data.iterator();
            SubChain chain = new SubChain();
            chain.setChainId(wallet.getChainId());
            chain.setAppId(vo.getAppId());
            List<SubChain> subCoin = subChainMapper.select(chain);
            Map<String,SubChain> subMap = new HashMap<>();
            for(SubChain tempBean : subCoin){
                subMap.put(tempBean.getCurrency(),tempBean);
            }
            while (coinIter.hasNext()) {
                LinkedHashMap<String,Object> map = (LinkedHashMap<String,Object>)coinIter.next();
                if(null != map) {
                    Map<String,String> amountMap = (Map<String, String>) map.get("amount");

                    if(service.getClass().getSimpleName().equals("SwtChainService")) {
                        String currency = amountMap.get("currency");
                        if (null != currency && subMap.containsKey(currency)) {
                            SubChain tempBean = subMap.get(currency);
                            amountMap.put("currency", tempBean.getCoinName());
                        }
                    }else if(service.getClass().getSimpleName().equals("EthChainService")){
                        String currency = amountMap.get("currency");
                        if (currency.toLowerCase().equals("eth")) {

                        } else if(null != currency && tokensMap.containsKey(currency.toLowerCase())) {
                            amountMap.put("currency", tokensMap.get(currency.toLowerCase()));
                        } else {
                            coinIter.remove();
                        }
                    }
                } else {
                    coinIter.remove();
                }
            }
            return JsonUtil.toJsonSuccess("查询成功",res.getDataMap());
        } else {
            return JsonUtil.toJsonError(res.getResCode(),res.getMessage());
        }


    }

    public Map<String,Object> chaindetail(WalletVO vo) {
        /* ETH
        Wallet wallet = getCheckOutWallet(vo.getCustId(),vo.getWalletId());
        if (wallet == null) {
            return JsonUtil.toJsonError(ERROR_21331);
        }
        ChainService service = getChainService(wallet.getChainId());
        BalanceVO bal = new BalanceVO();
        bal.setAddress(wallet.getPublicKey());
        bal.setChainId(wallet.getChainId());
        LBallanceVO res = service.getBallance(bal);
        if (!res.getResCode().equals(0)) {
            return JsonUtil.toJsonError(res.getResCode(),res.getMessage());
        }
        SubChain chain = new SubChain();
        chain.setChainId(vo.getChainId());
        List<SubChain> subCoin = subChainMapper.select(chain);
        Map<String,SubChain> subMap = new HashMap<>();

        for(SubChain tempBean : subCoin){
            subMap.put(tempBean.getCurrency(),tempBean);
        }

        Iterator<Object> coinIter = res.getDataList().iterator();
        while (coinIter.hasNext()) {
            LinkedHashMap<String,String> map = (LinkedHashMap<String,String>)coinIter.next();
            if(null != map) {
                String currency = map.get("currency");
                if (null != currency && subMap.containsKey(currency)) {
                    SubChain tempBean = subMap.get(currency);
                    map.put("remark", tempBean.getCoinRemark());
                    map.put("img", tempBean.getCoinImg());
                    map.put("cname", tempBean.getCoinName());
                } else {
                    coinIter.remove();
                }
            } else {
                coinIter.remove();
            }
        }
        return JsonUtil.toJsonSuccess("查询成功",res.getDataList());
        */


        Wallet wallet = getCheckOutWallet(vo.getCustId(),vo.getWalletId(),vo.getAppId());
        if (wallet == null) {
            return JsonUtil.toJsonError(ERROR_21331);
        }
        ChainService service = getChainService(wallet.getChainId(),vo.getAppId());
        BalanceVO bal = new BalanceVO();
        bal.setAddress(wallet.getPublicKey());
        bal.setChainId(wallet.getChainId());
        List<SubChain> subChains = walletCoinMapper.getOwnCoinByWalletId(vo.getWalletId(),vo.getAppId());

        for (SubChain subChain : subChains) {
            if(!StringUtils.isEmpty(subChain.getTokenAddress())) {
                bal.getTokens().add(subChain.getTokenAddress());
            }
        }
        bal.setAppId(vo.getAppId());
        LBallanceVO res = service.getBallance(bal);
        if (!res.getResCode().equals(0)) {
            return JsonUtil.toJsonError(res.getResCode(),res.getMessage());
        }


        SubChain chain = new SubChain();
        chain.setChainId(wallet.getChainId());
        Map<String, SubChain> subMap = new HashMap<>();

        for (SubChain tempBean : subChains) {
            subMap.put(tempBean.getCurrency(), tempBean);
        }

        Iterator<Object> coinIter = res.getDataList().iterator();
        while (coinIter.hasNext()) {
            LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) coinIter.next();
            if (null != map) {
                String currency = map.get("currency");
                if (null != currency && subMap.containsKey(currency)) {
                    SubChain tempBean = subMap.get(currency);
                    map.put("remark", tempBean.getCoinRemark());
                    map.put("img", tempBean.getCoinImg());
                    map.put("cname", tempBean.getCoinName());
                    map.put("taddress",StringUtils.isEmpty(tempBean.getTokenAddress()) ? "" : tempBean.getTokenAddress());
                    subMap.remove(currency);
                } else {
                    coinIter.remove();
                }
            } else {
                coinIter.remove();
            }
        }
        if(service.getClass().getSimpleName().contains("SwtChainService") || service.getClass().getSimpleName().contains("EthChainService")) {
            for (String key : subMap.keySet()) {
                Map<String,Object> tempMap = new HashMap<>();
                tempMap.put("value","0");
                tempMap.put("currency",subMap.get(key).getCoinName());
                tempMap.put("issuer",subMap.get(key).getTokenAddress());
                tempMap.put("remark",subMap.get(key).getCoinRemark());
                tempMap.put("img",subMap.get(key).getCoinImg());
                tempMap.put("cname", subMap.get(key).getCoinName());
                res.getDataList().add(tempMap);
            }
        }
        return JsonUtil.toJsonSuccess("查询成功",res.getDataList());

    }
    public Map<String,Object> getSubChainAmount(WalletVO vo) {
        /* ETH
        Wallet wallet = getCheckOutWallet(vo.getCustId(),vo.getWalletId());
        if (wallet == null) {
            return JsonUtil.toJsonError(ERROR_21331);
        }
        ChainService service = getChainService(wallet.getChainId());
        BalanceVO bal = new BalanceVO();
        bal.setAddress(wallet.getPublicKey());
        bal.setChainId(wallet.getChainId());
        LBallanceVO res = service.getBallance(bal);
        if (!res.getResCode().equals(0)) {
            return JsonUtil.toJsonError(res.getResCode(),res.getMessage());
        }
        SubChain chain = new SubChain();
        chain.setChainId(vo.getChainId());
        List<SubChain> subCoin = subChainMapper.select(chain);
        Map<String,SubChain> subMap = new HashMap<>();

        for(SubChain tempBean : subCoin){
            subMap.put(tempBean.getCurrency(),tempBean);
        }

        Iterator<Object> coinIter = res.getDataList().iterator();
        while (coinIter.hasNext()) {
            LinkedHashMap<String,String> map = (LinkedHashMap<String,String>)coinIter.next();
            if(null != map) {
                String currency = map.get("currency");
                if (null != currency && subMap.containsKey(currency)) {
                    SubChain tempBean = subMap.get(currency);
                    map.put("remark", tempBean.getCoinRemark());
                    map.put("img", tempBean.getCoinImg());
                    map.put("cname", tempBean.getCoinName());
                } else {
                    coinIter.remove();
                }
            } else {
                coinIter.remove();
            }
        }
        return JsonUtil.toJsonSuccess("查询成功",res.getDataList());
        */


        Wallet wallet = getCheckOutWallet(vo.getCustId(),vo.getWalletId());
        if (wallet == null) {
            return JsonUtil.toJsonError(ERROR_21331);
        }
        ChainService service = getChainService(wallet.getChainId(),vo.getAppId());
        BalanceVO bal = new BalanceVO();
        bal.setAddress(wallet.getPublicKey());
        bal.setChainId(wallet.getChainId());
        List<SubChain> subChains = walletCoinMapper.getOwnCoinByWalletId(vo.getWalletId(),vo.getAppId());

        for (SubChain subChain : subChains) {
            if(!StringUtils.isEmpty(subChain.getTokenAddress())) {
                bal.getTokens().add(subChain.getTokenAddress());
            }
        }
        LBallanceVO res = service.getBallance(bal);
        return JsonUtil.toJsonSuccess("查询成功",res.getDataList());

    }

    public Map<String,Object> moifpasswd(WalletVO vo) {
        Wallet wallet = getCheckOutWallet(vo.getCustId(),vo.getWalletId(),vo.getAppId());
        if(null == wallet){
            return JsonUtil.toJsonError( ERROR_21331);
        }
        ChainService service = getChainService(wallet.getChainId(),vo.getAppId());
        vo.setAddress(wallet.getPublicKey());
        vo.setSecret(wallet.getPayPasswd());
        vo.setChainId(wallet.getChainId());
        LWalletVO resvo = service.modifyPasswd(vo);
        if (resvo.getResCode().equals(0)) {
            Wallet updateBean = new Wallet();
            updateBean.setPayPasswd(resvo.getPayPasswd());
            Weekend<Wallet>weekend = Weekend.of(Wallet.class);
        	WeekendCriteria<Wallet,Object> Criteria =weekend.weekendCriteria();
        	Criteria.andEqualTo(Wallet::getWalletId, vo.getWalletId());
            if (walletMapper.updateByExampleSelective(updateBean, weekend) == 1) {
                return JsonUtil.toJsonSuccess("更新成功");
            } else {
                return JsonUtil.toJsonError(ERROR_21361);
            }
        } else {
            return JsonUtil.toJsonError(resvo.getResCode(),resvo.getMessage());
        }
    }

    public Map<String,Object> forgetpasswd(WalletVO vo) {
        Wallet wallet = getCheckOutWallet(vo.getCustId(),vo.getWalletId(),vo.getAppId());
        if(null == wallet){
            return JsonUtil.toJsonError( ERROR_21331);
        }
        ChainService service = getChainService(wallet.getChainId(),vo.getAppId());
        vo.setAddress(wallet.getPublicKey());
        vo.setChainId(wallet.getChainId());
        LWalletVO resvo = service.forgetPasswd(vo);
        if (resvo.getResCode().equals(0)) {
            Wallet updateBean = new Wallet();
            updateBean.setPayPasswd(resvo.getPayPasswd());
            Weekend<Wallet>weekend = Weekend.of(Wallet.class);
        	WeekendCriteria<Wallet,Object> Criteria =weekend.weekendCriteria();
        	Criteria.andEqualTo(Wallet::getWalletId, vo.getWalletId());
            if (walletMapper.updateByExampleSelective(updateBean, weekend) == 1) {
                return JsonUtil.toJsonSuccess("更新成功");
            } else {
                return JsonUtil.toJsonError(ERROR_21361);
            }
        } else {
            return JsonUtil.toJsonError(resvo.getResCode(),resvo.getMessage());
        }
    }

    public Map<String,Object> exportwallet(WalletVO vo) throws Exception {
        Wallet wallet = getCheckOutWallet(vo.getCustId(),vo.getWalletId(),vo.getAppId());
        if(null == wallet){
            return JsonUtil.toJsonError(ERROR_21331);
        }
        ChainService service = getChainService(wallet.getChainId(),vo.getAppId());
        vo.setChainId(wallet.getChainId());
        vo.setCustId(wallet.getCustId());
        vo.setSecret(wallet.getPayPasswd());
        LWalletVO resvo = service.exportWallet(vo);
        if (resvo.getResCode().equals(0)) {
        	//先验证支付密码，成功后验证短信验证码
        	String code = vo.getSmsCode();
            String account = vo.getAccount();
            String redisKey = "";
            String redisCode = "";
            if(!StringUtils.isEmpty(account)&&!account.contains("@")) {
            	redisKey = smsExportPrefix +vo.getAppId().trim()+"+"+vo.getCountryCode().trim()+vo.getAccount().trim();
            	logger.info("导出钱包key："+redisKey);
                redisCode = (String)redisTemplate.opsForValue().get(redisKey);
                
                if(StringUtils.isEmpty(redisCode)) {
                	throw new Exception(ERROR_21003.getCode()+"");
                }else if(!redisCode.equals(code)){
                	throw new Exception(ERROR_21002.getCode()+"");
                } else {
                    redisTemplate.delete(redisKey);
                }
            }
            return JsonUtil.toJsonSuccess("导出成功", resvo.getSecretKey());
        } else {
            return JsonUtil.toJsonError(resvo.getResCode(),resvo.getMessage());
        }
    }

    private ChainService getChainService(String chainId,String appId) {
        Weekend<MainChain> weekend = Weekend.of(MainChain.class);
        WeekendCriteria<MainChain, Object> criteria = weekend.weekendCriteria();
        criteria.andEqualTo(MainChain::getChainId, chainId);
        criteria.andEqualTo(MainChain::getAppId, appId);
        MainChain mainChain = mainChainMapper.selectOneByExample(weekend);
        if(null == mainChain || StringUtils.isEmpty(mainChain.getHandleName()) )
            return null;
        ChainService chainService = (ChainService) ApplicationContextHelper.getBean(mainChain.getHandleName());
        return chainService;

    }
    /**
     * 
     * 功能说明:备份
     * @param
     * @return
     */
    private ChainService getChainService(String chainId) {
        Weekend<MainChain> weekend = Weekend.of(MainChain.class);
        WeekendCriteria<MainChain, Object> criteria = weekend.weekendCriteria();
        criteria.andEqualTo(MainChain::getChainId, chainId);
        MainChain mainChain = mainChainMapper.selectOneByExample(weekend);
        if(null == mainChain || StringUtils.isEmpty(mainChain.getHandleName()) )
            return null;
        ChainService chainService = (ChainService) ApplicationContextHelper.getBean(mainChain.getHandleName());
        return chainService;

    }

    public Map<String,Object> allChainDetail(WalletVO vo) {
        List<LinkedHashMap<String,String>>  resList = new ArrayList<>();
        Wallet wallet = new Wallet();
        wallet.setCustId(vo.getCustId());
        List<Wallet> wallets = walletMapper.select(wallet);
        ExecutorService exec = Executors.newFixedThreadPool(wallets.size());
        List<Callable<LBallanceVO>> tasks = new ArrayList<>();
        for (int i=0; i< wallets.size(); i++) {
            workThread work = new workThread(wallets.get(i));
            tasks.add(work);
        }
        List<Future<LBallanceVO>> results = null;
        try {
            results = exec.invokeAll(tasks);
            exec.shutdown();
        } catch (Exception e) {
            logger.error("Thread err :",e);
            return JsonUtil.toJsonError(ERROR_21333);
        } finally {
            if(exec != null)
                exec.shutdown();
        }
        for (int i = 0; i < results.size(); i++){
            LBallanceVO bal = null;
            try {
                bal = results.get(i).get();
            } catch (Exception e) {
                logger.error("Thread err :",e);
                return JsonUtil.toJsonError(ERROR_21333);
            }


            Iterator<Object> coinIter = bal.getDataList().iterator();
            SubChain chain = new SubChain();
            chain.setChainId(bal.getChainId());

            //add
            MainChain mainChain = mainChainMapper.getMainChainById(bal.getChainId());
            List<SubChain> subCoin = null;
            for(Wallet temp : wallets) {
                if(temp.getChainId().equals(mainChain.getChainId())) {
                    subCoin = walletCoinMapper.getOwnCoinByWalletId(temp.getWalletId(),vo.getAppId());
                    break;
                }
            }
            //List<SubChain> subCoin = subChainMapper.select(chain);
            Map<String, SubChain> subMap = new HashMap<>();

            for (SubChain tempBean : subCoin) {
                subMap.put(tempBean.getCurrency(), tempBean);
            }
            while (coinIter.hasNext()) {
                LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) coinIter.next();
                if (null != map) {
                    String currency = map.get("currency");
                    if (null != currency && subMap.containsKey(currency)) {
                        SubChain tempBean = subMap.get(currency);
                        map.put("remark", tempBean.getCoinRemark());
                        map.put("img", tempBean.getCoinImg());
                        map.put("cname", tempBean.getCoinName());
                        map.put("taddress",StringUtils.isEmpty(tempBean.getTokenAddress()) ? "" : tempBean.getTokenAddress());
                        map.put("mainName",mainChain.getChainName());
                        resList.add(map);
                        subMap.remove(currency);
                    } else {
                        coinIter.remove();
                    }
                } else {
                    coinIter.remove();
                }
            }
            if(bal.getClassName().contains("SwtChainService")) {
                for (String key : subMap.keySet()) {
                    LinkedHashMap<String,String> tempMap = new LinkedHashMap<>();
                    tempMap.put("value","0");
                    tempMap.put("currency",subMap.get(key).getCoinName());
                    tempMap.put("issuer",subMap.get(key).getTokenAddress());
                    tempMap.put("remark",subMap.get(key).getCoinRemark());
                    tempMap.put("img",subMap.get(key).getCoinImg());
                    tempMap.put("cname", subMap.get(key).getCoinName());
                    tempMap.put("mainName",mainChain.getChainName());
                    resList.add(tempMap);
                }
            }

            /*
            MainChain mainChain = mainChainMapper.selectByPrimaryKey(bal.getChainId());
            List<SubChain> subCoin = subChainMapper.select(chain);
            Map<String,SubChain> subMap = new HashMap<>();
            for(SubChain tempBean : subCoin){
                subMap.put(tempBean.getCurrency(),tempBean);
            }
            while (coinIter.hasNext()) {
                LinkedHashMap<String,String> map = (LinkedHashMap<String,String>)coinIter.next();
                if(null != map) {
                    String currency = map.get("currency");
                    if (null != currency && subMap.containsKey(currency)) {
                        SubChain tempBean = subMap.get(currency);
                        map.put("remark", tempBean.getCoinRemark());
                        map.put("img", tempBean.getCoinImg());
                        map.put("cname", tempBean.getCoinName());
                        map.put("mainName",mainChain.getChainName());
                        resList.add(map);
                    } else {
                        coinIter.remove();
                    }
                } else {
                    coinIter.remove();
                }
            }
            */
        }

        return  JsonUtil.toJsonSuccess("查询成功",resList);
    }
    /**
     * 
     * 功能说明:查看链下所有的币
     * @param vo
     * @return
     */
    public Map<String,Object> allcoin(WalletVO vo) {
        Wallet wallet = getCheckOutWallet(vo.getCustId(),vo.getWalletId(),vo.getAppId());
        if (wallet == null) {
            return JsonUtil.toJsonError(ERROR_21331);
        }
        BalanceVO bal = new BalanceVO();
        bal.setAddress(wallet.getPublicKey());
        bal.setChainId(wallet.getChainId());
        List<Map> allcoin = walletCoinMapper.getAllCoinByWalletId(wallet.getWalletId(),wallet.getChainId(),wallet.getAppId());
        LWalletCoin res = new LWalletCoin();
        res.setDataList(allcoin);
        return JsonUtil.toJsonSuccess("查询成功",res.getDataList());

    }

    private class workThread implements Callable<LBallanceVO> {
        private Wallet wallet;
        public workThread(Wallet wallet) {
            this.wallet = wallet;
        }
        @Override
        public LBallanceVO call() throws Exception {
            /*
            ChainService service = getChainService(wallet.getChainId());
            BalanceVO bal = new BalanceVO();
            bal.setAddress(wallet.getPublicKey());
            bal.setChainId(wallet.getChainId());
            return service.getBallance(bal);
            */


            ChainService service = getChainService(wallet.getChainId(),wallet.getAppId());
            BalanceVO bal = new BalanceVO();
            bal.setAddress(wallet.getPublicKey());
            bal.setChainId(wallet.getChainId());
            List<SubChain> subChains = walletCoinMapper.getOwnCoinByWalletId(wallet.getWalletId(),wallet.getAppId());

            for (SubChain subChain : subChains) {
                if(!StringUtils.isEmpty(subChain.getTokenAddress())) {
                    bal.getTokens().add(subChain.getTokenAddress());
                }
            }
            bal.setAppId(wallet.getAppId());
            LBallanceVO res = service.getBallance(bal);
            res.setClassName(service.getClass().getSimpleName());
            return res;

        }
    }

    public Wallet getCheckOutWallet(String custId,String walletId,String appId) {
        Weekend<Wallet> weekend = Weekend.of(Wallet.class);
        WeekendCriteria<Wallet, Object> criteria = weekend.weekendCriteria();
        criteria.andEqualTo(Wallet::getCustId,custId);
        criteria.andEqualTo(Wallet::getWalletId,walletId);
        criteria.andEqualTo(Wallet::getAppId,appId);
        return walletMapper.selectOneByExample(weekend);
    }
    /**
     * 
     * 功能说明:备份
     * @param custId
     * @param walletId
     * @return
     */
    public Wallet getCheckOutWallet(String custId,String walletId) {
        Weekend<Wallet> weekend = Weekend.of(Wallet.class);
        WeekendCriteria<Wallet, Object> criteria = weekend.weekendCriteria();
        criteria.andEqualTo(Wallet::getCustId,custId);
        criteria.andEqualTo(Wallet::getWalletId,walletId);
        return walletMapper.selectOneByExample(weekend);
    }

    @Transactional
    public Map<String,Object> deleteWallet(WalletVO vo) {

        Wallet wallet = getCheckOutWallet(vo.getCustId(),vo.getWalletId(),vo.getAppId());
        if(null == wallet){
            return JsonUtil.toJsonError(ERROR_21331);
        }
        ChainService service = getChainService(wallet.getChainId(),vo.getAppId());
        vo.setChainId(wallet.getChainId());
        vo.setCustId(wallet.getCustId());
        vo.setSecret(wallet.getPayPasswd());
        LWalletVO resvo = service.exportWallet(vo);
        if (resvo.getResCode().equals(0)) {
            walletMapper.deleteByPrimaryKey(wallet.getId());
            walletMapper.insertHistory(wallet);
            walletMapper.deleteWalletCoin(vo.getWalletId());

            return JsonUtil.toJsonSuccess("删除成功", null);
        } else {
            return JsonUtil.toJsonError(resvo.getResCode(),resvo.getMessage());
        }
    }

    @Transactional
    public Map<String,Object> updatecoin(WalletCoinVO vo) {
        Wallet wallet = getCheckOutWallet(vo.getCustId(),vo.getWalletId());
        if(null == wallet){
            return JsonUtil.toJsonError(ERROR_21331);
        }
        if(vo.getIssave().equals("1")){
            Weekend<WalletCoin> weekend = Weekend.of(WalletCoin.class);
            WeekendCriteria<WalletCoin, Object> criteria = weekend.weekendCriteria();
            criteria.andEqualTo(WalletCoin::getCoinId,vo.getCoinId());
            criteria.andEqualTo(WalletCoin::getCustId,vo.getCustId());
            if(walletCoinMapper.selectCountByExample(weekend)>0){
                return JsonUtil.toJsonSuccess("更新成功", null);
            }

            WalletCoin coin = new WalletCoin();
            coin.setCoinId(vo.getCoinId());
            coin.setCreateTime(DateUtils.getDateTime());
            coin.setIsShow(true);
            coin.setWalletCoinId(UuidUtil.getUUID());
            coin.setCustId(vo.getCustId());
            coin.setWalletId(vo.getWalletId());
            coin.setAppId(vo.getAppId());
            walletCoinMapper.insertSelective(coin);
        }else{
            Weekend<WalletCoin> weekend = Weekend.of(WalletCoin.class);
            WeekendCriteria<WalletCoin, Object> criteria = weekend.weekendCriteria();
            criteria.andEqualTo(WalletCoin::getCoinId,vo.getCoinId());
            criteria.andEqualTo(WalletCoin::getCustId,vo.getCustId());
            criteria.andEqualTo(WalletCoin::getAppId,vo.getAppId());
            walletCoinMapper.deleteByExample(weekend);
        }
        return JsonUtil.toJsonSuccess("更新成功", null);
    }

    public Map<String,Object> getGasPrice(WalletVO vo) {
        Wallet wallet = getCheckOutWallet(vo.getCustId(),vo.getWalletId());
        if(null == wallet){
            return JsonUtil.toJsonError(ERROR_21331);
        }
        vo.setChainId(wallet.getChainId());
        ChainService service = getChainService(wallet.getChainId(),vo.getAppId());
        LBallanceVO res = service.getPasPrice(vo);
        if (res.getResCode().equals(0)) {
            return JsonUtil.toJsonSuccess("查询成功",res.getDataMap());
        } else {
            return JsonUtil.toJsonError(res.getResCode(),res.getMessage());
        }
    }

    public Map<String,Object> getTX(PaymentVO vo) {
        Wallet wallet = getCheckOutWallet(vo.getCustId(),vo.getWalletId());
        if(null == wallet){
            return JsonUtil.toJsonError(ERROR_21331);
        }
        LPayResVO resVO = new LPayResVO();
        resVO.setChainId(wallet.getChainId());
        resVO.setTxHash(vo.getPayHash());
        ChainService service = getChainService(wallet.getChainId(),vo.getAppId());
        service.getTx(resVO);
        if (resVO.getResCode().equals(0)) {
            return JsonUtil.toJsonSuccess("查询成功",resVO.getResMap());
        } else {
            return JsonUtil.toJsonError(resVO.getResCode(),resVO.getMessage());
        }
    }

    private void defaultWalletCoin(Wallet wallet) {
        //自动创建可见币
        SubChain subChain = new SubChain();
        subChain.setChainId(wallet.getChainId());
        subChain.setAppId(wallet.getAppId());
        subChain.setBaseChain(true);
        List<SubChain> subChains = subChainMapper.select(subChain);
        for(SubChain temp : subChains) {
            WalletCoin walletCoin = new WalletCoin();
            walletCoin.setCustId(wallet.getCustId());
            walletCoin.setWalletId(wallet.getWalletId());
            walletCoin.setIsShow(false);
            walletCoin.setWalletCoinId(UuidUtil.getUUID());
            walletCoin.setCoinId(temp.getCoinId());
            walletCoin.setAppId(wallet.getAppId());
            walletCoinMapper.insert(walletCoin);
        }
    }
}
