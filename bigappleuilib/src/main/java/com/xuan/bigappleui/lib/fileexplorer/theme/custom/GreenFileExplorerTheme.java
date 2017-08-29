package com.xuan.bigappleui.lib.fileexplorer.theme.custom;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.xuan.bigappleui.lib.fileexplorer.theme.DefaultFileExplorerTheme;
import com.xuan.bigappleui.lib.utils.ui.ColorUtils;
import com.xuan.bigappleui.lib.utils.ui.drawable.fileicon.FolderDrawable;

/**
 * 绿色主题
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-12-12 上午11:06:55 $
 */
public class GreenFileExplorerTheme extends DefaultFileExplorerTheme {
	@Override
	public int titleBgColor() {
		return ColorUtils.getColor("#7CCD7C");
	}

	@Override
	public int titleTextColor() {
		return Color.WHITE;
	}

	@Override
	public Drawable defaultFolderIcon() {
		FolderDrawable folderDrawable = new FolderDrawable();
		folderDrawable.setFolderBodyColor(ColorUtils.getColor("#7CCD7C"));
		folderDrawable.setFolderHeadColor(ColorUtils.getColor("#66CDAA"));
		return folderDrawable;
	}
}
