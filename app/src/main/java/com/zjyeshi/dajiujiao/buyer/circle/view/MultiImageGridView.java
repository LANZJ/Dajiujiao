package com.zjyeshi.dajiujiao.buyer.circle.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.circle.ShowMultiImageActivity;
import com.zjyeshi.dajiujiao.buyer.views.other.MyGridView;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.utils.CircleUtil;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigapple.lib.utils.display.DisplayUtils;
import com.xuan.bigappleui.lib.view.photoview.app.BUViewImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 酒友圈九宫格图片显示gridview
 *
 * Created by xuan on 15/11/6.
 */
public class MultiImageGridView extends MyGridView {
    private static final int IMAGE_SIZE_BY_DP = 80;//九宫格图片尺寸
    private static final int SPACE_SIZE_BY_DP = 5;//间隔尺寸

    private int mImageSize;//单张图片的宽度
    private int mSpaceSize;//间隔宽度
    private List<String> mImageList;//图片列表
    private MultiImageAdapter multiImageAdapter;//适配器
    private String fromMemberId;

    public MultiImageGridView(Context context) {
        super(context);
        init();
    }

    public MultiImageGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MultiImageGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mImageSize = (int) DisplayUtils.getPxByDp((Activity) getContext(), IMAGE_SIZE_BY_DP);
        mSpaceSize = (int) DisplayUtils.getPxByDp((Activity) getContext(), SPACE_SIZE_BY_DP);
        mImageList = new ArrayList<String>();
        multiImageAdapter = new MultiImageAdapter();
        setAdapter(multiImageAdapter);
    }

    public void bindData(final List<String> list , String userId) {
        fromMemberId = userId;
        if (Validators.isEmpty(list)) {
            mImageList.clear();
        }else{
            mImageList.clear();
            mImageList.addAll(list);

            if (mImageList.size() <= 4) {
                //少于4张的只显示两列,所以调整LayoutParams
                setNumColumns(2);
                ViewGroup.LayoutParams lp = getLayoutParams();
                lp.width = mImageSize * 2 + mSpaceSize;
                setLayoutParams(lp);
            } else {
                //多于4张的,显示九宫格
                setNumColumns(3);
                ViewGroup.LayoutParams lp = getLayoutParams();
                lp.width = mImageSize * 3 + mSpaceSize * 2;
                setLayoutParams(lp);
            }
        }

        multiImageAdapter.notifyDataSetChanged();
    }

    private class MultiImageAdapter extends MBaseAdapter {

        @Override
        public int getCount() {
            return mImageList.size();
        }

        @Override
        public View getView(final int position, View view, ViewGroup arg2) {
            if (null == view) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.view_image, null);
            }

            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            ViewGroup.LayoutParams lp = imageView.getLayoutParams();
            lp.height = mImageSize;
            lp.width = mImageSize;
            imageView.setLayoutParams(lp);
            setImage(imageView, mImageList.get(position));
            //单个图片收藏
            CircleUtil.circleCollect(imageView , CircleUtil.getCollectWithPic(fromMemberId, mImageList.get(position)));
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int size =  mImageList.size();
                    String[] images = (String[])mImageList.toArray(new String[size]);
                    //点击查看大图哦
//                    BUViewImageUtils.gotoViewImageActivityForUrls(getContext() , images , position ,null);
                    BUViewImageUtils.gotoViewImageActivity(getContext() , images , position , ShowMultiImageActivity.LOADTYPE_CIRCLE , new Object[]{fromMemberId} , ShowMultiImageActivity.class);
                }
            });
            return view;
        }
    }

    public void setImage(ImageView imageView, String url) {
//        if (!Validators.isEmpty(url)) {
//            BPBitmapLoader.getInstance().display(imageView, url, BitmapDisplayConfigFactory.getInstance().getCircleImageConfig());
//        } else {
//            imageView.setImageBitmap(App.defaultBitmapGray);
//        }
        GlideImageUtil.glidImage(imageView , url , R.drawable.default_img);
    }

}
