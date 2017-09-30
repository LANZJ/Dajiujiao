package com.zjyeshi.dajiujiao.buyer.widgets.store;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.ioc.ViewUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.entity.GoodsCar;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.circle.itemview.BaseView;

import java.util.HashMap;
import java.util.List;

/**
 * 店铺底部操作栏
 * Created by wuhk on 2016/9/18.
 */
public class BuyBottomWidget extends BaseView {
    @InjectView(R.id.carNumTv)
    private TextView catNumTv;
    @InjectView(R.id.carPriceTv)
    private TextView carPriceTv;
    @InjectView(R.id.marketCarNumTv)
    private TextView marketCarNumTv;
    @InjectView(R.id.marketCarPriceTv)
    private TextView marketCarPriceTv;
    @InjectView(R.id.goCarBtn)
    private Button goCarBtn;
//    @InjectView(R.id.myKcTv)
//    private TextView myKcTv;
    @InjectView(R.id.normalPriceLayout)
    private RelativeLayout normalPriceLayout;
    @InjectView(R.id.marketPriceLayout)
    private RelativeLayout marketPriceLayout;


    public BuyBottomWidget(Context context) {
        super(context);
    }

    public BuyBottomWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void dgInit() {
        inflate(getContext() , R.layout.widget_buy_bottom , this);
        ViewUtils.inject(this , this);
    }

    /**设置点击事件
     *
     * @param onClikListener
     */
    public void setGoCarOnClikListener(OnClickListener onClikListener){
        goCarBtn.setOnClickListener(onClikListener);
    }


//    /**设置我的库存
//     *
//     * @param myKc
//     */
//    public void setMyKc(String myKc){
//        myKcTv.setText(myKc);
//    }


    /**改变购物车数量和价格
     *
     */
    public void refreshData(boolean showAll) {
        //常规商品费用
        String allNum = " ";
        float allPrice = 0.00f;
        List<GoodsCar> goodList = DaoFactory.getGoodsCarDao().findAllGoods();
        HashMap<String , String> numMap = new HashMap<String, String>();

        for (GoodsCar goodsCar : goodList) {

            //计算底部数量价格
            if (numMap.keySet().contains(goodsCar.getGoodType())){
                String mapValue = numMap.get(goodsCar.getGoodType());
                if (goodsCar.getGoodType().equals("箱")){
                    float a = Float.parseFloat(mapValue) + ((Float.parseFloat(goodsCar.getGoodCount())) / (Float.parseFloat(goodsCar.getGoodBottole())));
                    numMap.put(goodsCar.getGoodType() , ExtraUtil.format(a));
                }else{
                    float b = Float.parseFloat(mapValue) + Float.parseFloat(goodsCar.getGoodCount());
                    numMap.put(goodsCar.getGoodType() , ExtraUtil.format(b));
                }
            }else{
                if (goodsCar.getGoodType().equals("箱")){
                    float a = ((Float.parseFloat(goodsCar.getGoodCount())) / (Float.parseFloat(goodsCar.getGoodBottole())));
                    numMap.put(goodsCar.getGoodType() , ExtraUtil.format(a));
                }else{
                    float b =Float.parseFloat(goodsCar.getGoodCount());
                    numMap.put(goodsCar.getGoodType() , ExtraUtil.format(b));
                }
            }
            float price = (Float.parseFloat(goodsCar.getGoodCount())) * (Float.parseFloat(goodsCar.getGoodPrice()));
            allPrice += price;
        }

        //设置箱和价格
        carPriceTv.setText("¥" + ExtraUtil.format(allPrice));
        if (!Validators.isEmpty(numMap.get("箱"))){
            allNum = numMap.get("箱") + "箱";
        }
        for (String key : numMap.keySet()) {
            if (!key.equals("箱")) {
                allNum += numMap.get(key) + key;
            }
        }
        catNumTv.setText("常规："+allNum);
        //市场支持商品费用
        String marketAllNum = " ";
        float marketAllPrice = 0.00f;
        List<GoodsCar> marketGoodList = DaoFactory.getMarketGoodsCarDao().findAllGoods();
        HashMap<String , String> marketNumMap = new HashMap<String, String>();
        for (GoodsCar goodsCar : marketGoodList) {
            if (marketNumMap.keySet().contains(goodsCar.getGoodType())){
                String mapValue = marketNumMap.get(goodsCar.getGoodType());
                if (goodsCar.getGoodType().equals("箱")){
                    float a = Float.parseFloat(mapValue) + ((Float.parseFloat(goodsCar.getGoodCount())) / (Float.parseFloat(goodsCar.getGoodBottole())));
                    marketNumMap.put(goodsCar.getGoodType() , ExtraUtil.format(a));
                }else{
                    float b = Float.parseFloat(mapValue) + Float.parseFloat(goodsCar.getGoodCount());
                    marketNumMap.put(goodsCar.getGoodType() , ExtraUtil.format(b));
                }
            }else{
                if (goodsCar.getGoodType().equals("箱")){
                    float a = ((Float.parseFloat(goodsCar.getGoodCount())) / (Float.parseFloat(goodsCar.getGoodBottole())));
                    marketNumMap.put(goodsCar.getGoodType() , ExtraUtil.format(a));
                }else{
                    float b =Float.parseFloat(goodsCar.getGoodCount());
                    marketNumMap.put(goodsCar.getGoodType() , ExtraUtil.format(b));
                }
            }
            float price = (Float.parseFloat(goodsCar.getGoodCount())) * (Float.parseFloat(goodsCar.getGoodPrice()));
            marketAllPrice += price;
        }
        marketCarPriceTv.setText("¥" + ExtraUtil.format(marketAllPrice));
        if (!Validators.isEmpty(marketNumMap.get("箱"))){
            marketAllNum = marketNumMap.get("箱") + "箱";
        }
        for (String key : marketNumMap.keySet()) {
            if (!key.equals("箱")) {
                marketAllNum += marketNumMap.get(key) + key;
            }
        }
        marketCarNumTv.setText("市场："+marketAllNum);

        if (showAll){
            normalPriceLayout.setVisibility(VISIBLE);
            marketPriceLayout.setVisibility(VISIBLE);
        }else{
            normalPriceLayout.setVisibility(VISIBLE);
          //  marketPriceLayout.setVisibility(GONE);
            marketPriceLayout.setVisibility(VISIBLE);
        }
    }


}
