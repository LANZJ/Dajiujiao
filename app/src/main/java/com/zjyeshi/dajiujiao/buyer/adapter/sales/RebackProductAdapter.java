package com.zjyeshi.dajiujiao.buyer.adapter.sales;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.enums.GoodTypeEnum;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.Order;
import com.zjyeshi.dajiujiao.buyer.entity.order.AddOrderRequest;
import com.zjyeshi.dajiujiao.buyer.receiver.order.ChangePriceReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.order.OrderProduct;
import com.zjyeshi.dajiujiao.buyer.utils.AuthUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.buyer.utils.NumberUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.views.AloneEditText;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by wuhk on 2017/5/11.
 */

public class RebackProductAdapter extends MBaseAdapter {
    private Context context;
    private List<OrderProduct> dataList;
    private int goodType;

    public RebackProductAdapter(Context context, List<OrderProduct> dataList , int goodType) {
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
        initTextView(desTv  , "x" + data.getCount() + data.getUnit() + " " + data.getBottlesPerBox() + data.getUnit() + "/箱");
        int boxNum = (Integer.parseInt(data.getCount())) / (Integer.parseInt(data.getBottlesPerBox()));
        int unitNum = (Integer.parseInt(data.getCount())) % (Integer.parseInt(data.getBottlesPerBox()));

        backBoxEt.setText(String.valueOf(boxNum));
        backUnitEt.setText(String.valueOf(unitNum));

        backBoxEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    backBoxEt.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            String boxText = backBoxEt.getText().toString();
                            try {
                                int editBox = Integer.parseInt(boxText);
                                int count = editBox * Integer.parseInt(data.getBottlesPerBox()) + Integer.parseInt(backUnitEt.getText().toString());
                                if (count > Integer.parseInt(data.getCount())){
                                    ToastUtil.toast("退货数量超过订单数量");
                                    return;
                                }else{
                                   if(count==0){
                                        ToastUtil.toast("退货数量不能为0");
                                   }else {
                                       data.setCount(String.valueOf(count));
                                   }
                                }
//                                if (Validators.isEmpty(backBoxEt.getText().toString())){
//                                    data.setBoxType(AddOrderRequest.BOX_TYPE_UNIT);
//                                }else{
//                                    data.setBoxType(AddOrderRequest.BOX_TYPE_XIANG);
//                                }

                            }catch (Exception e){
                                ToastUtil.toast("请输入正确的整数");
                                return;
                            }

                        }
                    });
                }else {
                    backBoxEt.clearTextChangedListeners();
                }
            }
        });

        backUnitEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    backUnitEt.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            String unitText = backUnitEt.getText().toString();
                            try {
                                int editUnit = Integer.parseInt(unitText);
                                int count = Integer.parseInt(backBoxEt.getText().toString()) * Integer.parseInt(data.getBottlesPerBox()) + editUnit;
                                if (count > Integer.parseInt(data.getCount())){
                                    ToastUtil.toast("退货数量超过订单数量");
                                    return;
                                }else{
                                    if(count==0){
                                        ToastUtil.toast("退货数量不能为0");
                                    }else {
                                        data.setCount(String.valueOf(count));
                                    }
                                }
//                                if (Validators.isEmpty(backBoxEt.getText().toString())){
//                                    data.setBoxType(AddOrderRequest.BOX_TYPE_UNIT);
//                                }else{
//                                    data.setBoxType(AddOrderRequest.BOX_TYPE_XIANG);
//                                }
                            }catch (Exception e){
                                ToastUtil.toast("请输入正确的整数");
                                return;
                            }
                        }
                    });
                }else {
                    backUnitEt.clearTextChangedListeners();
                }
            }
        });


        return view;
    }
}
