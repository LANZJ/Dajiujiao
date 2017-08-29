package com.jopool.crow.imkit.utils.album.entity;

import android.widget.GridView;
import android.widget.LinearLayout;

import com.jopool.crow.imkit.utils.TitleView;

/**
 * 显示某个相册内所有图片的布局
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-11 下午3:38:06 $
 */
public class BucketImageActivityView {
	public LinearLayout root;
	public TitleView titleView;
	public GridView gridView;

	public BucketImageActivityView() {
	}

	public BucketImageActivityView(LinearLayout root, TitleView titleView,
			GridView gridView) {
		this.root = root;
		this.titleView = titleView;
		this.gridView = gridView;
	}

}
