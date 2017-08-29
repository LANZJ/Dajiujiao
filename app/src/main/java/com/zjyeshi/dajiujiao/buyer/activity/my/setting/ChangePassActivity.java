package com.zjyeshi.dajiujiao.buyer.activity.my.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.task.my.ChangePassTask;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

/**
 * 修改密码
 * Created by wuhk on 2016/7/18.
 */
public class ChangePassActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;

    @InjectView(R.id.oldPassEt)
    private EditText oldPassEt;

    @InjectView(R.id.passEt)
    private EditText passEt;

    @InjectView(R.id.repassEt)
    private EditText repassEt;

    @InjectView(R.id.sureBtn)
    private Button sureBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_change_pass);
        initWidgets();
    }

    private void initWidgets(){
        titleLayout.configTitle("修改密码").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPass = oldPassEt.getText().toString();
                String pass = passEt.getText().toString();
                String rePass = repassEt.getText().toString();

                LoginedUser loginedUser = LoginedUser.getLoginedUser();
                if (Validators.isEmpty(oldPass) || Validators.isEmpty(pass) || Validators.isEmpty(rePass)){
                    ToastUtil.toast("请将信息填写完整");
                }else{
                    if (oldPass.equals(loginedUser.getPassword())){
                            if (pass.equals(rePass)){
                                changePass(pass);
                            }else{
                                ToastUtil.toast("两次输入密码不一致");
                            }
                    }
                }
            }
        });
    }

    private void changePass(final String pass){
        ChangePassTask changePassTask = new ChangePassTask(ChangePassActivity.this);
        changePassTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        changePassTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                ToastUtil.toast("密码修改成功");
                //将新密码保存到本地
                LoginedUser.getLoginedUser().setPassword(pass);
                LoginedUser.setLoginedUser(LoginedUser.getLoginedUser());
                finish();
            }
        });

        changePassTask.execute(LoginedUser.getLoginedUser().getPhone() , pass , String.valueOf(LoginedUser.getLoginedUser().getUserEnum().getValue()));
    }



}
