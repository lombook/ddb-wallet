package com.jinglitong.springshop.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import javax.persistence.*;
@Data
@ToString
@Table(name = "product_category")
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String zid;
    /**
     * 排序权重 0最大
     */
    private Integer orders;

    /**
     * 层级
     */
    private Integer grade;

    /**
     * 名称
     */
    private String name;

    /**
     * 页面描述
     */
    @Column(name = "seoDescription")
    private String seodescription;

    /**
     * 关键词
     */
    @Column(name = "seoKeywords")
    private String seokeywords;

    /**
     * 页面标题
     */
    @Column(name = "seoTitle")
    private String seotitle;

    /**
     * 树路径
     */
    @Column(name = "treePath")
    private String treepath;

    /**
     * 上级分类
     */
    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取排序权重 0最大
     *
     * @return orders - 排序权重 0最大
     */
    public Integer getOrders() {
        return orders;
    }

    /**
     * 设置排序权重 0最大
     *
     * @param orders 排序权重 0最大
     */
    public void setOrders(Integer orders) {
        this.orders = orders;
    }

    /**
     * 获取层级
     *
     * @return grade - 层级
     */
    public Integer getGrade() {
        return grade;
    }

    /**
     * 设置层级
     *
     * @param grade 层级
     */
    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    /**
     * 获取名称
     *
     * @return name - 名称
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
     * 获取页面描述
     *
     * @return seoDescription - 页面描述
     */
    public String getSeodescription() {
        return seodescription;
    }

    /**
     * 设置页面描述
     *
     * @param seodescription 页面描述
     */
    public void setSeodescription(String seodescription) {
        this.seodescription = seodescription == null ? null : seodescription.trim();
    }

    /**
     * 获取关键词
     *
     * @return seoKeywords - 关键词
     */
    public String getSeokeywords() {
        return seokeywords;
    }

    /**
     * 设置关键词
     *
     * @param seokeywords 关键词
     */
    public void setSeokeywords(String seokeywords) {
        this.seokeywords = seokeywords == null ? null : seokeywords.trim();
    }

    /**
     * 获取页面标题
     *
     * @return seoTitle - 页面标题
     */
    public String getSeotitle() {
        return seotitle;
    }

    /**
     * 设置页面标题
     *
     * @param seotitle 页面标题
     */
    public void setSeotitle(String seotitle) {
        this.seotitle = seotitle == null ? null : seotitle.trim();
    }

    /**
     * 获取树路径
     *
     * @return treePath - 树路径
     */
    public String getTreepath() {
        return treepath;
    }

    /**
     * 设置树路径
     *
     * @param treepath 树路径
     */
    public void setTreepath(String treepath) {
        this.treepath = treepath == null ? null : treepath.trim();
    }

    /**
     * 获取上级分类
     *
     * @return parent_id - 上级分类
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * 设置上级分类
     *
     * @param parentId 上级分类
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * @return created_time
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * @param createdTime
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * @return updated_time
     */
    public Date getUpdatedTime() {
        return updatedTime;
    }

    /**
     * @param updatedTime
     */
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}