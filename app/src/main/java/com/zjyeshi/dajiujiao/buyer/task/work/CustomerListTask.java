package com.zjyeshi.dajiujiao.buyer.task.work;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.task.work.data.CustomerListData;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.HashMap;

/**
 * 客户花名册
 * <p>
 * Created by zhum on 2016/6/20.
 */
public class CustomerListTask extends BaseTask<CustomerListData> {
    public CustomerListTask(Context context) {
        super(context);
    }

    @Override
    protected Result<CustomerListData> onHttpRequest(Object... params) {
        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("memberId", (String) params[0]);

        Result<CustomerListData> result = postCommon(UrlConstants.CUSTOMERLIST, paramMap);
        if (result.isSuccess()) {
            CustomerListData customerListData = JSON.parseObject(result.getMessage(), CustomerListData.class);
            result.setMessage(customerListData.getMessage());
            if (customerListData.codeOk()) {
                result.setValue(customerListData.getResult());
            } else {
                result.setSuccess(false);
            }
        }
        return result;
    }


    /**
     * 获取客户列表
     *
     * @param context
     * @param memberId
     * @param successCallback
     */
    public static void getCustomer(Context context, String memberId, AsyncTaskSuccessCallback<CustomerListData> successCallback) {
        CustomerListTask customerListTask = new CustomerListTask(context);
        customerListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<CustomerListData>() {
            @Override
            public void failCallback(Result<CustomerListData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        customerListTask.setAsyncTaskSuccessCallback(successCallback);

        customerListTask.execute(memberId);
    }
}
