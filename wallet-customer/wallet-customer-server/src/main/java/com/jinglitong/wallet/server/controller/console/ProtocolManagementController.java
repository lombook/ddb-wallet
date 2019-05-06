package com.jinglitong.wallet.server.controller.console;

import com.jinglitong.wallet.api.model.ProtocolManagement;
import com.jinglitong.wallet.api.model.view.PageVO;
import com.jinglitong.wallet.server.service.ProtocolManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/console")
public class ProtocolManagementController extends BaseController{

    @Autowired
    private ProtocolManagementService protocolManagementService;

    /**
     * 协议列表
     */
    @RequestMapping("/protocolManagementList.json")
    public Map protocolManagementList(@RequestBody PageVO pageVO) {
        return protocolManagementService.protocolManagementList(pageVO);
    }

    /**
     * 删除协议
     */
    @RequestMapping("/delProtocolManagement.json")
    public Map delProtocolManagement(@RequestBody ProtocolManagement protocolManagement) {
        return protocolManagementService.delProtocolManagement(protocolManagement);
    }

    /**
     * 查询单个协议
     */
    @RequestMapping("/getProtocolManagementById.json")
    public Map getProtocolManagementById(@RequestBody ProtocolManagement protocolManagement) {
        return protocolManagementService.getProtocolManagementById(protocolManagement);
    }

    /**
     * 增加协议
     */
    @RequestMapping("/addProtocolManagement.json")
    public Map addProtocolManagement(@RequestBody ProtocolManagement protocolManagement) {
        return protocolManagementService.addProtocolManagement(protocolManagement);
    }

    /**
     * 修改协议
     */
    @RequestMapping("/upProtocolManagement.json")
    public Map upProtocolManagement(@RequestBody ProtocolManagement protocolManagement) {
        return protocolManagementService.upProtocolManagement(protocolManagement);
    }

    /**
     * 协议下拉框
     */
    @RequestMapping("/protocolManagementSelect.json")
    public Map protocolManagementSelect() {
        return protocolManagementService.protocolManagementSelect();
    }
}
