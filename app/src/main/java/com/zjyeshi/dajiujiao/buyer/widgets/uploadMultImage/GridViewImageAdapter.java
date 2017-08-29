package com.zjyeshi.dajiujiao.buyer.widgets.uploadMultImage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xuan.bigappleui.lib.view.photoview.app.BUViewImageUtils;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.circle.ShowMultiImageActivity;
import com.zjyeshi.dajiujiao.buyer.views.SquareImageView;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.model.UploadGridViewImageModel;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;

import java.util.List;

/**
 * Created by wuhk on 2016/8/23.
 */
public class GridViewImageAdapter extends MBaseAdapter {
    private Context context;
    private List<String> dataList;
    private UploadGridViewImageModel uploadGridViewImageModel;

    public GridViewImageAdapter(Context context, List<String> dataList , UploadGridViewImageModel uploadGridViewImageModel) {
        this.context = context;
        this.dataList = dataList;
        this.uploadGridViewImageModel = uploadGridViewImageModel;
    }

    @Override
    public int getCount() {
        if (dataList.size() == GridViewImageWidget.MAX_IMAGE_SIZE) {
            return dataList.size();
        } else {
            return dataList.size() + 1;
        }
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        if (null == view){
            view = LayoutInflater.from(context).inflate(R.layout.view_leave_image , null);
        }

        final SquareImageView imageIv = (SquareImageView)view.findViewById(R.id.imageView);

        if (dataList.size() == position){
            //最后一个+号
//            imageIv.setImageResource(R.drawable.icon_add_pic);
            GlideImageUtil.glidImage(imageIv , "" , R.drawable.icon_add_pic);
            imageIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadGridViewImageModel.init(imageIv , context , GridViewImageAdapter.this);
                }
            });
        }else{
            GlideImageUtil.glidImage(imageIv , dataList.get(position) , R.drawable.default_img);
            imageIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int size = dataList.size();
                    String[] images = (String[])dataList.toArray(new String[size]);
                    BUViewImageUtils.gotoViewImageActivity(context , images , position , ShowMultiImageActivity.LOADTYPE_CIRCLE , null , ShowMultiImageActivity.class);
                }
            });
        }
        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        uploadGridViewImageModel.onActivityResult(requestCode , resultCode , data , dataList);
    }
}
