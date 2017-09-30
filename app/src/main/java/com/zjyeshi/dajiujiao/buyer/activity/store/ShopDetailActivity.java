package com.zjyeshi.dajiujiao.buyer.activity.store;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigapple.lib.utils.sharepreference.BPPreferences;
import com.xuan.bigappleui.lib.view.listview.BUHighHeightListView;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.store.ShopGoodAdapter;
import com.zjyeshi.dajiujiao.buyer.common.PreferenceConstants;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.entity.GoodsCar;
import com.zjyeshi.dajiujiao.buyer.entity.good.AllGoodInfo;
import com.zjyeshi.dajiujiao.buyer.receiver.buy.CarNumChangeReceiver;
import com.zjyeshi.dajiujiao.buyer.receiver.buy.GoodsSortChangeReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.pay.Wallet;
import com.zjyeshi.dajiujiao.buyer.task.data.store.goods.GoodData;
import com.zjyeshi.dajiujiao.buyer.task.data.store.goods.GoodList;
import com.zjyeshi.dajiujiao.buyer.task.data.store.goods.Product;
import com.zjyeshi.dajiujiao.buyer.task.data.store.shopdetails.Shop;
import com.zjyeshi.dajiujiao.buyer.task.data.store.shopdetails.ShopDetailData;
import com.zjyeshi.dajiujiao.buyer.task.data.store.sort.Category;
import com.zjyeshi.dajiujiao.buyer.task.data.store.sort.SortData;
import com.zjyeshi.dajiujiao.buyer.task.data.store.sort.SortList;
import com.zjyeshi.dajiujiao.buyer.task.my.MyAccountTask;
import com.zjyeshi.dajiujiao.buyer.task.sales.data.SalesListData;
import com.zjyeshi.dajiujiao.buyer.task.store.GoodListTask;
import com.zjyeshi.dajiujiao.buyer.task.store.ShopDetailTask;
import com.zjyeshi.dajiujiao.buyer.task.store.SortListTask;
import com.zjyeshi.dajiujiao.buyer.utils.AuthUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.views.MyScrollView;
import com.zjyeshi.dajiujiao.buyer.views.XListView;
import com.zjyeshi.dajiujiao.buyer.widgets.TwoTabClickWidget;
import com.zjyeshi.dajiujiao.buyer.widgets.addLessFormat.FormatWidget;
import com.zjyeshi.dajiujiao.buyer.widgets.addLessFormat.listener.FormatOpListener;
import com.zjyeshi.dajiujiao.buyer.widgets.search.SearchWidget;
import com.zjyeshi.dajiujiao.buyer.widgets.search.callback.SearchCallback;
import com.zjyeshi.dajiujiao.buyer.widgets.store.BuyBottomWidget;
import com.zjyeshi.dajiujiao.buyer.widgets.store.StoreRightLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.logging.Handler;

/**
 * 新的店铺详情
 * Created by wuhk on 2016/6/29.
 */
public class
ShopDetailActivity extends BaseActivity implements FormatOpListener, MyScrollView.OnScrollListener {
        //, com.zjyeshi.dajiujiao.buyer.views.XListView.IXListViewListener{
//    @InjectView(R.id.titleLayout)
//    private DGTitleLayout titleLayout;
    @InjectView(R.id.searchWidget)
    private SearchWidget searchWidget;
    @InjectView(R.id.storeRightLayout)
    private StoreRightLayout storeRightLayout;
    @InjectView(R.id.shopInfoView)
    private ShopInfoViews shopInfoView;
    @InjectView(R.id.scrollView)
    private MyScrollView scrollView;
    @InjectView(R.id.contentTabLayout)
    private TwoTabClickWidget contentTabLayout;
    @InjectView(R.id.topTabLayout)
    private TwoTabClickWidget topTabLayout;
    @InjectView(R.id.listView)
    private BUHighHeightListView listView;
    @InjectView(R.id.changeUnitIv)
    private ImageView changeUnitIv;
    @InjectView(R.id.buyBottomWidget)
    private BuyBottomWidget buyBottomWidget;
    @InjectView(R.id.softView)
    private View softView;
    @InjectView(R.id.returnTv)
    private TextView returnTv;
    @InjectView(R.id.rightTv)
    private TextView rightTv;


    private List<SortData> sortDataList = new ArrayList<SortData>();//分类信息列表

    private List<GoodData> dataList = new ArrayList<GoodData>();//请求的商品列表
    private List<GoodData> tempShowList = new ArrayList<GoodData>();//搜索的商品列表
    private List<AllGoodInfo> goodInfoList = new ArrayList<AllGoodInfo>();//显示商品列表信息
    private ShopGoodAdapter shopGoodAdapter;

    private GoodsSortChangeReceiver goodsSortChangeReceiver;//分类选择广播接收器
    private CarNumChangeReceiver carNumChangeReceiver;//购物车数量改变广播接收器
 // private boolean omu=true;
    private FormatWidget formatWidget;//加减控件
    private HashMap<String, GoodData> productHashMap = new HashMap<String, GoodData>();
    //默认的初始单位
    private String DEFAULT_TYPE = "箱";
    private boolean isDefaultType = true;//单位是箱的表示true
    //参数
    public static final String PARAM_SHOPID = "param.shop.id";//代下业务员ID
    public static final String PARAM_MEMBERID = "param.member.id";//代下客户ID
    public static final String OOOM = "werng";//代下客户ID
    private String shopId;//店铺Id
    private String categoryId = "";//分类Id
    private Shop shop;//店铺信息
    private String memberId = "";//代下单的时候用户Id
    private String typo="";
    private int page=1;
    private String marketCostTyp;
    //tab页
    public static final String TAB1 = "常规购买";
    public static final String TAB2 = "市场支持";
    private boolean isMarketGoods = false;//标记当前选择的商品种类，默认是常规购买
    private String accountMarketPrice = "0";// 市场支持费用,这个价格是*100的
    private String myKc = " "; //我的库存;
//    private boolean scrollFlag = false;// 标记是否滑动
//    private int lastVisibleItemPosition;// 标记上次滑动位置
    //标志位，resume时是否重新组合计算数据
    public static boolean reUnitData = false;

//    android.os.Handler mHandler = new android.os.Handler() {
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            page++;
//            loadProductList();
//            shopGoodAdapter.notifyDataSetChanged();
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_shop_detail);
        initWidgets();
        //广播注册
        registerReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        goodsSortChangeReceiver.unRegister(this);
        carNumChangeReceiver.unRegister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //为了购物车提交回来之后显示最新购物车数据
        reUnitData=true;
        if (reUnitData) {
            unionListAndRefresh(tempShowList, false);
            reUnitData = false;
        }
    }



    private void initWidgets() {
        formatWidget = null;
        reUnitData = false;

        buyBottomWidget.setClickable(true);
        storeRightLayout.setClickable(true);
        softView.setClickable(true);
        typo=getIntent().getStringExtra(OOOM);
        //判断市场支持费用的显示
        //显示市场支持tab页
        if(AuthUtil.showMarketCostTab()||typo!=null){
           topTabLayout.setVisibility(View.VISIBLE);
            contentTabLayout.setVisibility(View.VISIBLE);
            //滑动tab置顶
            scrollView.setOnScrollListener(this);
            //当布局的状态或者控件的可见性发生改变回调的接口
            findViewById(R.id.parent_layout).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @Override
                public void onGlobalLayout() {
                    //这一步很重要，使得上面的购买布局和下面的购买布局重合
                    onScroll(scrollView.getScrollY());

                }
            });
            //tab初始化
            topTabLayout.configTabTv(TAB1, TAB2);
            topTabLayout.configTabClickListner(new TwoTabClickWidget.OnTabClickListener() {
                @Override
                public void onTabClick(String str) {
                    if (TAB1.equals(str)) {
                        isMarketGoods = false;
                        unionListAndRefresh(tempShowList, false);

                    } else if (TAB2.equals(str)) {
                        isMarketGoods = true;
                        unionListAndRefresh(tempShowList, false);
                    }
                }
            });
            buyBottomWidget.refreshData(true);

        }else{
                topTabLayout.setVisibility(View.GONE);
                contentTabLayout.setVisibility(View.GONE);
                buyBottomWidget.refreshData(false);

        }

        //接收前一个页面传入的参数
        shopId = getIntent().getStringExtra(PARAM_SHOPID);
        memberId = getIntent().getStringExtra(PARAM_MEMBERID);
        if(memberId==null){
            memberId="";
        }
        returnTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validators.isEmpty(sortDataList)) {
                    ToastUtil.toast("暂无分类");
                } else {
                    storeRightLayout.setVisibility(View.VISIBLE);
                    storeRightLayout.refreshSort(sortDataList);
                }
            }
        });
//        //标题
//        titleLayout.configTitle("店铺详情").configReturn("返回", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        }).configRightText("分类", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Validators.isEmpty(sortDataList)) {
//                    ToastUtil.toast("暂无分类");
//                } else {
//                    storeRightLayout.setVisibility(View.VISIBLE);
//                    storeRightLayout.refreshSort(sortDataList);
//                }
//            }
//        });


        //底部栏
        buyBottomWidget.setGoCarOnClikListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ShopDetailActivity.this, BuyCarActivity.class);
                intent.putExtra(BuyCarActivity.PARAM_MEMBERID, memberId);
                startActivity(intent);
            }
        });
        //初始化数据
        loadDetail();
        loadSortList();
        loadAccount();

        //输入完成之后，点击任意地方，收起键盘，完成输入，显示之前隐藏的，
        //这里采用的方法是覆盖了一层View,有输入时显示，完成时隐藏
        softView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formatWidget.sureNum();
                buyBottomWidget.setVisibility(View.VISIBLE);
                changeUnitIv.setVisibility(View.VISIBLE);
                softView.setVisibility(View.GONE);
            }
        });

        //切换单位
        changeUnitIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDefaultType = !isDefaultType;
                unionListAndRefresh(tempShowList, true);
            }
        });

        //搜索
        searchWidget.sethintDes("输入商品名称搜索");
        searchWidget.configSearchCallback(new SearchCallback() {
            @Override
            public void search(String content) {
                tempShowList.clear();
                if (Validators.isEmpty(content)) {
                    tempShowList.addAll(dataList);
                } else {
                    for (GoodData data : dataList) {
                        if (data.getProduct().getName().contains(content)) {
                            tempShowList.add(data);
                        }
                    }
                }
                unionListAndRefresh(tempShowList, false);
            }
        });

//        listView.setPullRefreshEnable(true);
//        listView.setPullLoadEnable(true);
//        listView.setAutoLoadEnable(true);
//        listView.setXListViewListener((XListView.IXListViewListener) this);
//        listView.setRefreshTime(getTime());

        //listView初始化
        shopGoodAdapter = new ShopGoodAdapter(ShopDetailActivity.this, goodInfoList, isMarketGoods , memberId);
        listView.setAdapter(shopGoodAdapter);
//        listView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
//                ToastUtil.toast("回调完成");
//            }
//        });
      //  new Thread(mRunnable).start();

//        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if (firstVisibleItem == 0) {
//                    View firstVisibleItemView = listView.getChildAt(0);
//                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
//                       // ToastUtil.toast("ListView##### 滚动到顶部 #####");
//                    }
//                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
//                    View lastVisibleItemView = listView.getChildAt(listView.getChildCount() - 1);
//                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == listView.getHeight()) {
//                       // ToastUtil.toast("ListView##### 滚动到底部 ######");
//                    }
//                }
//                if (scrollFlag) {
//                    if (firstVisibleItem > lastVisibleItemPosition) {//上滑
//                        ToastUtil.toast("ListView#####  上滑 #####");
//                    }
//                    if (firstVisibleItem < lastVisibleItemPosition) {//下滑
//                        ToastUtil.toast("ListView##### 下滑   #####");
//                    }
//                    if (firstVisibleItem == lastVisibleItemPosition) {
//                        return;
//                    }
//                    lastVisibleItemPosition = firstVisibleItem;
//                }
//            }
//
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                //do nothing
//            }
//
//        });

    }

    /**
     * 获取店铺详情
     */
    private void loadDetail() {
        ShopDetailTask.getShopDetail(ShopDetailActivity.this, shopId,new AsyncTaskSuccessCallback<ShopDetailData>(){
            @Override
            public void successCallback(Result<ShopDetailData> result) {
                //显示店铺详情信息
                shopInfoView.bindData(result.getValue());
                //保存店铺信息
                shop = result.getValue().getShop();
                marketCostTyp=result.getValue().getMarketCostType();

                List<SalesListData.Sales> activityList = result.getValue().getActivities();
                StringBuilder activties = new StringBuilder();
                for (SalesListData.Sales sales : activityList){
                    activties.append(sales.getId());
                    activties.append(",");
                }
                if (activties.length() > 1){
                    activties.deleteCharAt(activties.length() - 1);
                }
                BPPreferences.instance().putString(PreferenceConstants.getShopActivitiesKey(shopId) , activties.toString());

                //获取店铺详情之后再获取商品列表
                loadProductList();
            }
        },memberId);
    }

    /**
     * 获取分类列表
     */
    private void loadSortList() {
        SortListTask.getGoodSortList(ShopDetailActivity.this, shopId, new AsyncTaskSuccessCallback<SortList>() {
            @Override
            public void successCallback(Result<SortList> result) {
                sortDataList.clear();
                //自动插入全部分类，在头部
                SortData sortData = new SortData();
                Category category = new Category();
                category.setName("全部");
                category.setId("");
                sortData.setCategory(category);
                sortDataList.add(sortData);
                sortDataList.addAll(result.getValue().getList());
            }
        });
    }

    /**
     * 获取商品列表
     */
    private void loadProductList() {
        GoodListTask goodListTask = new GoodListTask(ShopDetailActivity.this);
        goodListTask.setShowProgressDialog(true);
        goodListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<GoodList>() {
            @Override
            public void failCallback(Result<GoodList> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        goodListTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<GoodList>() {
            @Override
            public void successCallback(Result<GoodList> result) {
//                if(result.getValue().getList().size()==0){
//                   // ToastUtil.toast("sjkwekong");
//                    omu=false;
//                    try {
//                        new Thread(mRunnable).join();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    return;
//                }
                dataList.clear();
                dataList.addAll(result.getValue().getList());
                tempShowList.clear();
                tempShowList.addAll(result.getValue().getList());
                //将获取下来的商品数据和本地数据库中的数据结合处理，用于显示
                unionListAndRefresh(tempShowList, false);
            }
        });
        String orderId = "";
        goodListTask.execute(shopId, categoryId, orderId, memberId,page+"");
    }

    /**
     * 下载账户市场支持费用
     */
    private void loadAccount() {
        MyAccountTask.getMyAccountWhenBuy(ShopDetailActivity.this, memberId, new AsyncTaskSuccessCallback<Wallet>() {
            @Override
            public void successCallback(Result<Wallet> result) {
                //市场返还费用,这个价格是*100的
                accountMarketPrice = result.getValue().getMarketCost();
                showMarketPrice();
            }
        });
    }

    /**
     * 合并成需要显示数据
     */
    private void unionListAndRefresh(final List<GoodData> showList, final boolean isChangeUnit) {
        goodInfoList.clear();
        productHashMap.clear();
        myKc = "";

        HashMap<String, String> numMap = new HashMap<String, String>();

        for (int i = 0; i < showList.size(); i++) {
            AllGoodInfo allGoodInfo = new AllGoodInfo();
            GoodData goodData = showList.get(i);

            productHashMap.put(goodData.getProduct().getId(), goodData);
            Product product = goodData.getProduct();

            //库存小于0，显示为0，之前返回出现过小于0的情况
            if (Integer.parseInt(product.getInventory()) < 0) {
                product.setInventory("0");
            }
            //获取酒品的单位，没有的话默认为瓶
            String unit = product.getUnit();
            if (Validators.isEmpty(unit)) {
                unit = "瓶";
                product.setUnit(unit);
            }
            allGoodInfo.setUnit(unit);
            //商品Id
            allGoodInfo.setGoodId(product.getId());
            //商品名称
            allGoodInfo.setGoodName(product.getName());
            //商品图片
            allGoodInfo.setGoodIcon(product.getPic());
            //商品显示单价,服务器获取的价格除以100
            allGoodInfo.setGoodPrice(ExtraUtil.format(Float.parseFloat(product.getPrice()) / 100));
            //市场支持费用
            allGoodInfo.setMarketCost(product.getMarketCost());
            //商品上传时的单价,保留服务器上的数据,还是*100
            allGoodInfo.setUpPrice(product.getPrice());
            //库存
            allGoodInfo.setInvertory(product.getInventory());
            //一箱多少瓶
            allGoodInfo.setBottlesPerBox(product.getBottlesPerBox());
            //描述
            allGoodInfo.setDescription(product.getDescription());
            //店名
            allGoodInfo.setShopName(shop.getName());
            //店Id
            allGoodInfo.setShopId(shop.getId());
            //含量 ： ml/单位
            allGoodInfo.setFormat(product.getSpecifications());
            //店家库存
            allGoodInfo.setSellerInventory(product.getSellerInventory());
            //查看购物车中有没有这种这种商品,不同tab页不同查询
            GoodsCar goodsCar = null;
            if (isMarketGoods) {
                goodsCar = DaoFactory.getMarketGoodsCarDao().findByGoodId(product.getId());
            } else {
                goodsCar = DaoFactory.getGoodsCarDao().findByGoodId(product.getId());
            }

            //类型和数量
            if (goodsCar != null) {
                //判断转换单位
                if (isChangeUnit) {
                    resetGoodCarData(allGoodInfo.getUnit(), goodsCar);
                }
                //数量是以瓶计算的
                allGoodInfo.setGoodCount(goodsCar.getGoodCount());
                allGoodInfo.setGoodType(goodsCar.getGoodType());
            } else {
                allGoodInfo.setGoodCount("0");
                if (isDefaultType) {
                    allGoodInfo.setGoodType(DEFAULT_TYPE);
                } else {
                    allGoodInfo.setGoodType(product.getUnit());
                }
            }
            //如果是箱的，价格显示一箱的价格
            if (allGoodInfo.getGoodType().equals(DEFAULT_TYPE)) {
                String perBoxPrice = ExtraUtil.format((Float.parseFloat(product.getPrice()) / 100) * (Integer.parseInt(product.getBottlesPerBox())));
                allGoodInfo.setPriceWithUnit(perBoxPrice);
            } else {
                allGoodInfo.setPriceWithUnit(allGoodInfo.getGoodPrice());
            }

            allGoodInfo.setSalesList(goodData.getPreferentialActivities());
            goodInfoList.add(allGoodInfo);

            //计算我的总计
            int myBoxNum = (Integer.parseInt(allGoodInfo.getInvertory())) / (Integer.parseInt(allGoodInfo.getBottlesPerBox()));
            int myUnitNum = (Integer.parseInt(allGoodInfo.getInvertory())) % (Integer.parseInt(allGoodInfo.getBottlesPerBox()));
            if (myBoxNum != 0) {
                if (!Validators.isEmpty(numMap.get("箱"))) {
                    numMap.put("箱", String.valueOf(Integer.parseInt(numMap.get("箱")) + myBoxNum));
                } else {
                    numMap.put("箱", String.valueOf(myBoxNum));
                }
            }
            if (myUnitNum != 0) {
                if (!Validators.isEmpty(numMap.get(allGoodInfo.getUnit()))) {
                    numMap.put(allGoodInfo.getUnit(), String.valueOf(Integer.parseInt(numMap.get(allGoodInfo.getUnit())) + myUnitNum));
                } else {
                    numMap.put(allGoodInfo.getUnit(), String.valueOf(myUnitNum));
                }
            }
        }

        for (String key : numMap.keySet()) {
            myKc += numMap.get(key) + key;
        }

        shopGoodAdapter.notifyData(isMarketGoods);
       // buyBottomWidget.setMyKc("我的库存合计:" + myKc);
    }

    /**
     * 重置购物车中的数量和单位
     *
     * @param goodsCar
     */
    private void resetGoodCarData(String unit, GoodsCar goodsCar) {
        if (isDefaultType) {
            //默认箱
            //数据库里的单位不是箱，需要转成箱
            if (!goodsCar.getGoodType().equals(DEFAULT_TYPE)) {
                goodsCar.setGoodType(DEFAULT_TYPE);
                //改变数量
                //瓶转箱
                float unitNum = Float.parseFloat(goodsCar.getGoodCount());
                float boxNum = unitNum / Float.parseFloat(goodsCar.getGoodBottole());
                //入库
                //数量存unit计
                float num = Float.parseFloat(ExtraUtil.format(boxNum)) * Float.parseFloat(goodsCar.getGoodBottole());

                goodsCar.setGoodCount(FormatWidget.roundingNum(num));
                if (Float.parseFloat(goodsCar.getGoodCount()) == 0) {
                    if (isMarketGoods) {
                        DaoFactory.getMarketGoodsCarDao().deleteById(goodsCar.getGoodId());
                    } else {
                        DaoFactory.getGoodsCarDao().deleteById(goodsCar.getGoodId());
                    }
                } else {
                    if (isMarketGoods) {
                        DaoFactory.getMarketGoodsCarDao().replace(goodsCar);
                    } else {
                        DaoFactory.getGoodsCarDao().replace(goodsCar);
                    }
                }
            }
        } else {
            //装换的单位
            if (goodsCar.getGoodType().equals(DEFAULT_TYPE)) {
                goodsCar.setGoodType(unit);
                //改变数量
                //箱转瓶
                float unitNum = Float.parseFloat(goodsCar.getGoodCount());
                //入库
                goodsCar.setGoodCount(FormatWidget.roundingNum(unitNum));
                if (Float.parseFloat(goodsCar.getGoodCount()) == 0) {
                    if (isMarketGoods) {
                        DaoFactory.getMarketGoodsCarDao().deleteById(goodsCar.getGoodId());
                    } else {
                        DaoFactory.getGoodsCarDao().deleteById(goodsCar.getGoodId());
                    }
                } else {
                    if (isMarketGoods) {
                        DaoFactory.getMarketGoodsCarDao().replace(goodsCar);
                    } else {
                        DaoFactory.getGoodsCarDao().replace(goodsCar);
                    }
                }
            }

        }
    }

    /**
     * 注册广播
     */
    private void registerReceiver() {
        //分类
        goodsSortChangeReceiver = new GoodsSortChangeReceiver() {
            @Override
            public void changeGoodsList(String id) {
                storeRightLayout.setVisibility(View.GONE);
                categoryId = id;
                loadProductList();
            }
        };
        goodsSortChangeReceiver.register(this);

        //购物车数量
        carNumChangeReceiver = new CarNumChangeReceiver() {
            @Override
            public void changeCarnum(boolean isAddOrder) {
                refreshMarketPrice(isAddOrder);
                if (AuthUtil.showMarketCostTab()||typo!=null){
                    buyBottomWidget.refreshData(true);
                }else{
                    buyBottomWidget.refreshData(false);
                }
                shopGoodAdapter.notifyData(isMarketGoods);
            }
        };

        carNumChangeReceiver.register(this);
    }


    /**
     * 刷新底部购物车和市场价格
     */
    private void refreshMarketPrice(boolean isAddOrder) {
        if (AuthUtil.recordMarketCostFee()||marketCostTyp.equals(2+"")){
            List<GoodsCar> normalGoodList = DaoFactory.getGoodsCarDao().findAllGoods();
            float carMarketPrice = 0;
            for (GoodsCar goodsCar : normalGoodList) {
                //计算市场支持费用
                GoodData goodData = productHashMap.get(goodsCar.getGoodId());
                Product product = goodData.getProduct();
                float market = Float.parseFloat(goodsCar.getGoodCount()) * Float.parseFloat(product.getMarketCost());
                carMarketPrice += market;
            }

            //保存购物车中的市场支持费用,这个价格是*100的
            if (!isAddOrder){
                BPPreferences.instance().putFloat(PreferenceConstants.CAR_MARKET_COST, carMarketPrice);
            }
        }

        showMarketPrice();
    }

    /**
     * 显示一下顶部价格
     */
    private void showMarketPrice() {
        //钱包市场支持费用
        float walletMarketPrice = Float.parseFloat(accountMarketPrice)/100;
        //保存钱包市场支持费用
        BPPreferences.instance().putFloat(PreferenceConstants.MY_ACCOUNT_MARKET, walletMarketPrice);
        //购物车商品市场支持费用
        float price = BPPreferences.instance().getFloat(PreferenceConstants.CAR_MARKET_COST, 0.00f);
        float goodMarketPrice = price / 100;
        //设置文字
        if (marketCostTyp!=null&&marketCostTyp.equals(2+"")){
            topTabLayout.configPrice("¥" + ExtraUtil.format(walletMarketPrice )+"立返"+ goodMarketPrice);
        }else {
        topTabLayout.configPrice("¥" + ExtraUtil.format(walletMarketPrice + goodMarketPrice));}
    }

    @Override
    public void configFormatWidget(FormatWidget formatWidget) {
        this.formatWidget = formatWidget;
    }

    @Override
    public void changeShow() {
        //加减控件点击时，弹出覆盖View ，隐藏底部控件，和按钮切换
        // buyBottomWidget.setVisibility(View.GONE);
        changeUnitIv.setVisibility(View.GONE);
        softView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onScroll(int scrollY) {
        int tabLayout2ParentTop = Math.max(scrollY, contentTabLayout.getTop());
        topTabLayout.layout(0, tabLayout2ParentTop, topTabLayout.getWidth(), tabLayout2ParentTop + topTabLayout.getHeight());
    }

//    @Override
//    public void onRefresh() {
//
//
//    }
//
//    @Override
//    public void onLoadMore() {
//        ToastUtil.toast("dsadaskndah");
//      //  loadProductList();
//       // shopGoodAdapter.notifyDataSetChanged();
//        onLoad();
//    }
//    private void onLoad() {
//        listView.stopRefresh();
//        listView.stopLoadMore();
//        listView.setRefreshTime("刚刚");
//    }
//    private String getTime() {
//        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
//    }
//    private   Runnable mRunnable = new Runnable() {
//        public void run() {
//            while (omu) {
//                try {
//                    Thread.sleep(500);
//                //    mHandler.sendMessage(mHandler.obtainMessage());
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    };

//    @Override
//    public void onRefresh() {
//
//    }

//    @Override
//    public void onLoadMore() {
//        ToastUtil.toast("已经加载全部!");
//        listView.stopRefresh();
//        listView.stopLoadMore();
//        listView.setRefreshTime(getTime());
//     //   mListView.setRefreshTime("刚刚");
//
//    }

//    protected boolean isLastItemVisible() {
//        shopGoodAdapter = listView.getAdapter();
//
//        if (null == shopGoodAdapter || shopGoodAdapter.isEmpty()) {
//            return true;
//        }
//
//        final int lastItemPosition = shopGoodAdapter.getCount() - 1;
//        final int lastVisiblePosition = listView.getLastVisiblePosition();
//
//
//        if (lastVisiblePosition >= lastItemPosition - 1) {
//            final int childIndex = lastVisiblePosition - listView.getFirstVisiblePosition();
//            final int childCount = listView.getChildCount();
//            final int index = Math.min(childIndex, childCount - 1);
//            final View lastVisibleChild = listView.getChildAt(index);
//            if (lastVisibleChild != null) {
//                return lastVisibleChild.getBottom() <= listView.getBottom();
//            }
//        }
//
//        return false;
//    }

}
