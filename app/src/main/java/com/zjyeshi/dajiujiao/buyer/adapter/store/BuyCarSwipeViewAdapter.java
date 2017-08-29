package com.zjyeshi.dajiujiao.buyer.adapter.store;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigappleui.lib.widget.swipeview.adapters.BaseSwipeAdapter;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.entity.GoodsCar;
import com.zjyeshi.dajiujiao.buyer.entity.enums.GoodTypeEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.SelectEnum;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.views.other.IVCheckBox;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.my.personal.ShowImageActivity;
import com.zjyeshi.dajiujiao.buyer.common.PassConstans;
import com.zjyeshi.dajiujiao.buyer.receiver.buy.BuyCarSelectChangeReceiver;
import com.zjyeshi.dajiujiao.buyer.receiver.buy.CarNumChangeReceiver;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.buyer.widgets.addLessFormat.FormatWidget;

import java.util.List;

/**
 * 购物车适配器
 * <p/>
 * Created by xb on 2015/8/2.
 */
public class BuyCarSwipeViewAdapter extends BaseSwipeAdapter {
    protected Context context;
    protected List<GoodsCar> dataList;
    private String type;

    public BuyCarSwipeViewAdapter(Context context, List<GoodsCar> dataList, String type) {
        this.context = context;
        this.dataList = dataList;
        this.type = type;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.listitem_buycar_child, null);
    }

    @Override
    public void fillValues(final int position, final View convertView) {
        closeAllItems();

        final GoodsCar goodsCar = dataList.get(position);
        final IVCheckBox selectIv = (IVCheckBox) convertView.findViewById(R.id.selectIv);
        final TextView goodNameTv = (TextView) convertView.findViewById(R.id.goodNameTv);
        ImageView photoIv = (ImageView) convertView.findViewById(R.id.goodPhotoIv);
        TextView typeTv = (TextView) convertView.findViewById(R.id.typeTv);
        TextView priceTv = (TextView) convertView.findViewById(R.id.priceTv);
        TextView behind = (TextView) convertView.findViewById(R.id.behind);
        TextView unitTv = (TextView) convertView.findViewById(R.id.unitTv);
        final ImageView lessIv = (ImageView) convertView.findViewById(R.id.lessIv);
        final ImageView addIv = (ImageView) convertView.findViewById(R.id.addIv);
        final TextView numTv = (TextView) convertView.findViewById(R.id.numTv);
        //初始化
        initTextView(goodNameTv, goodsCar.getGoodName());
        GlideImageUtil.glidImage(photoIv, ExtraUtil.getResizePic(goodsCar.getGoodIcon() , 240 , 240), R.drawable.default_img);
        initTextView(typeTv, goodsCar.getGoodType());
        initTextView(unitTv, "/" + goodsCar.getGoodType());

        photoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowImageActivity.startActivity(context, goodsCar.getGoodIcon());
            }
        });

        if (goodsCar.getStatus() == SelectEnum.SELECTED.getValue()) {
            selectIv.setChecked(true);
        } else {
            selectIv.setChecked(false);
        }
        //初始化数量
        String num = goodsCar.getGoodCount();
        if (goodsCar.getGoodType().equals(PassConstans.XIANG)) {
            Float xiangPrice = (Float.parseFloat(goodsCar.getGoodPrice())) * (Float.parseFloat(goodsCar.getGoodBottole()));
            initTextView(priceTv, "¥" + ExtraUtil.format(xiangPrice));
            initTextView(numTv, ExtraUtil.format((Float.parseFloat(num)) / (Float.parseFloat(goodsCar.getGoodBottole()))));
        } else {
            initTextView(numTv, num);
            initTextView(priceTv, "¥" + goodsCar.getGoodPrice());
        }
        if (num.equals(PassConstans.ZERO)) {
            delete(goodsCar);

        } else {
            numTv.setVisibility(View.VISIBLE);
            lessIv.setVisibility(View.VISIBLE);
            addIv.setVisibility(View.VISIBLE);
        }

        //增加点击
        addIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float n = Float.parseFloat(numTv.getText().toString());
                n = n + 1;
                //判断箱或瓶
                if (goodsCar.getGoodType().equals(PassConstans.XIANG)) {
                    goodsCar.setGoodCount(FormatWidget.roundingNum(n * (Float.parseFloat(goodsCar.getGoodBottole()))));
                } else {
                    goodsCar.setGoodCount(ExtraUtil.format(n));
                }

                if (type.equals(GoodTypeEnum.NORMAL_BUY.toString())) {
                    DaoFactory.getGoodsCarDao().replace(goodsCar);
                } else {
                    DaoFactory.getMarketGoodsCarDao().replace(goodsCar);
                }
                CarNumChangeReceiver.notifyReceiver(context);
                BuyCarSelectChangeReceiver.notifyReceiver(BuyCarSelectChangeReceiver.SELECT_REFRESH);
            }
        });

        //减少点击
        lessIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float m = Float.parseFloat(numTv.getText().toString());
                m = m - 1;
                //判断箱或瓶
                if (goodsCar.getGoodType().equals(PassConstans.XIANG)) {
                    goodsCar.setGoodCount(FormatWidget.roundingNum(m * (Float.parseFloat(goodsCar.getGoodBottole()))));
                } else {
                    goodsCar.setGoodCount(ExtraUtil.format(m));
                }
                if (m == 0) {
                    delete(goodsCar);
                } else {
                    if (type.equals(GoodTypeEnum.NORMAL_BUY.toString())) {
                        DaoFactory.getGoodsCarDao().replace(goodsCar);
                    } else {
                        DaoFactory.getMarketGoodsCarDao().replace(goodsCar);
                    }
                    CarNumChangeReceiver.notifyReceiver(context);
                    BuyCarSelectChangeReceiver.notifyReceiver(BuyCarSelectChangeReceiver.SELECT_REFRESH);
                }
            }
        });

        //选择按钮点击事件
        selectIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectIv.isChecked()) {
                    goodsCar.setStatus(SelectEnum.SELECTED.getValue());
                } else {
                    goodsCar.setStatus(SelectEnum.UNSELECT.getValue());
                }
                if (type.equals(GoodTypeEnum.NORMAL_BUY.toString())){
                    DaoFactory.getGoodsCarDao().replace(goodsCar);
                }else{
                    DaoFactory.getMarketGoodsCarDao().replace(goodsCar);
                }
                CarNumChangeReceiver.notifyReceiver(context);
                BuyCarSelectChangeReceiver.notifyReceiver(BuyCarSelectChangeReceiver.SELECT_REFRESH);
            }
        });

        //删除
        behind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(goodsCar);
                closeItem(position);
            }
        });
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 设置文本
     *
     * @param textView
     * @param text
     */
    public void initTextView(TextView textView, String text) {
        if (!Validators.isEmpty(text)) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
        } else {
            textView.setVisibility(View.INVISIBLE);
        }
    }

    //删除
    private void delete(GoodsCar goodsCar) {
        if (type.equals(GoodTypeEnum.NORMAL_BUY.toString())) {
            DaoFactory.getGoodsCarDao().deleteById(goodsCar.getGoodId());
        } else {
            DaoFactory.getMarketGoodsCarDao().deleteById(goodsCar.getGoodId());
        }
        CarNumChangeReceiver.notifyReceiver(context);
        BuyCarSelectChangeReceiver.notifyReceiver(BuyCarSelectChangeReceiver.DELETE_REFRESH);
    }

}