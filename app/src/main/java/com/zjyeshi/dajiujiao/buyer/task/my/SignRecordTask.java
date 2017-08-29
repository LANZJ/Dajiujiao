package com.zjyeshi.dajiujiao.buyer.task.my;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.my.SignData;

import java.util.HashMap;

/**
 * 签到记录任务类
 * Created by wuhk on 2016/3/2.
 */
public class SignRecordTask extends BaseTask<SignData> {
    public SignRecordTask(Context context) {
        super(context);
    }

    @Override
    protected Result<SignData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String , String>();
        paramMap.put("lastTime" , (String)params[0]);
        paramMap.put("mode" , (String)params[1]);
        Result<SignData> result = postCommon(UrlConstants.SIGNRECORDLIST , paramMap);
        if (result.isSuccess()){
            SignData retData = JSON.parseObject(result.getMessage() , SignData.class);
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
