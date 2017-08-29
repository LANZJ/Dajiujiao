package com.zjyeshi.dajiujiao.buyer.task.sales;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.sales.data.RebackListData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.HashMap;

/**
 * Created by wuhk on 2017/5/14.
 */

public class GetRebackListTask extends BaseTask<RebackListData> {
    public static final int NO_DEAL = 1;
    public static final int DEALED  = 2;

    public GetRebackListTask(Context context) {
        super(context);
    }

    @Override
    protected Result<RebackListData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("shopId" , (String)params[0]);
        paramMap.put("reviewStatus" , (String)params[1]);

        Result<RebackListData> result = postCommon(UrlConstants.REBACLIST , paramMap);

        if (result.isSuccess()){
            RebackListData retData = JSON.parseObject(result.getMessage() , RebackListData.class);
            result.setMessage(retData.getMessage());
            if (retData.codeOk()){
                result.setValue(retData.getResult());
            }else{
                result.setSuccess(false);
            }
        }
        return result;
    }

    public static void getRebackList(Context context , String shopId , int status , AsyncTaskSuccessCallback<RebackListData> successCallback){
        GetRebackListTask getRebackListTask = new GetRebackListTask(context);
        getRebackListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<RebackListData>() {
            @Override
            public void failCallback(Result<RebackListData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        getRebackListTask.setAsyncTaskSuccessCallback(successCallback);

        getRebackListTask.execute(shopId , String.valueOf(status));
    }
}
