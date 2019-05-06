/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.jinglitong.wallet.server.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.wallet.api.model.view.AppNoticeVO;
import com.jinglitong.wallet.server.mapper.AppNoticeMapper;
import com.jinglitong.wallet.api.model.AppNotice;
import com.jinglitong.wallet.api.model.view.AppNSelVO;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NoticeService {

    @Autowired
    private AppNoticeMapper appNoticeMapper;


    public HashMap<String,Object> getNoticeList(AppNSelVO appNSelVO) {


        if (appNSelVO.getPage() != null && appNSelVO.getRows() != null) {
            PageHelper.startPage(appNSelVO.getPage(), appNSelVO.getRows());

        }
       List<AppNoticeVO> notices = appNoticeMapper.getNoticeList(appNSelVO);
        PageInfo pageinfo = new PageInfo(notices);
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageCount",pageinfo.getPageNum());
        map.put("notices",notices);
        return map;
    }

}
