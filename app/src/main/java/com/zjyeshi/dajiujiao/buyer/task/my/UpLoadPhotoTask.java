package com.zjyeshi.dajiujiao.buyer.task.my;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.other.GetUpLoadFileData;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

/**
 * Created by wuhk on 2015/11/22.
 */
public class UpLoadPhotoTask extends BaseTask<GetUpLoadFileData> {
    public UpLoadPhotoTask(Context context) {
        super(context);
    }

    @Override
    protected Result<GetUpLoadFileData> onHttpRequest(Object... params) {
        String file  = (String)params[0];
        Result<GetUpLoadFileData> result = upLoadPost(UrlConstants.UPLOAD , file);
        if (result.isSuccess()){
            GetUpLoadFileData retData = JSON.parseObject(result.getMessage(), GetUpLoadFileData.class);
            result.setMessage(retData.getMessage());
            if (retData.codeOk()){
                result.setValue(retData.getResult());
            }else{
                result.setSuccess(false);
            }
        }
        return result;
    }

    public static void getUrlPath(Context context , String loaclPath , AsyncTaskSuccessCallback<GetUpLoadFileData> successCallback){
        UpLoadPhotoTask upLoadPhotoTask = new UpLoadPhotoTask(context);
        upLoadPhotoTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<GetUpLoadFileData>() {
            @Override
            public void failCallback(Result<GetUpLoadFileData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        upLoadPhotoTask.setAsyncTaskSuccessCallback(successCallback);

        upLoadPhotoTask.execute(loaclPath);
    }
}
