package com.zjyeshi.dajiujiao.buyer.task.sales;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.sales.data.RebackDetailData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.HashMap;

/**
 * Created by wuhk on 2017/5/17.
 */

public class GetRebackDetailTask extends BaseTask<RebackDetailData> {
    public GetRebackDetailTask(Context context) {
        super(context);
    }

    @Override
    protected Result<RebackDetailData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("rebackId" , (String)params[0]);

        Result<RebackDetailData> result = postCommon(UrlConstants.REBACKDETAIL , paramMap);
        if (result.isSuccess()){
            RebackDetailData retData = JSON.parseObject(result.getMessage() , RebackDetailData.class);
            result.setMessage(retData.getMessage());
            if(retData.codeOk()){
                result.setValue(retData.getResult());
            }else{
                result.setSuccess(false);
            }
        }
        return result;
    }


    public static void getRebackDetail(Context context , String rebackId , AsyncTaskSuccessCallback<RebackDetailData> successCallback){
        GetRebackDetailTask getRebackDetailTask = new GetRebackDetailTask(context);
        getRebackDetailTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<RebackDetailData>() {
            @Override
            public void failCallback(Result<RebackDetailData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        getRebackDetailTask.setAsyncTaskSuccessCallback(successCallback);

        getRebackDetailTask.execute(rebackId);

    }
}
