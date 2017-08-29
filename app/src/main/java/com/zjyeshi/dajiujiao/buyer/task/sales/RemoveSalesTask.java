package com.zjyeshi.dajiujiao.buyer.task.sales;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.HashMap;

/**
 * Created by wuhk on 2017/4/27.
 */

public class RemoveSalesTask extends BaseTask<NoResultData> {
    public RemoveSalesTask(Context context) {
        super(context);
    }

    @Override
    protected Result<NoResultData> onHttpRequest(Object... params) {
        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("preferentialActivityId", (String) params[0]);

        Result<NoResultData> result = postCommon(UrlConstants.REMOVESALES, paramMap);
        if (result.isSuccess()) {
            NoResultData retData = JSON.parseObject(result.getMessage(), NoResultData.class);
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
     * 删除活动
     *
     * @param context
     * @param salesId
     * @param successCallback
     */
    public static void removeSales(Context context, String salesId, AsyncTaskSuccessCallback<NoResultData> successCallback) {
        RemoveSalesTask removeSalesTask = new RemoveSalesTask(context);
        removeSalesTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        removeSalesTask.setAsyncTaskSuccessCallback(successCallback);

        removeSalesTask.execute(salesId);

    }
}
