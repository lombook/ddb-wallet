package com.jinglitong.springshop.controller;

import com.jinglitong.springshop.IConstants;
import com.jinglitong.springshop.ShopRespose;
import com.jinglitong.springshop.entity.HotKeyWords;
import com.jinglitong.springshop.servcie.HotWordsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author fyy
 * @create 2019-01-10-12:29}
 * 热点关键词
 */

@RestController
@RequestMapping("/product")
@Api(value = "热点词 API", tags = "热点词 API")
public class HotKeyWordsController {

    @Autowired
    private HotWordsService hotWordsService;

    @ApiOperation(value = "获取热点词列表")
    @PostMapping(value = "/getHotWordsList")
    public ShopRespose getHotWordsList(@ApiParam(name = "num", value = "数量", required = true) @RequestParam(name = "num", defaultValue = "8") String num) {
        List<HotKeyWords> info = hotWordsService.getHotWordsList(num);
        return new ShopRespose(IConstants.SUCCESS, "查询成功", info);
    }
}
