package com.zjyeshi.dajiujiao.buyer.adapter.my;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.my.work.Rceyzs;
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
public class BuyProductadapter extends MBaseAdapter {
    private Context context ;
    private List<ALLStoreData> dataList;

    public BuyProductadapter(Context context, List<ALLStoreData> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final ShopViewHolder childViewHolder ;
        if (null == view) {
            view = LayoutInflater.from(context).inflate(R.layout.sellere_listitem_wroke, null);
            childViewHolder = new ShopViewHolder();
            childViewHolder.photoIv = (ImageView) view.findViewById(R.id.photoIv);
            childViewHolder.nameTv = (TextView) view.findViewById(R.id.nameTv);
            childViewHolder.kcTv = (TextView) view.findViewById(R.id.sendMoneyTv);

            view.setTag(childViewHolder);
        }else {
            childViewHolder = (ShopViewHolder) view.getTag();
        }
        final Shop data = dataList.get(position).getShop();
        final ShopActivity shopActivity = dataList.get(position).getShopActivity();
//        ImageView photoIv = (ImageView)view.findViewById(R.id.photoIv) ;
//        TextView nameTv = (TextView)view.findViewById(R.id.nameTv);
//        TextView sendMoneyTv = (TextView)view.findViewById(R.id.sendMoneyTv);
//        initImageView(photoIv, ExtraUtil.getSmallPic(data.getPic()) , R.drawable.default_img);
        GlideImageUtil.glidImage(childViewHolder.photoIv  , ExtraUtil.getResizePic(data.getPic() , 200 , 200) , R.drawable.default_img);
        initTextView(childViewHolder.nameTv, data.getName());
        initTextView( childViewHolder.kcTv , "共" + data.getProductCount() + "款商品");
        //点击头像显示活动
        childViewHolder.photoIv .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(PassConstans.SHOPACTIVITY, shopActivity);
                intent.putExtra("shopPic", data.getPic());
                intent.putExtra("shopName" , data.getName());
                intent.setClass(context, Rceyzs.class);
                context.startActivity(intent);
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(Rceyzs.RE, data.getId());
                intent.setClass(context , Rceyzs.class);
                context.startActivity(intent);
            }
        });
        return view;
    }

    private static class ShopViewHolder {
        ImageView photoIv;
        TextView nameTv;
        TextView kcTv;



    }
}
