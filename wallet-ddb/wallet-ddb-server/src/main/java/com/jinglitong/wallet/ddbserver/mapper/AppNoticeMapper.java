package com.jinglitong.wallet.ddbserver.mapper;

import com.jinglitong.wallet.api.model.AppNotice;
import com.jinglitong.wallet.api.model.view.AppNSelVO;
import com.jinglitong.wallet.api.model.view.AppNoticeVO;
import com.jinglitong.wallet.ddbserver.util.MyMapper;
import java.util.List;
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
}