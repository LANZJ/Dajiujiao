package com.xuan.bigappleui.lib.album.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xuan.bigappleui.lib.album.BUAlbum;
import com.xuan.bigappleui.lib.album.core.ImageLoader;
import com.xuan.bigappleui.lib.album.entity.BucketImageActivityView;
import com.xuan.bigappleui.lib.album.entity.BucketImageListItemView;
import com.xuan.bigappleui.lib.album.entity.ImageItem;
import com.xuan.bigappleui.lib.utils.BUDisplayUtil;
import com.xuan.bigappleui.lib.utils.BUToastUtil;

import java.util.List;

/**
 * 这个类是显示某一个相册的所有图片
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-11 下午3:35:00 $
 */
public class BucketImageActivity extends Activity {
	private BucketImageActivityView bucketImageActivityView;

	// 屏幕的高宽
	private int mScreenWidth;
	private int padding;

	private List<ImageItem> allImageList;// 当前相册的所有图片
	private boolean ifMultiple;// 是否是多选模式
	private int limitCount;// 显示可选数
	private String bucketName;// 相册名称

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bucketImageActivityView = AlbumUIHelper
				.getBucketImageActivityView(this);
		setContentView(bucketImageActivityView.root);

		// 获取参数
		allImageList = (List<ImageItem>) getIntent().getSerializableExtra(
				BucketActivity.PARAM_IMAGELIST);
		limitCount = getIntent().getIntExtra(BUAlbum.PARAM_LIMIT_COUNT, -1);
		bucketName = getIntent()
				.getStringExtra(BucketActivity.PARAM_BUCKETNAME);
		ifMultiple = getIntent().getBooleanExtra(
				BUAlbum.PARAM_IF_MULTIPLE_CHOICE, true);
		if (!ifMultiple) {
			bucketImageActivityView.titleView.rightTextView
					.setVisibility(View.GONE);// 表示单选
		}

		// 返回
		bucketImageActivityView.titleView.leftTextView
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						BucketImageActivity.this.finish();
					}
				});

		// 确定
		bucketImageActivityView.titleView.rightTextView
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						BUAlbum.selList.addAll(Temp.tempSelMap.values());
						setResult(RESULT_OK, getIntent());
						BucketImageActivity.this.finish();
					}
				});

		// 标题
		bucketImageActivityView.titleView.titleTextView.setText(bucketName);

		// 图片设配器
		bucketImageActivityView.gridView.setAdapter(new ImageAdapter());
		bucketImageActivityView.gridView
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View view,
							int arg2, long arg3) {
						BucketImageListItemView bucketImageListItemView = (BucketImageListItemView) view
								.getTag();

						if (!ifMultiple) {
							// 如果是单选，点击直接返回
							BUAlbum.selList
									.add(bucketImageListItemView.bucketImage);
							setResult(RESULT_OK, getIntent());
							BucketImageActivity.this.finish();
						} else {
							if (View.VISIBLE == bucketImageListItemView.imageViewSel
									.getVisibility()) {
								// 取消选中
								bucketImageListItemView.imageViewSel
										.setVisibility(View.GONE);
								bucketImageListItemView.hookImageSel
										.setVisibility(View.GONE);
								Temp.tempSelMap
										.remove(bucketImageListItemView.bucketImage.imageId);
							} else {
								// 选中，先判断是否还可选
								if (Temp.tempSelMap.size() >= limitCount
										&& -1 != limitCount) {
									// 提示不可选了
									BUToastUtil.displayTextShort("你最多只能选择"
											+ limitCount + "张照片");
								} else {
									bucketImageListItemView.imageViewSel
											.setVisibility(View.VISIBLE);
									bucketImageListItemView.hookImageSel
											.setVisibility(View.VISIBLE);
									Temp.tempSelMap
											.put(bucketImageListItemView.bucketImage.imageId,
													bucketImageListItemView.bucketImage);
								}
							}
							initCount();
						}
					}
				});

		// 限制图片大小
		mScreenWidth = BUDisplayUtil.getDisplayMetrics(this).widthPixels;
		padding = (int) BUDisplayUtil.getPxByDp(this, 40);

		initCount();// 初始化已选张数
	}

	// 更新选择的图片数量,如果数量大于0,设置确定按钮为可用,反之不可用并修改字体颜色
	private void initCount() {
		int currentSelected = Temp.tempSelMap.size();
		if (currentSelected > 0) {
			if (-1 == limitCount) {
				bucketImageActivityView.titleView.rightTextView.setText("完成 ("
						+ currentSelected + ")");
			} else {
				bucketImageActivityView.titleView.rightTextView.setText("完成 ("
						+ currentSelected + "/" + limitCount + ")");
			}

			bucketImageActivityView.titleView.rightTextView
					.setTextColor(0xFFFFFFFF);
			bucketImageActivityView.titleView.rightTextView.setEnabled(true);
			bucketImageActivityView.titleView.rightTextView.setClickable(true);
		} else {
			bucketImageActivityView.titleView.rightTextView.setText("完成");
			bucketImageActivityView.titleView.rightTextView
					.setTextColor(0x59ffffff);
			bucketImageActivityView.titleView.rightTextView.setEnabled(false);
			bucketImageActivityView.titleView.rightTextView.setClickable(false);
		}
	}

	/**
	 * 相册图片设配器
	 * 
	 * @author xuan
	 * @version $Revision: 1.0 $, $Date: 2014-9-24 下午5:24:40 $
	 */
	private class ImageAdapter extends BaseAdapter {
		@Override
		public View getView(int position, View view, ViewGroup parent) {
			// 创建View
			BucketImageListItemView bucketImageListItemView = AlbumUIHelper
					.getBucketImageListItemView(BucketImageActivity.this);
			view = bucketImageListItemView.root;
			ImageView imageView = bucketImageListItemView.imageView;
			ImageView imageViewSel = bucketImageListItemView.imageViewSel;
			ImageView hookImageSel = bucketImageListItemView.hookImageSel;
			view.setTag(bucketImageListItemView);

			// 调整ImageView的大小，padding大小为40dip,计算px值
			RelativeLayout.LayoutParams imageViewLp = (RelativeLayout.LayoutParams) imageView
					.getLayoutParams();
			imageViewLp.width = (mScreenWidth - padding) / 3;
			imageViewLp.height = imageViewLp.width;
			imageView.setLayoutParams(imageViewLp);

			RelativeLayout.LayoutParams imageViewSelLp = (RelativeLayout.LayoutParams) imageViewSel
					.getLayoutParams();
			imageViewSelLp.width = (mScreenWidth - padding) / 3;
			imageViewSelLp.height = imageViewSelLp.width;
			imageViewSel.setLayoutParams(imageViewSelLp);

			// 显示图片
			final ImageItem bucketImage = allImageList.get(position);
			bucketImageListItemView.bucketImage = bucketImage;
			ImageLoader.display(BucketImageActivity.this, imageView,
					bucketImage.thumbnailPath, bucketImage.imagePath);

			// 显示选中状态
			if (Temp.tempSelMap.containsKey(bucketImage.imageId)) {
				imageViewSel.setVisibility(View.VISIBLE);
				hookImageSel.setVisibility(View.VISIBLE);
			} else {
				imageViewSel.setVisibility(View.GONE);
				hookImageSel.setVisibility(View.GONE);
			}

			return view;
		}

		@Override
		public int getCount() {
			return allImageList.size();
		}

		@Override
		public Object getItem(int item) {
			return item;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}

}
