package com.zjyeshi.dajiujiao.buyer.activity.my.personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.task.my.GetAddressListTask;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.my.AddressListadapter;
import com.zjyeshi.dajiujiao.buyer.common.PassConstans;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.my.Address;
import com.zjyeshi.dajiujiao.buyer.receiver.info.RefreshAddressListReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.my.GetAddressListData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的地址
 * Created by wuhk on 2015/11/12.
 */
public class MyAddressActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;

    @InjectView(R.id.editLayout)
    private RelativeLayout editLayout;

    @InjectView(R.id.listView)
    private ListView listView;

    private AddressListadapter addressListadapter;
    private List<Address> addressList = new ArrayList<Address>();

    private RefreshAddressListReceiver refreshAddressListReceiver;
    private boolean isSelect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_my_address);
        refreshAddressListReceiver = new RefreshAddressListReceiver() {
            @Override
            public void refresh() {
                getAddressList();
                addressListadapter.notifyDataSetChanged();
            }
        };
        refreshAddressListReceiver.register();
        initWidgets();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        refreshAddressListReceiver.unRegister();
    }

    private void initWidgets(){
        titleLayout.configReturn("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle("我的地址");
        editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyAddressActivity.this, EditAddressActivity.class));
            }
        });
        getAddressList();
        String temp = getIntent().getStringExtra(PassConstans.ISSELECTADD);
        if (temp.equals("Yes")){
            isSelect = true;
        }else{
            isSelect = false;
        }
        addressListadapter = new AddressListadapter(MyAddressActivity.this , addressList , isSelect);
        listView.setAdapter(addressListadapter);
    }

    //获取地址列表数据
    private void getAddressList(){
        addressList.clear();
        //首先判断数据库中是否有数据，若无，则从服务器上取，若有，则使用本地数据
        List<Address> temp = DaoFactory.getAddressDao().findAll(LoginedUser.getLoginedUser().getId());
        if (Validators.isEmpty(temp)){
            getFromServer();
        }else{
            addressList.addAll(temp);
        }
    }

    //从服务器获取数据
    private void getFromServer(){
        GetAddressListTask getAddressListTask = new GetAddressListTask(MyAddressActivity.this);
        getAddressListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<GetAddressListData>() {
            @Override
            public void failCallback(Result<GetAddressListData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        getAddressListTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<GetAddressListData>() {
            @Override
            public void successCallback(Result<GetAddressListData> result) {
                List<Address> newList = result.getValue().getList();
                addressList.addAll(newList);
                for (Address address : addressList) {
                    address.setSelected(2);
                }
                addressListadapter.notifyDataSetChanged();
                DaoFactory.getAddressDao().insertBatch(addressList, LoginedUser.getLoginedUser().getId());
            }
        });

        getAddressListTask.execute();
    }



}
