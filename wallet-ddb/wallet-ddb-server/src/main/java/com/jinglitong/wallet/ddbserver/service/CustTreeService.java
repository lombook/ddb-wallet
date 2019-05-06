package com.jinglitong.wallet.ddbserver.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.wallet.api.model.Customer;
import com.jinglitong.wallet.ddbapi.model.DdbCustOwnTree;
import com.jinglitong.wallet.ddbapi.model.view.DdbCustOwnTreeVO;
import com.jinglitong.wallet.ddbserver.mapper.CustomerMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbCustOwnTreeMapper;
import com.jinglitong.wallet.ddbserver.mapper.DdbShoreholderMapper;
import com.jinglitong.wallet.ddbserver.util.UuidUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 用户树处理
 * auth fyy
 */
@Service
@Slf4j
@Transactional
public class CustTreeService {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private DdbCustOwnTreeMapper ddbCustOwnTreeMapper;

    @Autowired
    private DdbShoreholderMapper ddbShoreholderMapper;

    /**
     * 操作处理用户树
     *
     * @param lists    excel列表
     * @return
     */

    public List<HashMap<String, String>> doCustTreeExcel(List<List<Object>> lists,String appId) {
        Integer count = 0;
        String opertion = null;
        ArrayList<HashMap<String, String>> errorData = new ArrayList<>();
        //遍历excel表格
        for (List<Object> data : lists) {
            //账户
            String account = null;
            //树数量
            String treeNum = null;
            //操作类型
            opertion = null;
            //数值型树数量
            Integer treeNumInteger = null;
            //用户id
            String custId = null;
            //对账号判空错误
            try{
                //账户
                 account = data.get(0).toString();
                //树数量
                 treeNum = data.get(1).toString();
                //操作类型
                opertion = data.get(2).toString();
            }catch (Exception e){
                addErrorData(errorData,"","","","部分数据为空。");
                count++;
                continue;
            }
            if (account == null || "".equals(account)) {
                addErrorData(errorData,account,treeNum,opertion,"账号为空");
                count++;
                continue;
            }else if (treeNum == null || "".equals(treeNum)) {
                //对数量判空错误
                addErrorData(errorData,account,treeNum,opertion,"数量为空");
                count++;
                continue;
            }else if(opertion == null || (!"add".equals(opertion) && !"sub".equals(opertion))){
                addErrorData(errorData,account,treeNum,opertion,"操作类型错误");
                count++;
                continue;
            } else {
                //将数量转化为数字
                try {
                    treeNumInteger = Integer.valueOf(treeNum);
                    int i = new BigDecimal(treeNumInteger.toString()).compareTo(new BigDecimal(treeNum));
                    if(i != 0){
                        addErrorData(errorData,account,treeNum,opertion,"用户树数量格式错误");
                        count++;
                        continue;
                    }
                    if(treeNumInteger <= 0){
                        addErrorData(errorData,account,treeNum,opertion,"用户树数量格式错误");
                        count++;
                        continue;
                    }

                    //验证
                    Customer customer = customerMapper.selectByUsername(account, appId);
                    if(customer == null){
                        addErrorData(errorData,account,treeNum,opertion,"账户："+account+"  未找到该用户");
                        count++;
                        continue;
                    }else {
                        custId = customer.getCustId();
                        if("sub".equals(opertion)){
                            DdbCustOwnTree ownTree = ddbCustOwnTreeMapper.selectByCustId(custId);
                            if(ownTree == null){
                                addErrorData(errorData,account,treeNum,opertion,"用户没有基石种苗，无法执行减去操作");
                                count++;
                                continue;
                            }else {
                               if(ownTree.getTreeNum() < treeNumInteger){
                                   addErrorData(errorData,account,treeNum,opertion,"用户基石种苗数量不足，无法执行减去操作");
                                   count++;
                                   continue;
                               }
                            }
                        }
                    }
                }catch (Exception e){
                    log.info("数据转换错误"+e.toString());
                    addErrorData(errorData,account,treeNum,opertion,"数量不是数字");
                    count++;
                    continue;
                }
            }

        }
        if(count > 0){
            return errorData;
        }

        for (List<Object> data : lists) {
            //账户
            String account = data.get(0).toString();
            //树数量
            String treeNum = data.get(1).toString();
            //操作类型
            opertion = data.get(2).toString();
            //数值型树数量
            Integer treeNumInteger =  Integer.valueOf(treeNum);
            //用户id
            Customer customer = customerMapper.selectByUsername(account, appId);
            String custId = customer.getCustId();
            //对账号判空错误
            //处理正确的数据
            Boolean flag = opertionTreeNum(custId,treeNumInteger,opertion);
        }
        return errorData;
    }

    /**
     * 添加错误数据
     * @param errorData 错误集合
     * @param account 账户
     * @param treeNum 数量
     * @param opertion 操作符
     * @param remark 备注
     */
    private void addErrorData(ArrayList<HashMap<String,String>> errorData, String account, String treeNum, String opertion, String remark) {
        HashMap<String, String> dataOfError = new HashMap<>();
        dataOfError.put("account",account);
        dataOfError.put("treeNum",treeNum);
        dataOfError.put("error",remark);
        errorData.add(dataOfError);
    }

    /**
     * 处理数据
     * @param custId
     * @param treeNumInteger
     * @param opertion
     * @return
     */
    private Boolean opertionTreeNum(String custId, Integer treeNumInteger, String opertion) {
        Date date = new Date();
        DdbCustOwnTree ownTree = ddbCustOwnTreeMapper.selectByCustId(custId);
        //树增加
        if("add".equals(opertion)){
            if(ownTree == null){
                DdbCustOwnTree ddbCustOwnTree = new DdbCustOwnTree();
                ddbCustOwnTree.setCustId(custId);
                ddbCustOwnTree.setTreeNum(treeNumInteger);
                ddbCustOwnTree.setCreateTime(date);
                ddbCustOwnTree.setUpdateTime(date);
                ddbCustOwnTree.setZid(UuidUtil.getUUID());
                ddbCustOwnTreeMapper.insert(ddbCustOwnTree);
                ddbShoreholderMapper.updateBaseStoneByCustId(1,custId);
            }else {
               Integer update = ddbCustOwnTreeMapper.updateTreeNumBycustId(treeNumInteger,custId);
                if(update <= 0){
                    throw new RuntimeException("修改异常");
                }
               ddbShoreholderMapper.updateBaseStoneByCustId(1,custId);
            }
        }else if("sub".equals(opertion)){
            //树减少
            Integer update = ddbCustOwnTreeMapper.updateTreeNumBycustId(0-treeNumInteger,custId);
            if(update <= 0){
                throw new RuntimeException("修改异常");
            }
            if(ownTree.getTreeNum() - treeNumInteger == 0){
                ddbCustOwnTreeMapper.deleteByPrimaryKey(ownTree.getId());
                ddbShoreholderMapper.updateBaseStoneByCustId(0,custId);
            }
        }
        return true;
    }

    public HashMap<String,Object> getCustTree(DdbCustOwnTreeVO treeVo) {
        HashMap<String, Object> resultMap = new HashMap<>();
        if (treeVo.getPage() != null && treeVo.getRows() != null) {
            PageHelper.startPage(treeVo.getPage(), treeVo.getRows());
        }
        ArrayList<DdbCustOwnTreeVO> treeList = ddbCustOwnTreeMapper.selectByAccountList(treeVo);
        PageInfo pageinfo = new PageInfo(treeList);
        resultMap.put("pageCount",pageinfo.getTotal());
        resultMap.put("resultData",treeList);
        return resultMap ;
    }
}
