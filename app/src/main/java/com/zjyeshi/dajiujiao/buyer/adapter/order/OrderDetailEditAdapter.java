package com.zjyeshi.dajiujiao.buyer.adapter.order;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.task.data.order.OrderProduct;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.NumberUtil;
import com.zjyeshi.dajiujiao.buyer.views.AloneEditText;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.receiver.order.ChangePriceReceiver;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;

import java.util.List;

/**
 * 订单详情可修改适配器
 * Created by wuhk on 2016/7/20.
 */
public class OrderDetailEditAdapter extends MBaseAdapter {
    private Context context;
    private List<OrderProduct> dataList;

    public OrderDetailEditAdapter(Context context, List<OrderProduct> dataList) {
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
            view  = LayoutInflater.from(context).inflate(R.layout.listitem_order_detail_edit , null);
        }
        ImageView photoIv = (ImageView)view.findViewById(R.id.photoIv);
        TextView nameTv = (TextView)view.findViewById(R.id.nameTv);
        TextView contentTv = (TextView)view.findViewById(R.id.contentTv);
        TextView priceTv = (TextView)view.findViewById(R.id.priceTv);
        TextView numTv = (TextView)view.findViewById(R.id.numTv);
        final AloneEditText xiangEt = (AloneEditText)view.findViewById(R.id.xiangEt);
        final AloneEditText unitEt = (AloneEditText)view.findViewById(R.id.unitEt);
        TextView unitTv = (TextView)view.findViewById(R.id.unitTv);
        TextView delTv = (TextView)view.findViewById(R.id.delTv);


        final OrderProduct orderProduct = dataList.get(position);

        GlideImageUtil.glidImage(photoIv , ExtraUtil.getResizePic(orderProduct.getPic() , 130 , 130) , R.drawable.default_img);
        initTextView(nameTv , orderProduct.getName());
        initTextView(contentTv , orderProduct.getSpecifications());
        initTextView(priceTv, "¥" + ExtraUtil.format(Float.parseFloat(orderProduct.getPrice())/100));
        initTextView(unitTv , orderProduct.getUnit());

        if (orderProduct.getCount() == "0"){
            numTv.setVisibility(View.GONE);
        }else{
            numTv.setVisibility(View.VISIBLE);
            initTextView(numTv , "X"+ orderProduct.getCountUnit());
        }
        //不显示数量了
        numTv.setVisibility(View.GONE);

        //将商品数量按多少箱多少瓶显示出来
        int count = Integer.parseInt(orderProduct.getCount());
        int box = count/(Integer.parseInt(orderProduct.getBottlesPerBox()));
        final int unit = count % (Integer.parseInt(orderProduct.getBottlesPerBox()));
        xiangEt.setText(String.valueOf(box));
        unitEt.setText(String.valueOf(unit));

        //箱数量添加
        xiangEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    xiangEt.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            //取箱编辑框的数量加上单位编辑框的数量之和，赋值给该商品
                            String xiangNum = ExtraUtil.getUpLoadCount(String.valueOf(NumberUtil.toFloat(xiangEt.getText().toString())));
                            int unitNum = Integer.parseInt(unitEt.getText().toString());
                            orderProduct.setCount(String.valueOf((Integer.parseInt(xiangNum) * Integer.parseInt(orderProduct.getBottlesPerBox()))  + unitNum));
                            //刷新一下主界面的价格
                            ChangePriceReceiver.notifyReceiver();
                        }
                    });
                }else {
                    xiangEt.clearTextChangedListeners();
                }
            }
        });
        //unit数量添加
        unitEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    unitEt.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            //取箱编辑框的数量加上单位编辑框的数量之和，赋值给该商品
                            String unitNum = ExtraUtil.getUpLoadCount(String.valueOf(NumberUtil.toFloat(unitEt.getText().toString())));
                            int xiangNum = Integer.parseInt(xiangEt.getText().toString());
                            orderProduct.setCount(String.valueOf(Integer.parseInt(unitNum) + (xiangNum * Integer.parseInt(orderProduct.getBottlesPerBox()))));
                            //刷新一下主界面的价格
                            ChangePriceReceiver.notifyReceiver();
                        }
                    });
                }else{
                    unitEt.clearTextChangedListeners();
                }
            }
        });

        //删除
        delTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //从列表中删除该商品
                dataList.remove(orderProduct);
                notifyDataSetChanged();
                //刷新一下主界面的价格
                ChangePriceReceiver.notifyReceiver();
            }
        });

        return view;
    }

}
