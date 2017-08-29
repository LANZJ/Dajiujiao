package com.zjyeshi.dajiujiao.buyer.adapter.my.work;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zjyeshi.dajiujiao.buyer.activity.my.work.CompanyStockActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.Stock;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.R;

import java.util.List;

/**
 * 客户库存 列表适配器
 *
 * Created by zhum on 2016/6/15.
 */
public class CustomerStockListAdapter extends MBaseAdapter {
    private final Context context;
    private final List<Stock> dataList;

    public CustomerStockListAdapter(Context context,
                                   List<Stock> dataList) {
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
                    R.layout.listitem_customer_stock, null);
        }

        ImageView avatarIv = (ImageView)convertView.findViewById(R.id.avatarIv);
        TextView nameTv = (TextView)convertView.findViewById(R.id.nameTv);
        TextView numTv = (TextView)convertView.findViewById(R.id.numTv);

        final Stock stock = dataList.get(position);
//        initImageView(avatarIv , stock.getPic() , R.drawable.default_img);
        GlideImageUtil.glidImage(avatarIv , stock.getShopPic() , R.drawable.default_img);
        initTextView(nameTv , stock.getShopName());
        initTextView(numTv , "数量:" + stock.getInventory());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , CompanyStockActivity.class);
                intent.putExtra("memberInfo" , JSON.toJSONString(stock));
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
