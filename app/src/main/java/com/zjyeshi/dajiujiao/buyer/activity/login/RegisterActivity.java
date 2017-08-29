package com.zjyeshi.dajiujiao.buyer.activity.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.task.data.login.RegisterData;
import com.zjyeshi.dajiujiao.buyer.task.login.GetSmsCodeTask;
import com.zjyeshi.dajiujiao.buyer.task.login.RegisterTask;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;

/**
 * 注册
 * Created by wuhk on 2015/9/28.
 */
public class RegisterActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;

    @InjectView(R.id.phoneEt)
    private EditText phoneEt;

    @InjectView(R.id.phoneDel)
    private ImageView phoneDel;

    @InjectView(R.id.codeEt)
    private EditText codeEt;

    @InjectView(R.id.codeBtn)
    private Button codeBtn;

    @InjectView(R.id.setCodeEt)
    private EditText setCodeEt;

    @InjectView(R.id.registerBtn)
    private Button registerBtn;

    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        initWidgets();
    }

    private void initWidgets() {
        titleLayout.configReturn("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleLayout.configTitle("注册");
        //手机号有内容时显示清除按钮
        phoneEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (phoneEt.getText().length() > 0) {
                    phoneDel.setVisibility(View.VISIBLE);
                    if (phoneEt.getText().length() >= 11) {
                        codeBtn.setTextColor(Color.WHITE);
                        codeBtn.setBackgroundResource(R.drawable.btn_click);
                    } else {
                        codeBtn.setTextColor(Color.argb(255, 110, 110, 110));
                        codeBtn.setBackgroundResource(R.drawable.border_shape);
                    }
                } else {
                    phoneDel.setVisibility(View.GONE);
                }
            }
        });
        //清除手机号内容
        phoneDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneEt.setText("");
            }
        });
        //获取验证码点击事件
        codeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = phoneEt.getText().toString();
                if (phone.length() == 11) {
                    GetSmsCodeTask getSmsCodeTask = new GetSmsCodeTask(RegisterActivity.this);

                    getSmsCodeTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
                        @Override
                        public void failCallback(Result<NoResultData> result) {
                            ToastUtil.toast(result.getMessage());
                        }
                    });

                    getSmsCodeTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
                        @Override
                        public void successCallback(Result<NoResultData> result) {
//                            ToastUtil.toast("验证码获取成功 1234");
                        }
                    });

                    getSmsCodeTask.execute(phone);
                }
            }
        });

        //监听设置密码改变按钮的背景
        setCodeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (setCodeEt.getText().length() > 0) {
                    registerBtn.setBackgroundResource(R.drawable.can_select_shape);
                } else {
                    registerBtn.setBackgroundResource(R.drawable.login_btn_shape);
                }
            }
        });
        //立即注册
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = setCodeEt.getText().toString();
                String smsCode = codeEt.getText().toString();
                if (password.length() > 0) {
                    RegisterTask registerTask = new RegisterTask(RegisterActivity.this);

                    registerTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<RegisterData>() {
                        @Override
                        public void failCallback(Result<RegisterData> result) {
                            ToastUtil.toast(result.getMessage());
                        }
                    });

                    registerTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<RegisterData>() {
                        @Override
                        public void successCallback(Result<RegisterData> result) {
                            ToastUtil.toast("注册成功");
                            Intent intent = new Intent();
                            intent.putExtra(LoginActivity.PARAM_PHONE , phone);
                            intent.setClass(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                    registerTask.execute(phone, password, smsCode);
                }
            }
        });
    }
}
