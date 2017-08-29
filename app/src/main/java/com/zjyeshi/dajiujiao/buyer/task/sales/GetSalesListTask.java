package com.zjyeshi.dajiujiao.buyer.task.sales;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.sales.data.SalesListData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.HashMap;

/**
 * 获取优惠活动
 * Created by wuhk on 2017/4/26.
 */

public class GetSalesListTask extends BaseTask<SalesListData> {
    public GetSalesListTask(Context context) {
        super(context);
    }

    @Override
    protected Result<SalesListData> onHttpRequest(Object... params) {
        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("shopId", (String) params[0]);

        Result<SalesListData> result = postCommon(UrlConstants.GETSALESLIST, paramMap);
        if (result.isSuccess()) {
            SalesListData retData = JSON.parseObject(result.getMessage(), SalesListData.class);
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
     * 获取优惠活动列表
     *
     * @param context
     * @param shopId
     * @param successCallback
     */
    public static void getSalesList(Context context, String shopId, AsyncTaskSuccessCallback<SalesListData> successCallback) {
        GetSalesListTask getSalesListTask = new GetSalesListTask(context);
        getSalesListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<SalesListData>() {
            @Override
            public void failCallback(Result<SalesListData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        getSalesListTask.setAsyncTaskSuccessCallback(successCallback);

        getSalesListTask.execute(shopId);
    }
}
