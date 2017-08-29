package com.zjyeshi.dajiujiao.buyer.task.sales;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.sales.data.OrderSalesListData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.HashMap;

/**
 * Created by wuhk on 2017/5/18.
 */

public class GetOrderSalesListTask extends BaseTask<OrderSalesListData> {
    public GetOrderSalesListTask(Context context) {
        super(context);
    }

    @Override
    protected Result<OrderSalesListData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("orderId" , (String)params[0]);
        paramMap.put("productIds" , (String)params[1]);

        Result<OrderSalesListData> result = postCommon(UrlConstants.ORDERSALESLIST , paramMap);
        if (result.isSuccess()){
            OrderSalesListData retData = JSON.parseObject(result.getMessage() , OrderSalesListData.class);
            result.setMessage(retData.getMessage());
            if (retData.codeOk()){
                result.setValue(retData.getResult());
            }else{
                result.setSuccess(false);
            }
        }
        return result;
    }

    public static void getOrderSalesList(Context context , String orderId , String productIds , AsyncTaskSuccessCallback<OrderSalesListData> successCallback){
        GetOrderSalesListTask getOrderSalesListTask = new GetOrderSalesListTask(context);
        getOrderSalesListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<OrderSalesListData>() {
            @Override
            public void failCallback(Result<OrderSalesListData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        getOrderSalesListTask.setAsyncTaskSuccessCallback(successCallback);

        getOrderSalesListTask.execute(orderId , productIds);
    }
}
