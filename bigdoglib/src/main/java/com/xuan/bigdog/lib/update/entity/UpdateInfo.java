package com.xuan.bigdog.lib.update.entity;

/**
 * 返回的结果对象
 * 
 * @author xuan
 */
public class UpdateInfo {
	private int lastVersion;// 最新版本
	private String url;// apk下载地址
	private boolean isForceUpdate;// 是否强制更新
	private String description;// 更新提示

	public int getLastVersion() {
		return lastVersion;
	}

	public void setLastVersion(int lastVersion) {
		this.lastVersion = lastVersion;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isForceUpdate() {
		return isForceUpdate;
	}

	public void setForceUpdate(boolean isForceUpdate) {
		this.isForceUpdate = isForceUpdate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
