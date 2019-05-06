package com.jinglitong.wallet.server.service;

import com.jinglitong.wallet.server.mapper.WalletMapper;
import com.jinglitong.wallet.server.util.PasswordUtil;
import com.jinglitong.wallet.server.util.UuidUtil;
import com.jinglitong.wallet.server.mapper.CustomerMapper;
import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.api.model.Wallet;
import com.jinglitong.wallet.api.model.logic.LWalletVO;
import com.jinglitong.wallet.api.model.view.CustRegVO;
import com.jinglitong.wallet.api.model.view.WalletVO;
import com.jinglitong.wallet.server.util.DateUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BatchRegService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("swtChainService")
    private ChainService SwtChainService;

    @Autowired
    private WalletMapper walletMapper;

    @Autowired
    private CustomerMapper customerMapper;



    /**
     * 注册 创建钱包
     * @param walletVO
     * @return
     */
    public Integer regAndCreateWallet(CustRegVO custRegVO,WalletVO walletVO){
        String date = DateUtils.getDate();
        Customer entity = new Customer();
        //一期是使用的手机号注册所以直接用
        entity.setPhone(custRegVO.getAccount());
        entity.setAllPhone("+86"+custRegVO.getAccount());
        entity.setCustId(UuidUtil.getUUID());
        entity.setAccount(custRegVO.getAccount());
        entity.setCreatedTime(date);
        entity.setUpdatedTime(date);
        entity.setOrigin(1);// 来源：后台
        entity.setState(true);
        String salt = new SecureRandomNumberGenerator().nextBytes().toHex();
        entity.setSalt(salt);
        entity.setCountryId(custRegVO.getCountryId());
//        entity.setO
        entity.setInviteCode(custRegVO.getInviteCode());
        String password = PasswordUtil.createCustomPwd(custRegVO.getPasswd(), entity.getSalt());
        entity.setPassword(password);
        entity.setInviteCode(custRegVO.getInviteCode());
        //注册信息
        customerMapper.insertSelective(entity);

        //用户主键
        walletVO.setCustId(entity.getCustId());
        ChainService service = getChainService(walletVO.getChainId());
        if(null == service){
            throw new RuntimeException("钱包注册失败");
        }
        String uudi = UuidUtil.getUUID();
        LWalletVO res = service.createWallet(walletVO);
        if (res != null && res.getResCode().equals(0)){
            //生成新钱包
            String now = DateUtils.getDateTime();
            Wallet wallet = new Wallet();
            wallet.setCustId(walletVO.getCustId());
            wallet.setChainId(walletVO.getChainId());
            wallet.setCreatedTime(now);
            wallet.setUpdatedTime(now);
            wallet.setWalletId(uudi);
            wallet.setPayPasswd(res.getPayPasswd());
            wallet.setPublicKey(res.getPublicAddress());
            wallet.setPrompt(walletVO.getPrompt());
            wallet.setWalletName(walletVO.getWalletName());

            if (walletMapper.insertSelective(wallet) > 0){
                return 1;
            }else{
                throw new RuntimeException("钱包注册失败");
            }

        } else {
            throw new RuntimeException("钱包注册失败");
        }

    }


    private ChainService getChainService(String chainType) {
        if(chainType.equals("swt") || chainType.equals("test")) {
            return SwtChainService;
        }
        return  null;
    }
}
