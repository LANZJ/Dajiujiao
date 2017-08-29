package com.jopool.crow.imlib.entity;

import java.util.Date;

/**
 * 群组实体
 * 
 * @author xuan
 */
public class CWGroup {
	private String id;
	private String name;
	private String url;
	private String memberHash;
	private Date modifyTime;
	private Date creationTime;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMemberHash() {
		return memberHash;
	}

	public void setMemberHash(String memberHash) {
		this.memberHash = memberHash;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

}
