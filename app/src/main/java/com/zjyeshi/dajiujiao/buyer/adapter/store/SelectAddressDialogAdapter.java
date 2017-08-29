package com.zjyeshi.dajiujiao.buyer.adapter.store;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.my.Address;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;

import java.util.List;

/**
 * Created by wuhk on 2016/7/19.
 */
public class SelectAddressDialogAdapter extends MBaseAdapter {
    private Context context;
    private List<Address> dataList;

    public SelectAddressDialogAdapter(Context context, List<Address> dataList) {
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
            view = LayoutInflater.from(context).inflate(R.layout.listitem_sel_address_dialog , null);
        }
        TextView nameTv = (TextView)view.findViewById(R.id.nameTv);
        TextView addressTv = (TextView)view.findViewById(R.id.addressTv);

        Address address = dataList.get(position);
        if (address.getAddressType() == 2){
            initTextView(nameTv , address.getName()+ "(花名册地址)");
        }else{
            initTextView(nameTv , address.getName());
        }
        String area = ExtraUtil.getAreaByCode(address.getArea());
        initTextView(addressTv , area + address.getAddress());

        return view;
    }
}
