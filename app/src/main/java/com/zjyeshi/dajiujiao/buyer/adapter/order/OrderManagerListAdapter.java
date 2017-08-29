package com.zjyeshi.dajiujiao.buyer.adapter.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.Order;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.buyer.views.UnReadNumView;
import com.zjyeshi.dajiujiao.R;

import java.util.List;

/**
 * 订单管理 列表适配器
 *
 * Created by zhum on 2016/6/15.
 */
public class OrderManagerListAdapter extends MBaseAdapter {
    private final Context context;
    private final List<Order> dataList;

    public OrderManagerListAdapter(Context context,
                              List<Order> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (null != dataList) {
            count = dataList.size();
        }
        return count;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        if (null == convertView) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(
                    R.layout.listitem_order_manager, null);
        }

        ImageView avatarIv = (ImageView)convertView.findViewById(R.id.avatarIv);
        TextView nameTv = (TextView)convertView.findViewById(R.id.nameTv);
        TextView personNameTv = (TextView)convertView.findViewById(R.id.personNameTv);
        TextView telTv = (TextView)convertView.findViewById(R.id.telTv);
        TextView priceTv = (TextView)convertView.findViewById(R.id.priceTv);
        UnReadNumView unReadNumView = (UnReadNumView) convertView.findViewById(R.id.unReadNumView);

        Order order = dataList.get(position);

//        initImageView(avatarIv , order.getPic() , R.drawable.default_img);
        GlideImageUtil.glidImage(avatarIv , order.getPic() , R.drawable.default_img);
        initTextView(nameTv , order.getShopName());
        initTextView(personNameTv , order.getName());
        initTextView(telTv , "联系电话:" + order.getPhone());
        initTextView(priceTv , "¥" + order.getAmount());

        unReadNumView.setNum(order.getOrderCount());
        return convertView;
    }

}
