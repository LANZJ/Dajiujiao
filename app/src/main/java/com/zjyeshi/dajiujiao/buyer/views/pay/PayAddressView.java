package com.zjyeshi.dajiujiao.buyer.views.pay;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.ioc.ViewUtils;
import com.zjyeshi.dajiujiao.buyer.activity.my.personal.MyAddressActivity;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.circle.itemview.BaseView;
import com.zjyeshi.dajiujiao.buyer.common.PassConstans;
import com.zjyeshi.dajiujiao.buyer.entity.my.Address;

/**
 * 支付界面地址
 * Created by wuhk on 2016/12/16.
 */
public class PayAddressView extends BaseView {
    @InjectView(R.id.selectAddLayout)
    private LinearLayout selectAddLayout;//选择地址
    @InjectView(R.id.personInfoTv)
    private TextView personInfoTv;
    @InjectView(R.id.addressTv)
    private TextView addressTv;

    private String addressId = "";

    public PayAddressView(Context context) {
        super(context);
    }

    public PayAddressView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void dgInit() {
        inflate(getContext() , R.layout.view_pay_address , this);
        ViewUtils.inject(this , this);
    }

    public void bindAddressData(Address address){
        if(null == address){
            personInfoTv.setText("请先设置地址并选择");
            addressTv.setVisibility(View.GONE);
        }else{
            personInfoTv.setText("收货人:"+ address.getName() + "    "+ address.getPhone());
            String area = ExtraUtil.getAreaByCode(address.getArea());
            addressTv.setVisibility(View.VISIBLE);
            addressTv.setText(area + address.getAddress());
            addressId = address.getId();
        }

        //地址选择
        selectAddLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(PassConstans.ISSELECTADD , "Yes");
                intent.setClass(getContext() , MyAddressActivity.class);
                getContext().startActivity(intent);
            }
        });

    }

    /**获取地址Id
     *
     * @return
     */
    public String getAddressId(){
        return addressId;
    }
}
