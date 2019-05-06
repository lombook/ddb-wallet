package com.jinglitong.wallet.ddbserver.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.ddbapi.model.DdbCustIntegralRecord;
import com.jinglitong.wallet.ddbapi.model.DdbIntegralWallet;
import com.jinglitong.wallet.ddbapi.model.DdbOrder;
import com.jinglitong.wallet.ddbapi.model.view.DdbCustOrderTreeVO;
import com.jinglitong.wallet.ddbserver.mapper.DdbCustIntegralRecordMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbIntegralWalletMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @author fyy
 * @create 2018-12-26-11:26}
 * 定单中的树的处理
 */
@Service
public class OrderTreeService {


    @Autowired
    private DdbIntegralWalletMapper ddbIntegralWalletMapper;

    @Autowired
    private DdbCustIntegralRecordMapper ddbCustIntegralRecordMapper;

    @Autowired
    private DdbOrderMapper ddbOrderMapper;

    public ArrayList<DdbIntegralWallet> userOrderTree(Customer customer) {
        ArrayList<DdbIntegralWallet> arrayList = new ArrayList<>();
        HashSet<String> treeCode = new HashSet<>();
        treeCode.add("oyt_all");
        treeCode.add("osyt_all");
        treeCode.add("tyt_all");
        treeCode.add("tryt_all");
        treeCode.add("fyt_all");
        treeCode.add("fiyt_all");
        List<DdbIntegralWallet> ddbIntegralWallets = ddbIntegralWalletMapper.selectByCustId(customer.getCustId());
        for(DdbIntegralWallet ddbIntegralWallet:ddbIntegralWallets){
            if(treeCode.contains(ddbIntegralWallet.getInteName())){
                ddbIntegralWallet.setAmount(ddbIntegralWallet.getAmount()/100);
                arrayList.add(ddbIntegralWallet);
            }

        }
        ArrayList<String> treeorders = new ArrayList<>();
        treeorders.add("oyt_all");
        treeorders.add("osyt_all");
        treeorders.add("tyt_all");
        treeorders.add("tryt_all");
        treeorders.add("fyt_all");
        treeorders.add("fiyt_all");
        ArrayList<DdbIntegralWallet> wallets = new ArrayList<>();
        for (String tcode : treeorders) {
            for(DdbIntegralWallet ddbIntegralWallet:arrayList){
                if(tcode.equals(ddbIntegralWallet.getInteName())){
                    wallets.add(ddbIntegralWallet);
                }
            }
        }
        return wallets;
    }

    public PageInfo userOrderTreeDetail(Customer customer, DdbCustOrderTreeVO prodName) {
        if (prodName.getPage() != null && prodName.getRows() != null) {
            PageHelper.startPage(prodName.getPage(), prodName.getRows());
        }
        DdbCustIntegralRecord ddbCustIntegralRecord = new DdbCustIntegralRecord();
        ddbCustIntegralRecord.setIntegName(prodName.getInteName());
        ddbCustIntegralRecord.setCustId(customer.getCustId());
        List<DdbCustIntegralRecord> assetsRecord = ddbCustIntegralRecordMapper.getAssetsRecord(ddbCustIntegralRecord);
        for (DdbCustIntegralRecord ddb : assetsRecord) {
            ddb.setAmount(ddb.getAmount()/100);
            DdbOrder ddbOrder = ddbOrderMapper.selectByflowId(ddb.getFlowId());
            ddb.setFlowId(ddbOrder.getShopTrade());
        }
        PageInfo pageinfo = new PageInfo(assetsRecord);
        return pageinfo;
    }
}
