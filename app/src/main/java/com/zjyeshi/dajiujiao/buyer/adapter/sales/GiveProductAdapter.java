package com.zjyeshi.dajiujiao.buyer.adapter.sales;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.receiver.sales.GiveProductReceiver;
import com.zjyeshi.dajiujiao.buyer.receiver.sales.SelectSalesGiftReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.store.goods.Product;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.buyer.views.other.IVCheckBox;

import java.util.List;

/**
 * Created by wuhk on 2017/5/9.
 */

public class GiveProductAdapter extends MBaseAdapter {
    private Context context;
    private List<Product> dataList;

    public GiveProductAdapter(Context context, List<Product> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(int position, View view, final ViewGroup parent) {
        if (null == view){
            view = LayoutInflater.from(context).inflate(R.layout.listitem_all_can_buy , null);
        }

        ImageView avatarIv = (ImageView)view.findViewById(R.id.avatarIv);
        TextView nameTv = (TextView)view.findViewById(R.id.nameTv);
        TextView typeTv = (TextView)view.findViewById(R.id.typeTv);
        TextView priceTv = (TextView)view.findViewById(R.id.priceTv);
        final IVCheckBox checkIv = (IVCheckBox)view.findViewById(R.id.checkIv);
        TextView marketTv = (TextView) view.findViewById(R.id.marketTv);

        final Product product = dataList.get(position);
        GlideImageUtil.glidImage(avatarIv , product.getPic() , R.drawable.default_img);
        initTextView(nameTv , product.getName());
        initTextView(typeTv , product.getSpecifications() + "/" + product.getUnit()
                + "  " + product.getBottlesPerBox() + product.getUnit() + "/箱");
        initTextView(priceTv , "¥" + ExtraUtil.format(Float.parseFloat(product.getPrice()) / 100) + "/" + product.getUnit());

        initTextView(marketTv , "返还市场支持费用:¥" + ExtraUtil.format(Float.parseFloat(product.getMarketCost())/100));
        marketTv.setVisibility(View.GONE);


        if (product.isSelected()){
            checkIv.setChecked(true);
        }else{
            checkIv.setChecked(false);
        }

        checkIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                product.setSelected(checkIv.isChecked());
                notifyDataSetChanged();
                ((Activity)context).finish();
                GiveProductReceiver.notifyReceiver(product.getId() , product.getName());
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkIv.performClick();
            }
        });

        return view;
    }
}
