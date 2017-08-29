package com.zjyeshi.dajiujiao.buyer.task;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.task.data.UserData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/4/6.
 */
public class GetUserInfoTask extends BaseTask<UserData> {
    public GetUserInfoTask(Context context) {
        super(context);
    }

    @Override
    protected Result<UserData> onHttpRequest(Object... params) {
        HashMap<String, String> paramMap = new HashMap<String, String>();
        String memberId = (String) params[0];
        paramMap.put("memberId", memberId);

        Result<UserData> result = postCommon(UrlConstants.GETUSERINFO, paramMap);
        if (result.isSuccess()) {
            UserData retData = JSON.parseObject(result.getMessage(), UserData.class);
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
     * 获取用户信息
     *
     * @param context
     * @param memberId
     * @param successCallback
     */
    public static void getUserTodo(Context context, String memberId, AsyncTaskSuccessCallback<UserData> successCallback) {
        GetUserInfoTask getUserInfoTask = new GetUserInfoTask(context);
        getUserInfoTask.setShowProgressDialog(false);
        getUserInfoTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<UserData>() {
            @Override
            public void failCallback(Result<UserData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        getUserInfoTask.setAsyncTaskSuccessCallback(successCallback);

        getUserInfoTask.execute(memberId);

    }
}
