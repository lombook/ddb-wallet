package com.jinglitong.springshop.vo.request;


public class PropertieTabVO   extends PageVo {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 关联ID
     */
    private String zid;
    /**
     * 类别名字
     */
    private String groupName;

    /**
     * 类别code
     */
    private String groupKey;

    /**
     * 具体值
     */
    private String groupValue;

    /**
     * 描述
     */
    private String keyDesc;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getZid() {
        return zid;
    }

    public void setZid(String zid) {
        this.zid = zid;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public String getGroupValue() {
        return groupValue;
    }

    public void setGroupValue(String groupValue) {
        this.groupValue = groupValue;
    }

    public String getKeyDesc() {
        return keyDesc;
    }

    public void setKeyDesc(String keyDesc) {
        this.keyDesc = keyDesc;
    }

}