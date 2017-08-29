package com.zjyeshi.dajiujiao.buyer.adapter.my.work;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.task.data.store.sort.Category;

import java.util.List;

/**
 * Created by wuhk on 2016/6/23.
 */
public class CategoryFilterAdapter extends MBaseAdapter {
    private Context context;
    private List<Category> dataList;

    public CategoryFilterAdapter(Context context, List<Category> dataList) {
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
        ImageView checkIv = (ImageView)view.findViewById(R.id.checkIv);

        Category category = dataList.get(position);

        if (category.isSelected()){
            checkIv.setVisibility(View.VISIBLE);
        }else{
            checkIv.setVisibility(View.GONE);
        }
        initTextView(nameTv , category.getName());

        return view;
    }
}
