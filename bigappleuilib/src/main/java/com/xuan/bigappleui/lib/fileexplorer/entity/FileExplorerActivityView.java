package com.xuan.bigappleui.lib.fileexplorer.entity;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xuan.bigappleui.lib.utils.ui.entity.TitleView;

/**
 * FileExplorerActivity界面的布局
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-12-9 下午5:05:44 $
 */
public class FileExplorerActivityView {
	public View root;

	public TitleView titleView;

	public HorizontalScrollView navigationScrollView;
	public LinearLayout navigationLayout;

	public FrameLayout container;
	public ListView fileListView;
	public TextView noDataTextView;
}
