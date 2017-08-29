package com.zjyeshi.dajiujiao.buyer.task.order;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.order.OrderDetailData;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.HashMap;

/**
 * 获取订单详情
 *
 * Created by wuhk on 2015/11/6.
 */
public class GetOrderDetailTask extends BaseTask<OrderDetailData> {
    public GetOrderDetailTask(Context context) {
        super(context);
    }

    @Override
    protected Result<OrderDetailData> onHttpRequest(Object... params) {
        HashMap<String ,String> paramMap = new HashMap<String , String>();
        paramMap.put("orderId" , (String)params[0]);

        Result<OrderDetailData> result = postCommon(UrlConstants.ORDERDETAILS , paramMap);
        if (result.isSuccess()){
            OrderDetailData retData = JSON.parseObject(result.getMessage(), OrderDetailData.class);
            result.setMessage(retData.getMessage());
            if (retData.codeOk()){
                result.setValue(retData.getResult());
            }else{
                result.setSuccess(false);
            }
        }
        return result;
    }


    public static void getOrderDetail(Context context , String orderId , AsyncTaskSuccessCallback<OrderDetailData> successCallback){
        GetOrderDetailTask getOrderDetailTask = new GetOrderDetailTask(context);
        getOrderDetailTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<OrderDetailData>() {
            @Override
            public void failCallback(Result<OrderDetailData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        getOrderDetailTask.setAsyncTaskSuccessCallback(successCallback);

        getOrderDetailTask.execute(orderId);

    }
}
