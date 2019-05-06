package com.jinglitong.wallet.server.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.wallet.server.common.BatchTransfer.BatchTransferConstants;
import com.jinglitong.wallet.server.common.ErrorEnum;
import com.jinglitong.wallet.server.mapper.PayInfoMapper;
import com.jinglitong.wallet.api.model.PayInfo;
import com.jinglitong.wallet.api.model.Wallet;
import com.jinglitong.wallet.api.model.view.PayInfoVo;
import com.jinglitong.wallet.api.model.view.PaymentVO;
import com.jinglitong.wallet.server.util.JsonUtil;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PayInfoService {

    @Resource
    private PayInfoMapper payInfoMapper;

    @Resource
    private CustomerAPIService customerAPIService;

    /**
     * 商家信息
     * @param PayInfoVo
     * @return
     */
    public Map<String,Object> queryPayInoByPage(PayInfoVo PayInfoVo){
        if (PayInfoVo.getPage() != null && PayInfoVo.getRows() != null) {
            PageHelper.startPage(PayInfoVo.getPage(), PayInfoVo.getRows());
        }
        List<PayInfo> payInfos = payInfoMapper.select(PayInfoVo);
        PageInfo pageinfo = new PageInfo(payInfos);
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageCount",pageinfo.getTotal());
        map.put("payInfos",payInfos);
        return  map;
    }


    /**
     * 新增支付信息
     * @param payInfo
     * @return
     */
    public boolean addPayInfo(PayInfo payInfo){
        return payInfoMapper.insertSelective(payInfo)>0;
    }


    /**
     * 修改支付信息
     * @param payInfo
     * @return
     */
    public boolean updatePayInfo(PayInfo payInfo){
        return payInfoMapper.updateByPrimaryKey(payInfo)>0;
    }

    /**
     * 查询支付信息
     * @param payInfo
     * @return
     */
    public PayInfo queryOnePayInfo(PayInfo payInfo){
        return payInfoMapper.selectOne(payInfo);
    }


    /**
     *查询可以支付的订单
     * @return
     */
    public PayInfo queryPayInfoCanPay(String orderNo) {
        Weekend<PayInfo> weekend = Weekend.of(PayInfo.class);
        WeekendCriteria<PayInfo, Object> criteria = weekend.weekendCriteria();
        criteria.andEqualTo("orderNo",orderNo);
        criteria.andBetween("payStatus",0,1);
        return payInfoMapper.selectOneByExample(weekend);
    }

    public Map<String,Object> sendPayInfo(PayInfo payInfo,PaymentVO paymentVO){
        paymentVO.setAmount(payInfo.getAmount().toString());
        paymentVO.setCurrency(payInfo.getCoinType());
        paymentVO.setDestAddress(payInfo.getReceiveAddress());
        //钱包信息
        Wallet wallet = customerAPIService.getCheckOutWallet(paymentVO.getCustId(),paymentVO.getWalletId());
        paymentVO.setAppId(wallet.getAppId());
        Map<String,Object> map = customerAPIService.payment(paymentVO);
        if (wallet == null) {
            return JsonUtil.toJsonError(ErrorEnum.ERROR_21331);
        }
        if((int)map.get("code")==0){
            payInfo.setPayStatus(1);//申请支付成功
            payInfo.setPayAddress(wallet.getPublicKey());
            payInfo.setTradeHash((String)map.get("data")); //设置hash
            payInfo.setCustId(paymentVO.getCustId());
            payInfo.setWalletId(wallet.getWalletId());
            
            Weekend<PayInfo>weekend = Weekend.of(PayInfo.class);
        	WeekendCriteria<PayInfo,Object> Criteria =weekend.weekendCriteria();
        	Criteria.andEqualTo(PayInfo::getOrderNo, payInfo.getOrderNo());
            payInfoMapper.updateByExampleSelective(payInfo, weekend);
            
            return JsonUtil.toJsonSuccess("支付请求已经发送");
        }else {
            payInfo.setPayStatus(2);//申请支付失败
            payInfo.setPayAddress(wallet.getPublicKey());
            payInfo.setTradeHash((String)map.get("data")); //设置hash
            
            Weekend<PayInfo>weekend = Weekend.of(PayInfo.class);
        	WeekendCriteria<PayInfo,Object> Criteria =weekend.weekendCriteria();
        	Criteria.andEqualTo(PayInfo::getOrderNo, payInfo.getOrderNo());
            payInfoMapper.updateByExampleSelective(payInfo, weekend);
            
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31462);
        }

    }


    /**
     * 返回支付信息条数
     * @param payInfo
     * @return
     */
    public int getPayInfoCount(PayInfo payInfo){
        return payInfoMapper.selectCount(payInfo);
    }

    /**
     * 分页查询 支付信息
     * @param payInfo
     * @param pageSize
     * @param pageNum
     * @return
     */
    public List<PayInfo>  getPayInfoByPage(PayInfo payInfo,int pageNum,int pageSize){
        if(pageNum<0){
            pageNum = 0;
        }
        return payInfoMapper.selectByRowBounds(payInfo,new RowBounds(pageNum,pageSize));
    }


    /**
     * 查询以后支付结果的订单
     * @param pageSize
     * @param pageNum
     * @return
     */
    public List<PayInfo>  getHavedPayInfoByExample(int pageNum,int pageSize){
        if(pageNum<0){
            pageNum = 0;
        }
        Example example = new Example(PayInfo.class);
        //通知没有成功 或 通知没有到指定次数 支付有结果的记录
        example.createCriteria().andCondition("( notice_status <> 1 and notice_time <="+ BatchTransferConstants.NOTIFY_PAY_SELLER_TIMES+" ) and (pay_status = 3 or pay_status = 4) ");
        return payInfoMapper.selectByExampleAndRowBounds(example,new RowBounds(pageNum,pageSize));
    }




    /**
     * 查询以后支付结果的订单
     * @return
     */
    public int  getHavedPayCountInfoByExample(){

        Example example = new Example(PayInfo.class);
        example.createCriteria().andCondition("( notice_status <> 1 and notice_time <="+ BatchTransferConstants.NOTIFY_PAY_SELLER_TIMES+" ) and (pay_status = 3 or pay_status = 4) ");
        return payInfoMapper.selectCountByExample(example);
    }

    public HashMap queryPayInfos(PayInfoVo orderNo) {
        HashMap map = new HashMap();
        List<PayInfo> payInfos = payInfoMapper.selectByApp(orderNo);
        map.put("data",payInfos);
        map.put("pageCount",payInfos.size());
        return map;
    }
}
