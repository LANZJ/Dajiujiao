package com.zjyeshi.dajiujiao.buyer.activity.sales;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigappleui.lib.view.listview.BUHighHeightListView;
import com.xuan.bigdog.lib.dialog.DGSingleSelectDialog;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.activity.my.ReturnGoodsActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.sales.RebackProductAdapter;
import com.zjyeshi.dajiujiao.buyer.circle.task.UpLoadFileTask;
import com.zjyeshi.dajiujiao.buyer.entity.enums.GoodTypeEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.RebackTypeEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.SalesFillTypeEnum;
import com.zjyeshi.dajiujiao.buyer.task.data.order.OrderDetailData;
import com.zjyeshi.dajiujiao.buyer.task.data.order.OrderProduct;
import com.zjyeshi.dajiujiao.buyer.task.data.other.GetUpLoadFileData;
import com.zjyeshi.dajiujiao.buyer.task.order.GetOrderDetailTask;
import com.zjyeshi.dajiujiao.buyer.task.sales.GetOrderSalesMoneyTask;
import com.zjyeshi.dajiujiao.buyer.task.sales.data.OrderSalesMoneyData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.widgets.uploadMultImage.GridViewImageWidget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhk on 2017/5/11.
 */

public class ApplyRebackActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.rebackListView)
    private BUHighHeightListView rebackListView;
    @InjectView(R.id.rebacMarketListView)
    private BUHighHeightListView rebacMarketListView;
    @InjectView(R.id.rebackTypeLayout)
    private RelativeLayout rebackTypeLayout;
    @InjectView(R.id.rebackTypeTv)
    private TextView rebackTypeTv;
    @InjectView(R.id.remarkEt)
    private EditText remarkEt;
    @InjectView(R.id.gridViewImageWidget)
    private GridViewImageWidget gridViewImageWidget;
    @InjectView(R.id.nextBtn)
    private Button nextBtn;

    private List<OrderProduct> goodList = new ArrayList<>();
    private List<OrderProduct> marketList = new ArrayList<>();

    private RebackProductAdapter goodRebackProductAdapter;
    private RebackProductAdapter marketRebackProductAdapter;

    public static final String PARAM_ORDER_ID = "param.order.id";

    private int rebackType = RebackTypeEnum.REBACK_GIFT.getValue();
    private String orderId = "";
    private boolean hasSalesGive;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_apply_reback);
        orderId =  getIntent().getStringExtra(PARAM_ORDER_ID);
        initWidgets();
        judeSalesGive();
    }

    private void initWidgets(){

        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        }).configTitle("申请退货");

        rebackTypeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] itemStr = {RebackTypeEnum.REBACK_GIFT.getName(), RebackTypeEnum.NOT_REBACK_GOFT.getName()};
                View.OnClickListener[] ls = {new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //退回活动礼品
                        rebackTypeTv.setText(RebackTypeEnum.REBACK_GIFT.getName());
                        rebackType = RebackTypeEnum.REBACK_GIFT.getValue();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //不退回活动礼品
                        rebackTypeTv.setText(RebackTypeEnum.NOT_REBACK_GOFT.getName());
                        rebackType = RebackTypeEnum.NOT_REBACK_GOFT.getValue();
                    }
                }};
                DGSingleSelectDialog dialog = new DGSingleSelectDialog.Builder(ApplyRebackActivity.this).setItemTextAndOnClickListener(itemStr, ls).create();
                dialog.show();
            }
        });


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RebackSureActivity.RebackApplySureData sureData = new RebackSureActivity.RebackApplySureData();
                if (hasSalesGive){
                    if(Validators.isEmpty(rebackTypeTv.getText().toString())){
                        ToastUtil.toast("请选择退还方式");
                        return;
                    }
                }
                sureData.setHasSalesGive(hasSalesGive);
                sureData.setOrderId(orderId);
                sureData.setRebackTypeEnum(RebackTypeEnum.valueOf(rebackType));
                sureData.setGoodList(goodList);
                sureData.setMarketGoodList(marketList);
                sureData.setImageList(gridViewImageWidget.getDataList());
                sureData.setRemark(remarkEt.getText().toString());

                RebackSureActivity.startRebackSureActivty(ApplyRebackActivity.this , sureData);
            }
        });

        loadData(orderId);
    }

    private void loadData(String orderId){
        GetOrderDetailTask.getOrderDetail(ApplyRebackActivity.this, orderId, new AsyncTaskSuccessCallback<OrderDetailData>() {
            @Override
            public void successCallback(Result<OrderDetailData> result) {
                goodList.clear();
                goodList.addAll(result.getValue().getProducts());
                goodRebackProductAdapter = new RebackProductAdapter(ApplyRebackActivity.this , goodList , GoodTypeEnum.NORMAL_BUY.getValue());
                rebackListView.setAdapter(goodRebackProductAdapter);

                if (!Validators.isEmpty(result.getValue().getMarketCostProducts())){
                    rebacMarketListView.setVisibility(View.VISIBLE);
                    marketList.clear();
                    marketList.addAll(result.getValue().getMarketCostProducts());
                    marketRebackProductAdapter = new RebackProductAdapter(ApplyRebackActivity.this , marketList , GoodTypeEnum.MARKET_SUPPORT.getValue());
                    rebacMarketListView.setAdapter(marketRebackProductAdapter);
                }else{
                    rebacMarketListView.setVisibility(View.GONE);
                }
            }
        });
    }


    private void judeSalesGive(){
        GetOrderSalesMoneyTask.getOrderSalesMoney(ApplyRebackActivity.this, orderId, new AsyncTaskSuccessCallback<OrderSalesMoneyData>() {
            @Override
            public void successCallback(Result<OrderSalesMoneyData> result) {
                //礼品和酒品都没有就不显示退不退还了
                if (Validators.isEmpty(result.getValue().getGiftName()) && Validators.isEmpty(result.getValue().getProductName())){
                    hasSalesGive = false;
                    rebackTypeLayout.setVisibility(View.GONE);
                }else{
                    hasSalesGive = true;
                    rebackTypeLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        gridViewImageWidget.getUploadGridViewImageModel().onActivityResult(requestCode , resultCode , data , gridViewImageWidget.getDataList());
    }


    public static void startApplyRebackActivity(Context context , String orderId){
        Intent intent = new Intent();
        intent.setClass(context , ApplyRebackActivity.class);
        intent.putExtra(PARAM_ORDER_ID , orderId);
        context.startActivity(intent);
    }
}
