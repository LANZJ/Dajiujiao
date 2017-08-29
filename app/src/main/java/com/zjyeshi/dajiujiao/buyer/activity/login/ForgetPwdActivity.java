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

import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.login.FindPwdTask;
import com.zjyeshi.dajiujiao.buyer.task.login.GetSmsCodeTask;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;

/**
 * 忘记密码
 * Created by wuhk on 2015/9/28.
 */
public class ForgetPwdActivity extends BaseActivity {
    @InjectView(R.id.phoneEt)
    private EditText phoneEt;

    @InjectView(R.id.phoneDel)
    private ImageView phoneDel;

    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;

    @InjectView(R.id.codeBtn)
    private Button codeBtn;

    @InjectView(R.id.codeEt)
    private EditText codeEt;

    @InjectView(R.id.inputNewPwdEt)
    private EditText inputNewPwdEt;

    @InjectView(R.id.inputNewPwdAgainEt)
    private EditText inputNewPwdAgainEt;

    @InjectView(R.id.findBtn)
    private Button fingBtn;

    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_forget_pwd);
        initWidgets();
    }

    private void initWidgets() {
        titleLayout.configReturn("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleLayout.configTitle("忘记密码");

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
        //获取短信验证码
        codeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = phoneEt.getText().toString();
                if (phone.length() == 11) {
                    GetSmsCodeTask getSmsCodeTask = new GetSmsCodeTask(ForgetPwdActivity.this);

                    getSmsCodeTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
                        @Override
                        public void failCallback(Result<NoResultData> result) {
                            ToastUtil.toast(result.getMessage());
                        }
                    });

                    getSmsCodeTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
                        @Override
                        public void successCallback(Result<NoResultData> result) {
                        }
                    });

                    getSmsCodeTask.execute(phone);
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

        //监听重新输入新密码，改变button背景
        inputNewPwdAgainEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (inputNewPwdEt.getText().length() == 0) {
                    ToastUtil.toast("请先输入新密码");
                } else if (inputNewPwdAgainEt.getText().length() > 0) {
                    fingBtn.setBackgroundResource(R.drawable.can_select_shape);
                } else {
                    fingBtn.setBackgroundResource(R.drawable.login_btn_shape);
                }
            }
        });
        //找回密码
        fingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String smsCode = codeEt.getText().toString();
                if (inputNewPwdAgainEt.getText().length() > 0) {
                    String first = inputNewPwdEt.getText().toString();
                    String second = inputNewPwdAgainEt.getText().toString();

                    if (first.equals(second)) {
                        ToastUtil.toast("修改成功");
                        final FindPwdTask findPwdTask = new FindPwdTask(ForgetPwdActivity.this);
                        findPwdTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
                            @Override
                            public void failCallback(Result<NoResultData> result) {
                                ToastUtil.toast(result.getMessage());
                            }
                        });

                        findPwdTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
                            @Override
                            public void successCallback(Result<NoResultData> result) {
                                Intent intent = new Intent();
                                intent.setClass(ForgetPwdActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        findPwdTask.execute(phone, second, smsCode);
                    } else {
                        ToastUtil.toast("两次密码不一致");
                    }
                }
            }
        });


    }
}
