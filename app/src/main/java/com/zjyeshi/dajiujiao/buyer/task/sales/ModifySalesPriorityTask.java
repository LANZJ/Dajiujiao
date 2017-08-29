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
 * Created by wuhk on 2017/5/9.
 */

public class ModifySalesPriorityTask extends BaseTask<NoResultData> {
    public ModifySalesPriorityTask(Context context) {
        super(context);
    }

    @Override
    protected Result<NoResultData> onHttpRequest(Object... params) {
        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("preferentialActivityId", (String) params[0]);
        paramMap.put("priority", (String) params[1]);

        Result<NoResultData> result = postCommon(UrlConstants.MODIFYSALESPRIORITY, paramMap);
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
     * 修改店铺优先级
     *
     * @param context
     * @param salesId
     * @param priority
     * @param successCallback
     */
    public static void modifySalesPriority(Context context, String salesId, String priority, AsyncTaskSuccessCallback<NoResultData> successCallback) {
        ModifySalesPriorityTask modifySalesPriorityTask = new ModifySalesPriorityTask(context);
        modifySalesPriorityTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        modifySalesPriorityTask.setAsyncTaskSuccessCallback(successCallback);

        modifySalesPriorityTask.execute(salesId, priority);
    }
}
