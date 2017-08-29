package com.zjyeshi.dajiujiao.buyer.circle.task;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.circle.task.data.AddEvaluateData;
import com.xuan.bigapple.lib.asynctask.helper.Result;

import java.util.HashMap;

/**
 * Created by wuhk on 2015/11/18.
 */
public class AddEvaluateTask extends BaseTask<AddEvaluateData> {
    public AddEvaluateTask(Context context) {
        super(context);
    }

    @Override
    protected Result<AddEvaluateData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String , String>();
        paramMap.put("circleId" , (String)params[0]);
        paramMap.put("circleEvaluateId" , (String)params[1]);
        paramMap.put("content" , (String)params[2]);

        Result<AddEvaluateData> result = postCommon(UrlConstants.ADDEVALUATE , paramMap);
        if (result.isSuccess()){
            AddEvaluateData retData = JSON.parseObject(result.getMessage() , AddEvaluateData.class);
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
