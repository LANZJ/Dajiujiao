package com.zjyeshi.dajiujiao.buyer.task.sales;

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
 * Created by wuhk on 2017/5/23.
 */

public class GetRebackPathTask extends BaseTask<PathResData> {

    public GetRebackPathTask(Context context) {
        super(context);
    }

    @Override
    protected Result<PathResData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("rebackId" , (String)params[0]);

        Result<PathResData> result = postCommon(UrlConstants.GETREBACKPATH , paramMap);
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

    public static void getRebackPath(Context context , String rebackId , AsyncTaskSuccessCallback<PathResData> successCallback){
        GetRebackPathTask getRebackPathTask = new GetRebackPathTask(context);
        getRebackPathTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<PathResData>() {
            @Override
            public void failCallback(Result<PathResData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        getRebackPathTask.setAsyncTaskSuccessCallback(successCallback);

        getRebackPathTask.execute(rebackId);

    }
}
