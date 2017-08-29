package com.zjyeshi.dajiujiao.buyer.task.work;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.work.data.TimeCardRecordListData;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/6/20.
 */
public class TimeCardRecordTask extends BaseTask<TimeCardRecordListData> {
    public TimeCardRecordTask(Context context) {
        super(context);
    }

    @Override
    protected Result<TimeCardRecordListData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        if (params.length ==1){
            paramMap.put("time" , (String)params[0]);
        }else{
            paramMap.put("memberId" , (String)params[0]);
            paramMap.put("time" , (String)params[1]);
        }

        Result<TimeCardRecordListData> result = postCommon(UrlConstants.TIMECARDRECORDLIST , paramMap);
        if (result.isSuccess()){
            TimeCardRecordListData retData = JSON.parseObject(result.getMessage() , TimeCardRecordListData.class);
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
