package com.zjyeshi.dajiujiao.buyer.adapter.my.work;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.Employee;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.R;

import java.util.List;

/**
 * Created by wuhk on 2016/9/12.
 */
public class HmcChildrenAdapter extends MBaseAdapter {
    private Context context;
    private List<Employee> dataList;

    public HmcChildrenAdapter(Context context, List<Employee> dataList) {
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
            view = LayoutInflater.from(context).inflate(R.layout.listitem_hmc_children , null);
        }

        Employee employee = dataList.get(position);

        ImageView avatarIv = (ImageView) view.findViewById(R.id.avatarIv);
        TextView nameTv = (TextView) view.findViewById(R.id.nameTv);
        TextView companyTv = (TextView)view.findViewById(R.id.companyTv);
        TextView areaTv = (TextView)view.findViewById(R.id.areaTv);

//        initImageView(avatarIv,employee.getShopPic() , R.drawable.default_img);
        GlideImageUtil.glidImage(avatarIv , employee.getShopPic() , R.drawable.default_img);
        initTextView(nameTv,employee.getName());
        initTextView(companyTv,employee.getShopName());
        initTextView(areaTv , employee.getArea());

        return view;
    }
}
