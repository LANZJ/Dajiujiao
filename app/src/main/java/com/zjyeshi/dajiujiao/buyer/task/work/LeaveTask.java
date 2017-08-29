package com.zjyeshi.dajiujiao.buyer.task.work;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/6/21.
 */
public class LeaveTask extends BaseTask<NoResultData>{
    public LeaveTask(Context context) {
        super(context);
    }

    @Override
    protected Result<NoResultData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("approver" , (String)params[0]);
        paramMap.put("type" , (String)params[1]);
        paramMap.put("startTime" , (String)params[2]);
        paramMap.put("endTime" , (String)params[3]);
        paramMap.put("leaveDays" , (String)params[4]);
        paramMap.put("remark" , (String)params[5]);
        paramMap.put("pics" , (String)params[6]);

        Result<NoResultData> result = postCommon(UrlConstants.LEAVE , paramMap);
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
