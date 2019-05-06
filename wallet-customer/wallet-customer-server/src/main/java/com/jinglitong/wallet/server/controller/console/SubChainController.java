package com.jinglitong.wallet.server.controller.console;

import com.jinglitong.wallet.server.common.ErrorEnum;
import com.jinglitong.wallet.server.common.storage.AliCloudStorageService;
import com.jinglitong.wallet.api.model.MainChain;
import com.jinglitong.wallet.api.model.SubChain;
import com.jinglitong.wallet.api.model.view.SubChainVO;
import com.jinglitong.wallet.server.mapper.MainChainMapper;
import com.jinglitong.wallet.server.service.MainChainService;
import com.jinglitong.wallet.server.service.SubChainService;
import com.jinglitong.wallet.server.util.JsonUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by fan on 2018/5/22.
 */
@Controller
@RequestMapping(value = "/console")
public class SubChainController extends BaseController{
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    SubChainService subChainService;
    @Autowired
    private AliCloudStorageService aliCloudStorageService;
    @Autowired
    private MainChainService mainChainService;
    @Autowired
    MainChainMapper mainChainMapper;

    /**
     * 获取列表
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getSubChains.json", method = RequestMethod.POST)
    public Map getSubChains(@RequestBody SubChainVO subChainVO) {
        if(!StringUtils.isEmpty(subChainVO.getAppId())){
            setGlobalAdminAppId(subChainVO);
        }
        HashMap<String, Object> map = subChainService.getSubChains(subChainVO);
        return JsonUtil.toJsonSuccess("获取币列表", map);
    }

    /**
     * 增加
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/createSubChain.json", method = RequestMethod.POST)
    public Map createSubChains(@RequestBody SubChainVO subChainVO) {

            Boolean b = subChainService.checkCurrency(subChainVO.getCurrency(),subChainVO.getAppId(),subChainVO.getChainId());
            if(!b){
                return JsonUtil.toJsonError(ErrorEnum.ERROR_31445);
            }
        int insert = subChainService.createSubChains(subChainVO);
        if (insert > 0)
            return JsonUtil.toJsonSuccess("创建成功", insert);
        else
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31427);
    }

    /**
     * 修改
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateSubChain.json", method = RequestMethod.POST)
    public Map updateSubChain(@RequestBody SubChainVO subChainVO) {
        Integer id = subChainVO.getId();
        SubChain subChain = subChainService.selectById(id);
        if (subChain != null) {
            String currency = subChain.getCurrency();
            if(!currency.equals(subChainVO.getCurrency())){
                Boolean b = subChainService.checkCurrency(subChainVO.getCurrency(),subChainVO.getAppId(),subChainVO.getChainId());
                if(!b){
                    return JsonUtil.toJsonError(ErrorEnum.ERROR_31445);
                }
            }
            if (subChain.getBaseChain()) {
                if (subChainVO.getState().equals(false))
                    if (subChain.getState())
                        return JsonUtil.toJsonError(ErrorEnum.ERROR_31431);
            }
        }
        int update = subChainService.updateSubChain(subChainVO);
        if (update > 0)
            return JsonUtil.toJsonSuccess("修改成功", update);
        else
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31428);
    }

    /**
     * 状态
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateSubChainState.json", method = RequestMethod.POST)
    public Map updateSubChainState(@RequestBody SubChainVO subChainVO) {
        Integer id = subChainVO.getId();
        SubChain subChain = subChainService.selectById(id);
        if (subChain != null) {
            MainChain mainChain = mainChainMapper.getMainChainById(subChain.getChainId());
            if(mainChain != null){
                if(mainChain.getState()== null || mainChain.getState() == false){
                    return JsonUtil.toJsonError(ErrorEnum.ERROR_35004);
                }
            }
            if (subChain.getBaseChain()) {
                if (subChainVO.getState().equals(false))
                    if (subChain.getState())
                        return JsonUtil.toJsonError(ErrorEnum.ERROR_31431);
            }
        }
        int update = subChainService.updateSubChainState(subChainVO);
        if (update > 0)
            return JsonUtil.toJsonSuccess("修改成功", update);
        else
            return JsonUtil.toJsonError(ErrorEnum.ERROR_31428);
    }

    @ResponseBody
    @RequestMapping(value = "/upload.json")
    public Map upload(@RequestParam("file") MultipartFile file) {
        logger.debug("开始上传图片");
        Map map = new HashMap();
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
    @ResponseBody
    @RequestMapping(value = "/checkCurrency.json")
    public Map checkCurrency(@RequestBody SubChainVO subChainVO) {
        String currency = subChainVO.getCurrency();
        String appId = subChainVO.getAppId();
        String chainId = subChainVO.getChainId();
        if(!StringUtils.isEmpty(currency)){
           Boolean b = subChainService.checkCurrency(currency,appId,chainId);
           if(!b){
               return JsonUtil.toJsonError(ErrorEnum.ERROR_31445);
           }else {
               return JsonUtil.toJsonSuccess("成功");
           }
        }
        return JsonUtil.toJsonError(ErrorEnum.ERROR_31446);

    }
    @ResponseBody
    @RequestMapping(value = "/getChainCoins.json")
    public Map getChainCoins(@RequestBody SubChainVO subChainVO) {
        if(StringUtils.isEmpty(subChainVO.getAppId())){
            setGlobalAdminAppId(subChainVO);
        }
     List<SubChain> list = subChainService.getChainCoins(subChainVO);
        return JsonUtil.toJsonSuccess("成功",list);
    }
    @ResponseBody
    @RequestMapping(value = "/allSubChain.json")
    public Map getallSubChain() {
     List<SubChain> list = subChainService.geAllSubChain();

        return JsonUtil.toJsonSuccess("成功",list);

    }


}
