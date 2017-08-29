package com.zjyeshi.dajiujiao.buyer.activity.sales;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigappleui.lib.view.listview.BUHighHeightListView;
import com.xuan.bigdog.lib.utils.BDActivityManager;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.activity.my.ReturnGoodsActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.LeaveDetailImageAdapter;
import com.zjyeshi.dajiujiao.buyer.adapter.sales.RebackSureProductAdapter;
import com.zjyeshi.dajiujiao.buyer.circle.task.UpLoadFileTask;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.enums.GoodTypeEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.RebackTypeEnum;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.Order;
import com.zjyeshi.dajiujiao.buyer.entity.order.AddOrderRequest;
import com.zjyeshi.dajiujiao.buyer.entity.sales.RebackOrderRequest;
import com.zjyeshi.dajiujiao.buyer.receiver.order.ChangeOrderStatusReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.data.order.OrderProduct;
import com.zjyeshi.dajiujiao.buyer.task.data.other.GetUpLoadFileData;
import com.zjyeshi.dajiujiao.buyer.task.sales.ApplyRebackTask;
import com.zjyeshi.dajiujiao.buyer.task.sales.GetRebackTotalAmountTask;
import com.zjyeshi.dajiujiao.buyer.task.sales.data.RebackMoney;
import com.zjyeshi.dajiujiao.buyer.utils.AuthUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.views.other.MyGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhk on 2017/5/15.
 */

public class RebackSureActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.rebackListView)
    private BUHighHeightListView rebackListView;
    @InjectView(R.id.rebacMarketListView)
    private BUHighHeightListView rebacMarketListView;
    @InjectView(R.id.rebackTypeTv)
    private TextView rebackTypeTv;
    @InjectView(R.id.remarkTv)
    private TextView remarkTv;
    @InjectView(R.id.gridView)
    private MyGridView gridView;
    @InjectView(R.id.marketCostTv)
    private TextView marketCostTv;
    @InjectView(R.id.totalMarketMoneyTv)
    private TextView totalMarketMoneyTv;
    @InjectView(R.id.totalMoneyTv)
    private TextView totalMoneyTv;
    @InjectView(R.id.addBtn)
    private Button addBtn;
    @InjectView(R.id.marketCostLayout)
    private RelativeLayout marketCostLayout;
    @InjectView(R.id.rebackTypeLayout)
    private RelativeLayout rebackTypeLayout;
    @InjectView(R.id.totalMarketMoneyLayout)
    private RelativeLayout totalMarketMoneyLayout;

    public static final String PARAM_APPLY_DATA = "param.apply.data";

    private List<OrderProduct> goodList = new ArrayList<OrderProduct>();
    private List<OrderProduct> marketGoodList = new ArrayList<OrderProduct>();
    private List<String> imageList = new ArrayList<String>();

    private RebackSureProductAdapter goodRebackSureProductAdapter;
    private RebackSureProductAdapter marketRebackSureProductAdapter;
    private LeaveDetailImageAdapter imageAdapter;
    private RebackApplySureData applyData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_reback_sure);
        initWidgets();
    }

    private void initWidgets() {

        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        }).configTitle("申请退货");

        applyData = JSON.parseObject(getIntent().getStringExtra(PARAM_APPLY_DATA), RebackApplySureData.class);
        goodList.clear();
        goodList.addAll(applyData.getGoodList());
        goodRebackSureProductAdapter = new RebackSureProductAdapter(RebackSureActivity.this, goodList, GoodTypeEnum.NORMAL_BUY.getValue());
        rebackListView.setAdapter(goodRebackSureProductAdapter);

        marketGoodList.clear();
        marketGoodList.addAll(applyData.getMarketGoodList());
        marketRebackSureProductAdapter = new RebackSureProductAdapter(RebackSureActivity.this, marketGoodList, GoodTypeEnum.MARKET_SUPPORT.getValue());
        rebacMarketListView.setAdapter(marketRebackSureProductAdapter);

        imageList.clear();
        imageList.addAll(applyData.getImageList());
        imageAdapter = new LeaveDetailImageAdapter(RebackSureActivity.this, imageList);
        gridView.setAdapter(imageAdapter);

        //设置扣除市场支持费用和返回市场支持费用
        if (AuthUtil.showMarketCostTab()){
            marketCostLayout.setVisibility(View.VISIBLE);
            totalMarketMoneyLayout.setVisibility(View.VISIBLE);
            float marketCostSupport = 0.00f;
            for(OrderProduct orderProduct : goodList){
                marketCostSupport += (Integer.parseInt(orderProduct.getMarketCost()) * Integer.parseInt(orderProduct.getCount()))/100;
            }
            marketCostTv.setText("¥" + ExtraUtil.format(marketCostSupport));
        }else{
            marketCostLayout.setVisibility(View.GONE);
            totalMarketMoneyLayout.setVisibility(View.GONE);
        }

        if (applyData.isHasSalesGive()){
            rebackTypeLayout.setVisibility(View.VISIBLE);
            rebackTypeTv.setText(applyData.getRebackTypeEnum().getName());
        }else{
            rebackTypeLayout.setVisibility(View.GONE);
        }

        if (Validators.isEmpty(applyData.getRemark())){
            remarkTv.setText("暂无备注");
        }else{
            remarkTv.setText(applyData.getRemark());
        }


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final RebackOrderRequest request = new RebackOrderRequest();
                request.setOrderId(applyData.getOrderId());
                request.setType(applyData.getRebackTypeEnum().getValue());
                request.setReturnReason(applyData.getRemark());

                setProduct(request , false);
                setProduct(request , true);

                List<String> editImageList = applyData.getImageList();
                if (!Validators.isEmpty(editImageList)){

                    StringBuilder pics = new StringBuilder();
                    for (String str : applyData.getImageList()){
                        pics.append(str);
                        pics.append(",");
                    }
                    if (pics.length() > 1){
                        pics.deleteCharAt(pics.length() - 1);
                    }

                    UpLoadFileTask upLoadFileTask = new UpLoadFileTask(RebackSureActivity.this);
                    upLoadFileTask.setShowProgressDialog(true);
                    upLoadFileTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<GetUpLoadFileData>() {
                        @Override
                        public void failCallback(Result<GetUpLoadFileData> result) {
                            ToastUtil.toast(result.getMessage());
                        }
                    });

                    upLoadFileTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<GetUpLoadFileData>() {
                        @Override
                        public void successCallback(Result<GetUpLoadFileData> result) {
                            request.setPics(result.getMessage());
                            ApplyRebackTask.applyRebackOrder(RebackSureActivity.this, request, new AsyncTaskSuccessCallback<NoResultData>() {
                                @Override
                                public void successCallback(Result<NoResultData> result) {
                                    ToastUtil.toast("已提交申请");
                                    BDActivityManager.removeAndFinishIncludes(RebackSureActivity.class.getSimpleName() , ApplyRebackActivity.class.getSimpleName());
                                    ChangeOrderStatusReceiver.notifyReceiver();
                                }
                            });
                        }
                    });

                    upLoadFileTask.execute(pics.toString() , String.valueOf(editImageList.size()));
                }else{
                    ApplyRebackTask.applyRebackOrder(RebackSureActivity.this, request, new AsyncTaskSuccessCallback<NoResultData>() {
                        @Override
                        public void successCallback(Result<NoResultData> result) {
                            ToastUtil.toast("已提交申请");
                            BDActivityManager.removeAndFinishIncludes(RebackSureActivity.class.getSimpleName() , ApplyRebackActivity.class.getSimpleName());
                            ChangeOrderStatusReceiver.notifyReceiver();
                        }
                    });
                }


            }
        });
        loadData();

    }

    private void loadData() {
        StringBuilder productIds = new StringBuilder();
        StringBuilder nums = new StringBuilder();
        if (!Validators.isEmpty(goodList)) {
            for (OrderProduct orderProduct : goodList) {
                productIds.append(orderProduct.getOrderProductId());
                productIds.append(",");

                nums.append(orderProduct.getCount());
                nums.append(",");
            }
        }
        if (productIds.length() > 1) {
            productIds.deleteCharAt(productIds.length() - 1);
        }
        if (nums.length() > 1) {
            nums.deleteCharAt(nums.length() - 1);
        }


        StringBuilder marketProductIds = new StringBuilder();
        StringBuilder marketNums = new StringBuilder();
        if (!Validators.isEmpty(marketGoodList)) {
            for (OrderProduct orderProduct : goodList) {
                marketProductIds.append(orderProduct.getOrderProductId());
                marketProductIds.append(",");

                marketNums.append(orderProduct.getCount());
                marketNums.append(",");
            }
        }
        if (marketProductIds.length() > 1) {
            marketProductIds.deleteCharAt(marketProductIds.length() - 1);
        }
        if (marketNums.length() > 1) {
            marketNums.deleteCharAt(marketNums.length() - 1);
        }

        GetRebackTotalAmountTask.getRebackTotalMoney(RebackSureActivity.this, applyData.getOrderId()
                , productIds.toString(), nums.toString(), marketProductIds.toString() , marketNums.toString() , String.valueOf(applyData.getRebackTypeEnum().getValue()), new AsyncTaskSuccessCallback<RebackMoney>() {
                    @Override
                    public void successCallback(Result<RebackMoney> result) {
                        totalMoneyTv.setText("¥" + ExtraUtil.format(ExtraUtil.getShowPrice(result.getValue().getRebackOrderTotalAmount())));
                        if (AuthUtil.showMarketCostTab()){
                            totalMarketMoneyLayout.setVisibility(View.VISIBLE);
                            totalMarketMoneyTv.setText("¥"  + ExtraUtil.format(ExtraUtil.getShowPrice(result.getValue().getRebackOrderMarketCostTotalAmount())));
                        }else{
                            totalMarketMoneyLayout.setVisibility(View.GONE);
                        }
                    }
                });


    }

    private void setProduct(RebackOrderRequest rebackOrderRequest , boolean isMarket){
        List<OrderProduct> tempList = new ArrayList<OrderProduct>();
        tempList.clear();
        if (isMarket){
            tempList.addAll(applyData.getMarketGoodList());
        }else{
            tempList.addAll(applyData.getGoodList());
        }
        StringBuilder orderProuctIds = new StringBuilder();
        StringBuilder nums = new StringBuilder();
        StringBuilder boxTypes = new StringBuilder();
        for (OrderProduct orderProduct : tempList){
            orderProuctIds.append(orderProduct.getOrderProductId());
            orderProuctIds.append(",");
            nums.append(orderProduct.getCount());
            nums.append(",");
            if (Integer.parseInt(orderProduct.getCount()) > Integer.parseInt(orderProduct.getBottlesPerBox())){
                boxTypes.append(AddOrderRequest.BOX_TYPE_XIANG);
            }else{
                boxTypes.append(AddOrderRequest.BOX_TYPE_UNIT);
            }
            boxTypes.append(",");
        }
        if (orderProuctIds.length() > 1 && nums.length() > 1 && boxTypes.length() > 1){
            orderProuctIds.deleteCharAt(orderProuctIds.length() - 1);
            nums.deleteCharAt(nums.length() - 1);
            boxTypes.deleteCharAt(boxTypes.length() -1);
        }

        if (isMarket){
            rebackOrderRequest.setMarkCostOrderProductIds(orderProuctIds.toString());
            rebackOrderRequest.setMarkCostNums(nums.toString());
            rebackOrderRequest.setMarkCostBoxTypes(boxTypes.toString());
        }else{
            rebackOrderRequest.setOrderProductIds(orderProuctIds.toString());
            rebackOrderRequest.setNums(nums.toString());
            rebackOrderRequest.setBoxType(boxTypes.toString());
        }

    }

    /**
     * 启动该Activity
     *
     * @param context
     * @param data
     */
    public static void startRebackSureActivty(Context context, RebackApplySureData data) {
        Intent intent = new Intent();
        intent.setClass(context, RebackSureActivity.class);
        intent.putExtra(PARAM_APPLY_DATA, JSON.toJSONString(data));
        context.startActivity(intent);
    }

    public static class RebackApplySureData {
        private String orderId;
        private List<OrderProduct> goodList;
        private List<OrderProduct> marketGoodList;
        private List<String> imageList;
        private String remark;
        private boolean hasSalesGive;
        private RebackTypeEnum rebackTypeEnum;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public boolean isHasSalesGive() {
            return hasSalesGive;
        }

        public void setHasSalesGive(boolean hasSalesGive) {
            this.hasSalesGive = hasSalesGive;
        }

        public List<OrderProduct> getGoodList() {
            return goodList;
        }

        public void setGoodList(List<OrderProduct> goodList) {
            this.goodList = goodList;
        }

        public List<OrderProduct> getMarketGoodList() {
            return marketGoodList;
        }

        public void setMarketGoodList(List<OrderProduct> marketGoodList) {
            this.marketGoodList = marketGoodList;
        }

        public List<String> getImageList() {
            return imageList;
        }

        public void setImageList(List<String> imageList) {
            this.imageList = imageList;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public RebackTypeEnum getRebackTypeEnum() {
            return rebackTypeEnum;
        }

        public void setRebackTypeEnum(RebackTypeEnum rebackTypeEnum) {
            this.rebackTypeEnum = rebackTypeEnum;
        }
    }
}
