package com.zjyeshi.dajiujiao.buyer.adapter.seller;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.store.ShopDetailActivity;
import com.zjyeshi.dajiujiao.buyer.activity.store.ShopSalesActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.common.PassConstans;
import com.zjyeshi.dajiujiao.buyer.task.data.store.homepage.ALLStoreData;
import com.zjyeshi.dajiujiao.buyer.task.data.store.homepage.Shop;
import com.zjyeshi.dajiujiao.buyer.task.data.store.homepage.ShopActivity;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;

import java.util.List;

/**
 * 商品采购列表适配器
 *
 * Created by wuhk on 2015/11/5.
 */
public class BuyProductAdapter extends MBaseAdapter {
    private Context context ;
    private List<ALLStoreData> dataList;

    public BuyProductAdapter(Context context, List<ALLStoreData> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final Shop data = dataList.get(position).getShop();
        final ShopActivity shopActivity = dataList.get(position).getShopActivity();
        view = LayoutInflater.from(context).inflate(R.layout.seller_listitem_shop, null);
        ImageView photoIv = (ImageView)view.findViewById(R.id.photoIv) ;
        TextView nameTv = (TextView)view.findViewById(R.id.nameTv);
        TextView sendMoneyTv = (TextView)view.findViewById(R.id.sendMoneyTv);
        TextView scoreTv = (TextView)view.findViewById(R.id.scoreTv) ;

//        initImageView(photoIv, ExtraUtil.getSmallPic(data.getPic()) , R.drawable.default_img);
        GlideImageUtil.glidImage(photoIv , ExtraUtil.getResizePic(data.getPic() , 200 , 200) , R.drawable.default_img);
        initTextView(nameTv, data.getName());
        initTextView(sendMoneyTv, "共" + data.getProductCount() + "款商品");
        initTextView(scoreTv, data.getLevel() + "分");
        //点击头像显示活动
        photoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(PassConstans.SHOPACTIVITY, shopActivity);
                intent.putExtra("shopPic", data.getPic());
                intent.putExtra("shopName" , data.getName());
                intent.setClass(context, ShopSalesActivity.class);
                context.startActivity(intent);
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(ShopDetailActivity.PARAM_SHOPID, data.getId());
                intent.setClass(context , ShopDetailActivity.class);
                context.startActivity(intent);
            }
        });
        return view;
    }
}
