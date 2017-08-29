package com.xuan.bigappleui.lib.fileexplorer.entity;

import android.graphics.drawable.Drawable;

public class FileInfo {

	public String fileName;

	public String fileNameExt;

	public Drawable icon;

	public String filePath;

	public long fileSize;

	public boolean isDir;

	public int count;

	public long modifiedDate;

	public boolean selected;

	public boolean canRead;

	public boolean canWrite;

	public boolean isHidden;

	public long dbId; // id in the database, if is from database

}
