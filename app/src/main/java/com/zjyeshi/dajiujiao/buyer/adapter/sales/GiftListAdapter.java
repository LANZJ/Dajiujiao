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
import com.zjyeshi.dajiujiao.buyer.receiver.sales.SelectSalesGiftReceiver;
import com.zjyeshi.dajiujiao.buyer.task.sales.data.GiftListData;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.buyer.views.other.IVCheckBox;

import java.util.List;

/**
 * Created by wuhk on 2017/4/26.
 */

public class GiftListAdapter extends MBaseAdapter{
    private Context context;
    private List<GiftListData.Gift> dataList;

    public GiftListAdapter(Context context, List<GiftListData.Gift> dataList) {
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
            view = LayoutInflater.from(context).inflate(R.layout.listitem_gift_list , null);
        }

        ImageView photoIv = (ImageView)view.findViewById(R.id.photoIv);
        TextView giftNameTv = (TextView)view.findViewById(R.id.giftNameTv);
        TextView giftPriceTv = (TextView)view.findViewById(R.id.giftPriceTv);
        final IVCheckBox selectIv = (IVCheckBox)view.findViewById(R.id.selectIv);

        final GiftListData.Gift gift = dataList.get(position);

        GlideImageUtil.glidImage(photoIv , ExtraUtil.getResizePic(gift.getPic() , 80 , 80) , R.drawable.default_img);
        giftNameTv.setText(gift.getName());
        giftPriceTv.setText("¥" + gift.getPrice());

        if(gift.isSelected()){
            selectIv.setChecked(true);
        }else{
            selectIv.setChecked(false);
        }

        selectIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gift.setSelected(selectIv.isChecked());
                notifyDataSetChanged();
                ((Activity)context).finish();
                SelectSalesGiftReceiver.notifyReceiver(gift.getId() , gift.getName());
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectIv.performClick();
            }
        });

        return view;
    }
}
