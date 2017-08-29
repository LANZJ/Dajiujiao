package com.zjyeshi.dajiujiao.buyer.task.order;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.order.PathResData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/12/15.
 */
public class GetOrderPathTask extends BaseTask<PathResData> {
    public GetOrderPathTask(Context context) {
        super(context);
    }

    @Override
    protected Result<PathResData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("orderId" , (String)params[0]);

        Result<PathResData> result = postCommon(UrlConstants.GETORDERPATH , paramMap);
        if (result.isSuccess()){
            PathResData retData = JSON.parseObject(result.getMessage() , PathResData.class);
            result.setMessage(retData.getMessage());
            if (retData.codeOk()){
                result.setValue(retData.getResult());
            }else{
                result.setSuccess(false);
            }
        }
        return result;
    }

    public static void showOrderPath(Context context , String orderId , AsyncTaskSuccessCallback<PathResData> successCallback){
        GetOrderPathTask getOrderPathTask = new GetOrderPathTask(context);
        getOrderPathTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<PathResData>() {
            @Override
            public void failCallback(Result<PathResData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });
        getOrderPathTask.setAsyncTaskSuccessCallback(successCallback);

        getOrderPathTask.execute(orderId);
    }
}
