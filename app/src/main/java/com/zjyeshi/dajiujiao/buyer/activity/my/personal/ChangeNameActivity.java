package com.zjyeshi.dajiujiao.buyer.activity.my.personal;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.circle.receiver.OnlyNotifyReceiver;
import com.zjyeshi.dajiujiao.buyer.circle.task.data.CircleData;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.entity.contact.AddressUser;
import com.zjyeshi.dajiujiao.buyer.task.my.ChangeNameTask;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.receiver.info.ChangeNameReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;

import java.util.List;

/**
 * 修改名字
 * Created by wuhk on 2015/11/11.
 */
public class ChangeNameActivity extends BaseActivity {

    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;

    @InjectView(R.id.nameEt)
    private EditText nameEt;

    @InjectView(R.id.cleanIv)
    private ImageView cleanIv;

    public static final String PARAM_NAME = "param.name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_edit_name);
        initWidgets();
    }

    private void initWidgets() {
        String name = getIntent().getStringExtra(PARAM_NAME);
        titleLayout.configReturn("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle("更改名字").configRightText("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name  = nameEt.getText().toString();
                ChangeNameTask changeNameTask = new ChangeNameTask(ChangeNameActivity.this);
                changeNameTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
                    @Override
                    public void failCallback(Result<NoResultData> result) {
                        ToastUtil.toast(result.getMessage());
                    }
                });
                changeNameTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
                    @Override
                    public void successCallback(Result<NoResultData> result) {
                        ToastUtil.toast("修改名字成功");
                        LoginedUser.getLoginedUser().setName(name);
                        //将朋友圈中的动态名字改掉
                        List<CircleData.Circle> circleList = DaoFactory.getCircleDao().findAll();
                        for (CircleData.Circle circle : circleList){
                            if(circle.getMember().getId().equals(LoginedUser.getLoginedUser().getId())){
                                circle.getMember().setName(name);
                            }
                        }
                        DaoFactory.getCircleDao().replaceIntoBatch(circleList);
                        //将两天消息中的名字改掉
                        AddressUser addressUser = DaoFactory.getAddressUserDao().findUserById(LoginedUser.getLoginedUser().getId() , LoginedUser.getLoginedUser().getId());
                        addressUser.setName(name);
                        DaoFactory.getAddressUserDao().replaceOrInsert(addressUser);
                        //刷新酒友圈中的名字
                        OnlyNotifyReceiver.notifyReceiver();
                        //发起改变名字广播
                        ChangeNameReceiver.notifyReceiver();
                        finish();
                    }
                });

                changeNameTask.execute(name);
            }
        });
        nameEt.setText(name);

        nameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (nameEt.getText().toString().length() > 0) {
                    cleanIv.setVisibility(View.VISIBLE);
                } else {
                    cleanIv.setVisibility(View.GONE);
                }
            }
        });

        cleanIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameEt.setText("");
            }
        });
    }
}
