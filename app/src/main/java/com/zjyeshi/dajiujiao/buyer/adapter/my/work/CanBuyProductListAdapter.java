package com.zjyeshi.dajiujiao.buyer.adapter.my.work;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.buyer.activity.my.work.CanBuyProductInfoActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.task.data.store.goods.Product;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.R;

import java.util.List;

/**
 * Created by wuhk on 2016/12/14.
 */
public class CanBuyProductListAdapter extends MBaseAdapter {
    private Context context;
    private List<Product> dataList;
    private String shopId;

    public CanBuyProductListAdapter(Context context, List<Product> dataList, String shopId) {
        this.context = context;
        this.dataList = dataList;
        this.shopId = shopId;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (null == view) {
            view = LayoutInflater.from(context).inflate(R.layout.listitem_can_buy_product_list, null);
        }

        ImageView avatarIv = (ImageView) view.findViewById(R.id.avatarIv);
        TextView nameTv = (TextView) view.findViewById(R.id.nameTv);
        TextView typeTv = (TextView) view.findViewById(R.id.typeTv);
        TextView priceTv = (TextView) view.findViewById(R.id.priceTv);
        TextView marketTv = (TextView) view.findViewById(R.id.marketTv);

        final Product product = dataList.get(position);

        GlideImageUtil.glidImage(avatarIv, product.getPic(), R.drawable.default_img);
        initTextView(nameTv, product.getName());
        initTextView(typeTv, product.getSpecifications() + "/" + product.getUnit()
                + "  " + product.getBottlesPerBox() + product.getUnit() + "/箱");
        initTextView(priceTv, "¥" + ExtraUtil.format(Float.parseFloat(product.getPrice()) / 100) + "/" + product.getUnit());
        initTextView(marketTv , "返还市场支持费用:¥" + ExtraUtil.format(Float.parseFloat(product.getMarketCost())/100));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CanBuyProductInfoActivity.startActivity(context, shopId, product);
            }
        });
        return view;

    }
}
