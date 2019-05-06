package com.jinglitong.springshop.servcie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.springshop.entity.PropertieTab;
import com.jinglitong.springshop.mapper.PropertieTabMapper;
import com.jinglitong.springshop.utils.UuidUtil;
import com.jinglitong.springshop.vo.request.PropertieTabVO;
import com.jinglitong.springshop.vo.response.PropertieVO;

import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

@Service
public class PropertieTabService {

	@Autowired
	private PropertieTabMapper propertieTabMapper; 
	
	
	/**
	 * 
	 * 功能说明:列表
	 * @param vo
	 * @return
	 */
	public PageInfo<PropertieTab> getAppPList(PropertieTabVO vo) {
		PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
		List<PropertieTab> list  = propertieTabMapper.filterAboutManagement(vo);
		PageInfo<PropertieTab> info = new PageInfo<PropertieTab>(list);
    	return info;
	}
	
	/**
	 * 
	 * 功能说明:添加
	 * @param appControlVO
	 * @return
	 */
	@Transactional
	public void addAboutManagement(PropertieTabVO propertieTabVO) {

		PropertieTab propertieTab = new PropertieTab();
		propertieTab.setZid(UuidUtil.getUUID());
		propertieTab.setGroupName("about");
		if (!StringUtils.isEmpty(propertieTabVO.getGroupKey())) {
			propertieTab.setGroupKey(propertieTabVO.getGroupKey());
		}
		if (!StringUtils.isEmpty(propertieTabVO.getGroupValue())) {
			propertieTab.setGroupValue(propertieTabVO.getGroupValue());
		}
		if (!StringUtils.isEmpty(propertieTabVO.getKeyDesc())) {
			propertieTab.setKeyDesc(propertieTabVO.getKeyDesc());
		}
		int insert = propertieTabMapper.insert(propertieTab);
		if (insert != 1) {
			throw new RuntimeException("新增异常");
		}

	}
	/**
	 * 
	 * 功能说明:删除
	 * @param propertieTabVO
	 */
	@Transactional
	public void delAboutManagement(PropertieTabVO propertieTabVO) {

		Integer id = propertieTabVO.getId();
		int count = propertieTabMapper.deleteByPrimaryKey(id);
		if(count != 1) {
			throw new RuntimeException("删除异常");
		}
	}

	
	/**
	 * 
	 * 功能说明:查看详情
	 * @param propertieTabVO
	 * @return
	 */
	public PropertieTab getAboutById(PropertieTabVO propertieTabVO) {

		Integer id = propertieTabVO.getId();
		if(id == null) {
			throw new RuntimeException("参数异常");
		}
		PropertieTab propertieTab = propertieTabMapper.selectByPrimaryKey(id);
		
		return propertieTab;
	}
	
	/**
	 * 
	 * 功能说明:修改
	 * @param propertieTab
	 */
	public void updateAboutManagement(PropertieTab propertieTab) {
		if (propertieTab == null) {
			throw new RuntimeException("参数异常");
		}
		int up = propertieTabMapper.updateByPrimaryKeySelective(propertieTab);
		if (up != 1) {
			throw new RuntimeException("修改异常");
		}

	}
	/**
	 * 
	 * 功能说明:获取关于
	 * @param vo
	 * @return
	 */
    public List<Map<String,String>> getPropertiesMap(PropertieVO vo) {
   	 List resList = new ArrayList();
       if (!StringUtils.isEmpty(vo.getgName()) || !StringUtils.isEmpty(vo.getgKey())) {
           Weekend<PropertieTab> weekend = Weekend.of(PropertieTab.class);
           WeekendCriteria<PropertieTab, Object> criteria = weekend.weekendCriteria();
           if (!StringUtils.isEmpty(vo.getgName()))
               criteria.andEqualTo(PropertieTab::getGroupName, vo.getgName());
           if (!StringUtils.isEmpty(vo.getgKey()))
               criteria.andEqualTo(PropertieTab::getGroupKey, vo.getgKey());
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
}
