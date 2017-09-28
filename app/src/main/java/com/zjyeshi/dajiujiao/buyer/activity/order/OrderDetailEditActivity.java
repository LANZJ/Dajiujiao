package com.zjyeshi.dajiujiao.buyer.activity.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigappleui.lib.view.listview.BUHighHeightListView;
import com.xuan.bigdog.lib.dialog.DGPromptDialog;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.activity.my.MaxLevelManActivity;
import com.zjyeshi.dajiujiao.buyer.activity.my.personal.EditAddressActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.order.OrderDetailEditAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LoginEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.OrderStatusEnum;
import com.zjyeshi.dajiujiao.buyer.entity.my.Address;
import com.zjyeshi.dajiujiao.buyer.entity.order.AddOrderRequest;
import com.zjyeshi.dajiujiao.buyer.receiver.order.ChangeOrderStatusReceiver;
import com.zjyeshi.dajiujiao.buyer.receiver.order.ChangePriceReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.data.order.OrderDetailData;
import com.zjyeshi.dajiujiao.buyer.task.data.order.OrderProduct;
import com.zjyeshi.dajiujiao.buyer.task.data.order.PathResData;
import com.zjyeshi.dajiujiao.buyer.task.data.order.PerOrder;
import com.zjyeshi.dajiujiao.buyer.task.data.pay.Wallet;
import com.zjyeshi.dajiujiao.buyer.task.data.store.goods.GoodData;
import com.zjyeshi.dajiujiao.buyer.task.data.store.goods.GoodList;
import com.zjyeshi.dajiujiao.buyer.task.data.store.goods.Product;
import com.zjyeshi.dajiujiao.buyer.task.my.MyAccountTask;
import com.zjyeshi.dajiujiao.buyer.task.order.DrawBackOrderReviewTask;
import com.zjyeshi.dajiujiao.buyer.task.order.GetOrderDetailTask;
import com.zjyeshi.dajiujiao.buyer.task.order.GetOrderPathTask;
import com.zjyeshi.dajiujiao.buyer.task.order.ModifyOrderRemarkTask;
import com.zjyeshi.dajiujiao.buyer.task.order.ModifyOrderTask;
import com.zjyeshi.dajiujiao.buyer.task.order.RemoverOrderMistakeTask;
import com.zjyeshi.dajiujiao.buyer.task.store.GoodListTask;
import com.zjyeshi.dajiujiao.buyer.utils.AuthUtil;
import com.zjyeshi.dajiujiao.buyer.utils.DialogUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.FriendlyTimeUtil;
import com.zjyeshi.dajiujiao.buyer.utils.NumberUtil;
import com.zjyeshi.dajiujiao.buyer.utils.OrderUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.views.other.IVWalletCheck;
import com.zjyeshi.dajiujiao.buyer.widgets.PathData;
import com.zjyeshi.dajiujiao.buyer.widgets.PathWidget;
import com.zjyeshi.dajiujiao.buyer.widgets.dialog.SelProductDialog;
import com.zjyeshi.dajiujiao.buyer.widgets.store.OrderProgressWidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 订单详情可修改
 * Created by wuhk on 2016/7/20.
 */
public class OrderDetailEditActivity extends BaseActivity {

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
    @InjectView(R.id.truePayDesTv)
    private TextView truePayDesTv;
    @InjectView(R.id.truePayEt)
    private EditText truePayEt;
    @InjectView(R.id.userInfoLayout)
    private LinearLayout userInfoLayout;
    @InjectView(R.id.orderProgressWidget)
    private OrderProgressWidget orderProgressWidget;
    @InjectView(R.id.addWineTv)
    private TextView addWineTv;
    @InjectView(R.id.marketAddWineTv)
    private TextView marketAddWineTv;
    @InjectView(R.id.editAddressRl)
    private RelativeLayout editAddressRl;
    @InjectView(R.id.editRemarkRl)
    private RelativeLayout editRemarkRl;
    @InjectView(R.id.orderStatusDesTv)
    private TextView orderStatusDesTv;
    @InjectView(R.id.remarkTv)
    private TextView remarkTv;
    @InjectView(R.id.pathWidget)
    private PathWidget pathWidget;
    @InjectView(R.id.marketGoodLayout)
    private RelativeLayout marketGoodLayout;
    @InjectView(R.id.normalGoodNumAndPriceTv)
    private TextView normalGoodNumAndPriceTv;
    @InjectView(R.id.marketGoodNumAndPriceTv)
    private TextView marketGoodNumAndPriceTv;
    @InjectView(R.id.normalGoodLayout)
    private RelativeLayout normalGoodLayout;
    @InjectView(R.id.marketLayout)
    private RelativeLayout marketLayout;
    @InjectView(R.id.marketCostTv)
    private TextView marketCostTv;
    @InjectView(R.id.marketCheckIv)
    private IVWalletCheck marketCheckIv;
    @InjectView(R.id.priceDesLayout)
    private LinearLayout priceDesLayout;
    @InjectView(R.id.allPriceTv)
    private TextView allPriceTv;
    @InjectView(R.id.marketPriceUseTv)
    private TextView marketPriceUseTv;
    @InjectView(R.id.order_transfer)
    private Button order_transfer;//订单转审
    @InjectView(R.id.confirm_orders)
    private Button confirm_orders;//确认订单
    @InjectView(R.id.linearLayout)
    private LinearLayout linearLayout;

    //列表相关
    private List<OrderProduct> goodList = new ArrayList<OrderProduct>();
    private List<OrderProduct> marketGoodList = new ArrayList<OrderProduct>();
    private OrderDetailEditAdapter orderDetailEditAdapter;
    private OrderDetailEditAdapter marketOrderDetailEditAdapter;

    //参数
    public static final String ORDERID = "order_id";
    public static final String  CANRE="canrevoke";
    public static final String DEALW="dealwith";
    public static final String REPLA="replac";
    private String orderId;
    private boolean canRevoke;
    private boolean dealWith;
    private boolean replaceOrder;
    private String shopId;//添加商品和修改的时候都要用到
    private OrderDetailData orderData;//记录一下请求下来的订单详信息，之后其他地方要用到
    private boolean useMarket = true;//线下付款的时候默认使用市场支持费用
    private float canUseMarket = 0.00f;//可以使用的市场支持费用
    private boolean editProduct = false;//是否修改过订单商品和价格

    //广播
    private ChangePriceReceiver changePriceReceiver;//修改价格
    private ChangeOrderStatusReceiver changeOrderStatusReceiver;//地址修改后重新刷新数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_order_detail_edit);
        initWidgets();
        registerReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        changePriceReceiver.unRegister();
        changeOrderStatusReceiver.unRegister();
    }

    private void initWidgets() {
        //获取订单状态

        //终端和终端业务员不显示市场支持模块
        if (AuthUtil.showMarketCostTab()) {
            normalGoodLayout.setVisibility(View.VISIBLE);
            priceDesLayout.setVisibility(View.VISIBLE);
            marketGoodLayout.setVisibility(View.VISIBLE);
            marketAddWineTv.setVisibility(View.VISIBLE);
            marketLayout.setVisibility(View.VISIBLE);
        } else {
            normalGoodLayout.setVisibility(View.GONE);
            priceDesLayout.setVisibility(View.GONE);
            marketGoodLayout.setVisibility(View.GONE);
            marketAddWineTv.setVisibility(View.GONE);
            marketLayout.setVisibility(View.GONE);
        }

        //订单Id
        orderId = getIntent().getStringExtra(ORDERID);
        canRevoke = getIntent().getBooleanExtra(CANRE, false);
        dealWith = getIntent().getBooleanExtra(DEALW, false);
        replaceOrder = getIntent().getBooleanExtra(REPLA, false);

        titleLayout.configRightText("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOrder();
            }
        }).configTitle("订单详情").configReturn("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //请求订单详情
        getData();
        //添加常规商品按钮
        addWineTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAddWineInfo(shopId, false);
            }
        });
        //添加市场支持商品按钮
        marketAddWineTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAddWineInfo(shopId, true);
            }
        });


        //是否使用市场支持费用
        //默认选中
        marketCheckIv.setChecked(true);
        marketCheckIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                useMarket = marketCheckIv.isChecked();
                ChangePriceReceiver.notifyReceiver();

            }
        });
        marketLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                marketCheckIv.performClick();
            }
        });

        //详情下俩个按钮
        if (AuthUtil.isSalesEdit()) {

            //能不能操作
            if (dealWith) {
                //是否为代下
                if (replaceOrder) {
                    order_transfer.setText("删除订单");
                    order_transfer.setVisibility(View.VISIBLE);
                    order_transfer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            RemoverOrderMistakeTask.removeOrderWhenMistake(OrderDetailEditActivity.this, orderId, new AsyncTaskSuccessCallback<NoResultData>() {
                                @Override
                                public void successCallback(Result<NoResultData> result) {
                                    ToastUtil.toast("删除成功");
                                    ChangeOrderStatusReceiver.notifyReceiver();
                                    linearLayout.setVisibility(View.GONE);
                                }
                            });
                        }
                    });
                } else {
                    order_transfer.setVisibility(View.GONE);
                }
                confirm_orders.setVisibility(View.VISIBLE);
                confirm_orders.setText("订单转审");
                confirm_orders.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MaxLevelManActivity.startMaxLevelManActivity(OrderDetailEditActivity.this, orderId, MaxLevelManActivity.TYPE_ORDER);
                        // order_transfer.setVisibility(View.GONE);
//                        order_transfer.setText("撤销转审");
//                         canRevoke=true;
                        linearLayout.setVisibility(View.GONE);


                    }
                });

            }
            else if (canRevoke) {
                linearLayout.setVisibility(View.VISIBLE);
                order_transfer.setVisibility(View.VISIBLE);
                order_transfer.setText("撤销转审");
                order_transfer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtil.confirmSure(OrderDetailEditActivity.this, "是否撤销转审", "确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MaxLevelManActivity.startMaxLevelManActivity(OrderDetailEditActivity.this, orderId, MaxLevelManActivity.TYPE_ORDER);
                            }
                        });
                    }
                });
            }

        }
        else {//内勤 boss
            if (dealWith) {
                order_transfer.setText("订单转审");
                order_transfer.setVisibility(View.VISIBLE);
                order_transfer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MaxLevelManActivity.startMaxLevelManActivity(OrderDetailEditActivity.this, orderId, MaxLevelManActivity.TYPE_ORDER);
//                        canRevoke=true;
//                        order_transfer.setText("撤销转审");
                    }
                });
                confirm_orders.setVisibility(View.VISIBLE);
                confirm_orders.setText("确认订单");
                confirm_orders.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtil.confirmSure(OrderDetailEditActivity.this, "是否确认订单", "确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                              //  String status = String.valueOf(OrderStatusEnum.SURED.getValue());
                                String status="5";
                                OrderUtil.modifyOrderStatus(OrderDetailEditActivity.this, orderId, status);
                                ToastUtil.toast("确认完成");
                                linearLayout.setVisibility(View.GONE);

                            }
                        });
                    }
                });
                if(replaceOrder){

                }
            }else if (canRevoke) {
                order_transfer.setVisibility(View.VISIBLE);
                order_transfer.setText("撤销转审");
                order_transfer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtil.confirmSure(OrderDetailEditActivity.this, "是否撤销转审", "确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MaxLevelManActivity.startMaxLevelManActivity(OrderDetailEditActivity.this, orderId, MaxLevelManActivity.TYPE_ORDER);
                            }
                        });
                    }
                });
            }else {
                order_transfer.setVisibility(View.GONE);
            }
        }


    }



    /**
     * 获取订单详情数据
     */
    private void getData() {
        GetOrderDetailTask getOrderDetailTask = new GetOrderDetailTask(OrderDetailEditActivity.this);

        getOrderDetailTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<OrderDetailData>() {
            @Override
            public void failCallback(Result<OrderDetailData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        getOrderDetailTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<OrderDetailData>() {
            @Override
            public void successCallback(Result<OrderDetailData> result) {
                //显示数据,保存店铺Id,添加商品和修改酒品的时候都需要用到
                orderData = result.getValue();
                shopId = orderData.getShopId();
                bindData(orderData);
            }
        });

        getOrderDetailTask.execute(orderId);
    }

    //显示数据
    private void bindData(final OrderDetailData orderDetailData) {
        //卖家显示消费者名
        if (!Validators.isEmpty(orderDetailData.getBuyerShop())) {
            initTextView(shopNameTv, orderDetailData.getBuyerShop() + "(" + orderDetailData.getSalesman() + ")");
        } else {
            initTextView(shopNameTv, orderDetailData.getBuyer());
        }
        //订单号、时间
        orderNumTv.setText("编号:" + orderDetailData.getNumber());
        timeTv.setText(FriendlyTimeUtil.friendlyTime(orderDetailData.getCreationTime()));

        //获取一下市场支持费用,终端和终端业务员不用去获取市场支持费用，
        //消费者下的单没有市场支持和市场支持费用减免
        if (AuthUtil.showMarketCostTab()) {
            MyAccountTask.getMyAccountWhenBuy(OrderDetailEditActivity.this, orderDetailData.getBuyerId(), new AsyncTaskSuccessCallback<Wallet>() {
                @Override
                public void successCallback(Result<Wallet> result) {
                    //账户里的市场支持费用，刚请求下来的时候显示的是账户里的费用,账户里面的费用是*100的
                    float accountMarketCostMoney = Float.parseFloat(result.getValue().getMarketCost());
                    marketCostTv.setText("可用市场支持费用:¥" + ExtraUtil.format(accountMarketCostMoney/100));

                    //显示完之后，计算一下，回滚之后的市场支持费用，用于修改了订单之后的计算
                    //回滚到下单前的市场支持费用
                    float usedMarketCost = NumberUtil.toFloat(orderData.getMarketCost());//使用的市场支持费用
                    //现在账户里的费用加上订单里使用了的费用
                    canUseMarket = accountMarketCostMoney + usedMarketCost*100;
                    //减去订单商品提供的费用
                    int productMarket = 0;
                    for (OrderProduct product : orderDetailData.getProducts()) {
                        productMarket += Float.parseFloat(product.getMarketCost()) * Integer.parseInt(product.getCount());
                    }
                    canUseMarket -= productMarket;
                }
            });
        }

        //价格方面
        float amount = Float.parseFloat(orderDetailData.getAmount()) ;
        float marketAmount = Float.parseFloat(orderDetailData.getMarketCostAmount());

        //第一次下载下来数据显示的时候
        //注意：能进入修改的只有代付款的已付款的线下支付
        //待付款的订单,truePayEt显示的只是amount，即常规商品的总价,不显示使用市场支持费用
        if (orderDetailData.getStatus() == OrderStatusEnum.WAITPAY.getValue()) {
            marketLayout.setVisibility(View.GONE);
            priceDesLayout.setVisibility(View.GONE);
            //将使用市场支持费用置为false
            useMarket = false;
            truePayDesTv.setText("常规商品总价");
            truePayEt.setText(ExtraUtil.format(amount/100));
        } else {
            //已付款的线下支付，显示的就是otherCost，表示需要支付的价格,
            //做了一步容错处理：如果otherCost为空的时候，显示两种商品的总和
            //显示使用市场支持费用
            marketLayout.setVisibility(View.VISIBLE);
            priceDesLayout.setVisibility(View.VISIBLE);
            //价格描述中的总价
            allPriceTv.setText("¥" + ExtraUtil.format(amount/ 100 + marketAmount / 100));
            //价格描述中的使用的市场支持费用
            marketPriceUseTv.setText("-¥" + ExtraUtil.format(NumberUtil.toFloat(orderDetailData.getMarketCost())));

            useMarket = true;//默认使用市场支持费用
            truePayDesTv.setText("实付款");
            if (Validators.isEmpty(orderDetailData.getOtherCost())) {
                truePayEt.setText(ExtraUtil.format((amount + marketAmount)/100));
            } else {
                truePayEt.setText(ExtraUtil.format(Float.parseFloat(orderDetailData.getOtherCost())));
            }
        }

        //地址,地址不为空才显示
        if (null != orderDetailData.getAddress()) {
            userInfoLayout.setVisibility(View.VISIBLE);
            //备注
            initTextView(remarkTv, orderDetailData.getRemark());
            //收货人信息
            receiverTv.setText(orderDetailData.getAddress().getName());
            phoneTv.setText(orderDetailData.getAddress().getPhone());
            addressTv.setText(orderDetailData.getAddress().getAddress());
            //组合成Address数据，为了修改地址使用
            final Address address = new Address();
            address.setPhone(orderDetailData.getAddress().getPhone());
            address.setName(orderDetailData.getAddress().getName());
            address.setAddress(orderDetailData.getAddress().getAddress());
            //这里的订单状态显示待确认
            orderStatusDesTv.setText("待确认");
            //地址编辑
            editAddressRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditAddressActivity.startActivity(OrderDetailEditActivity.this, address, orderDetailData.getId());
                }
            });
            //备注编辑
            editRemarkRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DGPromptDialog dialog = new DGPromptDialog.Builder(
                            OrderDetailEditActivity.this)
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

        //订单里的商品显示
        goodList.clear();
        goodList.addAll(orderDetailData.getProducts());

        marketGoodList.clear();
        marketGoodList.addAll(orderDetailData.getMarketCostProducts());

        //显示两类商品顶部的总价，商品列表不为空显示
        if (!Validators.isEmpty(goodList)) {
            normalGoodNumAndPriceTv.setVisibility(View.VISIBLE);
            setGoodNumAndPriceTv(goodList.size(), ExtraUtil.format(ExtraUtil.getShowPrice(orderDetailData.getAmount())), false);
        } else {
            normalGoodNumAndPriceTv.setVisibility(View.GONE);
        }
        if (!Validators.isEmpty(marketGoodList)) {
            marketGoodNumAndPriceTv.setVisibility(View.VISIBLE);
            setGoodNumAndPriceTv(marketGoodList.size(), ExtraUtil.format(ExtraUtil.getShowPrice(orderDetailData.getMarketCostAmount())), true);
        } else {
            marketGoodNumAndPriceTv.setVisibility(View.GONE);
        }

        //显示商品列表
        orderDetailEditAdapter = new OrderDetailEditAdapter(OrderDetailEditActivity.this, goodList);
        listView.setAdapter(orderDetailEditAdapter);
        marketOrderDetailEditAdapter = new OrderDetailEditAdapter(OrderDetailEditActivity.this, marketGoodList);
        marketListView.setAdapter(marketOrderDetailEditAdapter);

        //订单正常流程，接单 ，发货等
        orderProgressWidget.bindData(LoginEnum.SELLER.toString(), orderDetailData);

        //显示订单审批流程,显示审批人
        GetOrderPathTask.showOrderPath(OrderDetailEditActivity.this, orderDetailData.getId(), new AsyncTaskSuccessCallback<PathResData>() {
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
                        //SURED以前等于三是
                       // if (orderDetailData.getStatus() == OrderStatusEnum.PAYED.getValue()) {
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
    }

    /**
     * 获取添加酒品信息
     *
     * @param shopId
     * @param isMarket
     */
    private void getAddWineInfo(String shopId, final boolean isMarket) {
        GoodListTask goodListTask = new GoodListTask(OrderDetailEditActivity.this);
        goodListTask.setShowProgressDialog(false);
        goodListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<GoodList>() {
            @Override
            public void failCallback(Result<GoodList> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        goodListTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<GoodList>() {
            @Override
            public void successCallback(Result<GoodList> result) {
                List<Product> tempList = new ArrayList<Product>();
                HashMap<String, OrderProduct> productHashMap = new HashMap<String, OrderProduct>();
                if (isMarket) {
                    for (OrderProduct orderProduct : marketGoodList) {
                        productHashMap.put(orderProduct.getId(), orderProduct);
                    }
                } else {
                    for (OrderProduct orderProduct : goodList) {
                        productHashMap.put(orderProduct.getId(), orderProduct);
                    }
                }

                for (GoodData goodData : result.getValue().getList()) {
                    if (null == productHashMap.get(goodData.getProduct().getId())) {
                        tempList.add(goodData.getProduct());
                    }
                }
                DialogUtil.selectProductDialog(OrderDetailEditActivity.this, tempList, new SelProductDialog.ItemClickListener() {
                    @Override
                    public void itemClick(Product product) {
                        //选择商品后将选中的商品插入列表中
                        OrderProduct orderProduct = new OrderProduct();
                        orderProduct.setId(product.getId());
                        orderProduct.setBottlesPerBox(product.getBottlesPerBox());
                        orderProduct.setCount("0");
                        orderProduct.setName(product.getName());
                        orderProduct.setPic(product.getPic());
                        orderProduct.setSpecifications(product.getSpecifications());
                        orderProduct.setUnit(product.getUnit());
                        orderProduct.setPrice(product.getPrice());
                        if (isMarket) {
                            marketGoodList.add(orderProduct);
                            marketOrderDetailEditAdapter.notifyDataSetChanged();
                        } else {
                            goodList.add(orderProduct);
                            orderDetailEditAdapter.notifyDataSetChanged();
                        }
                        //添加完酒品后，通知价格刷新一下
                        ChangePriceReceiver.notifyReceiver();
                    }
                });
            }
        });
        String categoryId = "";
        goodListTask.execute(shopId, categoryId, orderId, "");
    }

    /**
     * 保存订单
     */
    private void saveOrder() {


        if (Validators.isEmpty(goodList) && Validators.isEmpty(marketGoodList)){
            ToastUtil.toast("订单没有商品");
            return;
        }
        //这个totalPrice就是上传的总价，获取truePayEt上显示的价格
        String totalPrice = ExtraUtil.getUpLoadCount(ExtraUtil.format(NumberUtil.toFloat(truePayEt.getText().toString()) * 100));

        //常规购买
        StringBuilder productIds = new StringBuilder();
        StringBuilder prices = new StringBuilder();
        StringBuilder counts = new StringBuilder();
        StringBuilder boxType = new StringBuilder();
        //市场支持费用
        StringBuilder markCostProductIds = new StringBuilder();
        StringBuilder markCostPrices = new StringBuilder();
        StringBuilder markCostCounts = new StringBuilder();
        StringBuilder markCostBoxType = new StringBuilder();
        //卖家Id，取常规购买和市场支持长度长的一方的sellerId集合
        StringBuilder sellerIds = new StringBuilder();

        if (!Validators.isEmpty(goodList)){
            for (OrderProduct orderProduct : goodList) {
                productIds.append(orderProduct.getId());
                productIds.append(",");

                prices.append(orderProduct.getPrice());
                prices.append(",");

                counts.append(orderProduct.getCount());
                counts.append(",");

                //瓶为1 ， 箱为2
                boxType.append(AddOrderRequest.BOX_TYPE_UNIT);
                boxType.append(",");
            }

            productIds.deleteCharAt(productIds.length() - 1);
            prices.deleteCharAt(prices.length() - 1);
            counts.deleteCharAt(counts.length() - 1);
            boxType.deleteCharAt(boxType.length() - 1);
        }


        if (!Validators.isEmpty(marketGoodList)){
            for (OrderProduct orderProduct : marketGoodList) {
                markCostProductIds.append(orderProduct.getId());
                markCostProductIds.append(",");

                markCostPrices.append(orderProduct.getPrice());
                markCostPrices.append(",");

                markCostCounts.append(orderProduct.getCount());
                markCostCounts.append(",");

                //瓶为1 ， 箱为2
                markCostBoxType.append(AddOrderRequest.BOX_TYPE_UNIT);
                markCostBoxType.append(",");
            }

            markCostProductIds.deleteCharAt(markCostProductIds.length() - 1);
            markCostPrices.deleteCharAt(markCostPrices.length() - 1);
            markCostCounts.deleteCharAt(markCostCounts.length() - 1);
            markCostBoxType.deleteCharAt(markCostBoxType.length() - 1);
        }


        if (goodList.size() > marketGoodList.size() || goodList.size() == marketGoodList.size()) {
            for (int i = 0; i < goodList.size(); i++) {
                sellerIds.append(shopId);
                sellerIds.append(",");
            }
        } else {
            for (int i = 0; i < marketGoodList.size(); i++) {
                sellerIds.append(shopId);
                sellerIds.append(",");
            }
        }
        sellerIds.deleteCharAt(sellerIds.length() - 1);


        ModifyOrderTask modifyOrderTask = new ModifyOrderTask(OrderDetailEditActivity.this);
        modifyOrderTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        modifyOrderTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                ToastUtil.toast("订单修改成功");
                ChangeOrderStatusReceiver.notifyReceiver();
                finish();
            }
        });

        AddOrderRequest request = new AddOrderRequest(orderId, productIds.toString(), prices.toString(), counts.toString(), boxType.toString()
                , markCostProductIds.toString(), markCostPrices.toString(), markCostCounts.toString(), markCostBoxType.toString()
                , sellerIds.toString(), totalPrice, useMarket , editProduct);

        modifyOrderTask.execute(request);
    }

    /**
     * 修改备注
     *
     * @param orderId
     * @param remark
     */
    private void modifyOrderRemark(String orderId, String remark) {
        ModifyOrderRemarkTask modifyOrderRemarkTask = new ModifyOrderRemarkTask(OrderDetailEditActivity.this);
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
     * 注册广播
     */
    private void registerReceiver() {
        changePriceReceiver = new ChangePriceReceiver() {
            @Override
            public void changePrice() {
                editProduct = true;
                //这里只有两种情况，待付款和已付款的线下支付
                //一旦修改过商品，原先修改的价格就失效了，都使用列表总价
                float goodAllPrice = 0.00f;
                float haveMarket = 0.00f;
                for (OrderProduct product : goodList) {
                    goodAllPrice += Float.parseFloat(product.getPrice()) * Integer.parseInt(product.getCount());
                    haveMarket += Float.parseFloat(product.getMarketCost())* Integer.parseInt(product.getCount());
                }
                setGoodNumAndPriceTv(goodList.size(), ExtraUtil.format(ExtraUtil.getShowPrice(String.valueOf(goodAllPrice))), false);
                //市场支持价格
                int marketGoodAllPrice = 0;
                for (OrderProduct product : marketGoodList) {
                    marketGoodAllPrice += Float.parseFloat(product.getPrice()) * Integer.parseInt(product.getCount());
                }
                setGoodNumAndPriceTv(marketGoodList.size(), ExtraUtil.format(ExtraUtil.getShowPrice(String.valueOf(marketGoodAllPrice))), true);

                //不同的是底部的总价计算
                //待付款情况,不需要管其他东西，只需要把truePayEt改为常规商品总价就好了
                if (orderData.getStatus() == OrderStatusEnum.WAITPAY.getValue()) {
                    truePayEt.setText(ExtraUtil.format(ExtraUtil.getShowPrice(String.valueOf(goodAllPrice))));
                } else {
                    //已付款的线下支付
                    //将订单使用过的市场支持费用返还到账户市场支持费用,在加上添加的商品提供的市场支持费用，再进行判断费用扣减
                    float nowCanUser = canUseMarket + haveMarket;
                    marketCostTv.setText("可用市场支持费用:¥" + ExtraUtil.format(nowCanUser/100));

                    float amountShow = goodAllPrice + marketGoodAllPrice;

                    allPriceTv.setText("¥" + ExtraUtil.format(amountShow/100));
                    //如果选择使用了市场支持费用
                    if (useMarket) {
                        //如果市场支持商品总价小于可用市场支持费用
                        if (marketGoodAllPrice < nowCanUser) {
                            //减去市场支持商品总价
                            marketPriceUseTv.setText("-¥" + ExtraUtil.format(marketGoodAllPrice/100));
                            amountShow = amountShow - marketGoodAllPrice;
                        } else {
                            //否则减去可用市场支持费用
                            marketPriceUseTv.setText("-¥" + ExtraUtil.format(nowCanUser/100));
                            amountShow = amountShow - nowCanUser;
                        }

                    } else {
                        marketPriceUseTv.setText("-¥" + "0.00");
                    }
                    //实付款
                    truePayEt.setText(ExtraUtil.format(amountShow/100));
                }

            }
        };
        changePriceReceiver.register();

        changeOrderStatusReceiver = new ChangeOrderStatusReceiver() {
            @Override
            public void changeStatus() {
                //重新请求一下数据，刷新一下界面
                initWidgets();
            }
        };
        changeOrderStatusReceiver.register();
    }

    /**
     * 设置商品数量和价格
     *
     * @param size
     * @param price
     * @param isMarket
     */
    private void setGoodNumAndPriceTv(int size, String price, boolean isMarket) {
        if (isMarket) {
            marketGoodNumAndPriceTv.setText("共" + String.valueOf(size) + "件商品,共计" + price);
        } else {
            normalGoodNumAndPriceTv.setText("共" + String.valueOf(size) + "件商品,共计" + price);
        }
    }


    /**
     * 启动该Activity
     *  @param context
     * @param orderId
     */
    public static void startActivity(Context context,String orderId,boolean canRevoke,boolean dealWith,boolean replaceOrder ) {
        Intent intent = new Intent();
        intent.putExtra(ORDERID, orderId);
        intent.putExtra(CANRE,canRevoke);
        intent.putExtra(DEALW,dealWith);
        intent.putExtra(REPLA,replaceOrder);
        intent.setClass(context, OrderDetailEditActivity.class);
        context.startActivity(intent);
    }
}
