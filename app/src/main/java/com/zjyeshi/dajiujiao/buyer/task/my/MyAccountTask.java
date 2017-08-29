package com.zjyeshi.dajiujiao.buyer.task.my;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigapple.lib.utils.sharepreference.BPPreferences;
import com.zjyeshi.dajiujiao.buyer.common.PreferenceConstants;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.pay.Wallet;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/7/1.
 */
public class MyAccountTask extends BaseTask<Wallet> {
    public MyAccountTask(Context context) {
        super(context);
    }

    @Override
    protected Result<Wallet> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap =  new HashMap<String, String>();
       if (params.length == 1){
            paramMap.put("memberId" , (String)params[0]);
       }
        Result<Wallet> result = postCommon(UrlConstants.MYACCOUNT , paramMap);
        if (result.isSuccess()){
            Wallet retData = JSON.parseObject(result.getMessage(), Wallet.class);
            result.setMessage(retData.getMessage());
            if (retData.codeOk()){
                result.setValue(retData.getResult());
                float walletMarketPrice = Float.valueOf(result.getValue().getMarketCost()) / 100;
                //保存钱包市场支持费用
                BPPreferences.instance().putFloat(PreferenceConstants.MY_ACCOUNT_MARKET , walletMarketPrice);
            }else{
                result.setSuccess(false);
            }
        }
        return result;
    }

    public static void getMyAccountWhenBuy(Context context , String memberId , AsyncTaskSuccessCallback<Wallet> successCallback){
        MyAccountTask myAccountTask = new MyAccountTask(context);
        myAccountTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<Wallet>() {
            @Override
            public void failCallback(Result<Wallet> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        myAccountTask.setAsyncTaskSuccessCallback(successCallback);

        if (Validators.isEmpty(memberId)){
            myAccountTask.execute();
        }else{
            myAccountTask.execute(memberId);
        }
    }
}
