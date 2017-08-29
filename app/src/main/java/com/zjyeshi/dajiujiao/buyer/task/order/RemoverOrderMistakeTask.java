package com.zjyeshi.dajiujiao.buyer.task.order;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.HashMap;

/**
 * 业务员代下单下错删除
 * Created by wuhk on 2016/12/15.
 */
public class RemoverOrderMistakeTask extends BaseTask<NoResultData> {
    public RemoverOrderMistakeTask(Context context) {
        super(context);
    }

    @Override
    protected Result<NoResultData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("orderId" , (String)params[0]);

        Result<NoResultData> result = postCommon(UrlConstants.REMOVEORDERMISTAKE , paramMap);
        if (result.isSuccess()){
            NoResultData retData = JSON.parseObject(result.getMessage() , NoResultData.class);
            result.setMessage(retData.getMessage());
            if (retData.codeOk()){
                result.setValue(retData.getResult());
            }else{
                result.setSuccess(false);
            }
        }
        return result;
    }

    /**删除
     *
     * @param context
     * @param orderId
     * @param successCallback
     */
    public static void removeOrderWhenMistake(Context context , String orderId , AsyncTaskSuccessCallback<NoResultData> successCallback){
        RemoverOrderMistakeTask removerOrderMistakeTask = new RemoverOrderMistakeTask(context);
        removerOrderMistakeTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        removerOrderMistakeTask.setAsyncTaskSuccessCallback(successCallback);

        removerOrderMistakeTask.execute(orderId);

    }
}
