package com.zjyeshi.dajiujiao.buyer.adapter.my.work;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigappleui.lib.view.photoview.app.BUViewImageUtils;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.circle.ShowMultiImageActivity;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.buyer.views.SquareImageView;

import java.util.List;

/**
 * Created by wuhk on 2016/6/21.
 */
public class LeaveDetailImageAdapter extends MBaseAdapter {
    private Context context;
    private List<String> dataList;

    public LeaveDetailImageAdapter(Context context, List<String> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        if(Validators.isEmpty(dataList)){
            return 0;
        }else{
            return dataList.size();
        }
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        if (null == view) {
            view = LayoutInflater.from(context).inflate(R.layout.view_leave_image, null);
        }
        SquareImageView imageView = (SquareImageView) view.findViewById(R.id.imageView);

//        initImageViewDefault(imageView, ExtraUtil.getSmallPic(dataList.get(position)));
        GlideImageUtil.glidImage(imageView ,dataList.get(position), R.drawable.default_img);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int size =  dataList.size();
                String[] images = (String[])dataList.toArray(new String[size]);
                //点击查看大图哦
//                    BUViewImageUtils.gotoViewImageActivityForUrls(getContext() , images , position ,null);
                BUViewImageUtils.gotoViewImageActivity(context , images , position , ShowMultiImageActivity.LOADTYPE_CIRCLE , null , ShowMultiImageActivity.class);
            }
        });
        return view;

    }
}
