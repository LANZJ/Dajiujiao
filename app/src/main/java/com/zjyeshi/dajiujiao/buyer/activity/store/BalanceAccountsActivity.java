package com.zjyeshi.dajiujiao.buyer.activity.store;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigapple.lib.utils.sharepreference.BPPreferences;
import com.xuan.bigappleui.lib.view.listview.BUHighHeightListView;
import com.xuan.bigdog.lib.utils.BDActivityManager;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.activity.frame.FrameActivity;
import com.zjyeshi.dajiujiao.buyer.activity.my.work.HmcActivity;
import com.zjyeshi.dajiujiao.buyer.activity.order.MyOrderNewActivity;
import com.zjyeshi.dajiujiao.buyer.activity.order.OrderManagerActivity;
import com.zjyeshi.dajiujiao.buyer.activity.seller.ProductBuyActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.order.OrderGoodAdapter;
import com.zjyeshi.dajiujiao.buyer.adapter.store.MyExpandableListView;
import com.zjyeshi.dajiujiao.buyer.common.PassConstans;
import com.zjyeshi.dajiujiao.buyer.common.PreferenceConstants;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.entity.GoodsCar;
import com.zjyeshi.dajiujiao.buyer.entity.enums.GoodTypeEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LoginEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.PayTypeEnum;
import com.zjyeshi.dajiujiao.buyer.entity.my.Address;
import com.zjyeshi.dajiujiao.buyer.pay.MyAliPayConfig;
import com.zjyeshi.dajiujiao.buyer.pay.MyPay;
import com.zjyeshi.dajiujiao.buyer.pay.MyWxPayConfig;
import com.zjyeshi.dajiujiao.buyer.pay.aliutils.PayResult;
import com.zjyeshi.dajiujiao.buyer.receiver.LoadNewRemindReceiver;
import com.zjyeshi.dajiujiao.buyer.receiver.info.SelectAddressReceiver;
import com.zjyeshi.dajiujiao.buyer.receiver.order.ChangeOrderStatusReceiver;
import com.zjyeshi.dajiujiao.buyer.receiver.pay.AliPayReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.data.pay.PrePareData;
import com.zjyeshi.dajiujiao.buyer.task.data.pay.Wallet;
import com.zjyeshi.dajiujiao.buyer.task.my.MyAccountTask;
import com.zjyeshi.dajiujiao.buyer.task.order.CouponAndWalletTask;
import com.zjyeshi.dajiujiao.buyer.task.order.ModifyOrderRemarkTask;
import com.zjyeshi.dajiujiao.buyer.task.order.RemoverOrderMistakeTask;
import com.zjyeshi.dajiujiao.buyer.task.order.XianxiaOrderTask;
import com.zjyeshi.dajiujiao.buyer.task.pay.PayPrepareTask;
import com.zjyeshi.dajiujiao.buyer.task.sales.GetOrderSalesListTask;
import com.zjyeshi.dajiujiao.buyer.task.sales.GetOrderSalesMoneyTask;
import com.zjyeshi.dajiujiao.buyer.task.sales.data.OrderSalesListData;
import com.zjyeshi.dajiujiao.buyer.task.sales.data.OrderSalesMoneyData;
import com.zjyeshi.dajiujiao.buyer.task.sales.data.SalesListData;
import com.zjyeshi.dajiujiao.buyer.utils.AuthUtil;
import com.zjyeshi.dajiujiao.buyer.utils.DialogUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.views.coupon.CouponEntity;
import com.zjyeshi.dajiujiao.buyer.views.pay.PayAddressView;
import com.zjyeshi.dajiujiao.buyer.views.pay.PayDesView;
import com.zjyeshi.dajiujiao.buyer.views.pay.PaySelectTypeView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * 结算
 * Created by wuhk on 2015/10/13.
 */
public class BalanceAccountsActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.payAddressView)
    private PayAddressView payAddressView;//支付地址
    @InjectView(R.id.goodListView)
    private BUHighHeightListView goodListView;//常规购买商品列表
    @InjectView(R.id.marketGoodListView)
    private BUHighHeightListView marketGoodListView;//市场支持商品列表
    @InjectView(R.id.paySelectTypeView)
    private PaySelectTypeView paySelectTypeView;//支付方式
    @InjectView(R.id.payDesView)
    private PayDesView payDesView;//支付金额描述
    @InjectView(R.id.amountTv)
    private TextView amountTv;//底部实付金额
    @InjectView(R.id.balanceBtn)
    private Button balanceBtn;//结算按钮
    @InjectView(R.id.bzEt)
    private EditText bzTv;//备注
//    @InjectView(R.id.huodongll)
//    private BUHighHeightListView huodongll;
    private String activityidss="";
    // @InjectView(R.id.payyh)
    // private RelativeLayout payyh;
    // @InjectView(R.id.imageView2)
    //private ImageView imageView2;
 public static SparseArray<Boolean> checkStates=new SparseArray<>();
    @InjectView(R.id.expandable_list)
    ExpandableListView expandable_list;
    private OrderGoodAdapter orderGoodAdapter;
    private OrderGoodAdapter marketOrderGoodAdapter;
    private String remark="";//备注
    private int pointi;
    //请求参数
    private String walletAccount; //钱包余额
    private String marketCostMoney;//市场支持费用
    private PayTypeEnum payTypeEnum;
    private boolean isWallet = true;//默认钱包支付优先
    private boolean isMarket = true;//默认使用市场支持费用
    private CouponEntity couponData = null;
    private OrderSalesMoneyData orderSalesMoneyData = new OrderSalesMoneyData();
    List<OrderSalesListData.OrderSales> orderSalesListmm = null;

    //传入参数
    private String orderId;
    private List<GoodsCar> selectedList = new ArrayList<GoodsCar>();
    private List<GoodsCar> marketSelectedList = new ArrayList<GoodsCar>();
    List<String> ary = new ArrayList<String>();//保存结果的list
    private List<SalesListData.Sales> productSaleList;
//    private List<SalesListData.Sales> op=new ArrayList<SalesListData.Sales>();
//    private List<SalesListData.Sales> po=new ArrayList<SalesListData.Sales>();
    private String moneyToPay = "";//用于支付的金额
    private String totalMoney = "";//传入的价格，可能订单总价修改过的，并不是商品价格和，若没传，则自己计算商品总和
    private String from;//从哪里进入, 购物车||订单
    private boolean helpAddOrder = false;
    private String memberId = "";
    //参数Key
    public static final String PARAM_TOTAL_MONEY = "param.total.money";
    public static final String PARAM_GOOD_LIST = "param.good.list";
    public static final String PARAM_MARKET_GOOD_LIST = "param.market.good.list";
    public static final String PARAM_ORDER_ID = "param.total_order.id";
    public static final String PARAM_PAY_FROM = "param.pay_from";
    public static final String PARAM_IS_HELP_ADD ="param.is.help.add";
    public static final String PARAM_CUSTOMER_MEMBER_ID ="param.customer_member_id";
    //类型
    public static final String FROM_CAR = "from.car";//从购物车进入
    public static final String FROM_ORDER = "from.order";//从订单进入

    private SelectAddressReceiver selectAddressReceiver;//显示选中地址广播
    private AliPayReceiver aliPayReceiver;//支付宝支付结果通知

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pay);
        initWidgets();
        register();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        selectAddressReceiver.unRegister();
        aliPayReceiver.unRegister();
    }

    private void initWidgets() {
        titleLayout.configReturn("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        titleLayout.configTitle("结算");
        //获取传入的数据
        orderId = getIntent().getStringExtra(PARAM_ORDER_ID);
        selectedList = (List<GoodsCar>) getIntent().getSerializableExtra(PARAM_GOOD_LIST);
        marketSelectedList = (List<GoodsCar>) getIntent().getSerializableExtra(PARAM_MARKET_GOOD_LIST);
        from = getIntent().getStringExtra(PARAM_PAY_FROM);
        totalMoney = getIntent().getStringExtra(PARAM_TOTAL_MONEY);
        helpAddOrder = getIntent().getBooleanExtra(PARAM_IS_HELP_ADD , false);
        memberId = getIntent().getStringExtra(PARAM_CUSTOMER_MEMBER_ID);
        //代下单的，之前已经选过地址了，隐藏地址显示
        if (helpAddOrder){
            payAddressView.setVisibility(View.GONE);
        }else{
            //初始化地址，默认显示前一次选中的地址
            payAddressView.setVisibility(View.VISIBLE);
            final Address address = DaoFactory.getAddressDao().findAddressBySelected();
            payAddressView.bindAddressData(address);
        }
        //常规购买商品列表
        configOrderProductSales();
        //市场支持商品列表
        marketOrderGoodAdapter = new OrderGoodAdapter(BalanceAccountsActivity.this , marketSelectedList , GoodTypeEnum.MARKET_SUPPORT.getValue());
        marketGoodListView.setAdapter(marketOrderGoodAdapter);

        //默认线下支付
        payTypeEnum = PayTypeEnum.XIANXIA;
        //所有商品活动列表


        //支付方式选择
        paySelectTypeView.bindData(orderId,
                new PaySelectTypeView.PaySelCallback() {
            @Override
            public void selParam(PayTypeEnum payEnum, boolean isWalletFirst, boolean isMarketCost, CouponEntity couponEntity) {
                payTypeEnum = payEnum;
                isWallet = isWalletFirst;
                isMarket = isMarketCost;
                couponData = couponEntity;
                showPriceDes();
            }
        } , false);

        //加载钱包余额
        loadWalletAccount();
        //显示参与的活动描述
        loadOrderSalesMoney();

        //结算
        balanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validators.isEmpty(payAddressView.getAddressId())) {
                    //如果不是待下单的，地址Id不能为空
                    if (!helpAddOrder){
                        ToastUtil.toast("请选设置地址");
                        return;
                    }
                }
                if (null == payTypeEnum) {
                    ToastUtil.toast("请选择支付方式");
                } else if (payTypeEnum == PayTypeEnum.XIANXIA) {
                    linePayment();
                } else if (payTypeEnum == PayTypeEnum.WEIXIN) {
                    if (isWallet || null != couponData || isMarket) {
                        couponAndWallet(false);
                    } else {
                        new MyPay(BalanceAccountsActivity.this, new MyWxPayConfig())
                                .wxPay(orderId, payAddressView.getAddressId(), moneyToPay, BalanceAccountsActivity.this);
                    }
                } else if (payTypeEnum == PayTypeEnum.ZHIFUBAO) {
                    if (isWallet || null != couponData || isMarket) {
                        couponAndWallet(true);
                    } else {
                        pay(payAddressView.getAddressId(), orderId, moneyToPay);
                    }
                }
                remark = bzTv.getText().toString();
            }
        });
//        payyh.setOnClickListener(new View.OnClickListener() {
//            @Override
        modifyOrderRemark(orderId,remark);
//            public void onClick(View v) {
//               // imageView2.setImageDrawable(getResources().getDrawable(R.drawable.downarro));
//                final PopWindowutils popupWindow = new PopWindowutils(BalanceAccountsActivity.this,iDialogControl,productSaleList);
//                popupWindow.setItemClickListener(new OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                  ToastUtil.toast("adasdsadsad");
//                    }
//                });
//                popupWindow.show(v,1);
//            }
//        });

    }

    /**
     * 修改备注
     *
     * @param orderId
     * @param remark
     */
    private void modifyOrderRemark(String orderId, String remark) {
        ModifyOrderRemarkTask modifyOrderRemarkTask = new ModifyOrderRemarkTask(BalanceAccountsActivity.this);
        modifyOrderRemarkTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        modifyOrderRemarkTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                //ToastUtil.toast("修改成功");
                ChangeOrderStatusReceiver.notifyReceiver();
            }
        });

        modifyOrderRemarkTask.execute(orderId, remark);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //优惠券选择回调
        paySelectTypeView.onActivityForResult(requestCode, resultCode, data);

    }

    @Override
    public void onBackPressed() {
        if (!Validators.isEmpty(memberId)){
            //待下单
            DialogUtil.confirmSure(BalanceAccountsActivity.this, "若不结算，该订单将作废", "确定", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RemoverOrderMistakeTask.removeOrderWhenMistake(BalanceAccountsActivity.this, orderId, new AsyncTaskSuccessCallback<NoResultData>() {
                        @Override
                        public void successCallback(Result<NoResultData> result) {
                          finish();
                        }
                    });
                }
            });
        }else{
            DialogUtil.confirmSure(BalanceAccountsActivity.this,"若不结算，该订单将作废", "确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        }
    }

    /**
     * 显示价格描述
     */
    private void showPriceDes() {
        //计算商品总价 , 传入的价格，可能订单总价修改过的，并不是商品价格和，若为空，则自己计算商品总和
        payDesView.bindPayDesData(selectedList, marketSelectedList, totalMoney , payTypeEnum ,couponData, isWallet, walletAccount, isMarket, marketCostMoney , orderSalesMoneyData);
        moneyToPay = payDesView.getMoneyToPay();
        amountTv.setText("实付: ¥" + moneyToPay);
    }

    /**显示参与的活动详情
     *
     */
    private void loadOrderSalesMoney(){
        GetOrderSalesMoneyTask.getOrderSalesMoney(BalanceAccountsActivity.this, orderId, new AsyncTaskSuccessCallback<OrderSalesMoneyData>() {
            @Override
            public void successCallback(Result<OrderSalesMoneyData> result) {
                orderSalesMoneyData = result.getValue();
                showPriceDes();
            }
        });
    }
    /**
     * 获取钱包余额
     */
    private void loadWalletAccount() {
        MyAccountTask myAccountTask = new MyAccountTask(BalanceAccountsActivity.this);
        myAccountTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<Wallet>() {
            @Override
            public void failCallback(Result<Wallet> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        myAccountTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<Wallet>() {
            @Override
            public void successCallback(Result<Wallet> result) {
                walletAccount = ExtraUtil.format(ExtraUtil.getShowPrice(result.getValue().getAccount()));
                marketCostMoney = ExtraUtil.format(ExtraUtil.getShowPrice(result.getValue().getMarketCost()));
                paySelectTypeView.setWalletAccount(walletAccount);
                if (AuthUtil.showMarketCostTab() && AuthUtil.recordMarketCostFee()){
                    float tempMarketPrice = BPPreferences.instance().getFloat(PreferenceConstants.CAR_MARKET_COST , 0.00f);
                    marketCostMoney = ExtraUtil.format(Float.parseFloat(marketCostMoney) + tempMarketPrice/100);
                }
                paySelectTypeView.setMarketAccount(marketCostMoney);
                showPriceDes();
            }
        });

        if (Validators.isEmpty(memberId)){
            myAccountTask.execute();
        }else{
            myAccountTask.execute(memberId);
        }
    }

    /**
     * 支付宝支付预处理
     *
     * @param addressId
     * @param orderId
     * @param truePayMoney
     */
    private void pay(String addressId, final String orderId, final String truePayMoney) {
        final String orderInfo = MyPay.getOrderInfo("红酒", "好酒", truePayMoney);
        PayPrepareTask payPrepareTask = new PayPrepareTask(BalanceAccountsActivity.this);
        payPrepareTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<PrePareData>() {
            @Override
            public void failCallback(Result<PrePareData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        payPrepareTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<PrePareData>() {
            @Override
            public void successCallback(Result<PrePareData> result) {
                String sign = result.getValue().getSign();
                String tradeNo = result.getValue().getTradeNo();
                new MyPay(BalanceAccountsActivity.this, new MyAliPayConfig()).aliPay(orderInfo, sign, tradeNo);
            }
        });
        payPrepareTask.execute(orderId, addressId, orderInfo);
    }

    /**
     * 钱包和优惠券
     */
    private void couponAndWallet(final boolean isAlipay) {
        String type, couponId;
        if (isWallet) {
            type = CouponAndWalletTask.USE_WALLLET;
        } else {
            type = CouponAndWalletTask.NO_USE;
        }

        if (null != couponData) {
            couponId = couponData.getId();
        } else {
            couponId = "";
        }

        CouponAndWalletTask couponAndWalletTask = new CouponAndWalletTask(BalanceAccountsActivity.this);
        couponAndWalletTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<Wallet>() {
            @Override
            public void failCallback(Result<Wallet> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        couponAndWalletTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<Wallet>() {
            @Override
            public void successCallback(Result<Wallet> result) {

                if (moneyToPay.equals(ExtraUtil.format(ExtraUtil.getShowPrice(result.getValue().getAccount())))) {
                    if (Float.parseFloat(moneyToPay) == 0) {
                        if (from.equals(FROM_CAR)) {
                            Intent intent = new Intent();
                            intent.putExtra(PassConstans.TYPE, LoginEnum.BURER.toString());
                            intent.setClass(BalanceAccountsActivity.this, MyOrderNewActivity.class);
                            startActivity(intent);
                            BDActivityManager.removeAndFinishIncludes(BalanceAccountsActivity.class.getSimpleName(), BuyCarActivity.class.getSimpleName(), ShopDetailActivity.class.getSimpleName(), ProductBuyActivity.class.getSimpleName());
                           // FrameActivity.tab3Checked = true;
                        } else {
                            finish();
                            ChangeOrderStatusReceiver.notifyReceiver();
                        }
                    } else {
                        if (isAlipay) {
                            pay(payAddressView.getAddressId(), orderId, moneyToPay);
                        } else {
                            new MyPay(BalanceAccountsActivity.this, new MyWxPayConfig())
                                    .wxPay(orderId, payAddressView.getAddressId(), moneyToPay, BalanceAccountsActivity.this);
                        }
                    }
                } else {
                    ToastUtil.toast("价格计算有误");
                }
            }
        });

        couponAndWalletTask.execute(orderId, payAddressView.getAddressId(), couponId, type, String.valueOf(isMarket),activityidss);
    }

    /**
     * 线下支付
     */
    private void linePayment() {
        XianxiaOrderTask xianxiaOrderTask = new XianxiaOrderTask(BalanceAccountsActivity.this);
        xianxiaOrderTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        xianxiaOrderTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                //支付成功，清空购物车市场支持费用
                BPPreferences.instance().putFloat(PreferenceConstants.CAR_MARKET_COST , 0.00f);
                if (from.equals(FROM_CAR)) {
                    if (!Validators.isEmpty(memberId)){
                        //待下单进来的
                        LoadNewRemindReceiver.notifyReceiver();
                        Intent intent = new Intent(BalanceAccountsActivity.this,OrderManagerActivity.class);
                        startActivity(intent);
                        BDActivityManager.removeAndFinishIncludes(BalanceAccountsActivity.class.getSimpleName() , BuyCarActivity.class.getSimpleName(), ShopDetailActivity.class.getSimpleName() , HmcActivity.class.getSimpleName());
                    }else{
                        Intent intent = new Intent();
                        intent.putExtra(PassConstans.TYPE, LoginEnum.BURER.toString());
                        intent.setClass(BalanceAccountsActivity.this, MyOrderNewActivity.class);
                        startActivity(intent);
                        BDActivityManager.removeAndFinishIncludes(BalanceAccountsActivity.class.getSimpleName(), BuyCarActivity.class.getSimpleName(), ShopDetailActivity.class.getSimpleName(), ProductBuyActivity.class.getSimpleName());
                       // FrameActivity.tab3Checked = true;
                    }
                } else {
                    finish();
                    ChangeOrderStatusReceiver.notifyReceiver();
                }
            }
        });

        xianxiaOrderTask.execute(orderId, payAddressView.getAddressId(), String.valueOf(isMarket),activityidss);
    }


    /**
     * 启动付款界面
     *
     * @param context
     * @param goodList       常规商品列表
     * @param marketGoodList 市场支持商品列表
     * @param orderId        订单Id
     * @param from           从哪里进入，购物车||订单详情
     * @param amount         订单总价，可能修改过，不传的话自行计算商品总价
     */
    public static void startBalanceActivity(Context context, List<GoodsCar> goodList, List<GoodsCar> marketGoodList, String orderId, String from, String amount , boolean isHelpAdd , String customerMemberId) {
        Intent intent = new Intent();
        intent.putExtra(PARAM_ORDER_ID, orderId);
        intent.putExtra(PARAM_GOOD_LIST, (Serializable) goodList);
        intent.putExtra(PARAM_MARKET_GOOD_LIST, (Serializable) marketGoodList);
        intent.putExtra(PARAM_PAY_FROM, from);
        if (!Validators.isEmpty(amount)){
            intent.putExtra(PARAM_TOTAL_MONEY, ExtraUtil.format(ExtraUtil.getShowPrice(amount)));
        }else{
            intent.putExtra(PARAM_TOTAL_MONEY, amount);
        }
        intent.putExtra(PARAM_IS_HELP_ADD , isHelpAdd);
        intent.putExtra(PARAM_CUSTOMER_MEMBER_ID , customerMemberId);
        intent.setClass(context, BalanceAccountsActivity.class);
        context.startActivity(intent);

    }


    private void configOrderProductSales(){
        if (selectedList.size()==0){
            return;
        }
        final StringBuilder productIds = new StringBuilder();
        for(GoodsCar goodsCar : selectedList){
            productIds.append(goodsCar.getGoodId());
            productIds.append(",");
        }
        productIds.deleteCharAt(productIds.length() - 1);
        GetOrderSalesListTask.getOrderSalesList(BalanceAccountsActivity.this, orderId, productIds.toString(), new AsyncTaskSuccessCallback<OrderSalesListData>() {
            @Override
            public void successCallback(Result<OrderSalesListData> result) {
                HashMap<String , List<SalesListData.Sales>> salesMap = new HashMap<String, List<SalesListData.Sales>>();

                List<OrderSalesListData.OrderSales> orderSalesList = result.getValue().getList();
                for(OrderSalesListData.OrderSales orderSales : orderSalesList){
                    //   productSaleList = salesMap.get(orderSales.getProductId());
                    if (null == productSaleList){
                        productSaleList = new ArrayList<SalesListData.Sales>();
                        productSaleList.add(orderSales);
                        salesMap.put(orderSales.getProductId() , productSaleList);
                    }else{
                        productSaleList.add(orderSales);
                    }

                        checkStates.put(pointi, false);
                    pointi++;
                }

                for(GoodsCar goodsCar  : selectedList){
                    List<SalesListData.Sales> productSalesList = salesMap.get(goodsCar.getGoodId());
                    if (!Validators.isEmpty(productSalesList)){
                        goodsCar.setSalesList(productSalesList);
                    }else{
                        goodsCar.setSalesList(new ArrayList<SalesListData.Sales>());
                    }
                }

                orderGoodAdapter = new OrderGoodAdapter(BalanceAccountsActivity.this, selectedList , GoodTypeEnum.NORMAL_BUY.getValue());
                goodListView.setAdapter(orderGoodAdapter);
                if(productSaleList==null){
                    expandable_list.setVisibility(View.GONE);
                }else {
                    List<String> groupArray = new ArrayList<String>();
                    groupArray.add("可享受优惠");
                    List<List<SalesListData.Sales>> childArray = new ArrayList<List<SalesListData.Sales>>();
                    childArray.add(productSaleList);
                    final MyExpandableListView adapter = new MyExpandableListView(BalanceAccountsActivity.this, groupArray, childArray, productSaleList, checkStates, iDialogControl);
                    expandable_list.setAdapter(adapter);
                    expandable_list.setGroupIndicator(null);
                    expandable_list.setDivider(null);

//               huoDoAdapter=new HuoDoAdapter(BalanceAccountsActivity.this,iDialogControl,productSaleList);
//                huodongll.setAdapter(huoDoAdapter);

//  设置分组项的点击监听事件
                    expandable_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                        @Override
                        public boolean onGroupClick(ExpandableListView parent, View arg1, int groupPosition, long id) {
                            // boolean groupExpanded = parent.isGroupExpanded(groupPosition);
                            // adapter.setIndicatorState(groupPosition, groupExpanded);
                            // 请务必返回 false，否则分组不会展开
                            return false;
                        }
                    });

                    //  设置子选项点击监听事件
                    expandable_list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                        @Override
                        public boolean onChildClick(ExpandableListView parent, View arg1, int groupPosition, int childPosition, long id) {
                            //  Toast.makeText(IndicatorExpandActivity.this, Constant.FIGURES[groupPosition][childPosition], Toast.LENGTH_SHORT).show();
//                       final MyExpandableListView.ChildViewHolder holder = (MyExpandableListView.ChildViewHolder) arg1.getTag();
//                        //holder.checkbox.toggle();
//                     //   MyExpandableListView.getIsSelected().put(childPosition, holder.checkbox.isChecked());
//                        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                            @Override
//                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//
//
//                            }
//                        });
                            return true;
                        }
                    });
                }

            }
        });
    }
    /**
     * 广播注册
     */
    private void register() {
        //选择地址
        selectAddressReceiver = new SelectAddressReceiver() {
            @Override
            public void showAddress() {
                Address address = DaoFactory.getAddressDao().findAddressBySelected();
                payAddressView.bindAddressData(address);
            }
        };
        selectAddressReceiver.register();

        //支付宝支付回调
        aliPayReceiver = new AliPayReceiver() {
            @Override
            public void aliResult(String result) {
                PayResult payResult = new PayResult(result);
                // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                String resultInfo = payResult.getResult();
                String resultStatus = payResult.getResultStatus();
                String success = MyPay.getSuccess(resultInfo);
                String checkSuccess = success = "\"true\"";
                // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                if (TextUtils.equals(resultStatus, "9000") && success.equals(checkSuccess)) {
                    //支付成功，清空购物车市场支持费用
                    BPPreferences.instance().putFloat(PreferenceConstants.CAR_MARKET_COST , 0.00f);
                    ToastUtil.toast("支付成功");
                    if (from.equals(FROM_CAR)) {
                        Intent intent = new Intent();
                        intent.putExtra(PassConstans.TYPE, LoginEnum.BURER.toString());
                        intent.setClass(BalanceAccountsActivity.this, MyOrderNewActivity.class);
                        startActivity(intent);
                        BDActivityManager.removeAndFinishIncludes(BalanceAccountsActivity.class.getSimpleName(), BuyCarActivity.class.getSimpleName(), ShopDetailActivity.class.getSimpleName(), ProductBuyActivity.class.getSimpleName());
                     //   FrameActivity.tab3Checked = true;
                    } else {
                        finish();
                        ChangeOrderStatusReceiver.notifyReceiver();
                    }
                } else if (TextUtils.equals(resultStatus, "6001")) {
                    ToastUtil.toast("您取消了支付");
                } else {
                    // 判断resultStatus 为非“9000”则代表可能支付失败
                    // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                    if (TextUtils.equals(resultStatus, "8000")) {
                        ToastUtil.toast("支付结果确认中");

                    } else {
                        // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                        ToastUtil.toast("支付失败");
                    }
                }
            }
        };
        aliPayReceiver.register();
    }
    MyExpandableListView.IDialogControl iDialogControl=new MyExpandableListView.IDialogControl() {
        @Override
        public void onShowDialog(boolean kl,List<String> maru) {
            if (!kl) {
                ary.clear();
                activityidss="";
            }
                if (maru.size()==0) {
                    activityidss="";
                }
                ary = array_unique(maru);
            if (ary.size()==1){
                activityidss=ary.get(0);
            }else {
                activityidss="";
                for (int j = ary.size() - 1; j >= 0; j--) {
                    activityidss += ary.get(j) + ",";
                }
            }
           // popsr();
        }
        @Override
        public void onshio(String activityee) {
            activityidss=activityee;
           // popsr();
        }
    };

    public static <T> List<T> array_unique(List<T> list){
        List<T> temp = new LinkedList<T>();
        for(T a:list) {
           if(!temp.contains(a)){
                temp.add(a);
              }
            }
       return temp;
    }
//    public static <T> List<T> array(List<T> list){
//        List<T> temp = new LinkedList<T>();
//        List<T>sop=new LinkedList<T>();
//        for (int i=0;i<list.size();i++){
//            if(!temp.contains(list.get(i))) temp.add(list.get(i));
//            sop.remove(temp);
//        }
//        return sop;
//    }
    public void popsr(){
        CouponAndWalletTask couponAndWalletTask = new CouponAndWalletTask(BalanceAccountsActivity.this);
        couponAndWalletTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<Wallet>() {
            @Override
            public void successCallback(Result<Wallet> result) {
                amountTv.setText("实付: ¥" + result.getValue().getAccount());


            }
        });
        couponAndWalletTask.execute(orderId, payAddressView.getAddressId(), "", 2+"", false+"",activityidss);
    }

}

