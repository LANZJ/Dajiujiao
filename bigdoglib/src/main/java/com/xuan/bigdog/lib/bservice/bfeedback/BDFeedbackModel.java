package com.xuan.bigdog.lib.bservice.bfeedback;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigdog.lib.bservice.bcommon.BServiceBaseTask;
import com.xuan.bigdog.lib.bservice.bcommon.BServiceNoResultData;

import java.util.HashMap;

/**
 * 意见反馈
 * Created by wuhk on 2016/3/14.
 */
public class BDFeedbackModel {
    private static BDFeedbackModel instantce;

    private BDFeedbackModel(){

    }

    public static BDFeedbackModel getInstantce() {
        if (null == instantce){
            instantce = new BDFeedbackModel();
        }
        return instantce;
    }

    /**意见反馈
     *
     * @param context
     * @param website
     * @param content
     * @param l
     */
    public void feedback(Context context , String website , String content , String userId , final BDFeedbackListener l){
        String url = website + "common/feedback.htm";
        FeedbackTask feedbackTask = new FeedbackTask(context , url);
        feedbackTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<BServiceNoResultData>() {
            @Override
            public void failCallback(Result<BServiceNoResultData> result) {
                l.fail(result.getMessage());
            }
        });

        feedbackTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<BServiceNoResultData>() {
            @Override
            public void successCallback(Result<BServiceNoResultData> result) {
                l.success();
            }
        });

        feedbackTask.execute(content , userId);
    }

    /**意见反馈Task
     *
     */
    private class FeedbackTask extends BServiceBaseTask<BServiceNoResultData> {
        private String url;

        public FeedbackTask(Context context, String url) {
            super(context);
            this.url = url;
        }

        @Override
        protected Result<BServiceNoResultData> onHttpRequest(Object... params) {
            HashMap<String , String> paramMap = new HashMap<String , String>();
            paramMap.put("content" , (String)params[0]);
            paramMap.put("userId" , (String)params[1]);
            Result<BServiceNoResultData> result = bPost(url , paramMap);
            if (result.isSuccess()){
                BServiceNoResultData retData = JSON.parseObject(result.getMessage() , BServiceNoResultData.class);
                result.setMessage(retData.getMessage());
                if (retData.codeOk()){
                    result.setValue(retData.getResult());
                }else{
                    result.setSuccess(false);
                }
            }
            return result;
        }
    }
}
