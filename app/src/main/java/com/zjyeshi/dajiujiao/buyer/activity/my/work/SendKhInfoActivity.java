package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.work.SendPassPortTask;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

/**
 * 发送客户账户密码
 *
 * Created by zhum on 2016/6/17.
 */
public class SendKhInfoActivity extends BaseActivity {

    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;//标题栏
    @InjectView(R.id.usernameTv)
    private TextView usernameTv;

    private String phone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_send_kh_info);

        phone = getIntent().getStringExtra("phone");

        initWidgets();
    }

    private void initWidgets(){
        titleLayout.configTitle("发送账号").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleLayout.configRightText("发送短信", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendPassPortTask task = new SendPassPortTask(SendKhInfoActivity.this);
                task.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
                    @Override
                    public void successCallback(Result<NoResultData> result) {
                        ToastUtil.toast("已发送短信至用户手机");
                        finish();
                    }
                });

                task.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
                    @Override
                    public void failCallback(Result<NoResultData> result) {
                        ToastUtil.toast(result.getMessage());
                    }
                });

                task.execute(phone);
            }
        });

        initTextView(usernameTv,phone);
    }
}