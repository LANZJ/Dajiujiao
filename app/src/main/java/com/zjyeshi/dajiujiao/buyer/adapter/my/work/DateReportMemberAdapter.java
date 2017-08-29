package com.zjyeshi.dajiujiao.buyer.adapter.my.work;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.task.work.data.SalemanListData;
import com.zjyeshi.dajiujiao.buyer.views.UnReadNumView;

import java.util.List;

/**
 * Created by wuhk on 2017/3/22.
 */

public class DateReportMemberAdapter extends MBaseAdapter{
    private Context context;
    private List<SalemanListData.Saleman> dataList;

    public DateReportMemberAdapter(Context context, List<SalemanListData.Saleman> dataList) {
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
        UnReadNumView unReadNumView = (UnReadNumView) view.findViewById(R.id.unReadNumView);

        SalemanListData.Saleman saleman = dataList.get(position);

        initTextView(nameTv , saleman.getName());
        unReadNumView.setNum(saleman.getUnreadNum());

        return view;
    }
}
