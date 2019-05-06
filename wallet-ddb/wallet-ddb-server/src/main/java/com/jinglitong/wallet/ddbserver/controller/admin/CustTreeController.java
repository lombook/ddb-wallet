package com.jinglitong.wallet.ddbserver.controller.admin;

import com.jinglitong.wallet.ddbapi.model.view.DdbCustOwnTreeVO;
import com.jinglitong.wallet.ddbserver.common.ErrorEnum;
import com.jinglitong.wallet.ddbserver.controller.customer.CusBaseController;
import com.jinglitong.wallet.ddbserver.service.CustTreeService;
import com.jinglitong.wallet.ddbserver.util.JsonUtil;
import com.kvn.poi.imp.PoiImporter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/console/custTree")
@Slf4j
public class CustTreeController extends CusBaseController{

   @Autowired
    private CustTreeService custTreeService;

    /**
     * 处理用户树
     * @param file
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/custTree.json", method = RequestMethod.POST)
    public Map readExcel(@RequestParam("file") MultipartFile file, HttpServletRequest request)  {
        List<List<Object>> allExcel = null;
        String appId = request.getHeader("appId");
        if(appId == null ||  "".equals(appId)){
           return JsonUtil.toJsonError(ErrorEnum.EROR_42202,"appId异常");
        }
        try {
            allExcel = PoiImporter.importFirstSheetFrom(file.getInputStream()).getContent();
            List<Object> excelHadList = allExcel.get(0);
            if(excelHadList.get(0).equals("account") && excelHadList.get(1).equals("treeNum")
                    && (excelHadList.get(2).equals("type"))){
            } else {
                return JsonUtil.toJsonError(ErrorEnum.EROR_42200);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.toJsonError(ErrorEnum.EROR_42200);
        }
        if(allExcel.size()>1) {
            //处理数据
            List<HashMap<String, String>> resultMap = custTreeService.doCustTreeExcel(allExcel.subList(1, allExcel.size()), appId);
            if(resultMap.size() != 0){
               return JsonUtil.toJsonError(ErrorEnum.EROR_42201,resultMap);
           }
        }
        return JsonUtil.toJsonSuccess("处理成功");
    }

    /**
     * 获取用户树列表
     * @param treeVo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getCustTreeList.json", method = RequestMethod.POST)
    public Map getCustTree(@RequestBody DdbCustOwnTreeVO treeVo)  {
        return JsonUtil.toJsonSuccess("获取列表成功",custTreeService.getCustTree(treeVo));
    }
}
