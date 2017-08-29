package com.zjyeshi.dajiujiao.buyer.task.work;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.TimeCardData;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/6/20.
 */
public class TimeCardTask extends BaseTask<TimeCardData> {
    public TimeCardTask(Context context) {
        super(context);
    }

    @Override
    protected Result<TimeCardData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap  = new HashMap<String, String>();
        paramMap.put("type" , (String)params[0]);
        paramMap.put("clockProperty" , (String)params[1]);
        paramMap.put("lngE5" , (String)params[2]);
        paramMap.put("latE5" , (String)params[3]);
        paramMap.put("address" , (String)params[4]);
        paramMap.put("remark" , (String)params[5]);
        paramMap.put("pics" , (String)params[6]);

        Result<TimeCardData> result = postCommon(UrlConstants.TIMECARD  , paramMap);
        if (result.isSuccess()){
            TimeCardData retData = JSON.parseObject(result.getMessage() , TimeCardData.class);
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
