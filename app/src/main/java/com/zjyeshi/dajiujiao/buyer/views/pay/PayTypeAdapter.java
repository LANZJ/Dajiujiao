package com.zjyeshi.dajiujiao.buyer.views.pay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.entity.enums.PayTypeEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * 支付类型适配器
 * Created by wuhk on 2016/9/22.
 */
public class PayTypeAdapter extends MBaseAdapter {
    private Context context;
    private SelectCallback selectCallback;
    private List<PayTypeEntity> dataList = new ArrayList<PayTypeEntity>();

    public PayTypeAdapter(Context context , SelectCallback selectCallback , boolean onlyXianxia) {
        this.context = context;
        this.selectCallback = selectCallback;
        initDataList(onlyXianxia);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (null == view){
            view = LayoutInflater.from(context).inflate(R.layout.listitem_pay_type , null);
        }

        ImageView iconIv = (ImageView)view.findViewById(R.id.iconIv);
        TextView payTypeTv = (TextView)view.findViewById(R.id.payTypeTv);
        TextView payDesTv = (TextView)view.findViewById(R.id.payDesTv);
        ImageView checkIv = (ImageView)view.findViewById(R.id.checkIv);

        final PayTypeEntity data = dataList.get(position);

        iconIv.setImageResource(data.getPayIconId());
        initTextView(payTypeTv , data.getPayType());
        initTextView(payDesTv , data.getPayDes());

        if (data.isSelected()){
            checkIv.setVisibility(View.VISIBLE);
        }else{
            checkIv.setVisibility(View.GONE);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!data.isSelected()){
                    data.setSelected(true);
                    for(PayTypeEntity payTypeEntity : dataList){
                        if (!payTypeEntity.getPayEnum().equals(data.getPayEnum())){
                            payTypeEntity.setSelected(false);
                        }
                    }
                }
                selectCallback.select(data);
                notifyDataSetChanged();
            }
        });
        return view;
    }

    private void initDataList(boolean onlyXianxia){
        dataList.clear();
        if (!onlyXianxia){
            //线下支付
            PayTypeEntity linePay = new PayTypeEntity();
            linePay.setPayType("线下支付");
            // linePay.setPayDes("也可直接线下支付");
            linePay.setPayIconId(R.drawable.settle_crash);
            linePay.setPayEnum(PayTypeEnum.XIANXIA);
            linePay.setSelected(true);
            dataList.add(linePay);
            //微信支付
            PayTypeEntity wxPay = new PayTypeEntity();
            wxPay.setPayType("微信支付");
           // wxPay.setPayDes("简单快捷，推荐使用");
            wxPay.setPayIconId(R.drawable.weixin_pay);
            wxPay.setPayEnum(PayTypeEnum.WEIXIN);
            dataList.add(wxPay);

            //支付宝支付
            PayTypeEntity aliPay = new PayTypeEntity();
            aliPay.setPayType("支付宝支付");
           // aliPay.setPayDes("推荐已安装支付宝APP者使用");
            aliPay.setPayIconId(R.drawable.zhifu_pay);
            aliPay.setPayEnum(PayTypeEnum.ZHIFUBAO);
            dataList.add(aliPay);


        }else{
            //线下支付
            PayTypeEntity linePay = new PayTypeEntity();
            linePay.setPayType("线下支付");
          //  linePay.setPayDes("也可直接线下支付");
            linePay.setPayIconId(R.drawable.settle_crash);
            linePay.setPayEnum(PayTypeEnum.XIANXIA);
            linePay.setSelected(true);
            dataList.add(linePay);
        }

    }

    public interface SelectCallback{
        void select(PayTypeEntity payTypeEntity);
    }
}
