package com.jinglitong.wallet.server.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.wallet.server.common.ErrorEnum;
import com.jinglitong.wallet.server.mapper.PropertieTabMapper;
import com.jinglitong.wallet.api.model.PropertieTab;
import com.jinglitong.wallet.api.model.view.PageVO;
import com.jinglitong.wallet.api.model.view.PropertieTabVO;
import com.jinglitong.wallet.api.model.view.PropertieVO;
import com.jinglitong.wallet.server.util.JsonUtil;
import com.jinglitong.wallet.server.util.UuidUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

@Service
@Transactional
public class PropertieTabService {

    @Autowired
    private PropertieTabMapper propertieTabMapper;

    public Map getAboutManagementList(PropertieTabVO propertieTabVO) {
        try {
            if (propertieTabVO.getPage() != null && propertieTabVO.getRows() != null) {
                PageHelper.startPage(propertieTabVO.getPage(), propertieTabVO.getRows());

            }
            List<Map> propertieTabs = propertieTabMapper.filterAboutManagement(propertieTabVO);
            PageInfo pageinfo = new PageInfo(propertieTabs);
            HashMap<String, Object> map = new HashMap<>();
            map.put("dataCount", pageinfo.getSize());
            map.put("propertieTabs", propertieTabs);
            return JsonUtil.toJsonSuccess("关于配置列表查询成功",map);
        }catch (Exception e){
            e.printStackTrace();
            return JsonUtil.toJsonError(ErrorEnum.ERROR_37000.getCode(),e.getMessage());
        }
    }

    public Map addAboutManagement(PropertieTabVO propertieTabVO) {
        try {
            if (propertieTabVO == null) {
                return JsonUtil.toJsonError(ErrorEnum.ERROR_37003);
            }
            PropertieTab propertieTab = new PropertieTab();
            propertieTab.setZid(UuidUtil.getUUID());
            if (!StringUtils.isEmpty(propertieTabVO.getGroupName())) {
                propertieTab.setGroupName(propertieTabVO.getGroupName());
            }
            if (!StringUtils.isEmpty(propertieTabVO.getGroupKey())) {
                propertieTab.setGroupKey(propertieTabVO.getGroupKey());
            }
            if (!StringUtils.isEmpty(propertieTabVO.getGroupValue())) {
                propertieTab.setGroupValue(propertieTabVO.getGroupValue());
            }
            if (!StringUtils.isEmpty(propertieTabVO.getKeyDesc())) {
                propertieTab.setKeyDesc(propertieTabVO.getKeyDesc());
            }
            if (!StringUtils.isEmpty(propertieTabVO.getAppId())) {
                propertieTab.setAppId(propertieTabVO.getAppId());
            }
            int insert = propertieTabMapper.insert(propertieTab);
            if (insert > 0) {
                return JsonUtil.toJsonSuccess("关于配置数据新增成功");
            } else {
                return JsonUtil.toJsonError(ErrorEnum.ERROR_37002);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.toJsonError(ErrorEnum.ERROR_37002.getCode(), e.getMessage());
        }
    }

    public Map delAboutManagement(PropertieTabVO propertieTabVO) {
        try {
            Integer id = propertieTabVO.getId();
            if (id == null) {
                return JsonUtil.toJsonError(ErrorEnum.ERROR_37003);
            }
            propertieTabMapper.deleteByPrimaryKey(id);
            return JsonUtil.toJsonSuccess("关于配置数据删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtil.toJsonError(ErrorEnum.ERROR_37004.getCode(), e.getMessage());
        }
    }

    public Map getAboutManagementById(PropertieTabVO propertieTabVO) {
        try {
            Integer id = propertieTabVO.getId();
            if(id==null){
                return JsonUtil.toJsonError(ErrorEnum.ERROR_37003);
            }
            PropertieTab propertieTab = propertieTabMapper.selectByPrimaryKey(id);
            return JsonUtil.toJsonSuccess("关于配置数据查询成功",propertieTab);
        }catch (Exception e){
            e.printStackTrace();
            return JsonUtil.toJsonError(ErrorEnum.ERROR_37005.getCode(),e.getMessage());
        }
    }

    public Map updateAboutManagement(PropertieTab propertieTab) {
        try {
            if(propertieTab == null){
                return JsonUtil.toJsonError(ErrorEnum.ERROR_37003);
            }
             int up = propertieTabMapper.updateByPrimaryKey(propertieTab);
            if(up>0){
                return JsonUtil.toJsonSuccess("关于配置数据修改成功");
            }else {
                return JsonUtil.toJsonError(ErrorEnum.ERROR_37001);
            }
        }catch (Exception e){
            e.printStackTrace();
            return JsonUtil.toJsonError(ErrorEnum.ERROR_37001.getCode(),e.getMessage());
        }
    }









    public List<Map<String,String>> getPropertiesMap(PropertieVO vo) {
    	 List resList = new ArrayList();
        if (!StringUtils.isEmpty(vo.getgName()) || !StringUtils.isEmpty(vo.getgKey())) {
            Weekend<PropertieTab> weekend = Weekend.of(PropertieTab.class);
            WeekendCriteria<PropertieTab, Object> criteria = weekend.weekendCriteria();
            if (!StringUtils.isEmpty(vo.getgName()))
                criteria.andEqualTo(PropertieTab::getGroupName, vo.getgName());
            if (!StringUtils.isEmpty(vo.getgKey()))
                criteria.andEqualTo(PropertieTab::getGroupKey, vo.getgKey());
            if (!StringUtils.isEmpty(vo.getAppId()))
                criteria.andEqualTo(PropertieTab::getAppId, vo.getAppId());
            List<PropertieTab> res = propertieTabMapper.selectByExample(weekend);
            if (res.size() > 0) {
                Map<String, String> resMap ;
                for (PropertieTab temp : res) {
                    resMap = new HashMap();
                    resMap.put("group_key",temp.getGroupKey());
                    resMap.put("group_value",temp.getGroupValue());
                    resMap.put("key_desc",temp.getKeyDesc());
                    resList.add(resMap);
                }
            }
        } 
        return resList;
    }

    public HashMap<String, Object> getPropertiesList(PageVO page) {
        if (page.getPage() != null && page.getRows() != null) {
            PageHelper.startPage(page.getPage(), page.getRows());

        }
        List<PropertieTab> propertieTabs = propertieTabMapper.selectAll();
        PageInfo pageinfo = new PageInfo(propertieTabs);
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageCount", pageinfo.getPageNum());
        map.put("propertieTabs", propertieTabs);
        return map;
    }

    /*public int createPropertie(PropertieTabVO propertieVO) {
        PropertieTab propertieTab = new PropertieTab();
        propertieTab.setId(UuidUtil.getUUID());
        if (!StringUtils.isEmpty(propertieVO.getGroupName()))
            propertieTab.setGroupName(propertieVO.getGroupName());
        if (!StringUtils.isEmpty(propertieVO.getGroupKey()))
            propertieTab.setGroupKey(propertieVO.getGroupKey());
        if (!StringUtils.isEmpty(propertieVO.getGroupValue()))
            propertieTab.setGroupValue(propertieVO.getGroupValue());
        if (!StringUtils.isEmpty(propertieVO.getKeyDesc()))
            propertieTab.setKeyDesc(propertieVO.getKeyDesc());
        int insert = propertieTabMapper.insert(propertieTab);
        return insert;
    }

    public int updaePropertie(PropertieTabVO propertieVO) {
        PropertieTab propertieTab = propertieTabMapper.selectByPrimaryKey(propertieVO.getId());
        propertieTab.setId(propertieVO.getId());
        if (!StringUtils.isEmpty(propertieVO.getGroupName()))
            propertieTab.setGroupName(propertieVO.getGroupName());
        if (!StringUtils.isEmpty(propertieVO.getGroupKey())){
                propertieTab.setGroupKey(propertieVO.getGroupKey());
        }
        if (!StringUtils.isEmpty(propertieVO.getGroupValue()))
            propertieTab.setGroupValue(propertieVO.getGroupValue());
        if (!StringUtils.isEmpty(propertieVO.getKeyDesc()))
            propertieTab.setKeyDesc(propertieVO.getKeyDesc());
        return propertieTabMapper.updateByPrimaryKey(propertieTab);
    }

    public int deletePropertie( String id) {
        PropertieTab propertieTab = new PropertieTab();
        propertieTab.setId(id);
        int i = propertieTabMapper.deleteByPrimaryKey(propertieTab);
        return i;
    }*/

    public List<PropertieTab> getpropertiesByGoup(String handleName) {
       List<PropertieTab> list = propertieTabMapper.selectByGroup(handleName);
       return list;
    }

    public PropertieTab checkPropertie(String groupKey, String groupName) {
        PropertieTab propertieTab = propertieTabMapper.checkPropertie(groupKey,groupName);
        return propertieTab;
    }

    public PropertieTab selectById(String id) {
        PropertieTab propertieTab = propertieTabMapper.selectByPrimaryKey(id);
        return propertieTab;
    }
}
