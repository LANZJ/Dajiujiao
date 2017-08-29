package com.zjyeshi.dajiujiao.buyer.widgets.level;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.widgets.level.callback.LevelCallback;
import com.zjyeshi.dajiujiao.R;

import java.util.List;

/**
 * 评价等级控件
 * Created by wuhk on 2016/1/21.
 */
public class LevelAdapter extends MBaseAdapter {
    private Context context;
    private List<Integer> dataList;
    private LevelCallback levelCallback;

    public LevelAdapter(Context context, List<Integer> dataList, LevelCallback levelCallback) {
        this.context = context;
        this.dataList = dataList;
        this.levelCallback = levelCallback;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(context).inflate(R.layout.widget_level_item  , null);
        final ImageView levelIv = (ImageView)view.findViewById(R.id.levelIv);
        final int imageId = dataList.get(position);
        levelIv.setImageResource(imageId);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < position + 1; i++) {
                    dataList.set(i, R.drawable.star);
                }
                for (int i = position + 1; i < dataList.size(); i++) {
                    dataList.set(i, R.drawable.no_comment);
                }
//                ToastUtil.toast(String.valueOf(position + 1) + "星");
                levelCallback.dealWithLevel(position+1);
                notifyDataSetChanged();
            }
        });
        return view;
    }
}
