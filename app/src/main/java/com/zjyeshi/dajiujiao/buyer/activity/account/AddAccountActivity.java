package com.zjyeshi.dajiujiao.buyer.activity.account;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigdog.lib.dialog.DGSingleSelectDialog;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.task.account.AddCashAccountTask;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

/**
 * Created by wuhk on 2016/7/4.
 */
public class AddAccountActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.selectLayout)
    private RelativeLayout selectLayout;
    @InjectView(R.id.accountTv)
    private TextView accountTv;

    @InjectView(R.id.aliLayout)
    private LinearLayout aliLayout;
    @InjectView(R.id.aliAccountEt)
    private EditText aliAccountEt;
    @InjectView(R.id.aliNameEt)
    private EditText aliNameEt;

    @InjectView(R.id.bankLayout)
    private LinearLayout bankLayout;
    @InjectView(R.id.bankCardEt)
    private EditText bankCardEt;
    @InjectView(R.id.bankNameEt)
    private EditText bankNameEt;
    @InjectView(R.id.bankPeopleEt)
    private EditText bankPeopleEt;


    private String id = "";
    private int type;
    private String number;
    private String name;
    private String bankName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_account);
        initWidgets();
    }

    private void initWidgets(){
        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle("添加账号").configRightText("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 0){
                    ToastUtil.toast("请先填写账户信息");
                }else if (type == 1){
                    number = aliAccountEt.getText().toString();
                    name = aliNameEt.getText().toString();
                }else if (type == 2){
                    number = bankCardEt.getText().toString();
                    name = bankPeopleEt.getText().toString();
                    bankName = bankNameEt.getText().toString();
                }
                addCashAccount();
            }
        });

        selectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DGSingleSelectDialog d = new DGSingleSelectDialog.Builder(AddAccountActivity.this).setItemTextAndOnClickListener(new String[]{"支付宝", "银行"}, new View.OnClickListener[]{new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //选择支付宝账户
                        aliLayout.setVisibility(View.VISIBLE);
                        bankLayout.setVisibility(View.GONE);
                        accountTv.setText("支付宝");
                        type = 1;
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //选择银行账户
                        aliLayout.setVisibility(View.GONE);
                        bankLayout.setVisibility(View.VISIBLE);
                        accountTv.setText("银行");
                        type = 2;
                    }
                }}).create();
                d.show();
            }
        });
    }


    private void addCashAccount(){
        AddCashAccountTask addCashAccountTask = new AddCashAccountTask(AddAccountActivity.this);
        addCashAccountTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        addCashAccountTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                AccountSelectActivity.isReload = true;
                finish();
            }
        });

        addCashAccountTask.execute(id , String.valueOf(type) , number , name , bankName);
    }
}
