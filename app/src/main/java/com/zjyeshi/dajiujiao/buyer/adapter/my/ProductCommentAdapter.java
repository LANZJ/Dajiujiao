package com.zjyeshi.dajiujiao.buyer.adapter.my;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.buyer.activity.order.OrderCommentActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.common.PassConstans;
import com.zjyeshi.dajiujiao.buyer.task.data.order.OrderProduct;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by wuhk on 2016/1/12.
 */
public class ProductCommentAdapter extends MBaseAdapter {
    private Context context;
    private List<OrderProduct> dataList;
    private String orderId;
    private DecimalFormat decimalFormat=new DecimalFormat("0.00");

    public ProductCommentAdapter(Context context, List<OrderProduct> dataList, String orderId) {
        this.context = context;
        this.dataList = dataList;
        this.orderId = orderId;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        if (null == view){
            view = LayoutInflater.from(context).inflate(R.layout.listitem_product_comment , null);
        }
        final OrderProduct orderProduct = dataList.get(position);

        ImageView photoIv = (ImageView)view.findViewById(R.id.photoIv);
        TextView nameTv = (TextView)view.findViewById(R.id.nameTv);
        TextView contentTv = (TextView)view.findViewById(R.id.contentTv);
        TextView priceTv = (TextView)view.findViewById(R.id.priceTv);
        TextView numTv = (TextView)view.findViewById(R.id.numTv);
        TextView commentTv = (TextView)view.findViewById(R.id.commentTv);

        GlideImageUtil.glidImage(photoIv , ExtraUtil.getResizePic(orderProduct.getPic() , 150 , 150) , R.drawable.default_img);
        initTextView(nameTv ,orderProduct.getName());
        initTextView(contentTv , orderProduct.getSpecifications());
        initTextView(priceTv , "¥"+ decimalFormat.format(Float.parseFloat(orderProduct.getPrice()) / 100));
        initTextView(numTv , "x" + orderProduct.getCount());

        numTv.setVisibility(View.GONE);

//        initTextView(commentTv , "去评价");
        if (orderProduct.isEvaluated()){
            initTextView(commentTv , "追加评价");
        }else{
            initTextView(commentTv , "去评价");
        }

        commentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(PassConstans.PRODUCTID  , orderProduct.getId());
                intent.putExtra(PassConstans.ORDERID  , orderId);
                intent.putExtra(PassConstans.POSITION , position);
                intent.setClass(context , OrderCommentActivity.class);
                context.startActivity(intent);
            }
        });

        return view;
    }
}
