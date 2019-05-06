package com.jinglitong.wallet.server.controller.customer;

import com.jinglitong.wallet.api.model.*;
import com.jinglitong.wallet.api.model.view.*;
import com.jinglitong.wallet.server.common.ErrorEnum;
import com.jinglitong.wallet.server.service.*;
import com.jinglitong.wallet.server.service.*;
import com.jinglitong.wallet.api.model.*;
import com.jinglitong.wallet.api.model.logic.ReleaseCurrencyVo;
import com.jinglitong.wallet.server.util.DateUtils;
import com.jinglitong.wallet.server.util.JsonUtil;
import com.jinglitong.wallet.server.util.SessionUtil;
import java.math.BigDecimal;
import java.util.*;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer/api")
public class CustomerAPIController extends CusBaseController{
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CustomerAPIService apiService;

    @Autowired
    private WalletService walletService;

    @Resource
    private LockCoinRecordService recordService;

    @Resource
    private LockCoinRuleService ruleService;


    @Resource
    private LockCoinRuleService lockCoinRuleService;

    @Resource
    private LockCoinRecordService lockCoinRecordService;

    @Resource
    private MainChainService mainChainService;

    @Resource
    private SubChainService subChainService;

    @Resource
    private PayInfoService payInfoService;

    @Resource
    private SellerService sellerService;

    @Autowired
    private FrozenReleaseService frozenReleaseService;

    /**
     *利率分母
     */
    private Integer lilvFenmu = 10000;

    /**
     * 支付多少天后 计算钱财
     */
    private Integer startComputeLilv = 2;


    @ResponseBody
    @RequestMapping(value = "/balances.json", method = RequestMethod.POST)
    public Map balances(@RequestBody BalanceVO vo) {
        Customer customer = (Customer)SessionUtil.getUserInfo();
        vo.setCustId(customer.getCustId());
        return apiService.balances(vo);
    }
    /**
     * 
     * 功能说明:创建钱包
     * @param vo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/createwallet.json", method = RequestMethod.POST)
    public Map createwallet(@RequestBody WalletVO vo) {
    	setGlobalCustomAppId(vo);
        Customer customer = (Customer)SessionUtil.getUserInfo();
        vo.setCustId(customer.getCustId());
        if (walletService.countByChainId(customer.getCustId(),vo.getChainId(),vo.getAppId())>0) {
            return JsonUtil.toJsonError(ErrorEnum.ERROR_21302);
        } else {
            return apiService.createWallet(vo);
        }
    }
    /**
     * 
     * 功能说明:导入钱包
     * @param vo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/importwallet.json", method = RequestMethod.POST)
    public Map importwallet(@RequestBody WalletVO vo) {
    	setGlobalCustomAppId(vo);
        Customer customer = (Customer)SessionUtil.getUserInfo();
        vo.setCustId(customer.getCustId());
        if (walletService.countByChainId(customer.getCustId(),vo.getChainId(),vo.getAppId())>0) {
            return JsonUtil.toJsonError(ErrorEnum.ERROR_21322);
        } else {
            return apiService.importWallet(vo);
        }

    }
    /**
     * 
     * 功能说明:转出
     * @param vo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/payment.json", method = RequestMethod.POST)
    public Map payment(@RequestBody PaymentVO vo) {
    	setGlobalCustomAppId(vo);
        Customer customer = (Customer)SessionUtil.getUserInfo();
        vo.setCustId(customer.getCustId());
        return apiService.payment(vo);
    }

    /**
     * 锁仓
     * @param vo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/lockCoin.json", method = RequestMethod.POST)
    public Map payment(@RequestBody LockCoinVo vo) {
    	setGlobalCustomAppId(vo);
        Customer customer = (Customer)SessionUtil.getUserInfo();
        vo.setCustId(customer.getCustId());
        LockCoinRule rule = new LockCoinRule();
        rule.setLockRuleId(vo.getLockRuleId());
        rule = ruleService.getLockCoinRule(rule);
        if(null == rule && StringUtils.isEmpty(rule.getChainId())){
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31455);
        }
        vo.setDestAddress(rule.getLockAddress());
        Map<String,Object> map = apiService.payment(vo);
        if((int)map.get("code")==0){//提交成功
            LockCoinRecord record = new LockCoinRecord();
            record.setCoinId(rule.getCoinId());
            record.setCustId(vo.getCustId());
            record.setCreateTime(DateUtils.getDateTime());
            record.setLockAmount(vo.getAmount());
            record.setLockDays(rule.getLockDays());
            record.setLockRate(rule.getLockRate());
            record.setLockRoueId(rule.getLockRuleId());
            record.setLockStatus(1); //正在锁
            record.setPayHash((String)map.get("data")); //hash

            Wallet wallet = apiService.getCheckOutWallet(vo.getCustId(),vo.getWalletId(),vo.getAppId());
            record.setWalletId(wallet.getWalletId());
            record.setWalletAddress(wallet.getPublicKey());
            record.setPayTime(DateUtils.getDateTime());
            // 总数×利率除以lilvFenmu = 奖励
            BigDecimal rewardAmount = new BigDecimal(vo.getAmount()).multiply(BigDecimal.valueOf(rule.getLockRate())).divide(BigDecimal.valueOf(lilvFenmu));
            record.setRewardAmount(rewardAmount.toString());

            record.setLockStartDate(DateUtils.addDays(new Date(),startComputeLilv));

            record.setLockEndDate(DateUtils.addDays(new Date(),startComputeLilv+rule.getLockDays()));
            record.setAppId(vo.getAppId());
            recordService.addLockCoinRecord(record);
        }
        return map;
    }



    /**
     * 获取理财列表
     * @param lockCoinRuleVo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/rules.json", method = RequestMethod.POST)
    public Map getLockCoinRules(@RequestBody(required = false) LockCoinRuleVo lockCoinRuleVo) {
    	setGlobalCustomAppId(lockCoinRuleVo);
        lockCoinRuleVo.setRuleStatus(true);
        HashMap<String, Object> map = lockCoinRuleService.getLockCoinRules(lockCoinRuleVo);
        List<LockCoinRule> lockCoinRules = new ArrayList<>();
        for(LockCoinRule rule:(List<LockCoinRule>)map.get("rules")){
            MainChain mainChain = new MainChain();
            mainChain.setChainId(rule.getChainId());
            mainChain.setAppId(lockCoinRuleVo.getAppId());
            mainChain = mainChainService.getOneMainChain(mainChain);
            rule.setChainId(mainChain.getChainId());
            rule.setChainName(mainChain.getChainName());
            SubChain subChain = subChainService.selectByCoinId(rule.getCoinId());
            rule.setCoinId(subChain.getCoinId());
            rule.setCurrency(subChain.getCurrency());
            rule.setIssuer(subChain.getTokenAddress());
            rule.setCoinName(subChain.getCoinName());
            lockCoinRules.add(rule);
        }
        map.put("rules",lockCoinRules);
        return JsonUtil.toJsonSuccess("获取列表", map);
    }


    /**
     * 获取投资记录列表
     * @param coinRecordVo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/records.json", method = RequestMethod.POST)
    public Map getLockCoinRecords(@RequestBody LockCoinRecordVo coinRecordVo) {
    	setGlobalCustomAppId(coinRecordVo);
        Customer customer = (Customer) SessionUtil.getUserInfo();
        if(null == customer){
            return JsonUtil.toJsonError(ErrorEnum.ERROR_21011);
        }
        coinRecordVo.setCustId(customer.getCustId());
        HashMap<String, Object> map = lockCoinRecordService.getLockCoinRecords(coinRecordVo);
        List<LockCoinRecord> records = new ArrayList<>();
        for(LockCoinRecord record:(List<LockCoinRecord>)map.get("records")){
            SubChain subChain = subChainService.selectByCoinId(record.getCoinId());
            record.setCoinName(subChain.getCoinName());
            MainChain mainChain = new MainChain();
            mainChain.setChainId(subChain.getChainId());
            mainChain = mainChainService.getOneMainChain(mainChain);
            record.setChainName(mainChain.getChainName());
            records.add(record);
        }
        map.put("records",records);
        return JsonUtil.toJsonSuccess("获取列表", map);
    }

    /**
     * 用户支付列表 分页信息
     * @return
     */
    @RequestMapping("payInfos.json")
    public Map<String,Object> getPayInfosByPage(@RequestBody PayInfoVo payInfoVo){
        //验证用户
        Customer customer = (Customer) SessionUtil.getUserInfo();
        if(null == customer || StringUtils.isEmpty(customer.getCustId()))
        {
            return JsonUtil.toJsonError(ErrorEnum.ERROR_21005);
        }
        payInfoVo.setCustId(customer.getCustId());//当前用户
        Map<String, Object> stringObjectMap = payInfoService.queryPayInoByPage(payInfoVo);
        return JsonUtil.toJsonSuccess("查询成功",stringObjectMap);
    }



    /**
     * 获取订单信息
     * @return
     */
    @PostMapping ("/orderInfo/{orderNo}.json")
    public Map<String,Object> getOrderInfo(@PathVariable("orderNo") String orderNo){
        //验证用户
        Customer customer = (Customer) SessionUtil.getUserInfo();
        if(null ==customer || StringUtils.isEmpty(customer.getCustId()))
        {
            return JsonUtil.toJsonError(ErrorEnum.ERROR_21005);
        }

        //订单是否存在
        PayInfo info = new PayInfo();
        info.setOrderNo(orderNo);
        info = payInfoService.queryOnePayInfo(info);
        if(null ==info || StringUtils.isEmpty(info.getSellerOrderNo()) || null == info.getSellerId())
        {
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31451);
        }

        return JsonUtil.toJsonSuccess("订单信息",info);
    }


    /**
     * 支付订单
     * @param payCoinVo
     * @return
     */
    @RequestMapping(value = "payOrder.json",method = RequestMethod.POST)
    public Map<String,Object> payOrder(@RequestBody PayCoinVo payCoinVo){
        //验证用户
        Customer customer = (Customer) SessionUtil.getUserInfo();
        if(null ==customer || StringUtils.isEmpty(customer.getCustId()))
        {
            return JsonUtil.toJsonError(ErrorEnum.ERROR_21005);
        }
        //订单不存在
        if(StringUtils.isEmpty(payCoinVo.getOrderNo())){
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31451);
        }
        //钱包不存在
        if(StringUtils.isEmpty(payCoinVo.getWalletId())){
            return JsonUtil.toJsonError(ErrorEnum.ERROR_21331);
        }
        //订单是否存在
        PayInfo info =  payInfoService.queryPayInfoCanPay(payCoinVo.getOrderNo());
        if(null ==info || StringUtils.isEmpty(info.getSellerOrderNo()) || null == info.getSellerId())
        {
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31451);
        }
        payCoinVo.setIssuer(info.getIssuer());
        //金额不匹配
        if(info.getAmount().compareTo(new BigDecimal(payCoinVo.getAmount())) != 0){
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31453);
        }
        PaymentVO paymentVO = new PaymentVO();
        paymentVO.setCustId(customer.getCustId());
        paymentVO.setWalletId(payCoinVo.getWalletId());
        paymentVO.setPasswd(payCoinVo.getPasswd());
        return payInfoService.sendPayInfo(info,paymentVO);
    }


    /**
     * 获取订单信息
     * @return
     */
    @PostMapping ("/openWallet/{appId}")
    public Map<String,Object> openWallet(@PathVariable("appId") String appId){
        //验证用户
        Customer customer = (Customer) SessionUtil.getUserInfo();
        if(null ==customer || StringUtils.isEmpty(customer.getCustId()))
        {
            return JsonUtil.toJsonError(ErrorEnum.ERROR_21005);
        }

        //订单是否存在
        Seller info = new Seller();
        info.setAppAccessInfo(appId);
        info = sellerService.queryOneSeller(info);
        if(null ==info || StringUtils.isEmpty(info.getAccessKey()) || null == info.getId())
        {
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31448);
        }

        return JsonUtil.toJsonSuccess("打开钱包",true);
    }

    /**
     * 
     * 功能说明:交易记录
     * @param vo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getpayments.json", method = RequestMethod.POST)
    public Map getPayments(@RequestBody PaymentVO vo) {
    	setGlobalCustomAppId(vo);
        Customer customer = (Customer)SessionUtil.getUserInfo();
        vo.setCustId(customer.getCustId());
        return apiService.getPayments(vo);
    }

    /**
     * 
     * 功能说明:查看用户钱包
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/userwallets.json", method = RequestMethod.POST)
    public Map userwallets() {
        Customer customer = (Customer)SessionUtil.getUserInfo();
        logger.info(customer.getAppId()+"");
        logger.info(customer+"");
        return JsonUtil.toJsonSuccess("查询完成" ,walletService.userwallets(customer.getCustId()));
    }

    /**
     * 
     * 功能说明:查看钱包币余额
     * @param vo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/chaindetail.json", method = RequestMethod.POST)
    public Map chaindetail(@RequestBody WalletVO vo) {
    	setGlobalCustomAppId(vo);
        Customer customer = (Customer)SessionUtil.getUserInfo();
        vo.setCustId(customer.getCustId());
        return apiService.chaindetail(vo);
    }
    /**
     * 
     * 功能说明:资产总览  
     * @param vo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/allchaindetail.json", method = RequestMethod.POST)
    public Map allChainDetail(@RequestBody WalletVO vo) {
    	setGlobalCustomAppId(vo);
        Customer customer = (Customer)SessionUtil.getUserInfo();
        vo.setCustId(customer.getCustId());
        return apiService.allChainDetail(vo);
    }


    @ResponseBody
    @RequestMapping(value = "/moifpasswd.json", method = RequestMethod.POST)
    public Map moifpasswd(@RequestBody WalletVO vo) {
    	setGlobalCustomAppId(vo);
        Customer customer = (Customer)SessionUtil.getUserInfo();
        vo.setCustId(customer.getCustId());
        return apiService.moifpasswd(vo);
    }

    @ResponseBody
    @RequestMapping(value = "/forgetpasswd.json", method = RequestMethod.POST)
    public Map forgetpasswd(@RequestBody WalletVO vo) {
    	setGlobalCustomAppId(vo);
        Customer customer = (Customer)SessionUtil.getUserInfo();
        vo.setCustId(customer.getCustId());
        return apiService.forgetpasswd(vo);
    }
    /**
     * 
     * 功能说明:导出钱包
     * @param vo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/exportwallet.json", method = RequestMethod.POST)
    public Map exportwallet(@RequestBody WalletVO vo) {
    	setGlobalCustomAppId(vo);
        Customer customer = (Customer)SessionUtil.getUserInfo();
        vo.setCustId(customer.getCustId());
        try {
			return apiService.exportwallet(vo);
		} catch (Exception e) {
			String message=e.getMessage();
			logger.error("REG CUSTER ERROR:",e);
			if(ErrorEnum.ERROR_21003.getCode().toString().equals(message)) {
                return JsonUtil.toJsonError( ErrorEnum.ERROR_21003);
        	}else if(ErrorEnum.ERROR_21002.getCode().toString().equals(message)) {
                return JsonUtil.toJsonError( ErrorEnum.ERROR_21002);
        	}
			return JsonUtil.toJsonError( ErrorEnum.ERROR_21381);
		}
    }
    /**
     * 
     * 功能说明:查看链下所有的币
     * @param vo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/allcoin.json", method = RequestMethod.POST)
    public Map balances(@RequestBody WalletVO vo) {
    	setGlobalCustomAppId(vo);
        Customer customer = (Customer)SessionUtil.getUserInfo();
        vo.setCustId(customer.getCustId());
        return apiService.allcoin(vo);
    }
    /**
     * 
     * 功能说明:删除钱包
     * @param vo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteWallet.json", method = RequestMethod.POST)
    public Map deleteWallet(@RequestBody WalletVO vo) {
    	setGlobalCustomAppId(vo);
        Customer customer = (Customer)SessionUtil.getUserInfo();
        vo.setCustId(customer.getCustId());
        return apiService.deleteWallet(vo);
    }

    /**
     * 
     * 功能说明:给钱包添加币
     * @param vo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updatecoin.json", method = RequestMethod.POST)
    public Map updatecoin(@RequestBody WalletCoinVO vo) {
    	setGlobalCustomAppId(vo);
        Customer customer = (Customer)SessionUtil.getUserInfo();
        vo.setCustId(customer.getCustId());
        return apiService.updatecoin(vo);
    }

    @ResponseBody
    @RequestMapping(value = "/gasprice.json", method = RequestMethod.POST)
    public Map gasPrice(@RequestBody WalletVO vo) {
    	setGlobalCustomAppId(vo);
        Customer customer = (Customer)SessionUtil.getUserInfo();
        vo.setCustId(customer.getCustId());
        return apiService.getGasPrice(vo);
    }


    /**
     *查询用户冻结余额
     * @param vo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/fz/freeAmount.json", method = RequestMethod.POST)
    public Map freeAmount(@RequestBody ReleaseCurrencyVo vo)  {
        Map<String,Object> map = frozenReleaseService.getReleaseAmount(vo);
        return JsonUtil.toJsonSuccess("查询成功",map);
    }

}
