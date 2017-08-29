package com.zjyeshi.dajiujiao.buyer.adapter.my;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.buyer.activity.my.personal.SelectCityActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.enums.ProvinceEnum;
import com.zjyeshi.dajiujiao.buyer.entity.my.DetailArea;
import com.zjyeshi.dajiujiao.buyer.receiver.info.GetAreaReceiver;
import com.zjyeshi.dajiujiao.buyer.receiver.info.SelectAreaReceive;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.my.personal.SelectDistrictActivity;
import com.zjyeshi.dajiujiao.buyer.common.PassConstans;

import java.util.List;

/**
 * 省市区适配器
 * Created by wuhk on 2015/11/13.
 */
public class AreaAdapter extends MBaseAdapter {
    private Context context;
    private List<DetailArea> dataList;

    public AreaAdapter(Context context, List<DetailArea> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(context).inflate(R.layout.listitem_area , null);

        final DetailArea area = dataList.get(position);
        TextView nameTv = (TextView)view.findViewById(R.id.nameTv);
        initTextView(nameTv , area.getName());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (area.getProvinceEnum() == ProvinceEnum.CITY) {
                    Intent intent = new Intent();
                    intent.putExtra(PassConstans.CODE , area.getCode());
                    intent.putExtra(PassConstans.SPECODE , area.getSpeCode());
                    intent.putExtra(PassConstans.SPENAME , area.getSpeName());
                    intent.setClass(context , SelectDistrictActivity.class);
                    context.startActivity(intent);
                } else if (area.getProvinceEnum() == ProvinceEnum.PROVINCE) {
                    Intent intent = new Intent();
                    intent.putExtra(PassConstans.CODE , area.getCode());
                    intent.putExtra(PassConstans.SPENAME , area.getSpeName());
                    intent.putExtra(PassConstans.SPECODE , area.getSpeCode());
                    intent.setClass(context , SelectCityActivity.class);
                    context.startActivity(intent);
                } else if (area.getProvinceEnum() == ProvinceEnum.DISTRICT) {
                    GetAreaReceiver.notifyReceiver(area.getSpeName() , area.getSpeCode());
                    SelectAreaReceive.notifyReceiver();
                }
            }
        });
        return view;
    }
}
