package com.zjyeshi.dajiujiao.buyer.activity.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.DateUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigappleui.lib.view.listview.BUHighHeightListView;
import com.xuan.bigdog.lib.dialog.DGPromptDialog;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.activity.my.MaxLevelManActivity;
import com.zjyeshi.dajiujiao.buyer.activity.my.personal.EditAddressActivity;
import com.zjyeshi.dajiujiao.buyer.activity.sales.ApplyRebackActivity;
import com.zjyeshi.dajiujiao.buyer.activity.sales.RebackDetailActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.order.OrderDetailGoodListAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.GoodsCar;
import com.zjyeshi.dajiujiao.buyer.entity.enums.GoodTypeEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LoginEnum;
import com.zjyeshi.dajiujiao.buyer.receiver.order.ChangeOrderStatusReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.order.OrderProduct;
import com.zjyeshi.dajiujiao.buyer.task.order.GetOrderPathTask;
import com.zjyeshi.dajiujiao.buyer.task.order.ModifyOrderRemarkTask;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.OrderUtil;
import com.zjyeshi.dajiujiao.buyer.views.order.OrderPayDesView;
import com.zjyeshi.dajiujiao.buyer.widgets.store.OrderProgressWidget;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.my.ReturnGoodsActivity;
import com.zjyeshi.dajiujiao.buyer.activity.store.BalanceAccountsActivity;
import com.zjyeshi.dajiujiao.buyer.entity.enums.OrderStatusEnum;
import com.zjyeshi.dajiujiao.buyer.entity.my.Address;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.data.order.OrderDetailData;
import com.zjyeshi.dajiujiao.buyer.task.data.order.PathResData;
import com.zjyeshi.dajiujiao.buyer.task.order.GetOrderDetailTask;
import com.zjyeshi.dajiujiao.buyer.utils.AuthUtil;
import com.zjyeshi.dajiujiao.buyer.utils.DialogUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.widgets.PathData;
import com.zjyeshi.dajiujiao.buyer.widgets.PathWidget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhk on 2016/9/14.
 */
public class OrderDetailNewActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.listView)
    private BUHighHeightListView listView;
    @InjectView(R.id.marketListView)
    private BUHighHeightListView marketListView;
    @InjectView(R.id.shopNameTv)
    private TextView shopNameTv;
    @InjectView(R.id.orderNumTv)
    private TextView orderNumTv;
    @InjectView(R.id.timeTv)
    private TextView timeTv;
    @InjectView(R.id.receiverTv)
    private TextView receiverTv;
    @InjectView(R.id.phoneTv)
    private TextView phoneTv;
    @InjectView(R.id.addressTv)
    private TextView addressTv;
    @InjectView(R.id.leftTv)
    private TextView leftTv;
    @InjectView(R.id.redTv)
    private TextView redTv;
    @InjectView(R.id.bottomLayout)
    private LinearLayout bottomLayout;
    @InjectView(R.id.userInfoLayout)
    private LinearLayout userInfoLayout;
    @InjectView(R.id.orderProgressWidget)
    private OrderProgressWidget orderProgressWidget;
    @InjectView(R.id.moneyLayout)
    private LinearLayout moneyLayout;
    @InjectView(R.id.moneyDivider)
    private View moneyDivider;
    @InjectView(R.id.orderStatusDesTv)
    private TextView orderStatusDesTv;
    @InjectView(R.id.editAddressRl)
    private RelativeLayout editAddressRl;
    @InjectView(R.id.editRemarkRl)
    private RelativeLayout editRemarkRl;
    @InjectView(R.id.remarkTv)
    private TextView remarkTv;
    @InjectView(R.id.truePayTv)
    private TextView truePayTv;
    @InjectView(R.id.truePayLayout)
    private RelativeLayout truePayLayout;
    @InjectView(R.id.pathWidget)
    private PathWidget pathWidget;
    @InjectView(R.id.orderPayDesView)
    private OrderPayDesView orderPayDesView;

    private List<OrderProduct> goodList = new ArrayList<OrderProduct>();
    private OrderDetailGoodListAdapter orderDetailGoodListAdapter;
    private List<OrderProduct> marketGoodList = new ArrayList<OrderProduct>();
    private OrderDetailGoodListAdapter marketOrderDetailGoodListAdapter;

    private ChangeOrderStatusReceiver changeOrderStatusReceiver;//订单状态改变刷新广播

    public static String ORDERID = "order_id";
    public static String TYPE = "type";

    private String orderId;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_order_detail_new);
        initWidgets();

        changeOrderStatusReceiver = new ChangeOrderStatusReceiver() {
            @Override
            public void changeStatus() {
                initWidgets();
            }
        };
        changeOrderStatusReceiver.register();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        changeOrderStatusReceiver.unRegister();
    }


    private void initWidgets() {
        orderId = getIntent().getStringExtra(ORDERID);
        type = getIntent().getStringExtra(TYPE);

        titleLayout.configReturn("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle("订单详情");
        getData();
    }

    /**
     * 获取数据
     */
    private void getData() {
        GetOrderDetailTask getOrderDetailTask = new GetOrderDetailTask(OrderDetailNewActivity.this);

        getOrderDetailTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<OrderDetailData>() {
            @Override
            public void failCallback(Result<OrderDetailData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        getOrderDetailTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<OrderDetailData>() {
            @Override
            public void successCallback(Result<OrderDetailData> result) {
                OrderDetailData orderDetailData = result.getValue();
                bindData(orderDetailData);
            }
        });

        getOrderDetailTask.execute(orderId);
    }

    private void bindData(final OrderDetailData orderDetailData) {

        orderNumTv.setText("编号:" + orderDetailData.getNumber());
        timeTv.setText(DateUtils.date2StringBySecond(orderDetailData.getCreationTime()));

        if (Validators.isEmpty(orderDetailData.getRemark())) {
            remarkTv.setText("未添加备注");
        } else {
            remarkTv.setText(orderDetailData.getRemark());
        }

        //地址信息
        if (null != orderDetailData.getAddress()) {
            userInfoLayout.setVisibility(View.VISIBLE);
            receiverTv.setText(orderDetailData.getAddress().getName());
            phoneTv.setText(orderDetailData.getAddress().getPhone());
            addressTv.setText(orderDetailData.getAddress().getAddress());

            final Address address = new Address();
            address.setPhone(orderDetailData.getAddress().getPhone());
            address.setName(orderDetailData.getAddress().getName());
            address.setAddress(orderDetailData.getAddress().getAddress());
            //修改地址
            editAddressRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditAddressActivity.startActivity(OrderDetailNewActivity.this, address, orderDetailData.getId());
                }
            });
            //修改备注
            editRemarkRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DGPromptDialog dialog = new DGPromptDialog.Builder(
                            OrderDetailNewActivity.this)
                            .setTitle("提示")
                            .setMessage("请修改备注")
                            .setLeftBtnText("取消")
                            .setDefaultContent(orderDetailData.getRemark())
                            .setOnLeftBtnListener(new DGPromptDialog.PromptDialogListener() {
                                @Override
                                public void onClick(View view, String inputText) {

                                }
                            }).setRightBtnText("确定")
                            .setOnRightBtnListener(new DGPromptDialog.PromptDialogListener() {
                                @Override
                                public void onClick(View view, String inputText) {
                                    if (!TextUtils.isEmpty(inputText)) {
                                        modifyOrderRemark(orderDetailData.getId(), inputText);
                                    } else {
                                        ToastUtil.toast("请输入备注信息");
                                    }
                                }
                            }).create();
                    dialog.show();
                }
            });
        } else {
            userInfoLayout.setVisibility(View.GONE);
        }

        //先隐藏地址修改，需要的地方显示
        editAddressRl.setVisibility(View.GONE);
        editRemarkRl.setVisibility(View.GONE);
        //订单状态
        OrderStatusEnum orderStatusEnum = OrderStatusEnum.valueOf(orderDetailData.getStatus());
        //进度条
        orderProgressWidget.bindData(type, orderDetailData);

        bottomLayout.setVisibility(View.VISIBLE);
        /////////////////////////////////////////////如果是买家//////////////////////////////////////
        if (type.equals(LoginEnum.BURER.toString())) {

            //买家显示店铺名
            shopNameTv.setText(orderDetailData.getShoName());

            pathWidget.setVisibility(View.GONE);
            if (orderStatusEnum.equals(OrderStatusEnum.WAITPAY)) {
                //待付款(1)
                orderStatusDesTv.setText("待付款");

                redTv.setVisibility(View.VISIBLE);
                redTv.setText("去付款");
                //去付款
                redTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BalanceAccountsActivity.startBalanceActivity(OrderDetailNewActivity.this, getGoodList(orderDetailData.getProducts()), getGoodList(orderDetailData.getMarketCostProducts())
                                , orderDetailData.getId(), BalanceAccountsActivity.FROM_ORDER
                                , String.valueOf(Integer.parseInt(orderDetailData.getAmount()) + Integer.parseInt(orderDetailData.getMarketCostAmount()) ) , false , "","");
                    }
                });
                //关闭订单
                leftTv.setVisibility(View.VISIBLE);
                leftTv.setText("取消订单");
                leftTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtil.confirmSure(OrderDetailNewActivity.this, "是否取消订单", "确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String status = String.valueOf(OrderStatusEnum.CLOSED.getValue());
                                OrderUtil.changeOrderStatus(OrderDetailNewActivity.this, orderDetailData.getId(), status);
                            }
                        });
                    }
                });
            } else if (orderStatusEnum.equals(OrderStatusEnum.PAYED)) {
                //已付款(2)
                orderStatusDesTv.setText("等待商家确认");
                redTv.setVisibility(View.GONE);
                leftTv.setVisibility(View.VISIBLE);
                leftTv.setText("申请退货");
                leftTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApplyRebackActivity.startApplyRebackActivity(OrderDetailNewActivity.this , orderDetailData.getId());
                    }
                });

            } else if (orderStatusEnum.equals(OrderStatusEnum.SURED)) {
                //已确认(3)
                orderStatusDesTv.setText("订单已确认");
                leftTv.setVisibility(View.VISIBLE);
                leftTv.setText("申请退货");
                leftTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApplyRebackActivity.startApplyRebackActivity(OrderDetailNewActivity.this , orderDetailData.getId());
                    }
                });
                redTv.setVisibility(View.GONE);
            } else if (orderStatusEnum.equals(OrderStatusEnum.SENDED)) {
                //已发货(5)
                orderStatusDesTv.setText("订单已发货");
                leftTv.setVisibility(View.VISIBLE);
                leftTv.setText("申请退货");
                leftTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ApplyRebackActivity.startApplyRebackActivity(OrderDetailNewActivity.this , orderDetailData.getId());
                    }
                });
                redTv.setVisibility(View.VISIBLE);
                redTv.setText("确认收货");
                redTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtil.confirmSure(OrderDetailNewActivity.this, "是否确认收货", "确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String status = String.valueOf(OrderStatusEnum.ALREADYGET.getValue());
                                OrderUtil.changeOrderStatus(OrderDetailNewActivity.this, orderDetailData.getProducts(), orderDetailData.getMarketCostProducts(),orderDetailData.getId(), status, true);
                            }
                        });
                    }
                });

            } else if (orderStatusEnum.equals(OrderStatusEnum.ALREADYGET)) {
                //已收，未评价(7)
                orderStatusDesTv.setText("已收货");
                leftTv.setVisibility(View.GONE);
                redTv.setVisibility(View.VISIBLE);
                redTv.setText("去评价");
                redTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OrderProductListActivity.startActivity(OrderDetailNewActivity.this, orderDetailData.getProducts(), orderDetailData.getMarketCostProducts() , orderDetailData.getId());
                    }
                });
            } else if (orderStatusEnum.equals(OrderStatusEnum.ALREADYCOMMENT)) {
                //已收，已评价(8)
                orderStatusDesTv.setText("已收货");
                leftTv.setVisibility(View.GONE);
                redTv.setVisibility(View.VISIBLE);
                redTv.setText("追加评价");
                redTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OrderProductListActivity.startActivity(OrderDetailNewActivity.this, orderDetailData.getProducts(), orderDetailData.getMarketCostProducts(), orderDetailData.getId());
                    }
                });
            } else if (orderStatusEnum.equals(OrderStatusEnum.CLOSED)) {
                //已关闭，已取消(9)
                orderStatusDesTv.setText("已取消");
                bottomLayout.setVisibility(View.GONE);
                //已取消不显示价格
                moneyLayout.setVisibility(View.GONE);
                moneyDivider.setVisibility(View.GONE);
            } else if (orderStatusEnum.equals(OrderStatusEnum.WAITRETURN)) {
                //待退货(12)
                orderStatusDesTv.setText("待退货");
                bottomLayout.setVisibility(View.GONE);
            } else if (orderStatusEnum.equals(OrderStatusEnum.RETURNSUCCESS)) {
                //退货成功(13)
                orderStatusDesTv.setText("退货成功");
                bottomLayout.setVisibility(View.GONE);
            } else if (orderStatusEnum.equals(OrderStatusEnum.RETURNFAILED)) {
                //退货失败(14)
                orderStatusDesTv.setText("退货失败");
                bottomLayout.setVisibility(View.GONE);
            }else if (orderStatusEnum.equals(OrderStatusEnum.NEW_TOBEREBACKED) || orderStatusEnum.equals(OrderStatusEnum.NEW_REBACK_SUCCESS)
                    || orderStatusEnum.equals(OrderStatusEnum.NEW_REBACK_FAIL) || orderStatusEnum.equals(OrderStatusEnum.NEW_REBACK_CLOSE)){
                orderStatusDesTv.setText(orderStatusEnum.toString());
                bottomLayout.setVisibility(View.VISIBLE);
                leftTv.setVisibility(View.GONE);
                redTv.setVisibility(View.VISIBLE);
                redTv.setText("查看退款详情");
                redTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RebackDetailActivity.startReabckDetailActivityByBuyer(OrderDetailNewActivity.this , orderDetailData.getRebackId());
                    }
                });
            }else {
                orderStatusDesTv.setText("未知");
                bottomLayout.setVisibility(View.GONE);
            }
        }
        //////////////////////////////////////////////如果是卖家//////////////////////////////////////
        else {

            //卖家显示消费者名
            if (!Validators.isEmpty(orderDetailData.getBuyerShop())) {
                initTextView(shopNameTv, orderDetailData.getBuyerShop() + "(" + orderDetailData.getSalesman() + ")");
            } else {
                initTextView(shopNameTv, orderDetailData.getBuyer());
            }

            pathWidget.setVisibility(View.VISIBLE);
            //显示订单流程
            GetOrderPathTask.showOrderPath(OrderDetailNewActivity.this, orderDetailData.getId(), new AsyncTaskSuccessCallback<PathResData>() {
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
                           // if (orderDetailData.getStatus() == OrderStatusEnum.SURED.getValue()) {
                            if (orderPath.getStatus().equals(OrderStatusEnum.PAYED.getValue()+"")){
                                pathData.setShowCheck(true);
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

            if (orderStatusEnum.equals(OrderStatusEnum.WAITPAY)) {
                //待付款(1)
                editAddressRl.setVisibility(View.VISIBLE);
                editRemarkRl.setVisibility(View.VISIBLE);
                orderStatusDesTv.setText("待付款");
                bottomLayout.setVisibility(View.GONE);
            } else if (orderStatusEnum.equals(OrderStatusEnum.PAYED)) {
                //已付款(2)
                editAddressRl.setVisibility(View.VISIBLE);
                editRemarkRl.setVisibility(View.VISIBLE);
                orderStatusDesTv.setText("等待确认");
                //业务员不能确认订单，但是可以转订单，
                //Boss一开始没有操作，但是能看到订单，只有转到的订单才能转和确认，
                //转过的就没有转操作了
                if (AuthUtil.isSalesEdit()) {
                    if (orderDetailData.isDealWith()) {
                        leftTv.setVisibility(View.GONE);
                        redTv.setVisibility(View.VISIBLE);
                        redTv.setText("订单转审");
                        redTv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MaxLevelManActivity.startMaxLevelManActivity(OrderDetailNewActivity.this, orderDetailData.getId() , MaxLevelManActivity.TYPE_ORDER);
                            }
                        });
                    } else {
                        leftTv.setVisibility(View.GONE);
                        redTv.setVisibility(View.GONE);
                    }
                } else {
                    //boss
                    if (orderDetailData.isDealWith()) {
                        leftTv.setVisibility(View.VISIBLE);
                        leftTv.setText("订单转审");
                        leftTv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MaxLevelManActivity.startMaxLevelManActivity(OrderDetailNewActivity.this, orderDetailData.getId() , MaxLevelManActivity.TYPE_ORDER);
                            }
                        });
                        redTv.setVisibility(View.VISIBLE);
                        redTv.setText("确认订单");
                        redTv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String status = String.valueOf(OrderStatusEnum.SURED.getValue());
                                OrderUtil.modifyOrderStatus(OrderDetailNewActivity.this, orderDetailData.getId(), status);
                            }
                        });
                    } else {
                        leftTv.setVisibility(View.GONE);
                        redTv.setVisibility(View.GONE);
                    }

                }

            } else if (orderStatusEnum.equals(OrderStatusEnum.SURED)) {
                //已确认(3)
                orderStatusDesTv.setText("订单已确认");
                leftTv.setVisibility(View.GONE);
                if (AuthUtil.canDeliver()) {
                    redTv.setVisibility(View.VISIBLE);
                    redTv.setText("确认发货");
                    redTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogUtil.editDialog(OrderDetailNewActivity.this, "提示", "添加发货备注，没有可不填", "取消", "确定", new DGPromptDialog.PromptDialogListener() {
                                @Override
                                public void onClick(View view, String inputText) {
                                    //取消，不做操作
                                }
                            }, new DGPromptDialog.PromptDialogListener() {
                                @Override
                                public void onClick(View view, String inputText) {
                                    String status = String.valueOf(OrderStatusEnum.SENDED.getValue());
                                    //String status="5";
                                    OrderUtil.modifyOrderStatusForDeliver(OrderDetailNewActivity.this, orderDetailData.getId(), status, inputText);
                                }
                            });

                        }
                    });
                } else {
                    bottomLayout.setVisibility(View.GONE);
                }
            } else if (orderStatusEnum.equals(OrderStatusEnum.SENDED)) {
                //已发货(5)
                orderStatusDesTv.setText("订单已发货 , 等待买家收货");
                bottomLayout.setVisibility(View.GONE);

            } else if (orderStatusEnum.equals(OrderStatusEnum.ALREADYGET)) {
                //已收，未评价(7)
                orderStatusDesTv.setText("已收货");
                bottomLayout.setVisibility(View.GONE);
            } else if (orderStatusEnum.equals(OrderStatusEnum.ALREADYCOMMENT)) {
                //已收，已评价(8)
                orderStatusDesTv.setText("已收货");
                bottomLayout.setVisibility(View.GONE);
            } else if (orderStatusEnum.equals(OrderStatusEnum.CLOSED)) {
                //已关闭，已取消(9)
                orderStatusDesTv.setText("已取消");
                bottomLayout.setVisibility(View.GONE);
                //已取消不显示价格
                moneyLayout.setVisibility(View.GONE);
                moneyDivider.setVisibility(View.GONE);
            } else if (orderStatusEnum.equals(OrderStatusEnum.WAITRETURN)) {
                //待退货(12)
                orderStatusDesTv.setText("待退货");
                bottomLayout.setVisibility(View.GONE);
            } else if (orderStatusEnum.equals(OrderStatusEnum.RETURNSUCCESS)) {
                //退货成功(13)
                orderStatusDesTv.setText("退货成功");
                bottomLayout.setVisibility(View.GONE);
            } else if (orderStatusEnum.equals(OrderStatusEnum.RETURNFAILED)) {
                //退货失败(14)
                orderStatusDesTv.setText("退货失败");
                bottomLayout.setVisibility(View.GONE);
            } else {
                orderStatusDesTv.setText("未知");
                bottomLayout.setVisibility(View.GONE);
            }
        }

        //常规购买商品列表
        goodList.clear();
        goodList.addAll(orderDetailData.getProducts());
        //市场支持商品列表
        marketGoodList.clear();
        marketGoodList.addAll(orderDetailData.getMarketCostProducts());

        //待付款，不显示描述，总价显示Amount
        orderPayDesView.setVisibility(View.VISIBLE);
        truePayLayout.setVisibility(View.VISIBLE);

        float amount = Float.parseFloat(orderDetailData.getAmount()) / 100;
        float marketAmount = Float.parseFloat(orderDetailData.getMarketCostAmount())/100;

        if (orderDetailData.getStatus() == OrderStatusEnum.WAITPAY.getValue()){
            orderPayDesView.setVisibility(View.GONE);
            truePayTv.setText("¥" + ExtraUtil.format(amount + marketAmount));
        }else{
            orderPayDesView.setVisibility(View.VISIBLE);
            orderPayDesView.bindData(orderDetailData);
            if (Validators.isEmpty(orderDetailData.getOtherCost())){
                truePayTv.setText("¥" +ExtraUtil.format(amount + marketAmount));
            }else{
                truePayTv.setText("¥" + ExtraUtil.format(Float.parseFloat(orderDetailData.getOtherCost())));
            }
        }

        //已取消的订单或者用户是发货人不显示价格
        if (orderStatusEnum.equals(OrderStatusEnum.CLOSED) || AuthUtil.canDeliver()) {
            orderPayDesView.setVisibility(View.GONE);
            truePayLayout.setVisibility(View.GONE);
            orderDetailGoodListAdapter = new OrderDetailGoodListAdapter(OrderDetailNewActivity.this, goodList, false , GoodTypeEnum.NORMAL_BUY.getValue() , orderDetailData.getAmount());
            marketOrderDetailGoodListAdapter = new OrderDetailGoodListAdapter(OrderDetailNewActivity.this , marketGoodList , false , GoodTypeEnum.MARKET_SUPPORT.getValue() , orderDetailData.getMarketCostAmount());
        } else {
//            orderPayDesView.setVisibility(View.VISIBLE);
//            truePayLayout.setVisibility(View.VISIBLE);
            orderDetailGoodListAdapter = new OrderDetailGoodListAdapter(OrderDetailNewActivity.this, goodList, true , GoodTypeEnum.NORMAL_BUY.getValue() , orderDetailData.getAmount());
            marketOrderDetailGoodListAdapter = new OrderDetailGoodListAdapter(OrderDetailNewActivity.this , marketGoodList , true , GoodTypeEnum.MARKET_SUPPORT.getValue() , orderDetailData.getMarketCostAmount());

        }
        listView.setAdapter(orderDetailGoodListAdapter);
        marketListView.setAdapter(marketOrderDetailGoodListAdapter);
    }

    /**
     * 修改备注
     *
     * @param orderId
     * @param remark
     */
    private void modifyOrderRemark(String orderId, String remark) {
        ModifyOrderRemarkTask modifyOrderRemarkTask = new ModifyOrderRemarkTask(OrderDetailNewActivity.this);
        modifyOrderRemarkTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        modifyOrderRemarkTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                ToastUtil.toast("修改成功");
                ChangeOrderStatusReceiver.notifyReceiver();
            }
        });

        modifyOrderRemarkTask.execute(orderId, remark);
    }

    /**
     * 将订单商品列表转换成结算界面需要显示的列表
     *
     * @param productList
     * @return
     */
    private List<GoodsCar> getGoodList(List<OrderProduct> productList) {
        List<GoodsCar> retList = new ArrayList<GoodsCar>();
        for (OrderProduct orderProduct : productList) {
            GoodsCar goodsCar = new GoodsCar();
            goodsCar.setGoodId(orderProduct.getId());
            goodsCar.setGoodName(orderProduct.getName());
            goodsCar.setGoodIcon(orderProduct.getPic());
            goodsCar.setGoodCount(orderProduct.getCount());
            goodsCar.setGoodPrice(ExtraUtil.format(Float.parseFloat(orderProduct.getPrice()) / 100));
            retList.add(goodsCar);
        }
        return retList;
    }

    /**
     * 启动Activity
     *
     * @param context
     * @param orderId
     * @param type
     */
    public static void startOrderDetailNewActivity(Context context, String orderId, String type) {
        Intent intent = new Intent();
        intent.putExtra(ORDERID, orderId);
        intent.putExtra(TYPE, type);
        intent.setClass(context, OrderDetailNewActivity.class);
        context.startActivity(intent);
    }
}
