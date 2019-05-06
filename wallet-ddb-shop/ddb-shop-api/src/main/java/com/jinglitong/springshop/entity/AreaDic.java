package com.jinglitong.springshop.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "area_dic")
public class AreaDic {
    /**
     * 区域代码
     */
    @Id
    @Column(name = "AREA_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer areaId;

    /**
     * 名称
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 区域编码
     */
    @Column(name = "AREA_CODE")
    private Integer areaCode;

    /**
     * 隶属分类
【20000：省份】
【30000：地级市】
【40000：区/县级市】
     */
    @Column(name = "AREA_SPEC_ID")
    private Integer areaSpecId;

    /**
     * 父节点
     */
    @Column(name = "PARENT_ID")
    private String parentId;

    /**
     * 排序
     */
    @Column(name = "SORT_POSITION")
    private Integer sortPosition;

    /**
     * 名称全称
     */
    @Column(name = "FULL_NAME")
    private String fullName;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_DATE")
    private Date createDate;

    /**
     * 备注
     */
    @Column(name = "REMARKS")
    private String remarks;

    /**
     * 获取区域代码
     *
     * @return AREA_ID - 区域代码
     */
    public Integer getAreaId() {
        return areaId;
    }

    /**
     * 设置区域代码
     *
     * @param areaId 区域代码
     */
    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    /**
     * 获取名称
     *
     * @return NAME - 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取区域编码
     *
     * @return AREA_CODE - 区域编码
     */
    public Integer getAreaCode() {
        return areaCode;
    }

    /**
     * 设置区域编码
     *
     * @param areaCode 区域编码
     */
    public void setAreaCode(Integer areaCode) {
        this.areaCode = areaCode;
    }

    /**
     * 获取隶属分类
【20000：省份】
【30000：地级市】
【40000：区/县级市】
     *
     * @return AREA_SPEC_ID - 隶属分类
【20000：省份】
【30000：地级市】
【40000：区/县级市】
     */
    public Integer getAreaSpecId() {
        return areaSpecId;
    }

    /**
     * 设置隶属分类
【20000：省份】
【30000：地级市】
【40000：区/县级市】
     *
     * @param areaSpecId 隶属分类
【20000：省份】
【30000：地级市】
【40000：区/县级市】
     */
    public void setAreaSpecId(Integer areaSpecId) {
        this.areaSpecId = areaSpecId;
    }

    /**
     * 获取父节点
     *
     * @return PARENT_ID - 父节点
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * 设置父节点
     *
     * @param parentId 父节点
     */
    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    /**
     * 获取排序
     *
     * @return SORT_POSITION - 排序
     */
    public Integer getSortPosition() {
        return sortPosition;
    }

    /**
     * 设置排序
     *
     * @param sortPosition 排序
     */
    public void setSortPosition(Integer sortPosition) {
        this.sortPosition = sortPosition;
    }

    /**
     * 获取名称全称
     *
     * @return FULL_NAME - 名称全称
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * 设置名称全称
     *
     * @param fullName 名称全称
     */
    public void setFullName(String fullName) {
        this.fullName = fullName == null ? null : fullName.trim();
    }

    /**
     * 获取创建时间
     *
     * @return CREATE_DATE - 创建时间
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 设置创建时间
     *
     * @param createDate 创建时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取备注
     *
     * @return REMARKS - 备注
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * 设置备注
     *
     * @param remarks 备注
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }
}