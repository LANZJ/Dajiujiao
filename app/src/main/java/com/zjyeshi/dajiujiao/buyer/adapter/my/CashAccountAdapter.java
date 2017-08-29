package com.zjyeshi.dajiujiao.buyer.adapter.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.utils.sharepreference.BPPreferences;
import com.xuan.bigdog.lib.dialog.DGSingleSelectDialog;
import com.zjyeshi.dajiujiao.buyer.activity.account.AccountSelectActivity;
import com.zjyeshi.dajiujiao.buyer.activity.account.ApplyAccountActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.task.account.data.CashAccountList;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.account.AddAccountActivity;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.task.account.RemoveAccountTask;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.List;

/**
 * Created by wuhk on 2016/7/4.
 */
public class CashAccountAdapter extends MBaseAdapter {
    private Context context;
    private List<CashAccountList.CashAccount> dataList;

    public CashAccountAdapter(Context context, List<CashAccountList.CashAccount> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size() + 1;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (position == dataList.size()){
            view = LayoutInflater.from(context).inflate(R.layout.listitem_add_account , null);
        }else{
            view = LayoutInflater.from(context).inflate(R.layout.listitem_change_account , null);
        }

        if(position == dataList.size()){
            RelativeLayout addAccountLayout = (RelativeLayout)view.findViewById(R.id.addAccountLayout);

            addAccountLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //添加账户
                    Intent intent = new Intent();
                    intent.setClass(context , AddAccountActivity.class);
                    context.startActivity(intent);

                }
            });
        }else {
            ImageView iconIv = (ImageView) view.findViewById(R.id.iconIv);
            TextView nameTv = (TextView) view.findViewById(R.id.nameTv);
            TextView accountTv = (TextView) view.findViewById(R.id.accountTv);
            ImageView selectIv = (ImageView) view.findViewById(R.id.selectIv);
            TextView bankNameTv = (TextView) view.findViewById(R.id.bankNameTv);

            final CashAccountList.CashAccount cashAccount = dataList.get(position);
            final String selectId = BPPreferences.instance().getString("accountId" + LoginedUser.getLoginedUser().getId() , "");
            if (cashAccount.getType() == 1) {
                //支付宝
                iconIv.setImageResource(R.drawable.alipay);
                bankNameTv.setVisibility(View.GONE);
                nameTv.setText(cashAccount.getAlipay().getName());
                accountTv.setText(cashAccount.getAlipay().getAccountName());
                if (selectId.equals(cashAccount.getAlipay().getId())){
                    selectIv.setVisibility(View.VISIBLE);
                }else{
                    selectIv.setVisibility(View.GONE);
                }
            } else {
                //银行卡
                iconIv.setImageResource(R.drawable.bank);
                bankNameTv.setVisibility(View.VISIBLE);
                bankNameTv.setText("(" + cashAccount.getBank().getBankName() + ")");
                nameTv.setText(cashAccount.getBank().getName());
                accountTv.setText(cashAccount.getBank().getCarNumber());
                if (selectId.equals(cashAccount.getBank().getId())){
                    selectIv.setVisibility(View.VISIBLE);
                }else{
                    selectIv.setVisibility(View.GONE);
                }
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cashAccount.getType() == 1){
                        BPPreferences.instance().putString("accountId" + LoginedUser.getLoginedUser().getId() , cashAccount.getAlipay().getId());
                    }else{
                        BPPreferences.instance().putString("accountId" + LoginedUser.getLoginedUser().getId() , cashAccount.getBank().getId());
                    }
                    notifyDataSetChanged();
                    ApplyAccountActivity.isReload = true;
                    ((Activity)context).finish();
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    if (cashAccount.getType() == 1){
                        if (!cashAccount.getAlipay().getId().equals(selectId)){
                            DGSingleSelectDialog d = new DGSingleSelectDialog.Builder(context).setItemTextAndOnClickListener(new String[]{"删除该账号"}, new View.OnClickListener[]{new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    removeAccount(cashAccount.getAlipay().getId());
                                }
                            }}).create();
                            d.show();
                        }
                    }else{
                        if (!cashAccount.getBank().getId().equals(selectId)){
                            DGSingleSelectDialog d = new DGSingleSelectDialog.Builder(context).setItemTextAndOnClickListener(new String[]{"删除该账号"}, new View.OnClickListener[]{new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    removeAccount(cashAccount.getBank().getId());
                                }
                            }}).create();
                            d.show();
                        }
                    }

                    return true;
                }
            });
        }
        return view;
    }

    private void removeAccount(String id){
        RemoveAccountTask removeAccountTask = new RemoveAccountTask(context);
        removeAccountTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        removeAccountTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
                ((AccountSelectActivity)context).loadData();
            }
        });

        removeAccountTask.execute(id);
    }
}
