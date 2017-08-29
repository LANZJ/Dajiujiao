package com.zjyeshi.dajiujiao.buyer.adapter.store;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xuan.bigappleui.lib.view.listview.BUHighHeightListView;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.entity.GoodsCar;
import com.zjyeshi.dajiujiao.buyer.entity.enums.GoodTypeEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.SelectEnum;
import com.zjyeshi.dajiujiao.buyer.entity.store.GoodCar;
import com.zjyeshi.dajiujiao.buyer.receiver.buy.BuyCarSelectChangeReceiver;
import com.zjyeshi.dajiujiao.buyer.views.other.IVCheckBox;

import java.util.List;

/**
 * 购物车适配器
 *
 * Created by wuhk on 2015/10/15.
 */
public class BuyCarAllListAdapter extends MBaseAdapter {
    private List<GoodCar> dataList;
    private Context context;
    private BuyCarSwipeViewAdapter buyCarSwipeViewAdapter;

    public BuyCarAllListAdapter(Context context, List<GoodCar> dataList) {
        this.context = context;
        this.dataList = dataList;

    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        if (null == view){
            view = LayoutInflater.from(context).inflate(R.layout.listitem_buycar_all , null) ;
        }
        final IVCheckBox selectIv = (IVCheckBox)view.findViewById(R.id.selectIv);
        final GoodCar goodCar = dataList.get(position);
        TextView dianNameTv = (TextView)view.findViewById(R.id.dianNameTv);
        TextView typeTv = (TextView)view.findViewById(R.id.typeTv);

        final List<GoodsCar> childList = goodCar.getGoodList();
        BUHighHeightListView listView = (BUHighHeightListView)view.findViewById(R.id.listView);

        buyCarSwipeViewAdapter = new BuyCarSwipeViewAdapter(context , childList , goodCar.getType());
        listView.setAdapter(buyCarSwipeViewAdapter);

        if (position == 0){
            typeTv.setVisibility(View.VISIBLE);
            typeTv.setText(goodCar.getType());
        }else{
            typeTv.setVisibility(View.GONE);
        }
//        if (AuthUtil.showMarketCostTab()){
//
//        }else{
//            typeTv.setVisibility(View.GONE);
//        }

        initTextView(dianNameTv, goodCar.getStoreName());
        selectIv.setChecked(goodCar.isSelected());
        selectIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodCar.setIsSelected(selectIv.isChecked());
                for (GoodsCar goodsCar : childList) {
                    if (selectIv.isChecked()) {
                        goodsCar.setStatus(SelectEnum.SELECTED.getValue());
                    } else {
                        goodsCar.setStatus(SelectEnum.UNSELECT.getValue());
                    }
                    if (goodCar.getType().equals(GoodTypeEnum.NORMAL_BUY.toString())){
                        DaoFactory.getGoodsCarDao().replace(goodsCar);
                    }else{
                        DaoFactory.getMarketGoodsCarDao().replace(goodsCar);
                    }
                }
                BuyCarSelectChangeReceiver.notifyReceiver(BuyCarSelectChangeReceiver.SELECT_REFRESH);
            }
        });

        return view;
    }
}
