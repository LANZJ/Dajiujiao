package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.activity.store.ShopDetailActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.my.work.HmcListAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.Employee;
import com.zjyeshi.dajiujiao.buyer.receiver.work.UpdateReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.work.CustomerListTask;
import com.zjyeshi.dajiujiao.buyer.task.work.DeleteCustomerTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.CustomerListData;
import com.zjyeshi.dajiujiao.buyer.utils.DialogUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.widgets.callback.HmcItemClickListner;
import com.zjyeshi.dajiujiao.buyer.widgets.callback.HmcLongClickListener;
import com.zjyeshi.dajiujiao.buyer.widgets.search.SearchWidget;
import com.zjyeshi.dajiujiao.buyer.widgets.search.callback.SearchCallback;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;

/**
 * 花名册
 *
 * Created by zhum on 2016/6/14.
 */
public class HmcActivity extends BaseActivity {

    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;//标题栏
    @InjectView(R.id.searchWidget)
    private SearchWidget searchWidget;//搜索

    @InjectView(R.id.listView)
    private ListView listView;//员工列表
    private List<Employee> dataLists;
    private List<Employee> dataBaseLists;
    private List<Employee> dataSearchLists;

    private HmcListAdapter listAdapter;

    public static final String MEMBER_ID = "member_id";
    private String memberId = "";
    public static final String IS_CONTACT = "isContact";
    public static final String IS_ORDERED = "isOrdered";

    private UpdateReceiver receiver;
    private boolean isContact;
    private boolean isOrdered;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_hmc);

        receiver = new UpdateReceiver() {
            @Override
            public void aliResult() {
                initDatas();
            }
        };
        receiver.register();
        initWidgets();
    }

    private void initWidgets(){
        memberId = getIntent().getStringExtra(MEMBER_ID);
        if (Validators.isEmpty(memberId)){
            memberId = "";
        }
        titleLayout.configTitle("花名册").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        isContact = getIntent().getBooleanExtra(IS_CONTACT , false);
        isOrdered = getIntent().getBooleanExtra(IS_ORDERED , false);
        if (!isContact && !isOrdered){
            titleLayout.configRightText("添加", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HmcActivity.this,AddKhActivity.class);
                    intent.putExtra(AddKhActivity.MEMBERID ,  memberId);
                    startActivity(intent);
                }
            });
        }

        //搜索
        searchWidget.sethintDes("搜索");
        searchWidget.configSearchCallback(new SearchCallback() {
            @Override
            public void search(String content) {
                dataSearchLists.clear();
                if (content.length()>0){
                    for (Employee employee : dataBaseLists){
                        if (employee.getName().contains(content) || employee.getShopName().contains(content)){
                            dataSearchLists.add(employee);
                        }
                    }

                    dataLists.clear();
                    dataLists.addAll(dataSearchLists);
                    listAdapter.notifyDataSetChanged(dataLists);
                }else {
                    dataLists.clear();
                    dataLists.addAll(dataBaseLists);
                    listAdapter.notifyDataSetChanged(dataLists);
                }
            }
        });

        dataLists = new ArrayList<>();
        dataBaseLists = new ArrayList<>();
        dataSearchLists = new ArrayList<>();
        listAdapter = new HmcListAdapter(HmcActivity.this, dataLists, new HmcItemClickListner() {
            @Override
            public void click(Employee employee) {
                if (isContact) {
                  //  ChatManager.getInstance().startConversion(HmcActivity.this, employee.getMemberId());
                    RongIM.getInstance().startPrivateChat(HmcActivity.this,employee.getId(), employee.getName());
                } else if (isOrdered) {
                    String RTS="ywy";
                    Intent intent = new Intent(HmcActivity.this, ShopDetailActivity.class);
                    intent.putExtra(ShopDetailActivity.PARAM_SHOPID, LoginedUser.getLoginedUser().getShopId());
                    intent.putExtra(ShopDetailActivity.PARAM_MEMBERID, employee.getMemberId());
                    intent.putExtra(ShopDetailActivity.OOOM,RTS);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(HmcActivity.this, KhInfoActivity.class);
                    intent.putExtra("id", employee.getId());
                    intent.putExtra("memberId", employee.getMemberId());
                    startActivity(intent);
                }
            }
        }, new HmcLongClickListener() {
            @Override
            public void longClick(final Employee employee) {
                DialogUtil.confirm(HmcActivity.this, "确定删除客户", "取消" , new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }, "删除", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DeleteCustomerTask task = new DeleteCustomerTask(HmcActivity.this);
                        task.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
                            @Override
                            public void successCallback(Result<NoResultData> result) {
                                initDatas();
                            }
                        });

                        task.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
                            @Override
                            public void failCallback(Result<NoResultData> result) {
                                ToastUtil.toast(result.getMessage());
                            }
                        });

                        task.execute(employee.getId());
                    }
                });
            }
        });
        listView.setAdapter(listAdapter);

        initDatas();
    }

    private void initDatas(){
        dataLists.clear();
        dataBaseLists.clear();
        dataSearchLists.clear();

        CustomerListTask customerListTask = new CustomerListTask(HmcActivity.this);
        customerListTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<CustomerListData>() {
            @Override
            public void successCallback(Result<CustomerListData> result) {
                dataLists.addAll(result.getValue().getList());
                dataBaseLists.addAll(dataLists);
                listAdapter.notifyDataSetChanged(dataLists);
            }
        });

        customerListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<CustomerListData>() {
            @Override
            public void failCallback(Result<CustomerListData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        customerListTask.execute(memberId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        receiver.unRegister();
    }
}