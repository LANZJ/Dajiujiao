package com.jopool.crow.imkit.utils.album.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jopool.crow.imkit.utils.album.BUAlbum;
import com.jopool.crow.imkit.utils.album.core.AlbumHelper;
import com.jopool.crow.imkit.utils.album.core.ImageLoader;
import com.jopool.crow.imkit.utils.album.entity.BucketActivityView;
import com.jopool.crow.imkit.utils.album.entity.BucketListItemView;
import com.jopool.crow.imkit.utils.album.entity.ImageBucket;
import com.jopool.crow.imkit.utils.album.entity.ImageItem;
import com.jopool.crow.imlib.utils.CWValidator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 这个类是显示所有相册界面
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-11-10 上午9:53:46 $
 */
public class BucketActivity extends Activity {
	public static final int BACK_FROM_ALUBEN = 2;// 去某个相册获取图片
	public static final String PARAM_IMAGELIST = "param.imagelist";
	public static final String PARAM_BUCKETNAME = "param.bucketname";

	public static final String TAG = "BucketActivity";
	private BucketActivityView bucketActivityView;

	/** 相册列表 */
	private List<ImageBucket> bucketList;

	/** 判断是否是多选模式，true表示多选，false表示单选即选择后马上返回 */
	private boolean ifMultiple;
	/** 多选时可限制选择的图片张数，如果是-1表示可以无限制的选 */
	private int limitCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bucketActivityView = AlbumUIHelper.getBucketActivityView(this);
		setContentView(bucketActivityView.root);
		ImageLoader.init(this);

		// 获取参数
		ifMultiple = getIntent().getBooleanExtra(
				BUAlbum.PARAM_IF_MULTIPLE_CHOICE, true);
		if (!ifMultiple) {
			bucketActivityView.titleView.rightTextView.setVisibility(View.GONE);
		}
		limitCount = getIntent().getIntExtra(BUAlbum.PARAM_LIMIT_COUNT, -1);

		// 返回按钮事件
		bucketActivityView.titleView.leftTextView
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						BucketActivity.this.finish();
					}
				});

		// 选择后确定按钮事件
		bucketActivityView.titleView.rightTextView
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						BUAlbum.selList.addAll(Temp.tempSelMap.values());
						setResult(RESULT_OK, getIntent());
						BucketActivity.this.finish();
					}
				});

		// 获取图库中的所有相册图片
		AlbumHelper.init(this);
		Map<String, ImageBucket> bucketMap = AlbumHelper.instance()
				.getImagesBucketMapSortByDatemodify(true);
		bucketList = new ArrayList<ImageBucket>(bucketMap.values());

		// 设置设配器数据，并加上点击事件
		bucketActivityView.gridView.setAdapter(new BucketListAdapter());
		bucketActivityView.gridView
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// 点击跳转到具体相册，显示相册内所有图片
						ImageBucket bucket = (ImageBucket) view.getTag();
						Intent intent = new Intent();
						intent.setClass(BucketActivity.this,
								BucketImageActivity.class);
						intent.putExtra(PARAM_IMAGELIST,
								(Serializable) bucket.imageList);
						intent.putExtra(PARAM_BUCKETNAME, bucket.bucketName);
						intent.putExtra(BUAlbum.PARAM_IF_MULTIPLE_CHOICE,
								ifMultiple);
						intent.putExtra(BUAlbum.PARAM_LIMIT_COUNT,
								limitCount);
						startActivityForResult(intent, BACK_FROM_ALUBEN);
					}
				});
	}

	@Override
	protected void onResume() {
		super.onResume();
		initCount();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 清理临时选择的图片
		Temp.tempSelMap.clear();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}

		if (BACK_FROM_ALUBEN == requestCode) {
			setResult(RESULT_OK, getIntent());
			BucketActivity.this.finish();
		}
	}

	// 更新选择的图片数量,如果数量大于0,设置确定按钮为可用,反之不可用并修改字体颜色
	private void initCount() {
		int currentSelected = Temp.tempSelMap.size();
		if (currentSelected > 0) {
			if (-1 == limitCount) {
				bucketActivityView.titleView.rightTextView.setText("完成 ("
						+ currentSelected + ")");
			} else {
				bucketActivityView.titleView.rightTextView.setText("完成 ("
						+ currentSelected + "/"
						+ (limitCount - currentSelected) + ")");
			}

			bucketActivityView.titleView.rightTextView.setTextColor(0xFFFFFFFF);
			bucketActivityView.titleView.rightTextView.setEnabled(true);
			bucketActivityView.titleView.rightTextView.setClickable(true);
		} else {
			bucketActivityView.titleView.rightTextView.setText("完成");
			bucketActivityView.titleView.rightTextView.setTextColor(0x59ffffff);
			bucketActivityView.titleView.rightTextView.setEnabled(false);
			bucketActivityView.titleView.rightTextView.setClickable(false);
		}
	}

	/**
	 * 手机相册设配器
	 * 
	 * @author xuan
	 * @version $Revision: 1.0 $, $Date: 2014-9-24 下午3:04:05 $
	 */
	private class BucketListAdapter extends BaseAdapter {
		@Override
		public View getView(int position, View view, ViewGroup parent) {
			BucketListItemView bucketListItemView = AlbumUIHelper
					.getBucketListItemView(BucketActivity.this);
			view = bucketListItemView.root;
			ImageView imageView = bucketListItemView.imageView;
			TextView nameTextView = bucketListItemView.nameTextView;
			TextView countTextView = bucketListItemView.countTextView;

			ImageBucket imageBucket = bucketList.get(position);
			view.setTag(imageBucket);

			nameTextView.setText(imageBucket.bucketName);
			countTextView.setText(String.valueOf(imageBucket.imageList.size()));

			List<ImageItem> imageItemList = imageBucket.imageList;
			if (!CWValidator.isEmpty(imageItemList)) {
				String thumbPath = imageBucket.imageList.get(0).thumbnailPath;
				String sourcePath = imageBucket.imageList.get(0).imagePath;
				ImageLoader.display(BucketActivity.this, imageView, thumbPath,
						sourcePath);
			} else {
				imageView.setBackgroundColor(Color.parseColor("#D4D4D4"));
				Log.e(TAG, "没有图片在文件夹：" + imageBucket.bucketName);
			}
			return bucketListItemView.root;
		}

		@Override
		public int getCount() {
			return bucketList.size();
		}

		@Override
		public Object getItem(int object) {
			return object;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}

}
