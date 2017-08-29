package com.zjyeshi.dajiujiao.buyer.adapter.my.work;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.Employee;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;

import java.util.List;

/**
 * 选择员工
 *
 * Created by zhum on 2016/6/14.
 */
public class ChooseEmployeeAdapter extends MBaseAdapter {
    private Context context;
    private List<Employee> dataList;
    private boolean b;

    public ChooseEmployeeAdapter(Context context, List<Employee> dataList,boolean b) {
        this.context = context;
        this.dataList = dataList;
        this.b = b;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return dataList.get(arg0);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(context).inflate(R.layout.listitem_choose_employee , null);

        final Employee employee = dataList.get(position);

        ImageView avatarIv = (ImageView) view.findViewById(R.id.avatarIv);
        TextView nameTv = (TextView) view.findViewById(R.id.nameTv);
        TextView employerTv = (TextView)view.findViewById(R.id.employerTv);

        initTextView(nameTv,employee.getName());
        initTextView(employerTv,employee.getShopName());

        if (b){
            employerTv.setVisibility(View.VISIBLE);
//            initImageView(avatarIv,employee.getShopPic() , R.drawable.default_img);
            GlideImageUtil.glidImage(avatarIv , employee.getShopPic() , R.drawable.default_img);
        }else {
            employerTv.setVisibility(View.GONE);
//            initImageView(avatarIv,employee.getPic() , R.drawable.default_img);
            GlideImageUtil.glidImage(avatarIv , employee.getPic() , R.drawable.default_img);
        }
        return view;
    }
}
