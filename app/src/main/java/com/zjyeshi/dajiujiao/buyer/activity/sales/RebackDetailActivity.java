package com.zjyeshi.dajiujiao.buyer.activity.sales;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.StringUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigappleui.lib.view.listview.BUHighHeightListView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.activity.my.MaxLevelManActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.LeaveDetailImageAdapter;
import com.zjyeshi.dajiujiao.buyer.adapter.sales.RebackSureProductAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.enums.GoodTypeEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LoginEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.OrderStatusEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.PayTypeEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.RebackManagerStatusEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.RebackTypeEnum;
import com.zjyeshi.dajiujiao.buyer.receiver.sales.RefreshRebackManagerReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.data.order.OrderProduct;
import com.zjyeshi.dajiujiao.buyer.task.data.order.PathResData;
import com.zjyeshi.dajiujiao.buyer.task.sales.ApplyRebackTask;
import com.zjyeshi.dajiujiao.buyer.task.sales.CloseRebackTask;
import com.zjyeshi.dajiujiao.buyer.task.sales.GetRebackDetailTask;
import com.zjyeshi.dajiujiao.buyer.task.sales.GetRebackPathTask;
import com.zjyeshi.dajiujiao.buyer.task.sales.RebackChangeReviewMemberTask;
import com.zjyeshi.dajiujiao.buyer.task.sales.ReviewRebackApplyTask;
import com.zjyeshi.dajiujiao.buyer.task.sales.data.RebackDetailData;
import com.zjyeshi.dajiujiao.buyer.utils.AuthUtil;
import com.zjyeshi.dajiujiao.buyer.utils.DialogUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.views.other.MyGridView;
import com.zjyeshi.dajiujiao.buyer.widgets.PathData;
import com.zjyeshi.dajiujiao.buyer.widgets.PathWidget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhk on 2017/5/17.
 */

public class RebackDetailActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.rebackListView)
    private BUHighHeightListView rebackListView;
    @InjectView(R.id.rebacMarketListView)
    private BUHighHeightListView rebacMarketListView;
    @InjectView(R.id.rebackGiftLayout)
    private RelativeLayout rebackGiftLayout;
    @InjectView(R.id.rebackGiftTv)
    private TextView rebackGiftTv;
    @InjectView(R.id.rebackProductLayout)
    private RelativeLayout rebackProductLayout;
    @InjectView(R.id.rebackProductTv)
    private TextView rebackProductTv;
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
    @InjectView(R.id.redBtn)
    private Button redBtn;
    @InjectView(R.id.grayBtn)
    private Button grayBtn;
    @InjectView(R.id.nextBtn)
    private Button nextBtn;
    @InjectView(R.id.totalMarketMoneyLayout)
    private RelativeLayout totalMarketMoneyLayout;
    @InjectView(R.id.marketCostLayout)
    private RelativeLayout marketCostLayout;
    @InjectView(R.id.pathWidget)
    private PathWidget pathWidget;

    public static final String PARAM_REBACK_ID = "param.reback.id";
    public static final String PARAM_IS_DEALED = "param.is.dealed";
    public static final String PARAM_USER_TYPE = "param.user.type";

    private List<OrderProduct> goodList = new ArrayList<OrderProduct>();
    private List<OrderProduct> marketGoodList = new ArrayList<OrderProduct>();
    private List<String> imageList = new ArrayList<String>();

    private RebackSureProductAdapter goodRebackSureProductAdapter;
    private RebackSureProductAdapter marketRebackSureProductAdapter;
    private LeaveDetailImageAdapter imageAdapter;

    public static boolean reload = false;
    private String rebackId;
    private int userType;
    private boolean isDealed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_reback_detail);
        initWidgets();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (reload){
            isDealed = true;
            loadData();
        }

    }

    private void initWidgets(){
        titleLayout.configTitle("退货审核").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rebackId = getIntent().getStringExtra(PARAM_REBACK_ID);
        userType = getIntent().getIntExtra(PARAM_USER_TYPE , LoginEnum.SELLER.getValue());
        //是否转审过
        isDealed = getIntent().getBooleanExtra(PARAM_IS_DEALED , false);

        loadData();
    }

    private void loadData(){
        GetRebackDetailTask.getRebackDetail(RebackDetailActivity.this, rebackId, new AsyncTaskSuccessCallback<RebackDetailData>() {
            @Override
            public void successCallback(Result<RebackDetailData> result) {
                reload = false;
                final RebackDetailData data = result.getValue();
                goodList.clear();
                goodList.addAll(data.getProducts());
                goodRebackSureProductAdapter = new RebackSureProductAdapter(RebackDetailActivity.this, goodList, GoodTypeEnum.NORMAL_BUY.getValue());
                rebackListView.setAdapter(goodRebackSureProductAdapter);

                marketGoodList.clear();
                marketGoodList.addAll(data.getMarketCostProducts());
                marketRebackSureProductAdapter = new RebackSureProductAdapter(RebackDetailActivity.this, marketGoodList, GoodTypeEnum.MARKET_SUPPORT.getValue());
                rebacMarketListView.setAdapter(marketRebackSureProductAdapter);

                imageList.clear();
                if (!Validators.isEmpty(data.getPics())){
                    String image[] = StringUtils.split(data.getPics() , ",");
                    for (int i = 0 ; i < image.length ; i ++){
                        imageList.add(image[i]);
                    }
                }
                imageAdapter = new LeaveDetailImageAdapter(RebackDetailActivity.this, imageList);
                gridView.setAdapter(imageAdapter);

                //没有市场支持商品，就不要市场费用这一块了
                if (!AuthUtil.showMarketCostTab()){
                    totalMarketMoneyLayout.setVisibility(View.GONE);
                    marketCostLayout.setVisibility(View.GONE);
                }else{
                    totalMarketMoneyLayout.setVisibility(View.VISIBLE);
                    marketCostLayout.setVisibility(View.VISIBLE);
                    marketCostTv.setText("¥" + ExtraUtil.format(ExtraUtil.getShowPrice(data.getMarketCost())));
                    totalMarketMoneyTv.setText("¥" + ExtraUtil.format(ExtraUtil.getShowPrice(data.getReturnMarketCostAmount())));
                }

                totalMoneyTv.setText("¥" + ExtraUtil.format(ExtraUtil.getShowPrice(data.getReturnAmount())));

                //为空时就不显示了
                if(Validators.isEmpty(data.getGiftName())){
                    rebackGiftLayout.setVisibility(View.GONE);
                }else{
                    rebackGiftLayout.setVisibility(View.VISIBLE);
                    if (data.getType() == RebackTypeEnum.NOT_REBACK_GOFT.getValue()){
                        rebackGiftTv.setText("(不退)" + data.getGiftName());
                    }else{
                        rebackGiftTv.setText(data.getGiftName());
                    }
                }
                if (Validators.isEmpty(data.getProductName())){
                    rebackProductLayout.setVisibility(View.GONE);
                }else{
                    rebackProductLayout.setVisibility(View.VISIBLE);
                    if (data.getType() == RebackTypeEnum.NOT_REBACK_GOFT.getValue()){
                        rebackProductTv.setText("(不退)" + data.getProductName());
                    }else{
                        rebackProductTv.setText(data.getProductName());

                    }
                }

                //退货理由
                remarkTv.setText(data.getReturnReason());

                final int status = data.getStatus();

                if (userType == LoginEnum.SELLER.getValue()){
                    pathWidget.setVisibility(View.VISIBLE);
                    pathWidget.getPathDesTv().setText("退货审核流程");
                    GetRebackPathTask.getRebackPath(RebackDetailActivity.this, rebackId, new AsyncTaskSuccessCallback<PathResData>() {
                        @Override
                        public void successCallback(Result<PathResData> result) {
                            List<PathResData.Path> tempList = result.getValue().getList();
                            List<PathData> pathDataList = new ArrayList<PathData>();
                            for (int i = 0; i < tempList.size(); i++) {
                                PathResData.Path orderPath = tempList.get(i);
                                PathData pathData = new PathData();
                                pathData.setAvatar(orderPath.getPic());
                                pathData.setName(orderPath.getApplicate());
                                pathData.setCreationiTime(orderPath.getCreationTime());
                                if (i == tempList.size() - 1) {
                                    if (status != RebackManagerStatusEnum.WAIT_REVIEW.getValue()) {
                                        pathData.setShowCheck(true);
                                        if (status == RebackManagerStatusEnum.REFUSE_REVIEW.getValue()){
                                            pathData.setRefused(true);
                                        }else {
                                            pathData.setRefused(false);
                                        }
                                    } else {
                                        pathData.setShowCheck(false);
                                    }
                                } else {
                                    pathData.setShowCheck(true);
                                }
                                pathDataList.add(pathData);
                            }
                            pathWidget.bindData(pathDataList);
                        }
                    });

                }else{
                    pathWidget.setVisibility(View.GONE);
                }
                if (status == RebackManagerStatusEnum.WAIT_REVIEW.getValue()){
                    if (userType == LoginEnum.SELLER.getValue()){
                        if(AuthUtil.canSureOrder()){
                            redBtn.setVisibility(View.VISIBLE);
                            redBtn.setText("同意");
                            redBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (data.getOrderType() == 1){
                                        DialogUtil.confirmSure(RebackDetailActivity.this, "此订单为线上支付，请认真审核货品，退款由总公司退还用户，切勿现返还", "确定", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                passReback();
                                            }
                                        });
                                    }else{
                                        passReback();
                                    }
                                }
                            });

                            grayBtn.setVisibility(View.VISIBLE);
                            grayBtn.setText("拒绝");
                            grayBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    RefuseReasonListActivity.startRefuseReasonActivity(RebackDetailActivity.this ,rebackId);
                                }
                            });

                        }else{
                            redBtn.setVisibility(View.GONE);
                            grayBtn.setVisibility(View.GONE);
                        }
                        nextBtn.setVisibility(View.VISIBLE);
                        nextBtn.setText("下一级审批");
                        nextBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MaxLevelManActivity.startMaxLevelManActivity(RebackDetailActivity.this , rebackId , MaxLevelManActivity.TYPE_REBACK);
                            }
                        });

                        if (isDealed){
                            //我已经处理过了
                            redBtn.setVisibility(View.GONE);
                            grayBtn.setVisibility(View.GONE);
                            nextBtn.setVisibility(View.GONE);
                        }
                    }else{
                        redBtn.setVisibility(View.GONE);
                        grayBtn.setVisibility(View.VISIBLE);
                        grayBtn.setText("您的申请已提交，请及时与卖家联系");
                        nextBtn.setVisibility(View.GONE);
                    }
                }else if (status == RebackManagerStatusEnum.PASS_REVIEW.getValue()){
                    redBtn.setVisibility(View.GONE);
                    grayBtn.setVisibility(View.VISIBLE);
                    grayBtn.setText("已退款");
                    grayBtn.setEnabled(false);
                    nextBtn.setVisibility(View.GONE);
                }else if (status == RebackManagerStatusEnum.REFUSE_REVIEW.getValue()){
                    nextBtn.setVisibility(View.GONE);
                    if(userType == LoginEnum.SELLER.getValue()){
                        redBtn.setVisibility(View.GONE);
                        grayBtn.setVisibility(View.VISIBLE);
                        grayBtn.setText("已拒绝");
                    }else{
                        redBtn.setVisibility(View.VISIBLE);
                        redBtn.setText("重新申请");
                        redBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ApplyRebackActivity.startApplyRebackActivity(RebackDetailActivity.this , data.getOrderId());
                            }
                        });
                        grayBtn.setVisibility(View.VISIBLE);
                        grayBtn.setText("关闭");
                        grayBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CloseRebackTask.closeRebackOrder(RebackDetailActivity.this, data.getOrderId(), new AsyncTaskSuccessCallback<NoResultData>() {
                                    @Override
                                    public void successCallback(Result<NoResultData> result) {
                                        ToastUtil.toast("已关闭");
                                        loadData();
                                    }
                                });
                            }
                        });
                    }
                }else if (status == RebackManagerStatusEnum.CLOSE_REVIEW.getValue()){
                    nextBtn.setVisibility(View.GONE);
                    redBtn.setVisibility(View.GONE);
                    grayBtn.setVisibility(View.VISIBLE);
                    grayBtn.setText("已关闭");
                }

            }
        });
    }


    private void passReback(){
        ReviewRebackApplyTask.reviewReback(RebackDetailActivity.this, rebackId, String.valueOf(2), " ", new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                ToastUtil.toast("已同意退款");
                loadData();
                RefreshRebackManagerReceiver.notifyReceiver();
            }
        });
    }

    public static void startReabckDetailActivityBySeller(Context context , String rebackId , boolean isDealed){
        Intent intent = new Intent();
        intent.setClass(context , RebackDetailActivity.class);
        intent.putExtra(PARAM_REBACK_ID , rebackId);
        intent.putExtra(PARAM_USER_TYPE , LoginEnum.SELLER.getValue());
        intent.putExtra(PARAM_IS_DEALED , isDealed);
        context.startActivity(intent);
    }

    public static void startReabckDetailActivityByBuyer(Context context , String rebackId){
        Intent intent = new Intent();
        intent.setClass(context , RebackDetailActivity.class);
        intent.putExtra(PARAM_REBACK_ID , rebackId);
        intent.putExtra(PARAM_USER_TYPE , LoginEnum.BURER.getValue());
        context.startActivity(intent);
    }
}
