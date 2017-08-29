package com.zjyeshi.dajiujiao.buyer.adapter.sales;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.sales.RefuseReason;
import com.zjyeshi.dajiujiao.buyer.views.other.IVCheckBox;

import java.util.List;

/**
 * Created by wuhk on 2017/5/11.
 */

public class RefuseReasonAdapter extends MBaseAdapter {
    private Context context;
    private List<RefuseReason> dataList;

    public RefuseReasonAdapter(Context context, List<RefuseReason> dataList) {
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
            view = LayoutInflater.from(context).inflate(R.layout.listitem_refuse_reason , null);
        }

        TextView contentTv = (TextView)view.findViewById(R.id.contentTv);
        final IVCheckBox checkIv = (IVCheckBox)view.findViewById(R.id.checkIv);

        final RefuseReason data = dataList.get(position);

        contentTv.setText(data.getContent());
        if (data.isChecked()){
            checkIv.setChecked(true);
        }else{
            checkIv.setChecked(false);
        }


        checkIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.setChecked(checkIv.isChecked());
                notifyDataSetChanged();
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkIv.performClick();
            }
        });
        return view;
    }
}
