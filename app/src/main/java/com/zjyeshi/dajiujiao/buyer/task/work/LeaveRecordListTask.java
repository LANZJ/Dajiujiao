package com.zjyeshi.dajiujiao.buyer.task.work;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.LeaveRecordListData;

import java.util.HashMap;

/**
 * 请假记录
 * Created by wuhk on 2016/6/20.
 */
public class LeaveRecordListTask extends BaseTask<LeaveRecordListData> {
    public LeaveRecordListTask(Context context) {
        super(context);
    }


    @Override
    protected Result<LeaveRecordListData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();

        paramMap.put("lastTime" , (String)params[0]);
        paramMap.put("mode"  , (String)params[1]);

        Result<LeaveRecordListData> result = postCommon(UrlConstants.LEAVERECORDLIST , paramMap);
        if (result.isSuccess()){
            LeaveRecordListData retData = JSON.parseObject(result.getMessage() , LeaveRecordListData.class);
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
