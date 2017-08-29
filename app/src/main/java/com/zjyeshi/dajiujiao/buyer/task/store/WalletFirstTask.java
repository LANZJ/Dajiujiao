package com.zjyeshi.dajiujiao.buyer.task.store;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.pay.Wallet;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/6/30.
 */
public class WalletFirstTask extends BaseTask<Wallet> {
    public WalletFirstTask(Context context) {
        super(context);
    }

    @Override
    protected Result<Wallet> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("orderId" , (String)params[0]);
        paramMap.put("addressId" , (String)params[1]);

        Result<Wallet> result = postCommon(UrlConstants.WALLETDEDUCTION , paramMap);
        if (result.isSuccess()){
            Wallet retData = JSON.parseObject(result.getMessage() , Wallet.class);
            result.setMessage(retData.getMessage());
            if (retData.codeOk()){
                result.setValue(retData.getResult());
            }else{
                result.setSuccess(false);
            }
        }
        return result;
    }
}
