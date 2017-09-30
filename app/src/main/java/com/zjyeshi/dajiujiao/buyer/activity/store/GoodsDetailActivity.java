package com.zjyeshi.dajiujiao.buyer.activity.store;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.StringUtils;
import com.xuan.bigappleui.lib.view.photoview.app.BUViewImageUtils;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.circle.ShowMultiImageActivity;
import com.zjyeshi.dajiujiao.buyer.entity.good.AllGoodInfo;
import com.zjyeshi.dajiujiao.buyer.receiver.buy.BuyCarSelectChangeReceiver;
import com.zjyeshi.dajiujiao.buyer.receiver.buy.CarNumChangeReceiver;
import com.zjyeshi.dajiujiao.buyer.utils.AuthUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.buyer.views.SalesView;
import com.zjyeshi.dajiujiao.buyer.widgets.addLessFormat.FormatWidget;
import com.zjyeshi.dajiujiao.buyer.widgets.store.BuyBottomWidget;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.receiver.GoodsInfoReveiver;
import com.zjyeshi.dajiujiao.buyer.widgets.addLessFormat.listener.FormatOpListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品详情
 * Created by wuhk on 2016/9/18.
 */
public class GoodsDetailActivity extends BaseActivity implements FormatOpListener {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.photoIv)
    private ImageView photoIv;
    @InjectView(R.id.nameTv)
    private TextView nameTv;
    @InjectView(R.id.priceTv)
    private TextView priceTv;
    @InjectView(R.id.formatWidget)
    private FormatWidget formatWidget;
    @InjectView(R.id.desTv)
    private TextView desTv;
    @InjectView(R.id.salesView)
    private SalesView salesView;
    @InjectView(R.id.lookCommentLayout)
    private RelativeLayout lookCommentLayout;
    @InjectView(R.id.buyBottomWidget)
    private BuyBottomWidget buyBottomWidget;
    @InjectView(R.id.softView)
    private View softView;

    public static final String GOOD_INFO_LIST = "good_info_list";
    public static final String POSITION = "position";
    public static final String MEMBERID = "member_id";
    public static final String IS_MARKET_GOODS = "is_market_goods";

    private String memberId = "";
    private List<AllGoodInfo> allGoodList = new ArrayList<>();
    private AllGoodInfo allGoodInfo;

    private CarNumChangeReceiver carNumChangeReceiver;//购物车数量改变广播
    private GoodsInfoReveiver goodsInfoReveiver;//改变详情
//    //改变选择和价格广播
//    private BuyCarSelectChangeReceiver buyCarSelectChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_detail_goods);
        initWidgets();

        carNumChangeReceiver = new CarNumChangeReceiver() {
            @Override
            public void changeCarnum(boolean isAddOrder) {
                if (AuthUtil.showMarketCostTab()){
                    buyBottomWidget.refreshData(true);
                }else{
                    buyBottomWidget.refreshData(false);
                }
            }
        };
        carNumChangeReceiver.register(this);

        goodsInfoReveiver = new GoodsInfoReveiver() {
            @Override
            public void changeData(AllGoodInfo data) {
                allGoodInfo = data;
                refreshData();
//                bindData(allGoodInfo);
            }
        };
        goodsInfoReveiver.register();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        carNumChangeReceiver.unRegister(this);
        goodsInfoReveiver.unregister();
    }

    private void initWidgets() {
        titleLayout.configTitle("商品详情").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String jsonStr = getIntent().getStringExtra(GOOD_INFO_LIST);
        List<AllGoodInfo> tempList = JSONArray.parseArray(jsonStr , AllGoodInfo.class);
        allGoodList.clear();
        allGoodList.addAll(tempList);

        int position =  getIntent().getIntExtra(POSITION , 0);
        for(int i = 0 ; i < allGoodList.size() ; i ++){
            AllGoodInfo good = allGoodList.get(i);
            good.setGoodIcon(good.getGoodIcon() + "," + i);
        }

        allGoodInfo = allGoodList.get(position);
        memberId = getIntent().getStringExtra(MEMBERID);

        refreshData();
    }

    private void refreshData() {
        String[] params = StringUtils.split(allGoodInfo.getGoodIcon() , ",");
        String showUrl = params[0];
        final int position = Integer.parseInt(params[1]);

        GlideImageUtil.glidImage(photoIv, ExtraUtil.getResizePic(showUrl , 400 , 400), R.drawable.default_img);
        initTextView(nameTv, allGoodInfo.getGoodName());
//        initTextView(priceTv, "¥" + allGoodInfo.getGoodPrice());
        initTextView(priceTv, "¥" + allGoodInfo.getPriceWithUnit() + "/" + allGoodInfo.getGoodType());
        initTextView(desTv, allGoodInfo.getDescription());

        salesView.bindData(allGoodInfo.getSalesList());

        photoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] imageArray = new String[allGoodList.size()];
                for(int i = 0 ; i < allGoodList.size() ; i ++){
                    AllGoodInfo info = allGoodList.get(i);
                    imageArray[i] = info.getGoodIcon();
                }

                BUViewImageUtils.gotoViewImageActivity(GoodsDetailActivity.this, imageArray, position , ShowMultiImageActivity.LOADTYPE_CIRCLE, new Object[]{JSONArray.toJSONString(allGoodList)}, ShowGoodsMultiActivity.class);
            }
        });
        boolean isMarketGoods = getIntent().getBooleanExtra(IS_MARKET_GOODS , false);
        formatWidget.initData(allGoodInfo, this , isMarketGoods);

        if (AuthUtil.showMarketCostTab()){
            buyBottomWidget.refreshData(true);
        }else{
            buyBottomWidget.refreshData(false);
        }
        buyBottomWidget.setGoCarOnClikListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(GoodsDetailActivity.this, BuyCarActivity.class);
               // intent.putExtra("memberId", memberId);
                intent.putExtra(BuyCarActivity.PARAM_MEMBERID, memberId);
                startActivity(intent);
                finish();
            }
        });

        softView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formatWidget.sureNum();
                softView.setVisibility(View.GONE);
                buyBottomWidget.setVisibility(View.VISIBLE);
            }
        });

        lookCommentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodsCommentActivity.startActivity(GoodsDetailActivity.this, allGoodInfo.getGoodId());
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

/**
     * 注册广播
     */
//    private void registerReceiver() {
//        buyCarSelectChangeReceiver = new BuyCarSelectChangeReceiver() {
//            @Override
//            public void changeSelect(String type) {
//                if (type.equals(SELECT_REFRESH)) {
//                    refreshSelect();
//                } else {
//                    loadData(false);
//                }
//            }
//        };
//        buyCarSelectChangeReceiver.register();
  //  }
    /**
     * 启动该Activity
     *
     * @param context
     * @param allGoodInfoList
     * @param position
     * @param id
     */
    public static void startActivity(Context context, List<AllGoodInfo> allGoodInfoList, int position, String id , boolean isMarketGoods) {
        Intent intent = new Intent();
        intent.setClass(context, GoodsDetailActivity.class);
        intent.putExtra(GOOD_INFO_LIST, JSONArray.toJSONString(allGoodInfoList));
        intent.putExtra(POSITION, position);
        intent.putExtra(MEMBERID, id);
        intent.putExtra(IS_MARKET_GOODS , isMarketGoods);
        context.startActivity(intent);
    }

    @Override
    public void configFormatWidget(FormatWidget formatWidget) {
//        this.formatWidget = formatWidget;
    }

    @Override
    public void changeShow() {
        buyBottomWidget.setVisibility(View.GONE);
        softView.setVisibility(View.VISIBLE);
    }
}
