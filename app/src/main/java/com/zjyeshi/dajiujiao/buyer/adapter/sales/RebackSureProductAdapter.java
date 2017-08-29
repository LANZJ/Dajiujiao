package com.zjyeshi.dajiujiao.buyer.adapter.sales;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.enums.GoodTypeEnum;
import com.zjyeshi.dajiujiao.buyer.task.data.order.OrderProduct;
import com.zjyeshi.dajiujiao.buyer.utils.AuthUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.views.AloneEditText;

import java.util.List;

/**
 * Created by wuhk on 2017/5/15.
 */

public class RebackSureProductAdapter extends MBaseAdapter {
    private Context context;
    private List<OrderProduct> dataList;
    private int goodType;

    public RebackSureProductAdapter(Context context, List<OrderProduct> dataList , int goodType) {
        this.context = context;
        this.dataList = dataList;
        this.goodType = goodType;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (null == view){
            view = LayoutInflater.from(context).inflate(R.layout.listitem_reback_product , null);
        }

        RelativeLayout rebackGoodTypeLayout =(RelativeLayout)view.findViewById(R.id.rebackGoodTypeLayout);
        TextView goodTypeTv = (TextView)view.findViewById(R.id.goodTypeTv);
        ImageView avatarIv = (ImageView)view.findViewById(R.id.avatarIv);
        TextView nameTv = (TextView)view.findViewById(R.id.nameTv);
        TextView priceTv = (TextView)view.findViewById(R.id.priceTv);
        TextView desTv = (TextView)view.findViewById(R.id.desTv);
        final AloneEditText backBoxEt = (AloneEditText)view.findViewById(R.id.backBoxEt);
        final AloneEditText backUnitEt = (AloneEditText)view.findViewById(R.id.backUnitEt);

        final OrderProduct data = dataList.get(position);

        if(position == 0) {
            if (AuthUtil.showMarketCostTab()) {
                rebackGoodTypeLayout.setVisibility(View.VISIBLE);
                goodTypeTv.setText(GoodTypeEnum.valueOf(goodType).toString());
            } else {
                rebackGoodTypeLayout.setVisibility(View.GONE);
            }
        }else {
            rebackGoodTypeLayout.setVisibility(View.GONE);
        }

        GlideImageUtil.glidImage(avatarIv , ExtraUtil.getResizePic(data.getPic() , 120 , 120) , R.drawable.default_img);
        initTextView(nameTv ,  data.getName());
        initTextView(priceTv , "¥" + ExtraUtil.format(ExtraUtil.getShowPrice(data.getPrice())) + "/" + data.getUnit());
        initTextView(desTv  ,"x" + data.getCount() + data.getUnit() +  " " + data.getBottlesPerBox() + data.getUnit() + "/箱");
        int boxNum = (Integer.parseInt(data.getCount())) / (Integer.parseInt(data.getBottlesPerBox()));
        int unitNum = (Integer.parseInt(data.getCount())) % (Integer.parseInt(data.getBottlesPerBox()));

        backBoxEt.setText(String.valueOf(boxNum));
        backUnitEt.setText(String.valueOf(unitNum));

        backBoxEt.setEnabled(false);
        backBoxEt.setBackgroundResource(R.drawable.reback_sure_product_et_shape);
        backUnitEt.setEnabled(false);
        backUnitEt.setBackgroundResource(R.drawable.reback_sure_product_et_shape);


        return view;
    }
}
