package com.zjyeshi.dajiujiao.buyer.circle.task;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.zjyeshi.dajiujiao.buyer.task.data.other.GetUpLoadFileData;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.xuan.bigapple.lib.asynctask.helper.Result;

/**
 * Created by wuhk on 2015/11/18.
 */
public class UpLoadFileTask extends BaseTask<GetUpLoadFileData> {
    public UpLoadFileTask(Context context) {
        super(context);
    }

    @Override
    protected Result<GetUpLoadFileData> onHttpRequest(Object... params) {
        StringBuilder path = new StringBuilder();
        String fileNames = String.valueOf(params[0]);
        int size = Integer.parseInt(String.valueOf(params[1]));
        int num = 0;
        Result<GetUpLoadFileData> result = new Result<GetUpLoadFileData>();
        for (int i = 0 ; i < size ; i++){
            String fileName = fileNames.split(",")[i];
            result = upLoadPost(UrlConstants.UPLOAD , fileName);

            if (result.isSuccess()){
                GetUpLoadFileData retData = JSON.parseObject(result.getMessage(), GetUpLoadFileData.class);
                result.setMessage(retData.getMessage());
                if (retData.codeOk()){
                    result.setValue(retData.getResult());
                    path.append(result.getValue().getPath());
                    path.append(",");
                    num ++;
                }else{
                    result.setSuccess(false);
                }
            }
        }
        if (num == size){
            result.setSuccess(true);
            path.deleteCharAt(path.length() - 1);
            result.setMessage(path.toString());
//            BPPreferences.instance().putString(PreferenceConstants.PIC_PATH, path.toString());
        }
        else{
            result.setSuccess(false);
        }
        return  result;
    }
}
