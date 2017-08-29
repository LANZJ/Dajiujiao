package com.zjyeshi.dajiujiao.buyer.task.sales;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.sales.data.GiftListData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.HashMap;

/**
 * Created by wuhk on 2017/4/26.
 */

public class GetGiftListTask extends BaseTask<GiftListData> {
    public GetGiftListTask(Context context) {
        super(context);
    }

    @Override
    protected Result<GiftListData> onHttpRequest(Object... params) {
        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("shopId", (String) params[0]);

        Result<GiftListData> result = postCommon(UrlConstants.GIFTLIST, paramMap);
        if (result.isSuccess()) {
            GiftListData retData = JSON.parseObject(result.getMessage(), GiftListData.class);
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
     * 获取礼品列表
     *
     * @param context
     * @param shopId
     * @param successCallback
     */
    public static void getGiftList(Context context, String shopId, AsyncTaskSuccessCallback<GiftListData> successCallback) {
        GetGiftListTask getGiftListTask = new GetGiftListTask(context);
        getGiftListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<GiftListData>() {
            @Override
            public void failCallback(Result<GiftListData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        getGiftListTask.setAsyncTaskSuccessCallback(successCallback);

        getGiftListTask.execute(shopId);
    }
}
