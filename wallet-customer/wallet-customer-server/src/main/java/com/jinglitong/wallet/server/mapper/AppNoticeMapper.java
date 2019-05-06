package com.jinglitong.wallet.server.mapper;

import com.jinglitong.wallet.api.model.AppNotice;
import com.jinglitong.wallet.api.model.view.AppNSelVO;
import com.jinglitong.wallet.api.model.view.AppNoticeVO;
import com.jinglitong.wallet.server.util.MyMapper;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

@Service
public interface AppNoticeMapper extends MyMapper<AppNotice> {
    @Select("SELECT notice_id,title,is_top,notice_img ,DATE_FORMAT(view_ctime,'%Y-%c-%d') view_ctime,sub_title FROM app_notice where state=1 and app_id =#{appId} ")
    @Results({
            @Result(property = "noticeId",  column = "notice_id"),
            @Result(property = "isTop", column = "is_top"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "noticeImg", column = "notice_img"),
            @Result(property = "viewCtime", column = "view_ctime"),
            @Result(property = "subTitle", column = "sub_title"),
    })
    List<AppNotice> getTitle(AppNotice no);

    List<AppNoticeVO> getNoticeList(AppNSelVO appNSelVO);
    @Select("SELECT MAX(order_top) FROM app_notice")
    Integer selecMaxOrderTop();
    
    
    void deleteBannerRelation(String id);
    
    /**
     * 获得某个用户已经读过的通知id
     * @param cust_id cust_id
     * @param app_id app_id
     * @return
     */
    /*List<String> selectAllReadNotices(String cust_id,String app_id);*/
    /**
     * 获得未读通知的个数，包括系统升级消息个系统给个人的消息
     * @param cust_id cust_id
     * @param app_id app_id
     * @return
     */
	Integer selectUnReadNoticeNum(String cust_id,String app_id);
	
	/**
	 * 获得所有推送的通知
	 * @param app_id
	 * @return
	 */
	List<Map<String,Object>> selectAllPushedNotices(String cust_id,String app_id);
	
	
	/**
	 * 获得所有推送的通知
	 * @param app_id
	 * @return
	 */
	List<String> selectAllReadNotices(String cust_id,String app_id);

	
}