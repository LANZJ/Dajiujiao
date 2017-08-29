package com.zjyeshi.dajiujiao.buyer.adapter.store;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.store.GoodsDetailActivity;
import com.zjyeshi.dajiujiao.buyer.activity.store.ShopDetailActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.good.AllGoodInfo;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.buyer.widgets.addLessFormat.FormatWidget;

import java.util.List;

import static com.zjyeshi.dajiujiao.R.id.formatWidget;

/**
 * 商品列表适配器
 * Created by wuhk on 2016/6/29.
 */
public class ShopGoodAdapter extends MBaseAdapter {
    private Context context;
    private List<AllGoodInfo> dataList;
    private boolean isMarketGoods;
    private String memberId;

    public ShopGoodAdapter(Context context, List<AllGoodInfo> dataList, boolean isMarketGoods , String memberId) {
        this.context = context;
        this.dataList = dataList;
        this.isMarketGoods = isMarketGoods;
        this.memberId = memberId;
    }

    @Override
    public int getCount() {
        if (Validators.isEmpty(dataList)) {
            return 0;
        } else {
            return dataList.size();
        }
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ShopViewHolder childViewHolder ;
        if (null == view) {
            view = LayoutInflater.from(context).inflate(R.layout.listitem_shop_good, null);
            childViewHolder = new ShopViewHolder();
            childViewHolder.photoIv = (ImageView) view.findViewById(R.id.photoIv);
            childViewHolder.nameTv = (TextView) view.findViewById(R.id.nameTv);
            childViewHolder.kcTv = (TextView) view.findViewById(R.id.kcTv);
            childViewHolder. desTv = (TextView) view.findViewById(R.id.desTv);
            childViewHolder.bigPriceTv = (TextView) view.findViewById(R.id.bigPriceTv);
            childViewHolder. formatWidget = (FormatWidget) view.findViewById(formatWidget);
            childViewHolder.photoIvss= (ImageView) view.findViewById(R.id.imageView2);
            childViewHolder.bigPriceTvsb=(TextView) view.findViewById(R.id.textView11);
            view.setTag(childViewHolder);
        }else {
            childViewHolder = (ShopViewHolder) view.getTag();
        }
        ImageView photoIv = (ImageView) view.findViewById(R.id.photoIv);

        //View dividerView = (View)view.findViewById(R.id.dividerView);
       // SalesView salesView = (SalesView)view.findViewById(R.id.salesView);

        final AllGoodInfo data = dataList.get(position);
        GlideImageUtil.glidImage( photoIv, ExtraUtil.getResizePic(data.getGoodIcon() , 200 , 200), R.drawable.default_img);
//        photoIv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ShowImageActivity.startActivity(context ,  data.getGoodIcon());
//            }
//        });
        initTextView( childViewHolder.nameTv, data.getGoodName());
        int myBoxNum = (Integer.parseInt(data.getInvertory())) / (Integer.parseInt(data.getBottlesPerBox()));
        int myUnitNum = (Integer.parseInt(data.getInvertory())) % (Integer.parseInt(data.getBottlesPerBox()));
        initTextView(  childViewHolder.bigPriceTv, "¥" + data.getPriceWithUnit());
        initTextView(  childViewHolder.kcTv, "/" + data.getGoodType() + "  " + "库存:" + String.valueOf(myBoxNum) + "箱" + String.valueOf(myUnitNum) + data.getUnit());
        initTextView(  childViewHolder.desTv, "规格" + data.getFormat() + "/" + data.getUnit()
                + "  " + data.getBottlesPerBox() + data.getUnit() + "/箱");
        ShopDetailActivity activity = (ShopDetailActivity) context;

        childViewHolder.formatWidget.initData(data, activity, isMarketGoods);

        if (Validators.isEmpty(data.getSalesList())){
            childViewHolder.photoIvss.setVisibility(View.GONE);
            childViewHolder.bigPriceTvsb.setVisibility(View.GONE);
        }else{
            childViewHolder.photoIvss.setVisibility(View.VISIBLE);
            childViewHolder.bigPriceTvsb.setVisibility(View.VISIBLE);
        }

      if (!isMarketGoods){
//          childViewHolder.photoIvss.setVisibility(View.GONE);
//          childViewHolder.bigPriceTvsb.setVisibility(View.GONE);
     }else{
          childViewHolder.photoIvss.setVisibility(View.GONE);
          childViewHolder.bigPriceTvsb.setVisibility(View.GONE);
       }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoodsDetailActivity.startActivity(context, dataList, position, memberId, isMarketGoods);
            }
        });

        return view;
    }
    private static class ShopViewHolder {
        ImageView photoIv;
        TextView nameTv;
        TextView kcTv;
        TextView desTv;
        TextView bigPriceTv;
        FormatWidget formatWidget;
        ImageView photoIvss;
        TextView bigPriceTvsb;


    }
    /**
     * 刷新数据
     *
     * @param isMarketGoods
     */
    public void notifyData(boolean isMarketGoods) {
        this.isMarketGoods = isMarketGoods;
        notifyDataSetChanged();
    }
}
