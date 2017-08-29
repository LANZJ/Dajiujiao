package com.zjyeshi.dajiujiao.buyer.activity.sales;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigappleui.lib.view.listview.BUHighHeightListView;
import com.xuan.bigdog.lib.dialog.DGSingleSelectDialog;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.activity.my.seller.ChangeShopNameActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.sales.JoinAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.enums.SalesFillTypeEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.SalesFormTypeEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.SalesGiveTypeEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.ShopModifyEnum;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.Employee;
import com.zjyeshi.dajiujiao.buyer.entity.sales.AddSalesRequest;
import com.zjyeshi.dajiujiao.buyer.receiver.info.ModifyShopInfoReceiver;
import com.zjyeshi.dajiujiao.buyer.receiver.sales.GiveProductReceiver;
import com.zjyeshi.dajiujiao.buyer.receiver.sales.SelectSalesGiftReceiver;
import com.zjyeshi.dajiujiao.buyer.receiver.sales.SelectSalesJoinReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.data.store.goods.Product;
import com.zjyeshi.dajiujiao.buyer.task.sales.AddSalesInfoTask;
import com.zjyeshi.dajiujiao.buyer.task.sales.data.SalesDetailData;
import com.zjyeshi.dajiujiao.buyer.utils.DialogUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.views.other.IVButtonBox;
import com.zjyeshi.dajiujiao.buyer.widgets.dialog.LeaveTypeDialog;
import com.zjyeshi.dajiujiao.buyer.widgets.timePicker.DatePickerWidget;
import com.zjyeshi.dajiujiao.buyer.widgets.timePicker.callback.DatePickCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 添加新活动
 * Created by wuhk on 2017/5/5.
 */

public class AddNewSalesActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.salesTypeLayout)
    private RelativeLayout salesTypeLayout;
    @InjectView(R.id.salesTypeTv)
    private TextView salesTypeTv;
    @InjectView(R.id.conditionSelectRg)
    private RadioGroup conditionSelectRg;
    @InjectView(R.id.conditionLayout)
    private RelativeLayout conditionLayout;
    @InjectView(R.id.conditionEt)
    private EditText conditionEt;
    @InjectView(R.id.conditionUnitTv)
    private TextView conditionUnitTv;
    @InjectView(R.id.giftTypeLayout)
    private RelativeLayout giftTypeLayout;
    @InjectView(R.id.giftTypeTv)
    private TextView giftTypeTv;
    @InjectView(R.id.selectGiftLayout)
    private RelativeLayout selectGiftLayout;
    @InjectView(R.id.selectGiftTv)
    private TextView selectGiftTv;
    @InjectView(R.id.disMoneyLayout)
    private RelativeLayout disMoneyLayout;
    @InjectView(R.id.disMoneyTv)
    private TextView disMoneyTv;
    @InjectView(R.id.disMoneyDesTv)
    private TextView disMoneyDesTv;
    @InjectView(R.id.disMoneyEt)
    private EditText disMoneyEt;
    @InjectView(R.id.setGiveBoxLayout)
    private RelativeLayout setGiveBoxLayout;
    @InjectView(R.id.setGiveBoxEt)
    private EditText setGiveBoxEt;
    @InjectView(R.id.giveProductLayout)
    private RelativeLayout giveProductLayout;
    @InjectView(R.id.giveProductTv)
    private TextView giveProductTv;
    @InjectView(R.id.priorityLayout)
    private RelativeLayout priorityLayout;
    @InjectView(R.id.priorityTv)
    private TextView priorityTv;
    @InjectView(R.id.urlLayout)
    private RelativeLayout urlLayout;
    @InjectView(R.id.urlTv)
    private TextView urlTv;
    @InjectView(R.id.shareSalesIv)
    private IVButtonBox shareSalesIv;
    @InjectView(R.id.startTimeLayout)
    private RelativeLayout startTimeLayout;
    @InjectView(R.id.startTimeTv)
    private TextView startTimeTv;
    @InjectView(R.id.endTimeLayout)
    private RelativeLayout endTimeLayout;
    @InjectView(R.id.endTimeTv)
    private TextView endTimeTv;
    @InjectView(R.id.joinShopLayout)
    private RelativeLayout joinShopLayout;
    @InjectView(R.id.joinShopListView)
    private BUHighHeightListView joinShopListView;
    @InjectView(R.id.joinProductLayout)
    private RelativeLayout joinProductLayout;
    @InjectView(R.id.joinProductListView)
    private BUHighHeightListView joinProductListView;
    @InjectView(R.id.datePickerWidget)
    private DatePickerWidget datePickerWidget;

    private JoinAdapter joinShopAdapter;
    private JoinAdapter joinProductAdapter;

    private List<SalesDetailData.Join> joinShopList = new ArrayList<SalesDetailData.Join>();
    private List<SalesDetailData.Join> joinProductList = new ArrayList<SalesDetailData.Join>();

    private SelectSalesJoinReceiver selectSalesJoinReceiver;//参与者广播
    private ModifyShopInfoReceiver modifyShopInfoReceiver;//修改活动地址广播
    private SelectSalesGiftReceiver selectSalesGiftReceiver;//礼物选择广播
    private GiveProductReceiver giveProductReceiver;//酒品选择广播

    private AddSalesRequest addSalesRequest = new AddSalesRequest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_sales_info);
        initWidgets();
        //注册广播
        registerReceiver();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        selectSalesJoinReceiver.unregister();
        modifyShopInfoReceiver.unRegister();
        selectSalesGiftReceiver.unregister();
        giveProductReceiver.unregister();
    }

    private void initWidgets() {
        //选项先隐藏，点击相关类型显示
        conditionLayout.setVisibility(View.GONE);
        selectGiftLayout.setVisibility(View.GONE);
        disMoneyLayout.setVisibility(View.GONE);
        setGiveBoxLayout.setVisibility(View.GONE);
        giveProductLayout.setVisibility(View.GONE);

        //标题
        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        }).configTitle("活动信息").configRightText("提交", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSalesRequest.setShopId(LoginedUser.getLoginedUser().getShopId());
                addSalesRequest.setSatisfyCondition(conditionEt.getText().toString());
                addSalesRequest.setStartTime(startTimeTv.getText().toString());
                addSalesRequest.setEndTime(endTimeTv.getText().toString());
                addSalesRequest.setUrl(urlTv.getText().toString());
                addSalesRequest.setSuperposition(shareSalesIv.isChecked());
                if (addSalesRequest.getFavouredType() == SalesGiveTypeEnum.GIVE_WINE.getValue()) {
                    addSalesRequest.setFavouredPolicy(setGiveBoxEt.getText().toString());
                } else {
                    addSalesRequest.setFavouredPolicy(disMoneyEt.getText().toString());
                }

                //上传
                addSalesInfo(addSalesRequest);
            }
        });

        //活动类型
        salesTypeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] itemStr = {SalesFillTypeEnum.FILL_MONEY.getName(), SalesFillTypeEnum.FILL_BOX.getName(), SalesFillTypeEnum.MIX_BOX.getName()};
                View.OnClickListener[] ls = {new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //满额度
                        conditionLayout.setVisibility(View.VISIBLE);
                        conditionUnitTv.setVisibility(View.GONE);
                        salesTypeTv.setText("满足金额");
                        addSalesRequest.setSatisfyType(SalesFillTypeEnum.FILL_MONEY.getValue());
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //满箱数
                        conditionLayout.setVisibility(View.VISIBLE);
                        conditionUnitTv.setVisibility(View.VISIBLE);
                        salesTypeTv.setText("满足箱数");
                        addSalesRequest.setSatisfyType(SalesFillTypeEnum.FILL_BOX.getValue());

                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //混合满箱数
                        conditionLayout.setVisibility(View.VISIBLE);
                        conditionUnitTv.setVisibility(View.VISIBLE);
                        salesTypeTv.setText("满足箱数");
                        addSalesRequest.setSatisfyType(SalesFillTypeEnum.MIX_BOX.getValue());
                    }
                }};
                DGSingleSelectDialog dialog = new DGSingleSelectDialog.Builder(AddNewSalesActivity.this).setItemTextAndOnClickListener(itemStr, ls).create();
                dialog.show();
            }
        });

        //满和每
        conditionSelectRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (i == R.id.fillRb) {
                    addSalesRequest.setFormType(SalesFormTypeEnum.FILL.getValue());
                } else if (i == R.id.everyRb) {
                    addSalesRequest.setFormType(SalesFormTypeEnum.EVERY.getValue());
                }
            }
        });

        //优惠政策
        giftTypeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] itemStr = {SalesGiveTypeEnum.GIVE_GIFT.getName(), SalesGiveTypeEnum.BACK_MONEY.getName(), SalesGiveTypeEnum.CUT_MONEY.getName(), SalesGiveTypeEnum.GIVE_WINE.getName()};
                View.OnClickListener[] ls = {new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //送礼品
                        selectGiftLayout.setVisibility(View.VISIBLE);
                        disMoneyLayout.setVisibility(View.GONE);
                        setGiveBoxLayout.setVisibility(View.GONE);
                        giveProductLayout.setVisibility(View.GONE);
                        giftTypeTv.setText(SalesGiveTypeEnum.GIVE_GIFT.getName());
                        addSalesRequest.setFavouredType(SalesGiveTypeEnum.GIVE_GIFT.getValue());

                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //返金额
                        disMoneyTv.setText("返还金额");
                        disMoneyDesTv.setText("返");
                        selectGiftLayout.setVisibility(View.GONE);
                        disMoneyLayout.setVisibility(View.VISIBLE);
                        setGiveBoxLayout.setVisibility(View.GONE);
                        giveProductLayout.setVisibility(View.GONE);
                        giftTypeTv.setText(SalesGiveTypeEnum.BACK_MONEY.getName());
                        addSalesRequest.setFavouredType(SalesGiveTypeEnum.BACK_MONEY.getValue());

                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //减金额
                        disMoneyTv.setText("减免金额");
                        disMoneyDesTv.setText("减");
                        selectGiftLayout.setVisibility(View.GONE);
                        disMoneyLayout.setVisibility(View.VISIBLE);
                        setGiveBoxLayout.setVisibility(View.GONE);
                        giveProductLayout.setVisibility(View.GONE);
                        giftTypeTv.setText(SalesGiveTypeEnum.CUT_MONEY.getName());
                        addSalesRequest.setFavouredType(SalesGiveTypeEnum.CUT_MONEY.getValue());
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //送酒品
                        selectGiftLayout.setVisibility(View.GONE);
                        disMoneyLayout.setVisibility(View.GONE);
                        setGiveBoxLayout.setVisibility(View.VISIBLE);
                        giveProductLayout.setVisibility(View.VISIBLE);
                        giftTypeTv.setText(SalesGiveTypeEnum.GIVE_WINE.getName());
                        addSalesRequest.setFavouredType(SalesGiveTypeEnum.GIVE_WINE.getValue());
                    }
                }};
                DGSingleSelectDialog dialog = new DGSingleSelectDialog.Builder(AddNewSalesActivity.this).setItemTextAndOnClickListener(itemStr, ls).create();
                dialog.show();
            }
        });


        //选择礼品
        selectGiftLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GiftListActivity.startGiftListActivity(AddNewSalesActivity.this);
            }
        });

        //选择赠送商品
        giveProductLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GiveProductListActivity.startGiveProductListActivty(AddNewSalesActivity.this);
            }
        });

        //活动优先级
        priorityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.selectLeaveType(AddNewSalesActivity.this, new LeaveTypeDialog.ItemClickListener() {
                    @Override
                    public void itemClick(String content) {
                        addSalesRequest.setPriority(Integer.parseInt(content));
                        priorityTv.setText(content);
                    }
                } , getPriorityList());
            }
        });


        //与其他活动同享,默认不同享
        shareSalesIv.setChecked(false);
        shareSalesIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(shareSalesIv.isChecked()){
                    priorityLayout.setVisibility(View.GONE);
                    addSalesRequest.setPriority(1);
                }else{
                    priorityLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        //活动网址
        urlLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = urlTv.getText().toString();
                ChangeShopNameActivity.startActivity(AddNewSalesActivity.this, ShopModifyEnum.MODIFYSALESURL, content);
            }
        });



        startTimeTv.setText("未设置");
        endTimeTv.setText("未设置");
        //开始时间
        startTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calender = Calendar.getInstance();
                int year = calender.get(Calendar.YEAR);
                int month = calender.get(Calendar.MONTH) + 1;
                int day = calender.get(Calendar.DAY_OF_MONTH);

                if (!startTimeTv.getText().equals("未设置")) {
                    String[] times = startTimeTv.getText().toString().split("-");
                    year = Integer.parseInt(times[0]);
                    month = Integer.parseInt(times[1]);
                    day = Integer.parseInt(times[2]);
                }
                datePickerWidget.showSelect(year, month, day, new DatePickCallback() {
                    @Override
                    public void setTime(String time) {
                        startTimeTv.setText(time);
                    }
                });
            }
        });

        //结束时间
        endTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calender = Calendar.getInstance();
                int year = calender.get(Calendar.YEAR);
                int month = calender.get(Calendar.MONTH) + 1;
                int day = calender.get(Calendar.DAY_OF_MONTH);

                if (!endTimeTv.getText().equals("未设置")) {
                    String[] times = endTimeTv.getText().toString().split("-");
                    year = Integer.parseInt(times[0]);
                    month = Integer.parseInt(times[1]);
                    day = Integer.parseInt(times[2]);
                }
                datePickerWidget.showSelect(year, month, day, new DatePickCallback() {
                    @Override
                    public void setTime(String time) {
                        endTimeTv.setText(time);
                    }
                });
            }
        });

        //参与店铺
        joinShopLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectSalesShopActivity.startActivity(AddNewSalesActivity.this);
            }
        });

        //参与商品
        joinProductLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectSalesProductActivity.startActivity(AddNewSalesActivity.this);
            }
        });
    }

    /**
     * 添加活动
     *
     * @param request
     */
    private void addSalesInfo(AddSalesRequest request) {
        //参数判断等操作。。。。
        if(Validators.isEmpty(request.getShopIds())){
            ToastUtil.toast("请选择店铺");
            return;
        }else if (Validators.isEmpty(request.getProductIds())){
            ToastUtil.toast("请选择商品");
            return;
        }else if (request.getSatisfyType() == 0){
            ToastUtil.toast("请选择满足条件");
            return;
        }else if (request.getFavouredType() == 0){
            ToastUtil.toast("请选择优惠政策");
            return;
        }else if (Validators.isEmpty(request.getSatisfyCondition())){
            ToastUtil.toast("请填写满足条件");
            return;
        }else if (request.getStartTime().equals("未设置") || request.getEndTime().equals("未设置")){
            ToastUtil.toast("请设置好开始和结束时间");
            return;
        }else if (request.getFormType() == 0){
            ToastUtil.toast("请选择优惠政策，满或每");
            return;
        }else if (request.getPriority() == 0){
            ToastUtil.toast("请选择优先级");
            return;
        }else if(request.getFavouredType() == SalesGiveTypeEnum.GIVE_GIFT.getValue() && Validators.isEmpty(request.getGiftId())){
            ToastUtil.toast("请选择赠送的礼品");
            return;
        }else if(request.getFavouredType() != SalesGiveTypeEnum.GIVE_GIFT.getValue() && Validators.isEmpty(request.getFavouredPolicy())){
            ToastUtil.toast("请填写优惠数额");
            return;
        }
        AddSalesInfoTask.addSalesInfo(AddNewSalesActivity.this, request, new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                ToastUtil.toast("活动创建成功");
                SalesActivity.reload = true;
                finish();
            }
        });
    }

    /**
     * 启动该活动
     *
     * @param context
     */
    public static void startAddNewSalesActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, AddNewSalesActivity.class);
        context.startActivity(intent);
    }

    /**
     * 注册广播
     */
    private void registerReceiver() {
        selectSalesJoinReceiver = new SelectSalesJoinReceiver() {
            @Override
            public void change(String str, String listJson) {
                if (str.equals(SelectSalesJoinReceiver.JOIN_SHOP)) {
                    //店铺活动
                    List<Employee> selectShopList = JSON.parseArray(listJson, Employee.class);
                    joinShopList.clear();
                    StringBuilder sb = new StringBuilder();
                    for (Employee employee : selectShopList) {
                        SalesDetailData.Join data = new SalesDetailData.Join();
                        data.setShopProductId(employee.getId());
                        data.setName(employee.getShopName());
                        data.setPic(employee.getShopPic());
                        joinShopList.add(data);
                        sb.append(data.getShopProductId());
                        sb.append(",");
                    }
                    if (!Validators.isEmpty(sb.toString())) {
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    addSalesRequest.setShopIds(sb.toString());
                    joinShopAdapter = new JoinAdapter(AddNewSalesActivity.this, joinShopList);
                    joinShopListView.setAdapter(joinShopAdapter);
                } else if (str.equals(SelectSalesJoinReceiver.JOIN_PRODUCT)) {
                    //店铺活动
                    List<Product> selectProductList = JSON.parseArray(listJson, Product.class);
                    joinProductList.clear();
                    StringBuilder sb = new StringBuilder();
                    for (Product product : selectProductList) {
                        SalesDetailData.Join data = new SalesDetailData.Join();
                        data.setShopProductId(product.getId());
                        data.setName(product.getName());
                        data.setPic(product.getPic());
                        joinProductList.add(data);
                        sb.append(data.getShopProductId());
                        sb.append(",");
                    }
                    if (!Validators.isEmpty(sb.toString())) {
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    addSalesRequest.setProductIds(sb.toString());
                    joinProductAdapter = new JoinAdapter(AddNewSalesActivity.this, joinProductList);
                    joinProductListView.setAdapter(joinProductAdapter);
                }
            }
        };
        selectSalesJoinReceiver.register();

        selectSalesGiftReceiver = new SelectSalesGiftReceiver() {
            @Override
            public void dealWith(String giftId, String giftName) {
                addSalesRequest.setGiftId(giftId);
                selectGiftTv.setText(giftName);

            }
        };
        selectSalesGiftReceiver.register();

        giveProductReceiver = new GiveProductReceiver() {
            @Override
            public void dealWith(String wineId, String wineName) {
                addSalesRequest.setGiveProductId(wineId);
                giveProductTv.setText(wineName);
            }
        };
        giveProductReceiver.register();

        modifyShopInfoReceiver = new ModifyShopInfoReceiver() {
            @Override
            public void doModify(String str, int type) {
                if (type == ShopModifyEnum.MODIFYSALESURL.getValue()) {
                    urlTv.setText(str);
                    addSalesRequest.setUrl(urlTv.getText().toString());
                }
            }
        };
        modifyShopInfoReceiver.register();
    }


    public static List<String> getPriorityList(){
        List<String> resultList = new ArrayList<String>();
        for(int i = 1 ; i < 16 ; i ++){
            resultList.add(String.valueOf(i));
        }
        return resultList;
    }
}
