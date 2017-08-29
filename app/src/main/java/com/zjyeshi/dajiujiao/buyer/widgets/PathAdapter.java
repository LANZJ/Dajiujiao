package com.zjyeshi.dajiujiao.buyer.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.utils.DateUtils;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.views.SquareImageView;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by wuhk on 2016/12/15.
 */
public class PathAdapter extends MBaseAdapter {
    private Context context;
    private List<PathData> dataList;

    public PathAdapter(Context context, List<PathData> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (null == view){
            view = LayoutInflater.from(context).inflate(R.layout.listitem_path , null);
        }

        TextView nameTv = (TextView)view.findViewById(R.id.nameTv);
        TextView timeTv = (TextView)view.findViewById(R.id.timeTv);
        SquareImageView avatarIv = (SquareImageView)view.findViewById(R.id.avatarIv);
        ImageView checkIv = (ImageView)view.findViewById(R.id.checkIv);
        LinearLayout pointLayout = (LinearLayout)view.findViewById(R.id.pointLayout);

        PathData pathData = dataList.get(position);
        initTextView(nameTv , pathData.getName());
        initTextView(timeTv , DateUtils.date2StringByDay(new Date(pathData.getCreationiTime())));
        GlideImageUtil.glidImage(avatarIv , pathData.getAvatar() , R.drawable.default_img);

        if (position == dataList.size() - 1){
            pointLayout.setVisibility(View.INVISIBLE);
        }

        if (pathData.isShowCheck()){
            checkIv.setVisibility(View.VISIBLE);
            if (pathData.isRefused()){
                checkIv.setImageResource(R.drawable.path_refuse);
            }else{
                checkIv.setImageResource(R.drawable.selected);
            }
        }else{
            checkIv.setVisibility(View.GONE);
        }

        return view;
    }
}
