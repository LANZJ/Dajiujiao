package com.zjyeshi.dajiujiao.buyer.circle.task;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.xuan.bigapple.lib.asynctask.helper.Result;

import java.util.HashMap;

/**
 * Created by wuhk on 2015/11/19.
 */
public class DeleteEvaluateTask extends BaseTask<NoResultData> {
    public DeleteEvaluateTask(Context context) {
        super(context);
    }

    @Override
    protected Result<NoResultData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String , String>();
        paramMap.put("circleEvaluateId" , (String)params[0]);

        Result<NoResultData> result = postCommon(UrlConstants.REMOVEEVALUATE , paramMap);

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
}
