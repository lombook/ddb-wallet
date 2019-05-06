package com.jinglitong.wallet.server.controller.console;

import com.jinglitong.wallet.api.model.AppNotice;
import com.jinglitong.wallet.api.model.view.AppNSelVO;
import com.jinglitong.wallet.api.model.view.AppNoticeVO;
import com.jinglitong.wallet.server.common.ErrorEnum;
import com.jinglitong.wallet.server.common.NoticeTypeEnum;
import com.jinglitong.wallet.server.common.storage.AliCloudStorageService;
import com.jinglitong.wallet.server.service.AppNoiceService;
import com.jinglitong.wallet.server.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping(value = "/console")
public class AppNoticeController extends BaseController{

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AppNoiceService appNoticeService;
    
    @Autowired
    private AliCloudStorageService aliCloudStorageService;
    /**
     * 获取公告列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/noticeList.json", method = RequestMethod.POST)
    public Map noticeList(@RequestBody AppNSelVO appNSelVO) {
        if(StringUtils.isEmpty(appNSelVO.getAppId())){
            setGlobalAdminAppId(appNSelVO);
        }
        HashMap<String, Object> map = appNoticeService.getNoticeList(appNSelVO);
        return JsonUtil.toJsonSuccess("获取公告列表", map);
    }
    /**
     * 添加公告
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/createNotice.json", method = RequestMethod.POST)
    public Map createNotice(@RequestBody AppNoticeVO appNoticeVO) {
        int insert = appNoticeService.createNotice(appNoticeVO);
        if(insert > 0)
            return JsonUtil.toJsonSuccess("创建公告成功", insert);
        else
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31408);

    }
    /**
     * 获取单个公告
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getNotice.json", method = RequestMethod.POST)
    public Map getNotice(@RequestBody AppNoticeVO appNoticeVO ) {
        if(StringUtils.isEmpty(appNoticeVO.getNoticeId()) )
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31409);
        AppNotice appNotice = appNoticeService.getNotice(appNoticeVO);
        return JsonUtil.toJsonSuccess("获取公告", appNotice);
    }
    /**
     * 修改公告
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateNotice.json", method = RequestMethod.POST)
    public Map updateNotice(@RequestBody AppNoticeVO appNoticeVO) {
        int update = appNoticeService.updateNotice(appNoticeVO);
        if(update > 0)
            return JsonUtil.toJsonSuccess("修改成功", update);
        else
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31410);

    }
    /**
     * 启用或冻结公告
     * @param
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/updateaNoticeState.json", method = RequestMethod.POST)
    public Map memberState(@RequestBody AppNoticeVO appNoticeVO) {
        if(StringUtils.isEmpty(appNoticeVO.getNoticeId()) || appNoticeVO.getState() == null){
            return JsonUtil.toJsonError(ErrorEnum.ERROR_21101);
        }
        int i = appNoticeService.updateNoticeby(appNoticeVO);
        if(i >0){
            return JsonUtil.toJsonSuccess("修改成功", i);
        }else{
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31411);
        }
    }
    /**
     * 公告置顶
     * @param
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/topNotice.json", method = RequestMethod.POST)
    public Map topNotice(@RequestBody AppNoticeVO appNoticeVO ) {
        if(StringUtils.isEmpty(appNoticeVO.getNoticeId())||appNoticeVO.getIsTop() == null){
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31409);
        }
        int i = appNoticeService.updateNoticeby(appNoticeVO);
        if(i < 0){
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31410);
        }else{
            return JsonUtil.toJsonSuccess("修改状态成功");
        }
    }
    /**
     * 设置排序
     * @param
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/setOrderTop.json", method = RequestMethod.POST)
    public Map setOrderTop(@RequestBody AppNoticeVO appNoticeVO ) {
        if(StringUtils.isEmpty(appNoticeVO.getNoticeId())||StringUtils.isEmpty(appNoticeVO.getOrderTop())){
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31409);
        }
        int i = appNoticeService.updateNoticeby(appNoticeVO);
        if(i < 0){
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31410);
        }else{
            return JsonUtil.toJsonSuccess("修排序态成功");
        }
    }
    /**
     * 获取文本类型
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/noticeTypeEnmus.json", method = RequestMethod.POST)
    public Map enmus() {
        NoticeTypeEnum[] values = NoticeTypeEnum.values();
        Map<String,String> datas = new HashMap<>();
        for (NoticeTypeEnum enm : values){
            datas.put(enm.getName(),enm.getValue());
        }
        return JsonUtil.toJsonSuccess("成功", datas);
    }
    
    @ResponseBody
    @RequestMapping(value = "/uploadNotice.json")
    public Map upload(@RequestParam("file") MultipartFile file) throws IOException {
        logger.debug("开始上传图片");
        Map map = new HashMap();
        if(file.getBytes().length>1024*500) {
        	return JsonUtil.toJsonError(ErrorEnum.ERROR_31443);
        }
        String fileName=file.getOriginalFilename();
        if(!fileName.endsWith(".png")&&!fileName.endsWith(".PNG")&&!fileName.endsWith(".jpg")&&!fileName.endsWith(".JPG")) {
        	return JsonUtil.toJsonError(ErrorEnum.ERROR_31444);
        }
        try {
            String url = aliCloudStorageService.uploadFile(file);
            logger.debug("上传图片URL" + url);
            map.put("url", url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("ok", file.getName());
        logger.debug("图片上传成功");
        return JsonUtil.toJsonSuccess("成功", map);
    }
}