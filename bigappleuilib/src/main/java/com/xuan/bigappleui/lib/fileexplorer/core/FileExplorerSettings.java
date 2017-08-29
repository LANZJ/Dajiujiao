package com.xuan.bigappleui.lib.fileexplorer.core;

/**
 * 一些基本设置
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-12-11 下午4:00:40 $
 */
public class FileExplorerSettings {
	/** 是否显示系统很缓存图片，默认不显示 */
	private boolean showDotAndHiddenFiles;
	private static FileExplorerSettings instance;

	private FileExplorerSettings() {
	}

	/**
	 * 获取单例
	 * 
	 * @return
	 */
	public static FileExplorerSettings instance() {
		if (null == instance) {
			instance = new FileExplorerSettings();
		}
		return instance;
	}

	public boolean isShowDotAndHiddenFiles() {
		return showDotAndHiddenFiles;
	}

	public void setShowDotAndHiddenFiles(boolean showDotAndHiddenFiles) {
		this.showDotAndHiddenFiles = showDotAndHiddenFiles;
	}

}
