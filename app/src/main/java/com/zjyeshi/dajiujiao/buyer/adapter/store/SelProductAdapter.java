package com.zjyeshi.dajiujiao.buyer.adapter.store;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.task.data.store.goods.Product;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;

import java.util.List;

/**
 * Created by wuhk on 2016/7/20.
 */
public class SelProductAdapter extends MBaseAdapter {
    private Context context;
    private List<Product> dataList;

    public SelProductAdapter(Context context, List<Product> dataList) {
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
            view = LayoutInflater.from(context).inflate(R.layout.listitem_sel_product , null);
        }
        ImageView photoIv = (ImageView)view.findViewById(R.id.photoIv);
        TextView nameTv = (TextView)view.findViewById(R.id.nameTv);
        TextView speTv = (TextView)view.findViewById(R.id.speTv);
        TextView priceTv = (TextView)view.findViewById(R.id.priceTv);

        Product product = dataList.get(position);

//        initImageView(photoIv , ExtraUtil.getSmallPic(product.getPic()) , R.drawable.default_img);
        GlideImageUtil.glidImage(photoIv , ExtraUtil.getResizePic(product.getPic() , 130 , 130), R.drawable.default_img);
        initTextView(nameTv , product.getName());
        initTextView(speTv , product.getSpecifications());
        initTextView(priceTv , "¥" + ExtraUtil.format(ExtraUtil.getShowPrice(product.getPrice())));

        return view;
    }
}
