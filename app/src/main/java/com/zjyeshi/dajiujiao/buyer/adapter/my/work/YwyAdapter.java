package com.zjyeshi.dajiujiao.buyer.adapter.my.work;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.task.work.data.SalemanListData;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.buyer.views.UnReadNumView;

import java.util.List;

/**
 * Created by wuhk on 2016/6/24.
 */
public class YwyAdapter extends MBaseAdapter {
    private Context context;
    private List<SalemanListData.Saleman> dataList;

    public YwyAdapter(Context context, List<SalemanListData.Saleman> dataList) {
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
            view = LayoutInflater.from(context).inflate(R.layout.listitem_choose_employee , null);
        }

        ImageView avatarIv = (ImageView) view.findViewById(R.id.avatarIv);
        TextView nameTv = (TextView) view.findViewById(R.id.nameTv);
        TextView employerTv = (TextView)view.findViewById(R.id.employerTv);

        SalemanListData.Saleman saleman = dataList.get(position);

        initTextView(nameTv , saleman.getName());
        GlideImageUtil.glidImage(avatarIv , saleman.getPic() , R.drawable.default_img);

        employerTv.setVisibility(View.GONE);

        return view;
    }
}
