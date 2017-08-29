package com.zjyeshi.dajiujiao.buyer.activity.sales;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.DateUtils;
import com.xuan.bigdog.lib.dialog.DGSingleSelectDialog;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.sales.JoinAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.enums.EnvironmentTypeEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.SalesFillTypeEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.SalesFormTypeEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.SalesGiveTypeEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.SalesTypeEnum;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.sales.GetSalesDetailTask;
import com.zjyeshi.dajiujiao.buyer.task.sales.ModifySalesPriorityTask;
import com.zjyeshi.dajiujiao.buyer.task.sales.ModifySalesStatusTask;
import com.zjyeshi.dajiujiao.buyer.task.sales.RemoveSalesTask;
import com.zjyeshi.dajiujiao.buyer.task.sales.data.SalesDetailData;
import com.zjyeshi.dajiujiao.buyer.utils.DialogUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.views.other.IVButtonBox;
import com.zjyeshi.dajiujiao.buyer.widgets.dialog.LeaveTypeDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wuhk on 2017/4/25.
 */

public class SalesDetailActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.salesTypeTv)
    private TextView salesTypeTv;
    @InjectView(R.id.fillDesTv)
    private TextView fillDesTv;
    @InjectView(R.id.fillTv)
    private TextView fillTv;
    @InjectView(R.id.cutDesTv)
    private TextView cutDesTv;
    @InjectView(R.id.cutTv)
    private TextView cutTv;
    @InjectView(R.id.startTimeTv)
    private TextView startTimeTv;
    @InjectView(R.id.stopTimeTv)
    private TextView stopTimeTv;
    @InjectView(R.id.creationTimeTv)
    private TextView creationTimeTv;
    @InjectView(R.id.buttonIv)
    private IVButtonBox buttonIv;
    @InjectView(R.id.priorityTv)
    private TextView priorityTv;
    @InjectView(R.id.priorityIv)
    private ImageView priorityIv;
    @InjectView(R.id.joinShopListView)
    private ListView joinShopListView;
    @InjectView(R.id.joinProductListView)
    private ListView joinProductListView;
    @InjectView(R.id.joinProductLayout)

    private static final String PARAM_SALES_ID = "param.sales.id";
    private String salesId;

    private JoinAdapter joinShopAdapter;
    private List<SalesDetailData.Join> joinShopList = new ArrayList<SalesDetailData.Join>();

    private JoinAdapter joinProductAdapter;
    private List<SalesDetailData.Join> joinProductList = new ArrayList<SalesDetailData.Join>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sales_detail);
        initWidgets();
    }

    private void initWidgets() {
        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        }).configTitle("活动详情");

        salesId = getIntent().getStringExtra(PARAM_SALES_ID);

        joinShopAdapter = new JoinAdapter(SalesDetailActivity.this , joinShopList);
        joinShopListView.setAdapter(joinShopAdapter);

        joinProductAdapter = new JoinAdapter(SalesDetailActivity.this , joinProductList);
        joinProductListView.setAdapter(joinProductAdapter);

        loadData();
    }


    private void loadData(){
        GetSalesDetailTask.getSalesDetail(SalesDetailActivity.this, salesId, new AsyncTaskSuccessCallback<SalesDetailData>() {
            @Override
            public void successCallback(Result<SalesDetailData> result) {
                final SalesDetailData data = result.getValue();
                salesTypeTv.setText("活动详情");
                //满足条件
                SalesFillTypeEnum salesFillTypeEnum = SalesFillTypeEnum.valueOf(data.getSatisfyType());
                String fillDes = SalesFormTypeEnum.valueOf(data.getFormType()).getName();
                if (salesFillTypeEnum.equals(SalesFillTypeEnum.FILL_MONEY)){
                    fillDesTv.setText(fillDes);
                    fillTv.setText(data.getSatisfyCondition());
                }else if (salesFillTypeEnum.equals(SalesFillTypeEnum.FILL_BOX)){
                    fillDesTv.setText(fillDes);
                    fillTv.setText(data.getSatisfyCondition() + "箱");
                }else if (salesFillTypeEnum.equals(SalesFillTypeEnum.MIX_BOX)){
                    fillDesTv.setText("合购" + fillDes);
                    fillTv.setText(data.getSatisfyCondition() + "箱");
                }


                //优惠政策
                SalesGiveTypeEnum salesGiveTypeEnum = SalesGiveTypeEnum.valueOf(data.getFavouredType());
                if (salesGiveTypeEnum.equals(SalesGiveTypeEnum.GIVE_GIFT)){
                    cutDesTv.setText("送");
                    cutTv.setText(data.getGiftName());
                }else if (salesGiveTypeEnum.equals(SalesGiveTypeEnum.BACK_MONEY)){
                    cutDesTv.setText("返");
                    cutTv.setText(data.getFavouredPolicy());
                }else if (salesGiveTypeEnum.equals(SalesGiveTypeEnum.CUT_MONEY)){
                    cutDesTv.setText("减");
                    cutTv.setText(data.getFavouredPolicy());
                }else if (salesGiveTypeEnum.equals(SalesGiveTypeEnum.GIVE_WINE)){
                    cutDesTv.setText("送");
                    String giveProductName = data.getGiveProduct().getName();
                    cutTv.setText(giveProductName + data.getFavouredPolicy() + "箱");
                }

                joinShopList.clear();
                for(SalesDetailData.SalesShop salesShop : data.getShops()){
                    SalesDetailData.Join join = new SalesDetailData.Join();
                    join.setId(salesShop.getId());
                    join.setShopProductId(salesShop.getShopId());
                    join.setName(salesShop.getName());
                    join.setPic(salesShop.getPic());
                    joinShopList.add(join);
                }
                joinShopAdapter.notifyDataSetChanged();

                joinProductList.clear();
                for (SalesDetailData.SalesProduct salesProduct : data.getProducts()){
                    SalesDetailData.Join join = new SalesDetailData.Join();
                    join.setId(salesProduct.getId());
                    join.setShopProductId(salesProduct.getProductId());
                    join.setName(salesProduct.getName());
                    join.setPic(salesProduct.getPic());
                    joinProductList.add(join);
                }
                joinProductAdapter.notifyDataSetChanged();

                //时间
                startTimeTv.setText("起始日期:" + data.getStartTime());
                stopTimeTv.setText("截止日期:" + data.getEndTime());
                creationTimeTv.setText("创建日期:" + DateUtils.date2StringByDay(new Date(data.getCreationTime())));

                //活动状态
                if (data.getStatus() == 1){
                    //激活状态
                    buttonIv.setChecked(true);
                }else{
                    //暂停状态
                    buttonIv.setChecked(false);
                }
                buttonIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int status;
                        if (buttonIv.isChecked()){
                            status = 1;
                        }else{
                            status = 2;
                        }
                        ModifySalesStatusTask.modifSalesStatus(SalesDetailActivity.this, data.getId(), String.valueOf(status), new AsyncTaskSuccessCallback<NoResultData>() {
                            @Override
                            public void successCallback(Result<NoResultData> result) {
                                ToastUtil.toast("修改活动状态成功");
                                loadData();
                            }
                        });
                    }
                });

                if (data.isSuperposition()){
                    priorityTv.setVisibility(View.GONE);
                    priorityIv.setVisibility(View.GONE);
                }else{
                    priorityTv.setVisibility(View.VISIBLE);
                    priorityTv.setText("优先级:" + data.getPriority());
                    priorityIv.setVisibility(View.VISIBLE);
                    priorityIv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DialogUtil.selectLeaveType(SalesDetailActivity.this, new LeaveTypeDialog.ItemClickListener() {
                                @Override
                                public void itemClick(String content) {
                                    ModifySalesPriorityTask.modifySalesPriority(SalesDetailActivity.this, data.getId(), content, new AsyncTaskSuccessCallback<NoResultData>() {
                                        @Override
                                        public void successCallback(Result<NoResultData> result) {
                                            ToastUtil.toast("修改成功");
                                            loadData();
                                        }
                                    });
                                }
                            } , AddNewSalesActivity.getPriorityList());
                        }
                    });
                }

//                //删除活动
//                delIv.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        String[] itemStr = {"删除活动"};
//                        View.OnClickListener[] ls = {new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                RemoveSalesTask.removeSales(SalesDetailActivity.this, data.getId(), new AsyncTaskSuccessCallback<NoResultData>() {
//                                    @Override
//                                    public void successCallback(Result<NoResultData> result) {
//                                        ToastUtil.toast("删除活动成功");
//                                        SalesActivity.reload = true;
//                                        finish();
//                                    }
//                                });
//                            }
//                        }};
//                        DGSingleSelectDialog dialog = new DGSingleSelectDialog.Builder(SalesDetailActivity.this).setItemTextAndOnClickListener(itemStr, ls).create();
//                        dialog.show();
//                    }
//                });
            }

        });
    }
    /**
     * 启动该页面
     *
     * @param context
     * @param salesId
     */
    public static void startSalesDetailActivity(Context context, String salesId) {
        Intent intent = new Intent();
        intent.putExtra(PARAM_SALES_ID, salesId);
        intent.setClass(context, SalesDetailActivity.class);
        context.startActivity(intent);

    }
}
