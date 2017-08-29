package com.zjyeshi.dajiujiao.buyer.activity.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigapple.lib.utils.sharepreference.BPPreferences;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.task.account.GetCashAccountListTask;
import com.zjyeshi.dajiujiao.buyer.task.account.data.CashAccountList;
import com.zjyeshi.dajiujiao.buyer.task.data.pay.Wallet;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.task.account.CashApplyTask;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.my.MyAccountTask;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

/**
 * 申请提现
 * Created by wuhk on 2016/7/4.
 */
public class ApplyAccountActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.changeApplyLayout)
    private LinearLayout changeApplyLayout;
    @InjectView(R.id.iconIv)
    private ImageView iconIv;
    @InjectView(R.id.nameTv)
    private TextView nameTv;
    @InjectView(R.id.bankNameTv)
    private TextView bankNameTv;
    @InjectView(R.id.accountTv)
    private TextView accountTv;
    @InjectView(R.id.applyMoneyEt)
    private EditText applyMoneyEt;
    @InjectView(R.id.canApplyTv)
    private TextView canApplyTv;
    @InjectView(R.id.sureBtn)
    private Button sureBtn;
    @InjectView(R.id.clickDesTv)
    private TextView cliclDesTv;
    //上传参数
    private String cashAccountId;
    private String type;

    public static boolean isReload = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_apply_account);
        initWidgets();
    }

    private void initWidgets(){
        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle("申请提现").configRightText("提现记录", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ApplyAccountActivity.this , ApplyCashRecordActivity.class);
                startActivity(intent);
            }
        });

        //更换账号
        changeApplyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ApplyAccountActivity.this , AccountSelectActivity.class);
                startActivity(intent);
            }
        });

        //确定
        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String money = applyMoneyEt.getText().toString();
                if (Validators.isEmpty(money)){
                    ToastUtil.toast("提现金额不能为空");
                }else if (!Validators.isNumeric(money)){
                    ToastUtil.toast("提现金额不规范");
                }else{
                    cashApply(money);
                }
            }
        });
        configCanApply();
        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isReload){
            loadData();
        }
    }

    private void loadData(){
        GetCashAccountListTask getCashAccountListTask = new GetCashAccountListTask(ApplyAccountActivity.this);
        getCashAccountListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<CashAccountList>() {
            @Override
            public void failCallback(Result<CashAccountList> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        getCashAccountListTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<CashAccountList>() {
            @Override
            public void successCallback(Result<CashAccountList> result) {
                isReload = false;
                String accountId = BPPreferences.instance().getString("accountId" + LoginedUser.getLoginedUser().getId() , "");
                boolean noAccount = true;
                if (!Validators.isEmpty(accountId)){
                    for(CashAccountList.CashAccount cashAccount: result.getValue().getList()){
                        if (cashAccount.getType() == 1){
                            if (cashAccount.getAlipay().getId().equals(accountId)){
                                iconIv.setImageResource(R.drawable.alipay);
                                nameTv.setText(cashAccount.getAlipay().getAccountName());
                                bankNameTv.setVisibility(View.GONE);
                                accountTv.setText(cashAccount.getAlipay().getName());
                                cashAccountId = cashAccount.getAlipay().getId();
                                type = "1";
                                noAccount = false;
                                break;
                            }
                        }else if (cashAccount.getType() == 2){
                            if (cashAccount.getBank().getId().equals(accountId)){
                                iconIv.setImageResource(R.drawable.bank);
                                nameTv.setText(cashAccount.getBank().getCarNumber());
                                bankNameTv.setVisibility(View.VISIBLE);
                                bankNameTv.setText(cashAccount.getBank().getBankName());
                                accountTv.setText(cashAccount.getBank().getName());
                                cashAccountId = cashAccount.getBank().getId();
                                type = "2";
                                noAccount = false;
                                break;
                            }
                        }
                    }
                }
                if (noAccount){
                    cliclDesTv.setVisibility(View.VISIBLE);
                    Intent intent = new Intent();
                    intent.setClass(ApplyAccountActivity.this , AccountSelectActivity.class);
                    startActivity(intent);
                }else{
                    cliclDesTv.setVisibility(View.GONE);
                }
            }
        });

        getCashAccountListTask.execute();
    }


    private void configCanApply(){
        MyAccountTask myAccountTask = new MyAccountTask(ApplyAccountActivity.this);
        myAccountTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<Wallet>() {
            @Override
            public void failCallback(Result<Wallet> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        myAccountTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<Wallet>() {
            @Override
            public void successCallback(Result<Wallet> result) {
                canApplyTv.setText("可提现金额:" + ExtraUtil.format(Float.parseFloat(result.getValue().getAccount())/100));
            }
        });

        myAccountTask.execute();
    }

    //申请提现
    private void cashApply(String amount){
        float applyMoney = Float.parseFloat(amount) * 100;
        if (applyMoney <= 0){
            ToastUtil.toast("提现金额必须大于0");
        }else{
            CashApplyTask cashApplyTask = new CashApplyTask(ApplyAccountActivity.this);
            cashApplyTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
                @Override
                public void failCallback(Result<NoResultData> result) {
                    ToastUtil.toast(result.getMessage());
                }
            });

            cashApplyTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
                @Override
                public void successCallback(Result<NoResultData> result) {
                    ToastUtil.toast("提现申请已提交");
                    finish();
                }
            });

            cashApplyTask.execute(cashAccountId , ExtraUtil.getUpLoadCount(String.valueOf(applyMoney)), type);
        }
    }
}
