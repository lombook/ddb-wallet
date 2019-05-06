package com.jinglitong.wallet.server.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.wallet.api.model.ProtocolManagement;
import com.jinglitong.wallet.api.model.view.PageVO;
import com.jinglitong.wallet.server.common.ErrorEnum;
import com.jinglitong.wallet.server.common.storage.AliCloudStorageService;
import com.jinglitong.wallet.server.mapper.ProtocolManagementMapper;
import com.jinglitong.wallet.server.util.DateUtils;
import com.jinglitong.wallet.server.util.JsonUtil;
import com.jinglitong.wallet.server.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProtocolManagementService {

    @Resource
    private ProtocolManagementMapper protocolManagementMapper;
    @Autowired
    AliCloudStorageService aliCloudStorageService;


    public Map protocolManagementList(PageVO pageVO) {
        try {
            if (pageVO.getPage() != null && pageVO.getRows() != null) {
                PageHelper.startPage(pageVO.getPage(), pageVO.getRows());

            }
            List<ProtocolManagement> protocolManagements = protocolManagementMapper.protocolManagementList();
            PageInfo pageinfo = new PageInfo(protocolManagements);
            HashMap<String, Object> resultMap = new HashMap<>();
            resultMap.put("dataCount", pageinfo.getTotal());
            resultMap.put("protocolManagements", protocolManagements);
            return JsonUtil.toJsonSuccess("协议列表查询成功", resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.toJsonError(ErrorEnum.ERROR_38000.getCode(), e.getMessage());
        }
    }


    public Map delProtocolManagement(ProtocolManagement protocolManagement) {
        try {
            Integer id = protocolManagement.getId();
            if (id == null) {
                return JsonUtil.toJsonError(ErrorEnum.ERROR_38001);
            }

            Integer walletProCount = protocolManagementMapper.getWalletProCount(id);

            //walletProCount 大于0 表示此协议有钱包使用 不能删除
            if (walletProCount > 0) {
                return JsonUtil.toJsonError(ErrorEnum.ERROR_38002);
            }
            protocolManagementMapper.deleteByPrimaryKey(id);
            return JsonUtil.toJsonSuccess("协议删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.toJsonError(ErrorEnum.ERROR_38003.getCode(), e.getMessage());
        }
    }

    public Map getProtocolManagementById(ProtocolManagement protocolManagement) {
        try {
            Integer id = protocolManagement.getId();
            if (id == null) {
                return JsonUtil.toJsonError(ErrorEnum.ERROR_38001);
            }

            ProtocolManagement pro = protocolManagementMapper.selectByPrimaryKey(id);
            return JsonUtil.toJsonSuccess("协议查询成功", pro);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.toJsonError(ErrorEnum.ERROR_38004.getCode(), e.getMessage());
        }
    }


    public Map addProtocolManagement(ProtocolManagement protocolManagement) {
        try {
            if (protocolManagement == null) {
                return JsonUtil.toJsonError(ErrorEnum.ERROR_38001);
            }
            InputStream htmlIs = new ByteArrayInputStream(protocolManagement.getBody().getBytes());
            String dateTimeStr = " protocol_" + DateUtils.getDateTimeStr() + ".html";
            String resultOos = aliCloudStorageService.uploadProtocolHtml(htmlIs, dateTimeStr);

            if (resultOos == null) {
                return JsonUtil.toJsonError(ErrorEnum.ERROR_38005);
            }

            protocolManagement.setZid(UuidUtil.getUUID());
            protocolManagement.setProtocolUrl(resultOos);
            protocolManagement.setCreateTime(new Date());
            protocolManagement.setUpdateTime(new Date());
            int insert = protocolManagementMapper.insert(protocolManagement);
            if (insert > 0) {
                return JsonUtil.toJsonSuccess("协议新增成功");
            } else {
                return JsonUtil.toJsonError(ErrorEnum.ERROR_38006);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.toJsonError(ErrorEnum.ERROR_38006.getCode(), e.getMessage());
        }
    }

    public Map upProtocolManagement(ProtocolManagement protocolManagement) {
        try {
            if (protocolManagement == null) {
                return JsonUtil.toJsonError(ErrorEnum.ERROR_38001);
            }
            ProtocolManagement check = protocolManagementMapper.selectByPrimaryKey(protocolManagement.getId());
            if (!protocolManagement.equals(check)) {

                InputStream htmlIs = new ByteArrayInputStream(protocolManagement.getBody().getBytes());
                String dateTimeStr = "protocol_" + DateUtils.getDateTimeStr() + ".html";
                String resultOos = aliCloudStorageService.uploadProtocolHtml(htmlIs, dateTimeStr);

                if (resultOos == null) {
                    return JsonUtil.toJsonError(ErrorEnum.ERROR_38005);
                }

                protocolManagement.setProtocolUrl(resultOos);
                protocolManagement.setUpdateTime(new Date());
                protocolManagementMapper.updateByPrimaryKey(protocolManagement);
                return JsonUtil.toJsonSuccess("协议修改成功");
            } else {
                return JsonUtil.toJsonError(ErrorEnum.ERROR_38008);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.toJsonError(ErrorEnum.ERROR_38007.getCode(), e.getMessage());
        }
    }


    public Map protocolManagementSelect() {
        try {
            List<Map> proSelect = protocolManagementMapper.protocolManagementSelect();
            Map<String, Object> resultMap = new HashMap<String, Object>() {
                {
                    put("proSelect", proSelect);
                }
            };
            return JsonUtil.toJsonSuccess("协议下拉框查询成功", resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.toJsonError(ErrorEnum.ERROR_38009.getCode(), e.getMessage());
        }
    }


}
