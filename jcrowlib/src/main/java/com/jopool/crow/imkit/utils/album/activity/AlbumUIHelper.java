package com.jopool.crow.imkit.utils.album.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jopool.crow.imkit.utils.BUCompat;
import com.jopool.crow.imkit.utils.BURotationImageView;
import com.jopool.crow.imkit.utils.BaseUIHelper;
import com.jopool.crow.imkit.utils.ColorUtils;
import com.jopool.crow.imkit.utils.TitleView;
import com.jopool.crow.imkit.utils.album.BUAlbum;
import com.jopool.crow.imkit.utils.album.entity.BucketActivityView;
import com.jopool.crow.imkit.utils.album.entity.BucketImageActivityView;
import com.jopool.crow.imkit.utils.album.entity.BucketImageListItemView;
import com.jopool.crow.imkit.utils.album.entity.BucketListItemView;

/**
 * Activity创建帮助类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-10 上午10:04:23 $
 */
public abstract class AlbumUIHelper extends BaseUIHelper {

	/**
	 * 相册列表Activity
	 * 
	 * @param activity
	 * @return
	 */
	public static BucketActivityView getBucketActivityView(Activity activity) {
		// 根
		LinearLayout root = new LinearLayout(activity);
		root.setOrientation(LinearLayout.VERTICAL);
		root.setBackgroundColor(Color.WHITE);
		// 获取标题
		TitleView titleView = getTitleView(activity, root);
		titleView.titleTextView.setText("相册");
		titleView.leftTextView.setText("返回");
		titleView.rightTextView.setText("完成");
		// 内容GridView
		GridView gridView = new GridView(activity);
		LinearLayout.LayoutParams gridViewLp = new LinearLayout.LayoutParams(
				MATCH_PARENT, MATCH_PARENT);
		gridViewLp.leftMargin = getPx(activity, 10);
		gridViewLp.rightMargin = getPx(activity, 10);
		gridViewLp.topMargin = getPx(activity, 10);
		gridViewLp.bottomMargin = getPx(activity, 10);
		gridView.setLayoutParams(gridViewLp);
		gridView.setHorizontalSpacing(getPx(activity, 20));
		gridView.setVerticalSpacing(getPx(activity, 20));
		gridView.setNumColumns(2);
		gridView.setVerticalScrollBarEnabled(false);
		root.addView(gridView);
		// 设置按下效果
		BUCompat.setViewBackgroundDrawable(titleView.leftTextView,
				getPressedDrawable(ColorUtils.TRANSLUCENT));
		BUCompat.setViewBackgroundDrawable(titleView.rightTextView,
				getPressedDrawable(ColorUtils.TRANSLUCENT));
		return new BucketActivityView(root, titleView, gridView);
	}

	/**
	 * 相册Item
	 * 
	 * @param activity
	 * @return
	 */
	public static BucketListItemView getBucketListItemView(Activity activity) {
		// 根
		LinearLayout root = new LinearLayout(activity);
		root.setBackgroundColor(Color.WHITE);
		root.setOrientation(LinearLayout.VERTICAL);
		root.setPadding(getPx(activity, 10), getPx(activity, 10),
				getPx(activity, 10), getPx(activity, 10));
		// 图片
		ImageView imageView = new BURotationImageView(activity);
		LinearLayout.LayoutParams imageViewLp = new LinearLayout.LayoutParams(
				getPx(activity, 156), getPx(activity, 128));
		imageView.setLayoutParams(imageViewLp);
		imageView.setScaleType(ScaleType.CENTER_CROP);
		root.addView(imageView);
		// 相册名称
		TextView nameTextView = new TextView(activity);
		LinearLayout.LayoutParams nameTextViewLp = new LinearLayout.LayoutParams(
				WRAP_CONTENT, WRAP_CONTENT);
		nameTextView.setLayoutParams(nameTextViewLp);
		nameTextView.setTextColor(Color.BLACK);
		nameTextView.setEllipsize(TruncateAt.END);
		nameTextView.setGravity(Gravity.LEFT);
		nameTextView.setSingleLine(true);
		root.addView(nameTextView);
		// 相册数量
		TextView countTextView = new TextView(activity);
		LinearLayout.LayoutParams countTextViewLp = new LinearLayout.LayoutParams(
				WRAP_CONTENT, WRAP_CONTENT);
		countTextView.setLayoutParams(countTextViewLp);
		countTextView.setTextColor(Color.BLACK);
		countTextView.setGravity(Gravity.LEFT);
		root.addView(countTextView);

		BucketListItemView bucketListItemView = new BucketListItemView();
		bucketListItemView.root = root;
		bucketListItemView.imageView = imageView;
		bucketListItemView.nameTextView = nameTextView;
		bucketListItemView.countTextView = countTextView;
		return bucketListItemView;
	}

	/**
	 * 获取某个相册的所有图片
	 * 
	 * @param activity
	 * @return
	 */
	public static BucketImageActivityView getBucketImageActivityView(
			Activity activity) {
		// 根
		LinearLayout root = new LinearLayout(activity);
		root.setOrientation(LinearLayout.VERTICAL);
		root.setBackgroundColor(Color.WHITE);
		// 获取标题
		TitleView titleView = getTitleView(activity, root);
		titleView.titleTextView.setText("相册");
		titleView.leftTextView.setText("返回");
		titleView.rightTextView.setText("完成");
		// 内容GridView
		GridView gridView = new GridView(activity);
		LinearLayout.LayoutParams gridViewLp = new LinearLayout.LayoutParams(
				MATCH_PARENT, MATCH_PARENT);
		gridView.setLayoutParams(gridViewLp);
		gridView.setPadding(getPx(activity, 8), getPx(activity, 8),
				getPx(activity, 8), getPx(activity, 8));
		gridView.setHorizontalSpacing(getPx(activity, 8));
		gridView.setVerticalSpacing(getPx(activity, 8));
		gridView.setNumColumns(3);
		gridView.setVerticalScrollBarEnabled(false);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		root.addView(gridView);
		// 设置按下效果
		BUCompat.setViewBackgroundDrawable(titleView.leftTextView,
				getPressedDrawable(ColorUtils.TRANSLUCENT));
		BUCompat.setViewBackgroundDrawable(titleView.rightTextView,
				getPressedDrawable(ColorUtils.TRANSLUCENT));
		return new BucketImageActivityView(root, titleView, gridView);
	}

	/**
	 * 图片列表设配器布局Item
	 * 
	 * @param activity
	 * @return
	 */
	public static BucketImageListItemView getBucketImageListItemView(
			Activity activity) {
		// 根
		RelativeLayout root = new RelativeLayout(activity);
		// 图
		ImageView imageView = new BURotationImageView(activity);
		RelativeLayout.LayoutParams imageViewLp = new RelativeLayout.LayoutParams(
				WRAP_CONTENT, WRAP_CONTENT);
		imageView.setLayoutParams(imageViewLp);
		imageView.setScaleType(ScaleType.CENTER_CROP);
		root.addView(imageView);
		// 选中后的图
		ImageView imageViewSel = new ImageView(activity);// 遮盖层
		RelativeLayout.LayoutParams imageViewSelLp = new RelativeLayout.LayoutParams(
				WRAP_CONTENT, WRAP_CONTENT);
		imageViewSel.setLayoutParams(imageViewSelLp);
		imageViewSel.setBackgroundColor(Color.parseColor("#60000000"));
		imageViewSel.setVisibility(View.GONE);
		root.addView(imageViewSel);
		ImageView hookImageSel = new ImageView(activity);// 打钩层
		RelativeLayout.LayoutParams hookImageSelLp = new RelativeLayout.LayoutParams(
				getPx(activity, 50), getPx(activity, 50));
		hookImageSelLp.addRule(RelativeLayout.CENTER_IN_PARENT,
				RelativeLayout.TRUE);
		hookImageSel.setLayoutParams(hookImageSelLp);
		hookImageSel.setImageDrawable(BUAlbum.getConfig().selectedDrawable);
		hookImageSel.setVisibility(View.GONE);
		root.addView(hookImageSel);

		BucketImageListItemView bucketImageListItemView = new BucketImageListItemView();
		bucketImageListItemView.root = root;
		bucketImageListItemView.imageView = imageView;
		bucketImageListItemView.imageViewSel = imageViewSel;
		bucketImageListItemView.hookImageSel = hookImageSel;
		return bucketImageListItemView;
	}

}
