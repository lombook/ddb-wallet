package com.jinglitong.wallet.server.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.wallet.server.mapper.LockCoinRecordMapper;
import com.jinglitong.wallet.server.mapper.LockCoinRuleMapper;
import com.jinglitong.wallet.server.util.UuidUtil;
import com.jinglitong.wallet.api.model.LockCoinRecord;
import com.jinglitong.wallet.api.model.LockCoinRule;
import com.jinglitong.wallet.api.model.logic.LBallanceVO;
import com.jinglitong.wallet.api.model.logic.LockCoinRecordCustomer;
import com.jinglitong.wallet.api.model.logic.UnLockVo;
import com.jinglitong.wallet.api.model.view.BalanceVO;
import com.jinglitong.wallet.api.model.view.LockCoinRecordVo;
import com.jinglitong.wallet.api.model.view.LockCoinToltalVo;
import com.jinglitong.wallet.api.model.view.LockSelCoinRecordVO;
import com.jinglitong.wallet.server.util.DateUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

@Service
@Transactional
public class LockCoinRecordService {

	private Logger logger = LoggerFactory.getLogger(LockCoinRecordService.class);
    @Resource
    private LockCoinRecordMapper lockCoinRecordMapper;

    @Resource
    private LockCoinRuleMapper lockCoinRuleMapper;
    
    @Autowired
    private SwtChainService swtChainService;
    /**
     * 查看锁仓记录
     * @param lockCoinRecordVo
     * @return
     */
    public HashMap<String, Object> getLockCoinRecords(LockCoinRecordVo lockCoinRecordVo) {
        HashMap<String, Object> selectmap = new HashMap<>();
        if (lockCoinRecordVo.getPage() != null && lockCoinRecordVo.getRows() != null) {
            PageHelper.startPage(lockCoinRecordVo.getPage(), lockCoinRecordVo.getRows());
        }

        Weekend<LockCoinRecord> weekend = Weekend.of(LockCoinRecord.class);
        WeekendCriteria<LockCoinRecord, Object> criteria = weekend.weekendCriteria();
        if(null != lockCoinRecordVo.getCustId()){
            criteria.andEqualTo(LockCoinRecord::getCustId,lockCoinRecordVo.getCustId());
        }
        weekend.setOrderByClause(" create_time desc ");


        List<LockCoinRecord> records = lockCoinRecordMapper.selectByExample(weekend);
        PageInfo pageinfo = new PageInfo(records);
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageCount",pageinfo.getTotal());
        map.put("records",records);
        return  map;
    }

    /**
     * 添加锁仓记录
     * @param record
     * @return
     */
    public Integer addLockCoinRecord(LockCoinRecord record){
        record.setLockRecordId( UuidUtil.getUUID());
        return lockCoinRecordMapper.insert(record);
    }

    /**
     * 删除锁仓记录
     * @param record
     * @return
     */
    public Integer deleteLockCoinRecord(LockCoinRecord record){
        return lockCoinRecordMapper.delete(record);
    }

    /**
     * 修改锁仓记录
     * @param record
     * @return
     */
    public Integer updateLockCoinRecord(LockCoinRecord record){
        return lockCoinRecordMapper.updateByPrimaryKey(record);
    }

    /**
     *查询总是
     * @param record
     * @return
     */
    public Integer queryLockCoinCount(LockCoinRecord record){
        return lockCoinRecordMapper.selectCount(record);
    }


    /**
     * 查询记录
     * @param record
     * @return
     */
    public List<LockCoinRecord> queryLockCoinRecords(LockCoinRecord record,int pageNum,int pageSize){
        return lockCoinRecordMapper.selectByRowBounds(record,new RowBounds(pageNum,pageSize));
    }

    public HashMap<String,Object> recordListByChainCoin(LockSelCoinRecordVO coinRecordVo) {
        if (coinRecordVo.getPage() != null && coinRecordVo.getRows() != null) {
            PageHelper.startPage(coinRecordVo.getPage(), coinRecordVo.getRows());
        }
        List<LockCoinRecordCustomer> data = lockCoinRecordMapper.recordListByChainCoin(coinRecordVo);
        PageInfo pageinfo = new PageInfo(data);
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageCount", pageinfo.getTotal());
        map.put("data", data);
        return map;
    }
    /**
     * 
     * 功能说明:锁仓页面-到期用户合计信息
     * @return
     */
    public HashMap<String,Object> recordToltal(LockCoinToltalVo vo){
    	if (vo.getPage() != null && vo.getRows() != null) {
             PageHelper.startPage(vo.getPage(), vo.getRows());
         }
    	String endDate = DateUtils.getDateTime();
    	vo.setEndDate(endDate);
    	List<LockCoinToltalVo> data =lockCoinRecordMapper.recordToltal(vo);//"2018-7-12 01:00:00"
    	Map<String,String> ids = new HashMap<String, String>();
		for (LockCoinToltalVo v : data) {
			ids.put("chainId", v.getChainId());
			ids.put("coinId", v.getCoinId());
			ids.put("appId", vo.getAppId());
			List<LockCoinRule> list = lockCoinRuleMapper.selectById(ids);// 一条链一个币，一个总账
			if (list.size() > 0) {
				v.setAddress(list.get(0).getAddress());
				// 查询余额
				BalanceVO bo = new BalanceVO();
				bo.setAddress(list.get(0).getAddress());
				bo.setChainId(list.get(0).getChainId());
				bo.setCustId(list.get(0).getCustId());
				LBallanceVO res = swtChainService.getBallance(bo);
				if (res.getResCode().equals(0)) {
					List<Object> dataList = res.getDataList();
					for (int i = 0; i < dataList.size(); i++) {
						JSONObject js = JSONObject.fromObject(dataList.get(i));
						if(v.getCoinName().equals((String)js.get("currency"))) {
							if(null != js.get("value") && js.get("value") != "") {
								v.setBalance((String) js.get("value"));
							}
							break;
						}
					}
					if(null ==v.getBalance() || v.getBalance().trim() =="") {
						v.setBalance("0");
					}
					
				}
			}
		}
    	PageInfo pageinfo = new PageInfo(data);
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageCount", pageinfo.getTotal());
        map.put("data", data);
    	return map;
    }
    
    /**
     * 
     * 功能说明:锁仓页面-到期用户合计信息-合计信息用户详情
     * @param vo
     * @return
     */
    public HashMap<String,Object>  recordToltalDetail(LockCoinToltalVo vo){
    	if (vo.getPage() != null && vo.getRows() != null) {
            PageHelper.startPage(vo.getPage(), vo.getRows());
        }
    	String endDate = DateUtils.getDateTime();
    	vo.setEndDate(endDate);
    	List<LockCoinRecordCustomer> data =lockCoinRecordMapper.recordToltalDetail(vo);
    	PageInfo pageinfo = new PageInfo(data);
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageCount", pageinfo.getTotal());
        map.put("data", data);
        return map;
    }
    /**
     * 
     * 功能说明:获取已到期的个数
     * @return
     */
    public int countByDate(){
    	String lockEndDate =DateUtils.getDateTime();
    	return lockCoinRecordMapper.countByDate(lockEndDate);//"2018-07-08 00:00:00"
    }
    /**
     * 
     * 功能说明:查询需要解仓的用户信息
     * @return
     */
    public List<UnLockVo> selectByDate (){
    	String lockEndDate =DateUtils.getDateTime();
    	List<UnLockVo> list = lockCoinRecordMapper.selectByDate(lockEndDate);
    	return list;
    }
    /**
     * 根据id更新解仓操作的状态
     * 功能说明:
     * @param record
     * @return
     */
    public int updateStatusById(LockCoinRecord record){
    	return lockCoinRecordMapper.updateStatusById(record);
    }
   
    
}
