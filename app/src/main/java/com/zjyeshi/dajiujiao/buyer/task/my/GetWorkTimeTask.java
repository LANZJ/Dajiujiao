package com.zjyeshi.dajiujiao.buyer.task.my;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.data.my.WorkTimeData;

import java.util.HashMap;

/**
 * 上下班时间段
 * Created by wuhk on 2016/7/25.
 */
public class GetWorkTimeTask extends BaseTask<WorkTimeData> {
    public GetWorkTimeTask(Context context) {
        super(context);
    }

    @Override
    protected Result<WorkTimeData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();

        Result<WorkTimeData> result = postCommon(UrlConstants.GETWORKTIME , paramMap);
        if(result.isSuccess()){
            WorkTimeData retData = JSON.parseObject(result.getMessage() , WorkTimeData.class);
            result.setMessage(retData.getMessage());
            if(retData.codeOk()){
                result.setValue(retData.getResult());
            }else{
                result.setSuccess(false);
            }
        }
        return result;
    }
}
