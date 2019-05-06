package com.jinglitong.wallet.server.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jinglitong.wallet.api.model.Admin;
import com.jinglitong.wallet.api.model.CompanyInfo;
import com.jinglitong.wallet.api.model.view.CompanyInfoVo;
import com.jinglitong.wallet.server.mapper.CompanyInfoMapper;
import com.jinglitong.wallet.server.util.DateUtils;
import com.jinglitong.wallet.server.util.UuidUtil;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;
import tk.mybatis.orderbyhelper.OrderByHelper;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class CompanyInfoService {

    @Autowired
    CompanyInfoMapper companyInfoMapper;


    /**
     * 新增公司信息
     * @param companyInfo
     * @return
     */
    public int insertCompanyInfo(CompanyInfo companyInfo){
        companyInfo.setZid(UuidUtil.getUUID());
        String dateTime = DateUtils.getDateTime();
        companyInfo.setCreatedTime(dateTime);
        companyInfo.setUpdatedTime(dateTime);
        return companyInfoMapper.insert(companyInfo);
    }


    /**
     * 修改公司信息
     * @param companyInfo
     * @return
     */
    public int updateCompanyInfo(CompanyInfo companyInfo){
        companyInfo.setUpdatedTime(DateUtils.getDateTime());
        return companyInfoMapper.updateByPrimaryKeySelective(companyInfo);
    }

    /**
     * 删除公司信息
     * @param companyInfo
     * @return
     */
    public int deleteCompanyInfo(CompanyInfo companyInfo){
        return companyInfoMapper.deleteByPrimaryKey(companyInfo.getId());
    }

    /**
     * 查询公司信息
     * @param companyInfo
     * @return
     */
    public CompanyInfo getOneCompanyInfo(CompanyInfo companyInfo){
        return companyInfoMapper.selectOne(companyInfo);
    }


    /**
     * 分页查询公司信息
     * @param companyInfoVo
     * @return
     */
    public PageInfo<CompanyInfo> queryCompanyInfoByPage(CompanyInfoVo companyInfoVo){
        if (companyInfoVo.getPage() != null && companyInfoVo.getRows() != null) {
            PageHelper.startPage(companyInfoVo.getPage(), companyInfoVo.getRows());
        }

        OrderByHelper.orderBy("created_time desc");
        List<CompanyInfo> members = companyInfoMapper.select(companyInfoVo);
        PageInfo pageinfo = new PageInfo(members);
        return pageinfo;
    }

    public PageInfo<CompanyInfo> queryCompany(){
        OrderByHelper.orderBy("created_time desc");
        List<CompanyInfo> members = companyInfoMapper.selectAll();
        PageInfo pageinfo = new PageInfo(members);
        return pageinfo;
    }


    public Boolean checkCompanyName(CompanyInfo companyInfo) {
        Weekend<CompanyInfo> weekend = Weekend.of(CompanyInfo.class);
        WeekendCriteria<CompanyInfo, Object> criteria = weekend.weekendCriteria();
        criteria.andEqualTo(CompanyInfo::getCompanyName, companyInfo.getCompanyName());
        CompanyInfo company = companyInfoMapper.selectOneByExample(weekend);
        if(company == null)
            return true;
        else
            return false;

    }

    public CompanyInfo SelectCompanyInfoById(Integer id) {
      return  companyInfoMapper.selectByPrimaryKey(id);
    }
}
