package com.zjyeshi.dajiujiao.buyer.task.store;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.data.store.sort.SortList;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.HashMap;

/**
 * 商品分类列表
 * Created by wuhk on 2015/10/28.
 */
public class SortListTask extends BaseTask<SortList> {
    public SortListTask(Context context) {
        super(context);
    }

    @Override
    protected Result<SortList> onHttpRequest(Object... params) {
        HashMap<String, String> paramMap = new HashMap<String, String>();
        if (!Validators.isEmpty(params)) {
            paramMap.put("shopId", (String) params[0]);
        }

        Result<SortList> result = post(UrlConstants.CATEGORIES, paramMap);

        if (result.isSuccess()) {
            SortList retData = JSON.parseObject(result.getMessage(), SortList.class);
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
     * 获取商品分类列表
     *
     * @param context
     * @param shopId
     * @param successCallback
     */
    public static void getGoodSortList(Context context, String shopId, AsyncTaskSuccessCallback<SortList> successCallback) {
        SortListTask sortListTask = new SortListTask(context);
        sortListTask.setShowProgressDialog(true);
        sortListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<SortList>() {
            @Override
            public void failCallback(Result<SortList> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        sortListTask.setAsyncTaskSuccessCallback(successCallback);
        sortListTask.execute(shopId);

    }
}
