package com.zjyeshi.dajiujiao.buyer.task.work;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.LeaveInfoData;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/6/21.
 */
public class LeaveInfoTask extends BaseTask<LeaveInfoData> {
    public LeaveInfoTask(Context context) {
        super(context);
    }

    @Override
    protected Result<LeaveInfoData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("id" , (String)params[0]);

        Result<LeaveInfoData> result = postCommon(UrlConstants.LEAVEINFO , paramMap);
        if (result.isSuccess()){
            LeaveInfoData retData = JSON.parseObject(result.getMessage() , LeaveInfoData.class);
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
