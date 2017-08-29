package com.zjyeshi.dajiujiao.buyer.task.sales;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.sales.data.SalesDetailData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.HashMap;

/**
 * Created by wuhk on 2017/4/27.
 */

public class GetSalesDetailTask extends BaseTask<SalesDetailData> {

    public GetSalesDetailTask(Context context) {
        super(context);
    }

    @Override
    protected Result<SalesDetailData> onHttpRequest(Object... params) {
        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("preferentialActivityId", (String) params[0]);

        Result<SalesDetailData> result = postCommon(UrlConstants.GETSALESDETAIL, paramMap);
        if (result.isSuccess()) {
            SalesDetailData retData = JSON.parseObject(result.getMessage(), SalesDetailData.class);
            result.setMessage(retData.getMessage());
            if (retData.codeOk()) {
                result.setValue(retData.getResult());
            } else {
                result.setSuccess(false);
            }
        }
        return result;
    }

    /**
     * 获取店铺详情
     *
     * @param context
     * @param id
     * @param successCallback
     */
    public static void getSalesDetail(Context context, String id, AsyncTaskSuccessCallback<SalesDetailData> successCallback) {
        GetSalesDetailTask getSalesDetailTask = new GetSalesDetailTask(context);
        getSalesDetailTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<SalesDetailData>() {
            @Override
            public void failCallback(Result<SalesDetailData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        getSalesDetailTask.setAsyncTaskSuccessCallback(successCallback);

        getSalesDetailTask.execute(id);
    }
}
