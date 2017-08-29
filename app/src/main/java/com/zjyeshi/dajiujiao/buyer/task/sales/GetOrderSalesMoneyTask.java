package com.zjyeshi.dajiujiao.buyer.task.sales;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.sales.data.OrderSalesMoneyData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.HashMap;

/**
 * Created by wuhk on 2017/5/19.
 */

public class GetOrderSalesMoneyTask extends BaseTask<OrderSalesMoneyData> {

    public GetOrderSalesMoneyTask(Context context) {
        super(context);
    }

    @Override
    protected Result<OrderSalesMoneyData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("orderId" , (String)params[0]);
       // paramMap.put("orderPreferentialActivities",(String)params[1]);
        Result<OrderSalesMoneyData> result = postCommon(UrlConstants.GETORDERSALESMONEY , paramMap);

        if (result.isSuccess()){
            OrderSalesMoneyData retData = JSON.parseObject(result.getMessage() , OrderSalesMoneyData.class);
            result.setMessage(retData.getMessage());
            if (retData.codeOk()){
                result.setValue(retData.getResult());
            }else{
                result.setSuccess(false);
            }
        }
        return result;
    }


    public static void getOrderSalesMoney(Context context , String orderId , AsyncTaskSuccessCallback<OrderSalesMoneyData> successCallback){

        GetOrderSalesMoneyTask getOrderSalesMoneyTask = new GetOrderSalesMoneyTask(context);
        getOrderSalesMoneyTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<OrderSalesMoneyData>() {
            @Override
            public void failCallback(Result<OrderSalesMoneyData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        getOrderSalesMoneyTask.setAsyncTaskSuccessCallback(successCallback);

        getOrderSalesMoneyTask.execute(orderId);
    }
}
