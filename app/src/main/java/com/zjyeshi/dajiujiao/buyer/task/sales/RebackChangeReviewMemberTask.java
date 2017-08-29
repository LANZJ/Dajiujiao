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
 * Created by wuhk on 2017/5/23.
 */

public class RebackChangeReviewMemberTask extends BaseTask<NoResultData> {
    public RebackChangeReviewMemberTask(Context context) {
        super(context);
    }

    @Override
    protected Result<NoResultData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("rebackId" , (String)params[0]);
        paramMap.put("memberId" , (String)params[1]);

        Result<NoResultData> result = postCommon(UrlConstants.REBACKCHANGEREVIEWMEMBER , paramMap);
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

    public static void  changeRebackReview(Context context , String rebackId , String memberId , AsyncTaskSuccessCallback<NoResultData> successCallback){
        RebackChangeReviewMemberTask rebackChangeReviewMemberTask  = new RebackChangeReviewMemberTask(context);
        rebackChangeReviewMemberTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        rebackChangeReviewMemberTask.setAsyncTaskSuccessCallback(successCallback);

        rebackChangeReviewMemberTask.execute(rebackId , memberId);

    }
}
