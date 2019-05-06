package com.jinglitong.wallet.server.service;

import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.api.model.Wallet;
import com.jinglitong.wallet.server.mapper.WalletMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class WalletService {

    @Autowired
    private WalletMapper walletMapper;

    public Integer countByChainId(String custId,String chainId,String appId) {
        Weekend<Wallet> weekend = Weekend.of(Wallet.class);
        WeekendCriteria<Wallet, Object> criteria = weekend.weekendCriteria();
        criteria.andEqualTo(Wallet::getCustId,custId);
        criteria.andEqualTo(Wallet::getChainId,chainId);
        criteria.andEqualTo(Wallet::getAppId,appId);
        return walletMapper.selectCountByExample(weekend);
    }

    public List<Map> userwallets(String custId){
        Customer cus = new Customer();
        cus.setCustId(custId);
        return walletMapper.userwallets(cus);
    }
}
