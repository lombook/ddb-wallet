package com.jinglitong.wallet.server.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.wallet.api.model.*;
import com.jinglitong.wallet.server.mapper.*;
import com.jinglitong.wallet.api.model.logic.LWalletVO;
import com.jinglitong.wallet.api.model.logic.ReleaseCurrencyVo;
import com.jinglitong.wallet.api.model.view.WalletVO;
import com.jinglitong.wallet.api.model.walletVo.BatchFinishVo;
import com.jinglitong.wallet.api.model.walletVo.FRDatilRuleVO;
import com.jinglitong.wallet.api.model.walletVo.FrozenReleaseVO;
import com.jinglitong.wallet.server.util.DateUtils;
import com.jinglitong.wallet.server.util.JsonUtil;
import com.jinglitong.wallet.server.util.UuidUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.jinglitong.wallet.server.common.ErrorEnum.ERROR_35101;
import static com.jinglitong.wallet.server.common.ErrorEnum.ERROR_35103;

@Service
@Transactional
public class FrozenReleaseService {

    private static final String password = "123456";

    @Autowired
    private SwtChainService swtChainService;

    @Autowired
    private WalletMapper walletMapper;

    @Autowired
    private FrozenReleaseRuleMapper frozenReleaseRuleMapper;

    @Autowired
    private FrozenReleaseDetailRuleMapper frozenReleaseDetailRuleMapper;

    @Autowired
    private FrozenReleaseExcelSourceMapper frozenReleaseExcelSourceMapper;

    @Autowired
    private FrozenReleaseExcelSourceAmountMapper frozenReleaseExcelSourceReleaseAmountMapper;

    @Autowired
    private FrozenReleaseLogsMapper frozenReleaseLogsMapper;

    @Autowired
    private CustomerMapper customerMapper;

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private final int wei = 1000000;


    public Map<String,String> prossesExcel(List<List<Object>> excelData, FrozenReleaseVO vo) {
        String uuid = UuidUtil.getUUID();
        LWalletVO walletVO = checkWallet(uuid,vo.getSecret(),vo.getChainId());
        if(walletVO.getResCode() != 0) {
            return JsonUtil.toJsonError(ERROR_35101);
        }
        //创建总规则
        FrozenReleaseRule rule = new FrozenReleaseRule();
        String nowstr = DateUtils.getDateTime();
        rule.setRuleId(uuid);
        rule.setChianId(vo.getChainId());
        rule.setCoinId(vo.getCoinId());
        rule.setCreateTime(nowstr);
        rule.setUpdateTime(nowstr);
        rule.setFrStatus(0);
        rule.setFrType(Integer.parseInt(vo.getFrType()));
        rule.setMom(vo.getMom());
        rule.setPayPasswd(walletVO.getPayPasswd());
        rule.setPublicKey(walletVO.getPublicAddress());
        rule.setAppId(vo.getAppId());
        rule.setRuleName(vo.getRuleName());
        frozenReleaseRuleMapper.insert(rule);

        if(rule.getFrType().intValue() == 0) {
            String piciId = UuidUtil.getUUID();
            String percent = vo.getDataList().get(0).getPercent();
            String avgDays = vo.getDataList().get(0).getAvgDay();
            FrozenReleaseDetailRule detailRule = new FrozenReleaseDetailRule();
            detailRule.setDetailId(piciId);
            detailRule.setRuleId(uuid);
            detailRule.setExecutTime(vo.getDataList().get(0).getStartDate());
            detailRule.setAvgDays(Integer.parseInt(avgDays));
            detailRule.setCreateTime(nowstr);
            detailRule.setDetailStatus(0);
            Float proportion = Float.parseFloat(percent)*wei;
            detailRule.setProportion(proportion.intValue());
            detailRule.setLeftProportion(wei-proportion.intValue());
            detailRule.setUpdateTime(nowstr);
            detailRule.setAppId(vo.getAppId());
            frozenReleaseDetailRuleMapper.insert(detailRule);
        } else {
            int totle = wei;
            for(FRDatilRuleVO temp : vo.getDataList()) {
                String piciId = UuidUtil.getUUID();
                String percent = temp.getPercent();
                String excuteDate = temp.getStartDate();
                FrozenReleaseDetailRule detailRule = new FrozenReleaseDetailRule();
                detailRule.setDetailId(piciId);
                detailRule.setRuleId(uuid);
                detailRule.setCreateTime(nowstr);
                detailRule.setDetailStatus(0);
                detailRule.setExecutTime(excuteDate);
                Float proportion = Float.parseFloat(percent)*wei;
                detailRule.setProportion(proportion.intValue());
                detailRule.setLeftProportion(totle = totle - proportion.intValue());
                detailRule.setUpdateTime(nowstr);
                detailRule.setAppId(vo.getAppId());
                frozenReleaseDetailRuleMapper.insert(detailRule);
            }
        }

        Map<String,String> errorMap = new HashMap<String,String>();
        List<Map<String,String>> errList = new ArrayList<>();
        List<FrozenReleaseExcelSource> sourceList = new ArrayList<>();
        List<FrozenReleaseExcelSourceAmount> sourceAmountList = new ArrayList<>();
        List<String> accountList = new ArrayList<>();
        for(List<Object> linelist : excelData) {
            String account = null;
            String amount = null;
            try {
                account = linelist.get(0).toString();
                amount = linelist.get(1).toString();
            } catch (Exception e) {
                errorMap = new HashMap<>();
                errorMap.put("account",account);
                errorMap.put("amount",amount);
                errorMap.put("err","excel中有空数据");
                errList.add(errorMap);
                continue;
            }
            logger.info("checkSql:start"+System.currentTimeMillis());
            Wallet wallet = checkUser(vo.getChainId(),account,vo.getAppId());
            logger.info("checkSql:end"+System.currentTimeMillis());
            if(accountList.contains(account)){
                errorMap = new HashMap<>();
                errorMap.put("account",account);
                errorMap.put("amount",amount);
                errorMap.put("err","账户重复");
                errList.add(errorMap);
                continue;
            }
            if(StringUtils.isEmpty(account) || StringUtils.isEmpty(amount)) {
                errorMap = new HashMap<>();
                errorMap.put("account",account);
                errorMap.put("amount",amount);
                errorMap.put("err","数据为空");
                errList.add(errorMap);
                continue;
            }
            if(!isInteger(amount)) {
                errorMap = new HashMap<>();
                errorMap.put("account",account);
                errorMap.put("amount",amount);
                errorMap.put("err","钱数不是整数");
                errList.add(errorMap);
                continue;
            }
            accountList.add(account);
            if(null == wallet){
                errorMap = new HashMap<>();
                errorMap.put("account",account);
                errorMap.put("amount",amount);
                errorMap.put("err","钱包不存在");
                errList.add(errorMap);
            } else {
                FrozenReleaseExcelSource sourceBean = new FrozenReleaseExcelSource();
                String souuceId = UuidUtil.getUUID();
                sourceBean.setSourceId(souuceId);
                sourceBean.setAmount(amount);
                sourceBean.setAccount(account);
                sourceBean.setAddress(wallet.getPublicKey());
                sourceBean.setChainId(wallet.getChainId());
                sourceBean.setCoinId(vo.getCoinId());
                sourceBean.setCreateTime(nowstr);
                sourceBean.setCustomerId(wallet.getCustId());
                sourceBean.setRuleId(rule.getRuleId());
                sourceBean.setImportTime(nowstr);
                sourceBean.setWalletId(wallet.getWalletId());
                sourceBean.setSourceStatus(0);
                sourceBean.setUpdateTime(nowstr);
                sourceBean.setAppId(vo.getAppId());
                sourceList.add(sourceBean);

                FrozenReleaseExcelSourceAmount sourceAmountBean = new FrozenReleaseExcelSourceAmount();
                sourceAmountBean.setId(souuceId);
                sourceAmountBean.setSourceId(souuceId);
                sourceAmountBean.setAmount(amount);
                sourceAmountBean.setCreateTime(nowstr);
                sourceAmountBean.setUpdateTime(nowstr);
                sourceAmountBean.setAppId(vo.getAppId());
                sourceAmountList.add(sourceAmountBean);

            }
        }

        if(errList.size() != 0) {
        	FrozenReleaseRule r = new FrozenReleaseRule();
        	r.setRuleId(rule.getRuleId());
            frozenReleaseRuleMapper.delete(r);
            FrozenReleaseDetailRule detailRule = new FrozenReleaseDetailRule();
            detailRule.setRuleId(rule.getRuleId());
            frozenReleaseDetailRuleMapper.delete(detailRule);
            return JsonUtil.toJsonError(ERROR_35103,errList);
        } else {
            int step = sourceList.size()/2000;
            List<FrozenReleaseExcelSource> excBatch = null;
            List<FrozenReleaseExcelSourceAmount> excBatchA = null;
            for (int i=0;i<step;i++){
                excBatch = sourceList.subList(i*2000, (i+1)*2000);
                frozenReleaseExcelSourceMapper.insertBatch(excBatch);
                excBatchA = sourceAmountList.subList(i*2000, (i+1)*2000);
                frozenReleaseExcelSourceReleaseAmountMapper.insertBatch(excBatchA);
            }
            if(sourceList.size()%2000>0) {
                excBatch = sourceList.subList(step * 2000, sourceList.size());
                frozenReleaseExcelSourceMapper.insertBatch(excBatch);
                excBatchA = sourceAmountList.subList(step * 2000, sourceAmountList.size());
                logger.info("insertSql:start"+System.currentTimeMillis());
                frozenReleaseExcelSourceReleaseAmountMapper.insertBatch(excBatchA);
                logger.info("insertSql:end"+System.currentTimeMillis());
            }
        }
        return JsonUtil.toJsonSuccess("保存成功");
    }

    private LWalletVO checkWallet(String uuid, String secret, String chainId) {
        WalletVO vo = new WalletVO();
        vo.setChainId(chainId);
        vo.setCustId(uuid);
        vo.setPasswd(password);
        vo.setSecret(secret);
        LWalletVO res = swtChainService.importWallet(vo);
        return res;
    }

    private Wallet checkUser(String chainId, String account,String appId) {
        List<Wallet> wallets = walletMapper.getFRByAccount(account, chainId,appId);
        if(wallets.size() == 0){
            return null;
        }
        return wallets.get(0);
    }

    public Map ruleList(FrozenReleaseVO pageVO) {
        if (pageVO.getPage() != null && pageVO.getRows() != null) {
            PageHelper.startPage(pageVO.getPage(), pageVO.getRows());
        }
        List<Map> ruleList = frozenReleaseRuleMapper.getFRList(pageVO);
        PageInfo pageinfo = new PageInfo(ruleList);
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageCount", pageinfo.getTotal());
        map.put("data", ruleList);
        return map;
    }

    public FrozenReleaseRule getById(FrozenReleaseVO vo) {
        return frozenReleaseRuleMapper.getById(vo);
    }


    @Transactional
    public Map update(FrozenReleaseVO vo) {
       
        FrozenReleaseRule rule = new FrozenReleaseRule();
        if(!StringUtils.isEmpty(vo.getRuleName())) {
        	rule.setRuleName(vo.getRuleName());
        }
        rule.setId(vo.getId());
        if(!StringUtils.isEmpty(vo.getSecret())) {
        	LWalletVO walletVO = checkWallet(vo.getRuleId(),vo.getSecret(),vo.getChainId());
            if(walletVO.getResCode() != 0) {
                return JsonUtil.toJsonError(ERROR_35101);
            }
            rule.setPayPasswd(walletVO.getPayPasswd());
            rule.setPublicKey(walletVO.getPublicAddress());
        }
        
        frozenReleaseRuleMapper.updateByPrimaryKeySelective(rule);
        
        FrozenReleaseDetailRule rozenReleaseDetailRule = new FrozenReleaseDetailRule();
        rozenReleaseDetailRule.setRuleId(vo.getRuleId());
        frozenReleaseDetailRuleMapper.delete(rozenReleaseDetailRule);
        
        String nowstr = DateUtils.getDateTime();
        if(vo.getFrType().equals("0")) {
            String piciId = UuidUtil.getUUID();
            String percent = vo.getDataList().get(0).getPercent();
            String avgDays = vo.getDataList().get(0).getAvgDay();
            FrozenReleaseDetailRule detailRule = new FrozenReleaseDetailRule();
            detailRule.setDetailId(piciId);
            detailRule.setRuleId(vo.getRuleId());
            detailRule.setExecutTime(vo.getDataList().get(0).getStartDate());
            detailRule.setAvgDays(Integer.parseInt(avgDays));
            detailRule.setCreateTime(nowstr);
            detailRule.setDetailStatus(0);
            Float proportion = Float.parseFloat(percent)*wei;
            detailRule.setProportion(proportion.intValue());
            detailRule.setLeftProportion(wei-proportion.intValue());
            detailRule.setUpdateTime(nowstr);
            detailRule.setAppId(vo.getAppId());
            frozenReleaseDetailRuleMapper.insert(detailRule);
        } else {
            int totle = wei;
            for(FRDatilRuleVO temp : vo.getDataList()) {
                String piciId = UuidUtil.getUUID();
                String percent = temp.getPercent();
                String excuteDate = temp.getStartDate();
                FrozenReleaseDetailRule detailRule = new FrozenReleaseDetailRule();
                detailRule.setDetailId(piciId);
                detailRule.setRuleId(vo.getRuleId());
                detailRule.setCreateTime(nowstr);
                detailRule.setDetailStatus(0);
                detailRule.setExecutTime(excuteDate);
                Float proportion = Float.parseFloat(percent)*wei;
                detailRule.setProportion(proportion.intValue());
                detailRule.setLeftProportion(totle = totle - proportion.intValue());
                detailRule.setUpdateTime(nowstr);
                detailRule.setAppId(vo.getAppId());
                frozenReleaseDetailRuleMapper.insert(detailRule);
            }
        }
        return JsonUtil.toJsonSuccess("更新成功");
    }

    public Map<String,Object> selectByRuleID(FrozenReleaseVO vo) {
        HashMap<String, Object> map = new HashMap<>();
        if (vo.getPage() != null && vo.getRows() != null) {
            PageHelper.startPage(vo.getPage(), vo.getRows());
        }
       List<FrozenReleaseExcelSource> data = frozenReleaseExcelSourceMapper.selectByRuleId(vo);
       for (int i = 0; i < data.size(); i++) {
    	   BigDecimal num = new BigDecimal(data.get(i).getAmount());
    	   BigDecimal num2 = new BigDecimal(data.get(i).getRelea());
    	   BigDecimal result = num.subtract(num2);
    	   data.get(i).setRest(result+"");
       }
        PageInfo pageinfo = new PageInfo(data);
        map.put("data",data);
        map.put("pageCount",pageinfo.getTotal());
        return map;
    }

    public Map<String,Object> batchFinish(BatchFinishVo vo) {
        Boolean flag =true;
        for (String ss :vo.getSourceIdList()) {
            FrozenReleaseExcelSource frozenReleaseExcelSource = new FrozenReleaseExcelSource();
            frozenReleaseExcelSource.setId(Integer.valueOf(ss));
            frozenReleaseExcelSource.setSourceStatus(3);
            int i = frozenReleaseExcelSourceMapper.updateByPrimaryKeySelective(frozenReleaseExcelSource);
            if(i<=0)
                flag = false;
            else{
                //int i1 = frozenReleaseExcelSourceReleaseAmountMapper.deleteByPrimaryKey(ss);
                //if(i1 <= 0)
                  //  flag =false;
            }
        }
        HashMap<String, Object> map = new HashMap<>();
        if(flag)
            map.put("data",true);
        else
            map.put("data",false);
        return map;
    }

    public Map<String,Object> getReleaseAmount(ReleaseCurrencyVo vo) {
        Customer customer =customerMapper.selectByUsername(vo.getAccount(),vo.getAppId());
        List<ReleaseCurrencyVo> data =new ArrayList<>();
        data = frozenReleaseRuleMapper.selectBy(customer.getCustId(),vo.getWalletId(),vo.getAppId());
        List<ReleaseCurrencyVo> da = new ArrayList<>();
        for (ReleaseCurrencyVo rs:data) {
           Boolean flag = true;
           if(da.size() != 0){
               for (ReleaseCurrencyVo ta:da) {
                   if(ta.getCurrency().equals(rs.getCurrency())){
                       BigDecimal bigDecimal = new BigDecimal(ta.getSum());
                       BigDecimal bigDecimal1 = new BigDecimal(rs.getSum());
                       BigDecimal add = bigDecimal1.add(bigDecimal);
                       ta.setSum(add.toPlainString());
                       flag =false;
                   }
               }
               if(flag){
                   ReleaseCurrencyVo releaseCurrencyVo = new ReleaseCurrencyVo();
                   releaseCurrencyVo.setCurrency(rs.getCurrency());
                   releaseCurrencyVo.setSum(rs.getSum());
                   da.add(releaseCurrencyVo);
               }
           }else {
               ReleaseCurrencyVo releaseCurrencyVo = new ReleaseCurrencyVo();
               releaseCurrencyVo.setCurrency(rs.getCurrency());
               releaseCurrencyVo.setSum(rs.getSum());
               da.add(releaseCurrencyVo);
           }

        }
        HashMap<String, Object> map = new HashMap<>();
        if(data ==null||data.size() == 0)
            map.put("data",da);
        else
        map.put("data",da);
        return map;
    }

    //查询指定规则指定钱包释放成功总金额
   /* private String selectReleaseMoney(String walletId, String ruleId){
        BigDecimal amount = new BigDecimal("0");
        List<FrozenReleaseLogs> list = frozenReleaseLogsMapper.selectByWRid(walletId,ruleId);
        if(list!= null || list.size() > 0)
            for (FrozenReleaseLogs f:list) {
                amount = amount.add(new BigDecimal(f.getSendAmount()));
            }
        return amount.toString();
    }*/

    private static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
    
    
  //根据标题查询释放规则
    public Map qryRule(FrozenReleaseVO vo) {
        if (vo.getPage() != null && vo.getRows() != null) {
            PageHelper.startPage(vo.getPage(), vo.getRows());
        }
        List<Map> ruleList = frozenReleaseRuleMapper.qryRule(vo.getRuleName(),vo.getAppId());
        PageInfo pageinfo = new PageInfo(ruleList);
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageCount", pageinfo.getTotal());
        map.put("data", ruleList);
        return map;
    }
}
