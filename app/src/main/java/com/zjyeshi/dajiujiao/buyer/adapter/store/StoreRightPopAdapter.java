package com.zjyeshi.dajiujiao.buyer.adapter.store;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.receiver.buy.GoodsSortChangeReceiver;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.task.data.store.sort.SortData;

import java.util.List;

/**
 * 商家右侧popView适配器
 *
 * Created by wuhk on 2015/10/12.
 */
public class StoreRightPopAdapter extends MBaseAdapter {
    private Context context ;
    private List<SortData> dataList ;

    public StoreRightPopAdapter(Context context, List<SortData> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final SortData data = dataList.get(position);
        view = LayoutInflater.from(context).inflate(R.layout.listitem_store_right , null);
        TextView typeTv = (TextView)view.findViewById(R.id.typeTv);
        typeTv.setText(data.getCategory().getName());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryId = data.getCategory().getId();
                GoodsSortChangeReceiver.notifyReceiver(context ,categoryId);

            }
        });
        return view;
    }
}
