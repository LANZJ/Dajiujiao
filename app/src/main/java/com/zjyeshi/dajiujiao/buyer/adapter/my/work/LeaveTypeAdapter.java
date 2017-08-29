package com.zjyeshi.dajiujiao.buyer.adapter.my.work;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.R;

import java.util.List;

/**
 * Created by wuhk on 2016/6/20.
 */
public class LeaveTypeAdapter extends MBaseAdapter {
    private Context context;
    private List<String> dataList;

    public LeaveTypeAdapter(Context context, List<String> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(context).inflate(R.layout.listitem_leavetype , null);
        String content = dataList.get(position);

        TextView contentTv = (TextView)view.findViewById(R.id.contentTv);
        initTextView(contentTv , content);

        return view;
    }
}
