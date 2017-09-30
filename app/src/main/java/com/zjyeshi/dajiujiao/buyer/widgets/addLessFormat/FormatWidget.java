package com.zjyeshi.dajiujiao.buyer.widgets.addLessFormat;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.utils.ContextUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.entity.GoodsCar;
import com.zjyeshi.dajiujiao.buyer.entity.enums.SelectEnum;
import com.zjyeshi.dajiujiao.buyer.entity.good.AllGoodInfo;
import com.zjyeshi.dajiujiao.buyer.receiver.buy.CarNumChangeReceiver;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.NumberUtil;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.widgets.addLessFormat.listener.FormatOpListener;

/**
 * 购买控件
 * Created by wuhk on 2016/6/28.
 */
public abstract class FormatWidget extends LinearLayout {
    private TextView unitTv;
    private TextView boxTv;
    private ImageView addIv;
    private EditText numEt;
    private TextView numTv;
    private ImageView lessIv;
    private AllGoodInfo data;
    private FormatOpListener formatOpListener;

    private boolean isMarkerGoods = false;

    public FormatWidget(Context context) {
        super(context);
        init();
    }

    public FormatWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        loadView();
        unitTv = (TextView) findViewById(R.id.unitTv);
        boxTv = (TextView) findViewById(R.id.boxTv);
        addIv = (ImageView) findViewById(R.id.addIv);
        lessIv = (ImageView) findViewById(R.id.lessIv);
        numEt = (EditText) findViewById(R.id.numEt);
        numTv = (TextView) findViewById(R.id.numTv);
    }

    /**
     * 初始化选择规格
     *
     * @param data
     */
    public void initData(final AllGoodInfo data, final FormatOpListener formatOpListener, boolean isMarketGoods) {
        this.data = data;
        this.formatOpListener = formatOpListener;
        this.isMarkerGoods = isMarketGoods;

        unitTv.setText(data.getUnit());
        if (data.getGoodType().equals("箱")) {
            //卖家默认选中箱
            changeSel(boxTv, unitTv);
        } else {
            changeSel(unitTv, boxTv);
        }
        if (!data.getGoodType().equals("箱")) {
            changeNum(data.getGoodCount());
        } else {
            float allNum = (Float.parseFloat(data.getGoodCount())) / (Float.parseFloat(data.getBottlesPerBox()));
            changeNum(ExtraUtil.format(allNum));
        }

        //单位点击
        unitTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSel(unitTv, boxTv);
                data.setGoodType(data.getUnit());
                data.setPriceWithUnit(data.getGoodPrice());
                //箱转瓶
                float boxNum = Float.parseFloat(numTv.getText().toString());
                float unitNum = Float.parseFloat(data.getBottlesPerBox()) * boxNum;

                changeNum(roundingNum(unitNum));
                //入库
                data.setGoodCount(numTv.getText().toString());
                //转成存入数据库的购物车类型
                GoodsCar goodsCar = getGoodsCarData(data);

                if (Float.parseFloat(goodsCar.getGoodCount()) == 0) {
                    if (isMarkerGoods){
                        DaoFactory.getMarketGoodsCarDao().deleteById(goodsCar.getGoodId());
                    }else{
                        DaoFactory.getGoodsCarDao().deleteById(goodsCar.getGoodId());
                    }
                } else {
                    if (isMarkerGoods){
                        DaoFactory.getMarketGoodsCarDao().replace(goodsCar);
                    }else{
                        DaoFactory.getGoodsCarDao().replace(goodsCar);
                    }
                }
                CarNumChangeReceiver.notifyReceiver(getContext());
            }
        });

        //箱点击
        boxTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSel(boxTv, unitTv);
                data.setGoodType("箱");
                data.setPriceWithUnit(ExtraUtil.format((Float.parseFloat(data.getGoodPrice()) * (Integer.parseInt(data.getBottlesPerBox())))));
                //瓶转箱
                float unitNum = Float.parseFloat(numTv.getText().toString());
                float boxNum = unitNum / Float.parseFloat(data.getBottlesPerBox());
                changeNum(ExtraUtil.format(boxNum));
                //入库
                //数量存unit计
                float num = Float.parseFloat(numTv.getText().toString()) * Float.parseFloat(data.getBottlesPerBox());

                data.setGoodCount(roundingNum(num));
                GoodsCar goodsCar = getGoodsCarData(data);
                if (Float.parseFloat(goodsCar.getGoodCount()) == 0) {
                    if (isMarkerGoods){
                        DaoFactory.getMarketGoodsCarDao().deleteById(goodsCar.getGoodId());
                    }else{
                        DaoFactory.getGoodsCarDao().deleteById(goodsCar.getGoodId());
                    }
                } else {
                    if (isMarkerGoods){
                        DaoFactory.getMarketGoodsCarDao().replace(goodsCar);
                    }else{
                        DaoFactory.getGoodsCarDao().replace(goodsCar);
                    }
                }
                CarNumChangeReceiver.notifyReceiver(getContext());
            }
        });

        //加
        addIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                float num = Float.parseFloat(numTv.getText().toString());
                num++;

                float mNum = NumberUtil.toFloat(String.valueOf(num));
                float maxBottole = NumberUtil.toFloat(data.getSellerInventory());
                float maxBox = (Float.parseFloat(data.getSellerInventory())) / (Float.parseFloat(data.getBottlesPerBox()));
                if (data.getGoodType().equals("箱")) {
                    if (mNum > maxBox) {
                        ToastUtil.toast("该商品库存不足");
                        //点击其他地方显示加减
                        opBuyWidget(true);
                        ContextUtils.showSoftInput(numEt, false);
                        return;
                    }
                } else {
                    if (mNum > maxBottole) {
                        ToastUtil.toast("该商品库存不足");
                        //点击其他地方显示加减
                        opBuyWidget(true);
                        ContextUtils.showSoftInput(numEt, false);
                        return;
                    }
                }

                changeNum(ExtraUtil.format(num));
                if (data.getGoodType().equals("箱")) {
                    float allNum = Float.parseFloat(numTv.getText().toString()) * Float.parseFloat(data.getBottlesPerBox());
                    data.setGoodCount(roundingNum(allNum));
                } else {
                    data.setGoodCount(numTv.getText().toString());
                }
                GoodsCar goodsCar = getGoodsCarData(data);
                if (isMarkerGoods){
                    DaoFactory.getMarketGoodsCarDao().replace(goodsCar);
                }else{
                    DaoFactory.getGoodsCarDao().replace(goodsCar);
                }
                CarNumChangeReceiver.notifyReceiver(getContext());

            }
        });

        //减
        lessIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                float num = Float.parseFloat(numTv.getText().toString());
                if (num > 1) {
                    num--;
                } else {
                    num = 0.00f;
                }
                changeNum(ExtraUtil.format(num));
                if (data.getGoodType().equals("箱")) {
                    float allNum = Float.parseFloat(numTv.getText().toString()) * Float.parseFloat(data.getBottlesPerBox());
                    data.setGoodCount(roundingNum(allNum));
                } else {
                    data.setGoodCount(numTv.getText().toString());
                }

                GoodsCar goodsCar = getGoodsCarData(data);
                if (Float.parseFloat(numTv.getText().toString()) == 0) {
                    if (isMarkerGoods){
                        DaoFactory.getMarketGoodsCarDao().deleteById(goodsCar.getGoodId());
                    }else{
                        DaoFactory.getGoodsCarDao().deleteById(goodsCar.getGoodId());
                    }
                } else {
                    if (isMarkerGoods){
                        DaoFactory.getMarketGoodsCarDao().replace(goodsCar);
                    }else{
                        DaoFactory.getGoodsCarDao().replace(goodsCar);
                    }
                }
                CarNumChangeReceiver.notifyReceiver(getContext());
            }
        });

        //数量
        numTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //隐藏加减，显示输入
                opBuyWidget(false);
                numEt.setText(numTv.getText().toString());
                numEt.setSelection(numEt.getText().length());
                formatOpListener.configFormatWidget(FormatWidget.this);
                formatOpListener.changeShow();

                ContextUtils.showSoftInput(numEt);
            }
        });
        //软键盘确认
        numEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if ((i == 0 || i == 3) && keyEvent != null) {
                    sureNum();
                    ContextUtils.showSoftInput(numEt , false);
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 自定义界面
     */
    public abstract void loadView();

    /**
     * 改变选择状态
     *
     * @param selTv
     * @param noTv
     */
    private void changeSel(TextView selTv, TextView noTv) {
        selTv.setBackgroundResource(R.drawable.type_btn_select_shape);
        selTv.setTextColor(Color.WHITE);
        noTv.setBackgroundResource(R.drawable.type_btn_shape);
        noTv.setTextColor(Color.BLACK);
        noTv.setEnabled(true);
        selTv.setEnabled(false);
    }

    /**
     * 点击其他地方保存Et中的数量
     */
    public void sureNum() {
        if (numEt.getVisibility() == View.VISIBLE) {

            String num = numEt.getText().toString();

            if (!Validators.isEmpty(num)){

                if (Validators.isNumeric(num)) {

                    float mNum = NumberUtil.toFloat(num);
                    float maxBottole = NumberUtil.toFloat(data.getSellerInventory());
                    float maxBox = (Float.parseFloat(data.getSellerInventory())) / (Float.parseFloat(data.getBottlesPerBox()));
                    if (data.getGoodType().equals("箱")) {
                        if (mNum > maxBox) {
                            ToastUtil.toast("该商品库存不足");
                            //点击其他地方显示加减
                            opBuyWidget(true);
                            ContextUtils.showSoftInput(numEt, false);
                            return;
                        }
                    } else {
                        if (mNum > maxBottole) {
                            ToastUtil.toast("该商品库存不足");
                            //点击其他地方显示加减
                            opBuyWidget(true);
                            ContextUtils.showSoftInput(numEt, false);
                            return;
                        }
                    }

                    if (data.getGoodType().equals("箱")) {
                        float allNum = (Float.parseFloat(num)) * (Float.parseFloat(data.getBottlesPerBox()));
                        float showNum = (Float.parseFloat(roundingNum(allNum))) / (Float.parseFloat(data.getBottlesPerBox()));
                        changeNum(String.valueOf(showNum));
                        data.setGoodCount(roundingNum(allNum));
                    } else {
                        data.setGoodCount(num);
                        changeNum(num);
                    }
                    GoodsCar goodsCar = getGoodsCarData(data);
                    if (Float.parseFloat(goodsCar.getGoodCount()) == 0) {
                        if (isMarkerGoods){
                            DaoFactory.getMarketGoodsCarDao().deleteById(goodsCar.getGoodId());
                        }else{
                            DaoFactory.getGoodsCarDao().deleteById(goodsCar.getGoodId());
                        }
                    } else {
                        if (isMarkerGoods){
                            DaoFactory.getMarketGoodsCarDao().replace(goodsCar);
                        }else{
                            DaoFactory.getGoodsCarDao().replace(goodsCar);
                        }
                    }
                    CarNumChangeReceiver.notifyReceiver(getContext());

                } else {
                    ToastUtil.toast("您输入的数量不规范");
                }

                //点击其他地方显示加减
                opBuyWidget(true);
                ContextUtils.showSoftInput(numEt, false);
            }
        }
    }

    /**
     * 修改数量,和加减显示
     *
     * @param num
     */
    private void changeNum(String num) {
        float mNum = NumberUtil.toFloat(num);
        float maxBottole = NumberUtil.toFloat(data.getSellerInventory());
        float maxBox = (Float.parseFloat(data.getSellerInventory())) / (Float.parseFloat(data.getBottlesPerBox()));

        if (data.getGoodType().equals("箱")) {
            if (mNum > maxBox) {
                ToastUtil.toast("该商品库存不足");
                return;
            }
        } else {
            if (mNum > maxBottole) {
                ToastUtil.toast("该商品库存不足");
                return;
            }
        }
        if (lessIv.getVisibility() == GONE) {
            lessIv.setVisibility(VISIBLE);
        }
        numTv.setText(ExtraUtil.format(mNum));
        if (mNum == 0) {
            //数量为0时，显示加，隐藏减，
            addIv.setVisibility(VISIBLE);
            lessIv.setVisibility(GONE);
            numTv.setBackgroundResource(0);
        } else {
            //不为0时，显示加减
            addIv.setVisibility(VISIBLE);
            lessIv.setVisibility(VISIBLE);
            numTv.setBackgroundResource(R.drawable.no_corner_color_shape);
        }
    }

    /**
     * 显示隐藏加减
     *
     * @param show
     */
    private void opBuyWidget(boolean show) {
        if (show) {
            numTv.setVisibility(VISIBLE);
            addIv.setVisibility(VISIBLE);
            if (Float.parseFloat(data.getGoodCount()) == 0){
                lessIv.setVisibility(GONE);
            }else{
                lessIv.setVisibility(VISIBLE);

            }
            numEt.setVisibility(GONE);
        } else {
            numTv.setVisibility(GONE);
            addIv.setVisibility(GONE);
            lessIv.setVisibility(GONE);
            numEt.setVisibility(VISIBLE);
        }
    }

    //获得购物车数据类型
    private GoodsCar getGoodsCarData(AllGoodInfo allGoodInfo) {
        GoodsCar goodsCar = new GoodsCar();
        goodsCar.setShopId(allGoodInfo.getShopId());
        goodsCar.setGoodId(allGoodInfo.getGoodId());
        goodsCar.setGoodCount(allGoodInfo.getGoodCount());
        goodsCar.setShopName(allGoodInfo.getShopName());
        goodsCar.setGoodName(allGoodInfo.getGoodName());
        goodsCar.setGoodIcon(allGoodInfo.getGoodIcon());
        goodsCar.setGoodPrice(allGoodInfo.getGoodPrice());
        goodsCar.setGoodUpPrice(allGoodInfo.getUpPrice());
        goodsCar.setGoodMarketPrice(allGoodInfo.getMarketCost());
        goodsCar.setGoodType(allGoodInfo.getGoodType());
        goodsCar.setGoodBottole(allGoodInfo.getBottlesPerBox());
        goodsCar.setStatus(SelectEnum.SELECTED.getValue());
        return goodsCar;
    }

    /**
     * 箱转瓶四舍五入
     *
     * @param num
     * @return
     */
    public static String roundingNum(float num) {
        String[] nums = ExtraUtil.format(num).split("\\.");
        if (Integer.parseInt(nums[1].substring(0, 1)) < 5) {
            return ExtraUtil.format(Integer.parseInt(nums[0]));
        } else {
            return ExtraUtil.format(Integer.parseInt(nums[0]) + 1);
        }
    }
}
