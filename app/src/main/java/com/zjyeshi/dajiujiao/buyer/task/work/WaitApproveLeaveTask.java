package com.zjyeshi.dajiujiao.buyer.task.work;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.LeaveRecordListData;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/6/21.
 */
public class WaitApproveLeaveTask extends BaseTask<LeaveRecordListData> {
    public WaitApproveLeaveTask(Context context) {
        super(context);
    }

    @Override
    protected Result<LeaveRecordListData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("status" , (String)params[0]);
        paramMap.put("lastTime" , (String)params[1]);
        paramMap.put("mode" , (String)params[2]);

        Result<LeaveRecordListData> result = postCommon(UrlConstants.WAITREVIEWLEAVELIST , paramMap);
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
