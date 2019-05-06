package com.jinglitong.wallet.server.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jinglitong.wallet.server.util.DateUtils;
import com.jinglitong.wallet.server.util.SessionUtil;
import com.jinglitong.wallet.server.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.wallet.server.mapper.AppNoticeMapper;
import com.jinglitong.wallet.server.mapper.FeedbackMapper;
import com.jinglitong.wallet.server.mapper.FeedbackSubMapper;
import com.jinglitong.wallet.api.model.Admin;
import com.jinglitong.wallet.api.model.AppNotice;
import com.jinglitong.wallet.api.model.FeedBackSub;
import com.jinglitong.wallet.api.model.Feedback;
import com.jinglitong.wallet.api.model.view.FeedbackVo;
import org.springframework.transaction.annotation.Transactional;

import static com.jinglitong.wallet.server.common.FeedbackTypeEnum.*;


@Service
@Transactional
public class FeedbackService {

	@Autowired
	FeedbackMapper feedbackMapper;
	@Autowired
	FeedbackSubMapper feedbackSubMapper;

	@Autowired
    private AppNoticeMapper appNoticeMapper;
	
	public int addFeedback(FeedbackVo vo) {
		
		Feedback fb=new Feedback();
		fb.setFeedbackid(UuidUtil.getUUID());
		fb.setCustId(vo.getCustId());
		fb.setContent(vo.getContent());
		fb.setCreateTime(DateUtils.getDateTime());
		fb.setImgs(vo.getImgs());
		fb.setPhoneOrMail(vo.getPhoneOrMail());
		fb.setType(vo.getFeedbackType());
		fb.setStatus("0");
		fb.setAppId(vo.getAppId());
		fb.setHaveseen(false); 
		int i = feedbackMapper.insert(fb);
		return i;
	}
	
	/**
	 * 获得未读客服回复消息的数目
	 * @param cust_id 用户cust_id
	 * @return 数目
	 */
	public Map<String,String> getUnreadNum(String cust_id,String app_id) {
		Map<String,String> resmap = new HashMap<String,String>();
		if(cust_id != null){
			Integer num = feedbackMapper.selectUnReadFeedbackNum(cust_id,app_id);
			resmap.put("unreadNumFeedback", num.toString());
			
			Integer unReadNum = appNoticeMapper.selectUnReadNoticeNum(cust_id,app_id);
			
			resmap.put("unreadNumNotice", unReadNum + "");			
			resmap.put("unreadNumAll", (Integer.valueOf(num) + unReadNum) + "");
		}
		
		return resmap;
	}
	/**
	 * 获得已经回复的反馈列表
	 * @param vo
	 * @return
	 */
	public Map<String, Object> getAlreadyRepliedFeedbackList(Map<String,String> mapIn) {
		if (mapIn.get("page") != null && mapIn.get("rows") != null) {
            PageHelper.startPage(Integer.parseInt(mapIn.get("page").toString()), Integer.parseInt(mapIn.get("rows").toString()));
        }
		List<Map<String,Object>> feedbacks = feedbackMapper.selectAlreadyRepliedFeedbackList(mapIn.get("cust_id"),mapIn.get("app_id"));
		int unreadnum = feedbackMapper.selectUnReadFeedbackNum(mapIn.get("cust_id"),mapIn.get("app_id"));
		for(int i = 0;i < feedbacks.size(); i ++){
			Map<String,Object> map = feedbacks.get(i);
			if(map.get("haveseen").equals(false)){
				map.put("haveseen", "0");
			}else{
				map.put("haveseen", "1");
			}	
		}		
        PageInfo<Map<String,Object>> pageinfo = new PageInfo<Map<String,Object>>(feedbacks);
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageCount",pageinfo.getTotal());
        map.put("type", 2);
        map.put("unReadNum", unreadnum);
        map.put("feedbacklist",feedbacks);
        return  map;
	}
	
	/**
	 * 获得所有公告列表，包含某个用户是否查看过此公告的信息
	 * @param vo
	 * @return
	 */
	public Map<String, Object> getAllPushNoticeList(Map<String,String> mapIn) {
		Integer unReadNum = appNoticeMapper.selectUnReadNoticeNum(mapIn.get("cust_id"),mapIn.get("app_id"));
		List<String> readlist = appNoticeMapper.selectAllReadNotices(mapIn.get("cust_id"),mapIn.get("app_id"));			

		if (mapIn.get("page") != null && mapIn.get("rows") != null) {
            PageHelper.startPage(Integer.parseInt(mapIn.get("page").toString()), Integer.parseInt(mapIn.get("rows").toString()));
        }
		List<Map<String,Object>> notices = appNoticeMapper.selectAllPushedNotices(mapIn.get("cust_id"),mapIn.get("app_id"));
		PageInfo<Map<String, Object>> pageinfo = new PageInfo<Map<String, Object>>(notices);
		for(Map<String,Object> map:notices){
			if(readlist.contains(map.get("notice_id"))){
				map.put("haveseen", "1");
			}else{
				map.put("haveseen", "0");
			}
		}		
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageCount",pageinfo.getTotal());
        map.put("type", 1);
        map.put("unReadNum", unReadNum);
        map.put("noticelist",notices);
        return  map;
	}
	
	/**
	 * 获得反馈详情
	 * @param cust_id cust_id
	 * @param feedback_id feedback_id
	 * @return
	 */
	public Map<String, Object> getFeedbackDetail(String cust_id,String feedback_id,String app_id) {		
		Map<String, Object> map = feedbackMapper.selectFeedbackDetail(cust_id, feedback_id,app_id);	
		if(map != null){
			if(!map.containsKey("imgs")){
				map.put("imgs", "");
			}
			if(!map.containsKey("content")){
				map.put("content", "");
			}
			if(!map.containsKey("feedback_result") ){
				map.put("feedback_result", "");
			}

			//用户查看详情后，更新反馈中心表，说明一下已经查看过，havaseen字段设为1
			feedbackMapper.upFeedbackStatus(cust_id,feedback_id,1,app_id);
		}
		return map;
	}
	
	public Map getFeedbackList(FeedbackVo vo) {
		
		if (vo.getPage() != null && vo.getRows() != null) {
            PageHelper.startPage(vo.getPage(), vo.getRows());
        }
		
		// FeedBackSub   源文件  update by  liangtf 2018.08.07
//        List<Feedback> feedbacks = feedbackMapper.getFeedbackList(vo);
//        int n=feedbacks.size();
//        for(int i=0;i<n;i++) {
//        	Feedback fd=feedbacks.get(i);
//        	if(OPION_FEEDBACK.getCode().equals(fd.getType())) {
//        		fd.setType(OPION_FEEDBACK.getName());
//        	}else if(PRODUCT_SUGGEST.getCode().equals(fd.getType())) {
//        		fd.setType(PRODUCT_SUGGEST.getName());
//        	}else if(FEATURE_APPLY.getCode().equals(fd.getType())) {
//        		fd.setType(FEATURE_APPLY.getName());
//        	}else if(OTHER.getCode().equals(fd.getType())) {
//        		fd.setType(OTHER.getName());
//        	}
//        }
		
		
		 List<FeedBackSub> feedbacks = feedbackSubMapper.getFeedbackList(vo);
		 
	        int n=feedbacks.size();
	        for(int i=0;i<n;i++) {
	        	FeedBackSub fd=(FeedBackSub)feedbacks.get(i);
	        	if(OPION_FEEDBACK.getCode().equals(fd.getType())) {
	        		fd.setType(OPION_FEEDBACK.getName());
	        	}else if(PRODUCT_SUGGEST.getCode().equals(fd.getType())) {
	        		fd.setType(PRODUCT_SUGGEST.getName());
	        	}else if(FEATURE_APPLY.getCode().equals(fd.getType())) {
	        		fd.setType(FEATURE_APPLY.getName());
	        	}else if(OTHER.getCode().equals(fd.getType())) {
	        		fd.setType(OTHER.getName());
	        	}
	        }
		// 
		
		
        PageInfo pageinfo = new PageInfo(feedbacks);
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageCount",pageinfo.getTotal());
        map.put("feedbacks",feedbacks);
        return  map;
	}
	
	
	
	public int updFeedback(FeedbackVo vo) {
		Feedback fd= new Feedback();
		fd.setId(vo.getId());
		fd.setFeedbackid(vo.getFeedbackid());// add by  ltf
		fd.setFeedback(vo.getFeedback());
		fd.setStatus(vo.getStatus());
		fd.setUpdateTime(DateUtils.getDateTime());
		fd.setAppId(vo.getAppId());
		fd.setHaveseen(false);
		Admin admin=(Admin) SessionUtil.getUserInfo();
		fd.setDealer(admin.getUsername());
		return feedbackMapper.updateByPrimaryKeySelective(fd);
	}
} 
