package com.zjyeshi.dajiujiao.buyer.activity.store;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
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
import com.zjyeshi.dajiujiao.buyer.adapter.store.BuyCarAllListAdapter;
import com.zjyeshi.dajiujiao.buyer.common.PassConstans;
import com.zjyeshi.dajiujiao.buyer.common.PreferenceConstants;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.entity.GoodsCar;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.enums.GoodTypeEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.SelectEnum;
import com.zjyeshi.dajiujiao.buyer.entity.my.Address;
import com.zjyeshi.dajiujiao.buyer.entity.order.AddOrderRequest;
import com.zjyeshi.dajiujiao.buyer.entity.store.CarShop;
import com.zjyeshi.dajiujiao.buyer.entity.store.GoodCar;
import com.zjyeshi.dajiujiao.buyer.receiver.buy.BuyCarSelectChangeReceiver;
import com.zjyeshi.dajiujiao.buyer.receiver.buy.CarNumChangeReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.my.GetAddressListData;
import com.zjyeshi.dajiujiao.buyer.task.data.order.GetAddOrderData;
import com.zjyeshi.dajiujiao.buyer.task.my.GetAddressListTask;
import com.zjyeshi.dajiujiao.buyer.task.order.AddOrderTask;
import com.zjyeshi.dajiujiao.buyer.utils.AuthUtil;
import com.zjyeshi.dajiujiao.buyer.utils.DialogUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.views.other.IVCheckBox;
import com.zjyeshi.dajiujiao.buyer.widgets.dialog.AddressDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车
 * Created by wuhk on 2015/10/13.
 */
public class BuyCarActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.normalListView)
    private BUHighHeightListView normalListView;
    @InjectView(R.id.marketListView)
    private BUHighHeightListView marketListView;
    @InjectView(R.id.postOrderBtn)
    private Button postOrderBtn;
    @InjectView(R.id.selectIv)
    private IVCheckBox selectIv;
    @InjectView(R.id.priceTv)
    private TextView priceTv;
    @InjectView(R.id.bottomLayout)
    private RelativeLayout bottomLayout;

    private boolean showDialog = true;//是否显示市场费用超出提示

    //常规商品和市场支持列表
    private List<GoodCar> normalList = new ArrayList<GoodCar>();//常规购买
    private List<GoodCar> marketList = new ArrayList<GoodCar>();//市场支持
    private BuyCarAllListAdapter normalBuyCarAllListAdapter;
    private BuyCarAllListAdapter marketBuyCarAllListAdapter;

    //改变选择和价格广播
    private BuyCarSelectChangeReceiver buyCarSelectChangeReceiver;
    //参数
    public static final String PARAM_MEMBERID = "param.member.id";
    private String memberId = "";//待客户下单，客户Id, 正常下单没有客户Id
    private String addressId = "";//地址Id
    private String remark = "";//备注

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_buy_car);
        registerReceiver();
        initWidgets();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        buyCarSelectChangeReceiver.unRegister();
    }

    private void initWidgets() {

        memberId = getIntent().getStringExtra(PARAM_MEMBERID);

        titleLayout.configReturn("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle("购物车");
        bottomLayout.setClickable(true);

        //listView初始化
        normalBuyCarAllListAdapter = new BuyCarAllListAdapter(this, normalList);
        normalListView.setAdapter(normalBuyCarAllListAdapter);
        marketBuyCarAllListAdapter = new BuyCarAllListAdapter(this, marketList);
        marketListView.setAdapter(marketBuyCarAllListAdapter);


        loadData(true);

        //全选按钮点击事件
        selectIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击购物车页面全选按钮时，将购物车列表中的数据全部置为全选点击之后的状态，并保存在数据库
                // 两个列表都是这样操作的
                for (GoodCar goodCar : normalList) {
                    goodCar.setIsSelected(selectIv.isChecked());
                    for (GoodsCar goodsCar : goodCar.getGoodList()) {
                        if (selectIv.isChecked()) {
                            goodsCar.setStatus(SelectEnum.SELECTED.getValue());
                        } else {
                            goodsCar.setStatus(SelectEnum.UNSELECT.getValue());
                        }
                        DaoFactory.getGoodsCarDao().replace(goodsCar);
                    }
                }

                for (GoodCar goodCar : marketList) {
                    goodCar.setIsSelected(selectIv.isChecked());
                    for (GoodsCar goodsCar : goodCar.getGoodList()) {
                        if (selectIv.isChecked()) {
                            goodsCar.setStatus(SelectEnum.SELECTED.getValue());
                        } else {
                            goodsCar.setStatus(SelectEnum.UNSELECT.getValue());
                        }
                        DaoFactory.getMarketGoodsCarDao().replace(goodsCar);
                    }
                }
                //刷新一下列表
                refreshSelect();
            }
        });

        //提交订单
        postOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //只有登录状态才能提交订单
                if (LoginedUser.checkLogined()) {
                    //购物车中选中的两类商品
                    final List<GoodsCar> selectedList = DaoFactory.getGoodsCarDao().findGoodListByStatus();
                    final List<GoodsCar> marketSelectedList = DaoFactory.getMarketGoodsCarDao().findGoodListByStatus();
                    if (Validators.isEmpty(marketSelectedList)){
                        post();
                    }else{
                        judge(selectedList , marketSelectedList);
                    }
                }
            }
        });
    }
    /**
     * 判断并提交
     */
    private void judge(List<GoodsCar> selectedList , List<GoodsCar> marketSelectedList) {
        float marketCost = 0.00f;
        for (GoodsCar goodsCar : selectedList) {
            if (goodsCar.getStatus() == SelectEnum.SELECTED.getValue()) {
                marketCost += Float.parseFloat(goodsCar.getGoodMarketPrice()) / 100 * Float.parseFloat(goodsCar.getGoodCount());
            }
        }
        float selectMarketPrice = 0.00f;
        for (GoodsCar goodsCar : marketSelectedList) {
            //选择的市场费用
            if (goodsCar.getStatus() == SelectEnum.SELECTED.getValue()) {
                selectMarketPrice += Float.parseFloat(goodsCar.getGoodPrice()) * Float.parseFloat(goodsCar.getGoodCount());
            }
        }
        if (showDialog&&memberId!=null) {
            float myMarketPrice = BPPreferences.instance().getFloat(PreferenceConstants.MY_ACCOUNT_MARKET, 0.00f);
            if(AuthUtil.recordMarketCostFee()){
                myMarketPrice = myMarketPrice + marketCost;
            }

            if (myMarketPrice < selectMarketPrice) {
                DialogUtil.confirm(BuyCarActivity.this, "市场支持费用不足,多余价格将使用现金支付", "重新选择", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                }, "继续下单", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        post();
                    }
                });
                showDialog = false;
            }else{
                post();
            }
        }
    }
    private void post(){
        if (Validators.isEmpty(memberId)) {
            //正常下单, 先选中默认地址，没有赋空
            Address address = DaoFactory.getAddressDao().findAddressBySelected();
            if (null != address) {
                addressId = address.getId();
            } else {
                addressId = "";
            }
            alertRemarkAndAddOrder();
        } else {
            //代下单
            getAddressAndAddOrder();
        }
    }

    /**
     * 第一次进入和下单成功之后
     * 从数据库中取出数据组合成需要的结构，并刷新
     *
     * @param setAllSelected 是否全选的标志，刚进购物车是全选
     */
    private void loadData(boolean setAllSelected) {
        normalList.clear();
        //从常规商品数据库中取出购买商品的全部店铺
        List<CarShop> shopList = DaoFactory.getGoodsCarDao().findAllShopList();
        for (CarShop carShop : shopList) {
            GoodCar goodCar = new GoodCar();
            //赋值店铺名字
            goodCar.setStoreId(carShop.getId());
            goodCar.setStoreName(carShop.getName());
            goodCar.setType(GoodTypeEnum.NORMAL_BUY.toString());
            //将每一个店铺下的常规商品取出
            List<GoodsCar> goodList = DaoFactory.getGoodsCarDao().findGoodListByShopId(carShop.getId());
            for (GoodsCar goodsCar : goodList) {
                //刚进入购物车需要全选的时候设置全选
                if (setAllSelected) {
                    //进入购物车默认全选
                    goodsCar.setStatus(SelectEnum.SELECTED.getValue());
                    DaoFactory.getGoodsCarDao().replace(goodsCar);
                }
            }
            goodCar.setGoodList(goodList);
            normalList.add(goodCar);
        }

       // if (AuthUtil.showMarketCostTab()){
            //市场支持商品列表同上
            marketList.clear();
            //从数据库中取出全部店铺
            List<CarShop> marketShopList = DaoFactory.getMarketGoodsCarDao().findAllShopList();
            for (CarShop carShop : marketShopList) {
                GoodCar goodCar = new GoodCar();
                //赋值店铺名字
                goodCar.setStoreId(carShop.getId());
                goodCar.setStoreName(carShop.getName());
                goodCar.setType(GoodTypeEnum.MARKET_SUPPORT.toString());
                //赋值店铺下的商品
                List<GoodsCar> goodList = DaoFactory.getMarketGoodsCarDao().findGoodListByShopId(carShop.getId());
                for (GoodsCar goodsCar : goodList) {
                    if (setAllSelected) {
                        goodsCar.setStatus(SelectEnum.SELECTED.getValue());
                        DaoFactory.getMarketGoodsCarDao().replace(goodsCar);
                    }
                }
                goodCar.setGoodList(goodList);
                marketList.add(goodCar);
            }
      //  }

        //刷新一下
        refreshSelect();
    }

    /**
     * 购物车选择或者加减之后的刷新,包括勾选和价格的刷新
     * 直接将现有的数据刷新，不重新取值
     */
    private void refreshSelect() {

        ShopDetailActivity.reUnitData = true;
        //改变选择
        int normalSize = 0;
        for (GoodCar goodCar : normalList) {
            boolean selected = true;
            for (GoodsCar goodsCar : goodCar.getGoodList()) {
                if (goodsCar.getStatus() != SelectEnum.SELECTED.getValue()) {
                    selected = false;
                }
            }
            if (selected) {
                normalSize++;
            }
            goodCar.setIsSelected(selected);
        }
        normalBuyCarAllListAdapter.notifyDataSetChanged();

        //判断市场支持费用够不够,只提示一次
        int marketSize = 0;
        for (GoodCar goodCar : marketList) {
            boolean selected = true;
            for (GoodsCar goodsCar : goodCar.getGoodList()) {
                //选择的市场费用
                if (goodsCar.getStatus() != SelectEnum.SELECTED.getValue()) {
                    selected = false;
                }
            }
            if (selected) {
                marketSize++;
            }
            goodCar.setIsSelected(selected);
        }
        marketBuyCarAllListAdapter.notifyDataSetChanged();

        //底部全选的判定
        if ((marketSize + normalSize) == (normalList.size() + marketList.size())) {
            if ((marketSize + normalSize) == 0) {
                selectIv.setChecked(false);
            } else {
                selectIv.setChecked(true);
            }
        } else {
            selectIv.setChecked(false);
        }

        //改变价格
        float allPrice = 0.00f;
        List<GoodsCar> goodList = DaoFactory.getGoodsCarDao().findGoodListByStatus();
        for (GoodsCar goodsCar : goodList) {
            float price = (Float.parseFloat(goodsCar.getGoodCount())) * (Float.parseFloat(goodsCar.getGoodPrice()));
            allPrice += price;
        }

        float marketPrice = 0.00f;
        List<GoodsCar> marketGoodList = DaoFactory.getMarketGoodsCarDao().findGoodListByStatus();
        for (GoodsCar goodsCar : marketGoodList) {
            float price = (Float.parseFloat(goodsCar.getGoodCount())) * (Float.parseFloat(goodsCar.getGoodPrice()));
            marketPrice += price;
        }

        priceTv.setText(ExtraUtil.format(allPrice + marketPrice));
    }

    /**
     * 下单
     * 设置选择的商品并整理好数据
     * 两类商品：常规购买，市场支持
     */
    private void addOrder() {
        //购物车中选中的两类商品
        final List<GoodsCar> selectedList = DaoFactory.getGoodsCarDao().findGoodListByStatus();
        final List<GoodsCar> marketSelectedList = DaoFactory.getMarketGoodsCarDao().findGoodListByStatus();

        if (Validators.isEmpty(selectedList) && Validators.isEmpty(marketSelectedList)) {
            ToastUtil.toast("请先选择商品");
        } else {
            //记录Id，用于下单成功之后删除商品
            final List<String> goodIdList = new ArrayList<>();
            final List<String> marketGoodIdList = new ArrayList<>();
            //常规购买参数
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

            //常规购买
            if (!Validators.isEmpty(selectedList)) {
                for (GoodsCar goodsCar : selectedList) {
                    goodIdList.add(goodsCar.getGoodId());

                    productIds.append(goodsCar.getGoodId());
                    productIds.append(",");

                    prices.append(String.valueOf(goodsCar.getGoodUpPrice()));
                    prices.append(",");

                    if (goodsCar.getGoodType().equals(PassConstans.XIANG)) {
                        counts.append(ExtraUtil
                                .format(Float.parseFloat(goodsCar.getGoodCount()) / Float.parseFloat(goodsCar.getGoodBottole())));
                        boxType.append(AddOrderRequest.BOX_TYPE_XIANG);
                    } else {
                        counts.append(ExtraUtil.getUpLoadCount(goodsCar.getGoodCount()));
                        boxType.append(AddOrderRequest.BOX_TYPE_UNIT);
                    }

                    counts.append(",");
                    boxType.append(",");
                }

                productIds.deleteCharAt(productIds.length() - 1);
                prices.deleteCharAt(prices.length() - 1);
                counts.deleteCharAt(counts.length() - 1);
                boxType.deleteCharAt(boxType.length() - 1);
            }

            //市场支持
            if (!Validators.isEmpty(marketSelectedList)) {
                for (GoodsCar goodsCar : marketSelectedList) {
                    marketGoodIdList.add(goodsCar.getGoodId());

                    markCostProductIds.append(goodsCar.getGoodId());
                    markCostProductIds.append(",");

                    markCostPrices.append(String.valueOf(goodsCar.getGoodUpPrice()));
                    markCostPrices.append(",");

                    if (goodsCar.getGoodType().equals(PassConstans.XIANG)) {
                        markCostCounts.append(ExtraUtil
                                .format(Float.parseFloat(goodsCar.getGoodCount()) / Float.parseFloat(goodsCar.getGoodBottole())));
                        markCostBoxType.append(AddOrderRequest.BOX_TYPE_XIANG);
                    } else {
                        markCostCounts.append(ExtraUtil.getUpLoadCount(goodsCar.getGoodCount()));
                        markCostBoxType.append(AddOrderRequest.BOX_TYPE_UNIT);
                    }

                    markCostCounts.append(",");
                    markCostBoxType.append(",");
                }

                markCostProductIds.deleteCharAt(markCostProductIds.length() - 1);
                markCostPrices.deleteCharAt(markCostPrices.length() - 1);
                markCostCounts.deleteCharAt(markCostCounts.length() - 1);
                markCostBoxType.deleteCharAt(markCostBoxType.length() - 1);
            }

            //取较长商品的卖家Id
            if (selectedList.size() > marketSelectedList.size() || selectedList.size() == marketSelectedList.size()) {
                for (GoodsCar goodsCar : selectedList) {
                    sellerIds.append(goodsCar.getShopId());
                    sellerIds.append(",");
                }
            } else {
                for (GoodsCar goodsCar : marketSelectedList) {
                    sellerIds.append(goodsCar.getShopId());
                    sellerIds.append(",");
                }
            }
            sellerIds.deleteCharAt(sellerIds.length() - 1);


            //发起请求
            AddOrderTask addOrderTask = new AddOrderTask(BuyCarActivity.this);

            addOrderTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<GetAddOrderData>() {
                @Override
                public void failCallback(Result<GetAddOrderData> result) {
                    ToastUtil.toast(result.getMessage());
                }
            });

            addOrderTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<GetAddOrderData>() {
                @Override
                public void successCallback(Result<GetAddOrderData> result) {
                    //ToastUtil.toast("下单成功");
                    ShopDetailActivity.reUnitData = true;
                    List<GetAddOrderData.OrderInfo> orderInfoList = new ArrayList<GetAddOrderData.OrderInfo>();
                    orderInfoList.addAll(result.getValue().getList());
                    StringBuilder orderId = new StringBuilder();
                    for (GetAddOrderData.OrderInfo orderInfo : orderInfoList) {
                        orderId.append(orderInfo.getId());
                        orderId.append(",");
                    }
                    orderId.deleteCharAt(orderId.length() - 1);
                    //删除购物车中下单成功的商品
                    for (String selected : goodIdList) {
                        DaoFactory.getGoodsCarDao().deleteById(selected);
                    }
                    for (String selected : marketGoodIdList) {
                        DaoFactory.getMarketGoodsCarDao().deleteById(selected);
                    }
                    //刷新列表和商品界面底部购物车数量
                    loadData(false);
                    CarNumChangeReceiver.notifyReceiverWhenAddOrder(BuyCarActivity.this);

                    //根据是否是代下单的决定下单成功之后跳转的界面
                    if (!Validators.isEmpty(memberId)) {
                        BDActivityManager.removeAndFinishIncludes(BuyCarActivity.class.getSimpleName(), ShopDetailActivity.class.getSimpleName());
                        BalanceAccountsActivity.startBalanceActivity(BuyCarActivity.this, selectedList, marketSelectedList
                                , orderId.toString(), BalanceAccountsActivity.FROM_CAR, "", true , memberId);
                        finish();
                    } else {
                        BalanceAccountsActivity.startBalanceActivity(BuyCarActivity.this, selectedList, marketSelectedList
                                , orderId.toString(), BalanceAccountsActivity.FROM_CAR, "", false , "");
                        finish();
                    }
                }
            });

            //组合成请求需要的数据
            AddOrderRequest request = new AddOrderRequest(memberId, productIds.toString(), prices.toString(), counts.toString(), boxType.toString()
                    , markCostProductIds.toString(), markCostPrices.toString(), markCostCounts.toString()
                    , markCostBoxType.toString(), sellerIds.toString(), addressId, remark);

            addOrderTask.execute(request);
        }
    }

    /**
     * 代下单时，业务员先获取客户地址列表
     * 然后选择地址，添加备注，下单
     */
    private void getAddressAndAddOrder() {
        GetAddressListTask getAddressListTask = new GetAddressListTask(BuyCarActivity.this);
        getAddressListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<GetAddressListData>() {
            @Override
            public void failCallback(Result<GetAddressListData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        getAddressListTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<GetAddressListData>() {
            @Override
            public void successCallback(Result<GetAddressListData> result) {
                DialogUtil.selectAddressDialog(BuyCarActivity.this, result.getValue().getList(), new AddressDialog.ItemClickListener() {
                    @Override
                    public void itemClick(Address address) {
                        //取到地址Id
                        addressId = address.getId();
                        //添加备注并下单
                       // alertRemarkAndAddOrder();
                        addOrder();

                    }
                });
            }
        });

        getAddressListTask.execute(memberId);
    }


    /**
     * 提示添加备注并下单
     */
    private void alertRemarkAndAddOrder() {
//        DialogUtil.editDialog(BuyCarActivity.this, "添加备注信息", "您可以填写备注，不需要可不填"
//                , "取消", "确定", new DGPromptDialog.PromptDialogListener() {
//                    @Override
//                    public void onClick(View view, String inputText) {
//
//                    }
//                }, new DGPromptDialog.PromptDialogListener() {
//                    @Override
//                    public void onClick(View view, String inputText) {
//                        //确定
//                        remark = inputText;
//                        addOrder();
//                    }
//                });
        addOrder();
    }

    /**
     * 注册广播
     */
    private void registerReceiver() {
        buyCarSelectChangeReceiver = new BuyCarSelectChangeReceiver() {
            @Override
            public void changeSelect(String type) {
                if (type.equals(SELECT_REFRESH)) {
                    refreshSelect();
                } else {
                    loadData(false);
                }
            }
        };
        buyCarSelectChangeReceiver.register();
    }
}
