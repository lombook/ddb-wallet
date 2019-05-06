package com.jinglitong.wallet.api.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name="area_dic")
public class Area implements Serializable{

	@Id
	@Column(name="area_id")
	private String areaId;
	
	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getAreaSpecId() {
		return areaSpecId;
	}

	public void setAreaSpecId(String areaSpecId) {
		this.areaSpecId = areaSpecId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getSortPosition() {
		return sortPosition;
	}

	public void setSortPosition(String sortPosition) {
		this.sortPosition = sortPosition;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Column(name="name")
	private String name;
	
	@Column(name="area_code")
	private String areaCode;
	
	@Column(name="area_spec_id")
	private String areaSpecId;
	
	@Column(name="parent_id")
	private String parentId;
	
	@Column(name="sort_position")
	private String sortPosition;
	
	@Column(name="full_name")
	private String fullName;
	
	@Column(name="create_date")
	private String createDate;
	
	@Column(name="remarks")
	private String remarks;

}
