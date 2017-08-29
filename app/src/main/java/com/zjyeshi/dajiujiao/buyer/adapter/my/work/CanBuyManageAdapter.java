package com.zjyeshi.dajiujiao.buyer.adapter.my.work;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.buyer.activity.my.work.CanBuyProductListActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.Stock;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;

import java.util.List;

/**
 * 可购品项管理
 * Created by wuhk on 2016/12/14.
 */
public class CanBuyManageAdapter extends MBaseAdapter {
    private Context context;
    private List<Stock> dataList;

    public CanBuyManageAdapter(Context context, List<Stock> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView){
            convertView = LayoutInflater.from(context).inflate(R.layout.listitem_can_buy_manage , null);
        }

        ImageView avatarIv = (ImageView)convertView.findViewById(R.id.avatarIv);
        TextView nameTv = (TextView)convertView.findViewById(R.id.nameTv);

        final Stock stock = dataList.get(position);
        GlideImageUtil.glidImage(avatarIv , stock.getShopPic() , R.drawable.default_img);
        initTextView(nameTv , stock.getShopName());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CanBuyProductListActivity.startActivity(context , stock);
            }
        });
        return convertView;
    }
}
