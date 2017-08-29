package com.zjyeshi.dajiujiao.buyer.adapter.sales;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.sales.SelectSalesShopActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.Employee;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.buyer.views.other.IVCheckBox;

import java.util.List;

/**
 * Created by wuhk on 2017/4/27.
 */

public class SelelctSalesShopAdapter extends MBaseAdapter {
    private Context context;
    private List<Employee> dataList;
    private boolean hideSelect;

    public SelelctSalesShopAdapter(Context context, List<Employee> dataList , boolean hideSelect) {
        this.context = context;
        this.dataList = dataList;
        this.hideSelect = hideSelect;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (null == view){
            view = LayoutInflater.from(context).inflate(R.layout.listitem_select_sale_shop , null);
        }

        ImageView avatarIv = (ImageView)view.findViewById(R.id.avatarIv);
        TextView nameTv = (TextView)view.findViewById(R.id.nameTv);
        final IVCheckBox checkIv = (IVCheckBox)view.findViewById(R.id.checkIv);

        final Employee employee = dataList.get(position);
        GlideImageUtil.glidImage(avatarIv , ExtraUtil.getResizePic(employee.getShopPic() , 80 , 80) , R.drawable.default_img);
        initTextView(nameTv , employee.getShopName());




        if (hideSelect){
            checkIv.setVisibility(View.GONE);
        }else{

            if (employee.isSelected()){
                checkIv.setChecked(true);
            }else{
                checkIv.setChecked(false);
            }
            checkIv.setVisibility(View.VISIBLE);
            checkIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    employee.setSelected(checkIv.isChecked());
                    notifyData(hideSelect);
                    ((SelectSalesShopActivity)context).refreshSelect();
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkIv.performClick();
                }
            });
        }

        return view;
    }

    public void notifyData(boolean hideSelect){
        this.hideSelect = hideSelect;
        notifyDataSetChanged();

    }
}
