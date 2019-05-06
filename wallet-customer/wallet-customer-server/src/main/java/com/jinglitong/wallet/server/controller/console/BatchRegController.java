package com.jinglitong.wallet.server.controller.console;

import com.jinglitong.wallet.api.model.walletVo.*;
import com.jinglitong.wallet.server.common.BatchTransfer.BatchTransferConstants;
import com.jinglitong.wallet.api.model.view.CustRegVO;
import com.jinglitong.wallet.api.model.view.WalletVO;
import com.jinglitong.wallet.server.service.BatchRegService;
import com.jinglitong.wallet.server.service.JingtongWalletService;
import com.jinglitong.wallet.server.util.DateUtils;
import com.kvn.poi.exp.PoiExporter;
import com.kvn.poi.imp.PoiImporter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 批量注册
 */
@Controller
@RequestMapping("batchReg")
public class BatchRegController extends BaseBatchController{
    /**
     * 默认中国 编码 1
     */
    private static final String COUNTRY_CODE_CHINA = "1";

    /**
     * 为了设置交易密码 不做严格认证
     */
    private static final int IDCARD_MIN_LEN = 6;

    @Resource
    private BatchRegService batchRegService;


    @Resource
    JingtongWalletService jingtongWalletService;

    /**
     * 批量注册excel
     * @param file
     * @return
     */
    @RequestMapping(value = "/regExcel.json", method = RequestMethod.POST)
    public ResponseEntity uploadExcel(@RequestParam("chainId")String chainId,@RequestParam("file") MultipartFile file) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);



        OutputStream outputStream = new ByteArrayOutputStream();
        Map<String,Object> map = new HashMap<>();
        List<BatchRegExtVo> batchRegExtVos = new ArrayList<>();
        try{
            String fileName = new String(BatchTransferConstants.DEFAULT_REG_BACK_FILE_NAME.getBytes("UTF-8"),"iso-8859-1");
            headers.setContentDispositionFormData("attachment", fileName);
            List<List<Object>> lists = PoiImporter.importFirstSheetFrom(file.getInputStream()).getContent();
            int seq = 1;
            for(List<Object> regInfo:lists) {
                if(seq ==1 &&( !((String)regInfo.get(0)).equals("手机号")|| !((String)regInfo.get(1)).equals("身份证号")|| !((String)regInfo.get(2)).equals("钱包名称")|| !((String)regInfo.get(3)).equals("邀请码") )){
                    throw new RuntimeException(BatchTransferConstants.TEMPLATE_ERROR);
                }
                seq++;
                if (null == regInfo || regInfo.size()==0||  ((String) regInfo.get(0)).indexOf("手机号")>-1) {
                    continue;
                }

                BatchRegExtVo regExtVo = new BatchRegExtVo();
                regExtVo.setPhone((String)regInfo.get(0));
                regExtVo.setIdCard(((String)regInfo.get(1)).trim());
                regExtVo.setWalletName((String)regInfo.get(2));
                regExtVo.setInviteCode((String)regInfo.get(3));
                String checkStatus = checkRegInfo(regExtVo);
                if(!"ok".equals(checkStatus)){
                    regExtVo.setRemark(checkStatus);
                    batchRegExtVos.add(regExtVo);
                    continue;
                }

                CustRegVO custRegVO = new CustRegVO();
                custRegVO.setAccount(regExtVo.getPhone());//手机号
                custRegVO.setCountryCode(COUNTRY_CODE_CHINA);
                custRegVO.setPasswd(BatchTransferConstants.DEFAULT_LOGIN_PASSWORD);//密码
                custRegVO.setInviteCode((String)regInfo.get(3));//邀请码

                WalletVO walletVO = new WalletVO();
                walletVO.setChainId(chainId);  //链id
                walletVO.setPasswd(StringUtils.truncate(regExtVo.getIdCard(),regExtVo.getIdCard().length()-IDCARD_MIN_LEN,IDCARD_MIN_LEN) ); //支付密码
                walletVO.setWalletName(regExtVo.getWalletName());
                try{
                    int m = batchRegService.regAndCreateWallet(custRegVO,walletVO);
                    if(m>0){
                        regExtVo.setRemark("注册成功！");
                    }else{
                        regExtVo.setRemark("钱包生成错误");
                    }
                }catch (Exception e){
                    regExtVo.setRemark("钱包生成错误");
                }


                batchRegExtVos.add(regExtVo);
            }

        }catch (Exception e){
            e.printStackTrace();
            BatchRegExtVo regExtVo = new BatchRegExtVo();
            regExtVo.setPhone(BatchTransferConstants.TEMPLATE_ERROR);
            batchRegExtVos.add(regExtVo);
//            return new ResponseEntity<>("excel格式非法", HttpStatus.OK);
        }
        map.put("list",batchRegExtVos);
        InputStream is = BatchRegController.class.getClassLoader().getResourceAsStream("excelTemplate/batch-reg-export.xlsx");
        PoiExporter.export2Destination(is, map, outputStream);
        return new ResponseEntity<byte[]>(((ByteArrayOutputStream) outputStream).toByteArray(),
                headers, HttpStatus.CREATED);

    }


    /**
     * 批量注册excel
     * @param file
     * @return
     */
    @RequestMapping(value = "/userInfo.json", method = RequestMethod.POST)
    public ResponseEntity queryUserInfo(@RequestParam("file") MultipartFile file) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);



        OutputStream outputStream = new ByteArrayOutputStream();
        Map<String,Object> map = new HashMap<>();
        List<JingtongUserInfo> userInfos = new ArrayList<>();
        try{
            String fileName = new String(BatchTransferConstants.DEFAULT_USER_INFO_BACK_FILE_NAME.getBytes("UTF-8"),"iso-8859-1");
            headers.setContentDispositionFormData("attachment", fileName);
            List<List<Object>> lists = PoiImporter.importFirstSheetFrom(file.getInputStream()).getContent();
            int seq = 1;
            for(List<Object> regInfo:lists) {
                if(seq ==1 &&( !((String)regInfo.get(0)).equals("姓名")|| !((String)regInfo.get(1)).equals("手机号")|| !((String)regInfo.get(2)).equals("地址")|| !((String)regInfo.get(3)).equals("备注") )){
                    throw new RuntimeException(BatchTransferConstants.TEMPLATE_ERROR);
                }
                seq++;
                if (null == regInfo || regInfo.size()==0||  ((String) regInfo.get(0)).indexOf("姓名")>-1) {
                    continue;
                }

                JingtongUserInfo userInfo = new JingtongUserInfo();
                userInfo.setUserName((String)regInfo.get(0));
                userInfo.setPhone(((String)regInfo.get(1)).trim());
                userInfo.setAddress((String)regInfo.get(2));
                userInfo.setRemark((String)regInfo.get(3));
                if(StringUtils.isEmpty(userInfo.getAddress())){
                    continue;
                 }


                //获得swt  cnt 总金额 余额
                BalancesResponse balancesResponse = jingtongWalletService.getBalancesOfWallet(BatchTransferConstants.JINGTONG_OFFICIAL_API_URL,userInfo.getAddress());
                for(WalletBalance walletBalance : balancesResponse.getBalances()){
                    BigDecimal canUse ;
                    if(walletBalance.getCurrency().equals(BatchTransferConstants.JINGTONG_CURRENCY_SWT)){
                        userInfo.setSwtTotal(walletBalance.getValue());//总额
                        userInfo.setSwtFreeze(walletBalance.getFreezed());
                        canUse =  new BigDecimal(walletBalance.getValue()).subtract(new BigDecimal(walletBalance.getFreezed()));
                        userInfo.setSwtCanUse(canUse.toString());
                    }
                    if(walletBalance.getCurrency().equals(BatchTransferConstants.JINGTONG_CURRENCY_CNT)){
                        userInfo.setCntTotal(walletBalance.getValue());//总额
                        userInfo.setCntFreeze(walletBalance.getFreezed());
                        canUse =  new BigDecimal(walletBalance.getValue()).subtract(new BigDecimal(walletBalance.getFreezed()));
                        userInfo.setCntCanUse(canUse.toString());
                    }
                }

                JingtongTransaction jingtongTransaction =  jingtongWalletService.getFirstJingtongTransaction(BatchTransferConstants.JINGTONG_OFFICIAL_API_URL,userInfo.getAddress());
                if(null != jingtongTransaction.getTransactions() && jingtongTransaction.getTransactions().size()>0){
                    userInfo.setRegTime(DateUtils.formatDateTime(Long.parseLong(jingtongTransaction.getTransactions().get(0).getDate())));
                }

                userInfos.add(userInfo);
            }

        }catch (Exception e){
            e.printStackTrace();
            JingtongUserInfo userInfo = new JingtongUserInfo();
            userInfo.setUserName(BatchTransferConstants.TEMPLATE_ERROR);
            userInfos.add(userInfo);
            return new ResponseEntity<>(BatchTransferConstants.TEMPLATE_ERROR, HttpStatus.OK);
        }
        map.put("list",userInfos);
        InputStream is = BatchRegController.class.getClassLoader().getResourceAsStream("excelTemplate/batch-userinfo.xlsx");
        PoiExporter.export2Destination(is, map, outputStream);
        return new ResponseEntity<byte[]>(((ByteArrayOutputStream) outputStream).toByteArray(),
                headers, HttpStatus.CREATED);

    }



    /**
     * 检查注册信息
     * @param regExtVo
     * @return
     */
    private String checkRegInfo(BatchRegExtVo regExtVo){
        String phoneStatus = super.checkPhone(regExtVo.getPhone());
        if(!phoneStatus.equals("ok")){
            return phoneStatus;
        }
        String idCard = regExtVo.getIdCard();
        if(StringUtils.isEmpty(idCard)||idCard.length()<IDCARD_MIN_LEN){
            return "身份证号不正确!";
        }

        String walletName = regExtVo.getWalletName();
        if(StringUtils.isEmpty(regExtVo.getWalletName())){
            regExtVo.setWalletName(walletName);
            return "钱包不能为空!";
        }


        return "ok";
    }



}
