package com.zjyeshi.dajiujiao.buyer.adapter.my.work;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.task.work.data.SubSetListData;

import java.util.List;

/**
 * Created by wuhk on 2016/8/24.
 */
public class SubMemberAdapter extends MBaseAdapter {
    private Context context;
    private List<SubSetListData.SubSet> dataList;

    public SubMemberAdapter(Context context, List<SubSetListData.SubSet> dataList) {
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
            view = LayoutInflater.from(context).inflate(R.layout.listitem_category , null);
        }
        TextView nameTv = (TextView)view.findViewById(R.id.nameTv);
        final SubSetListData.SubSet subSet = dataList.get(position);
        initTextView(nameTv , subSet.getShopName());

        return view;
    }
}
