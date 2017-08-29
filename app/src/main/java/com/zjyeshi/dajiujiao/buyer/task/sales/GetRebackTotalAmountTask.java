package com.zjyeshi.dajiujiao.buyer.task.sales;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.sales.data.RebackMoney;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.HashMap;

/**
 * Created by wuhk on 2017/5/14.
 */

public class GetRebackTotalAmountTask extends BaseTask<RebackMoney> {

    public GetRebackTotalAmountTask(Context context) {
        super(context);
    }

    @Override
    protected Result<RebackMoney> onHttpRequest(Object... params) {
        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("orderId", (String) params[0]);
        paramMap.put("orderProductIds", (String) params[1]);
        paramMap.put("nums", (String) params[2]);
        paramMap.put("orderMarketCostProductIds" , (String)params[3]);
        paramMap.put("marketCostNums" , (String)params[4]);
        paramMap.put("type", (String) params[5]);

        Result<RebackMoney> result = postCommon(UrlConstants.REBACKTOTALMONEY, paramMap);
        if (result.isSuccess()) {
            RebackMoney retData = JSON.parseObject(result.getMessage(), RebackMoney.class);
            result.setMessage(retData.getMessage());
            if (retData.codeOk()) {
                result.setValue(retData.getResult());
            } else {
                result.setSuccess(false);
            }
        }

        return result;
    }

    public static void getRebackTotalMoney(Context context, String orderId, String ids, String nums, String orderMarketCostProductIds ,
            String marketCostNums , String type, AsyncTaskSuccessCallback<RebackMoney> successCallback) {
        GetRebackTotalAmountTask getRebackTotalAmountTask = new GetRebackTotalAmountTask(context);
        getRebackTotalAmountTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<RebackMoney>() {
            @Override
            public void failCallback(Result<RebackMoney> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        getRebackTotalAmountTask.setAsyncTaskSuccessCallback(successCallback);

        getRebackTotalAmountTask.execute(orderId , ids , nums , orderMarketCostProductIds , marketCostNums , type);


    }
}
