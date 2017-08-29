package com.zjyeshi.dajiujiao.buyer.task.work;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.task.work.data.DateReportData;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;

import java.util.HashMap;

/**
 * 获取工作日报列表
 *
 * Created by zhum on 2016/6/23.
 */
public class DailyListTask extends BaseTask<DateReportData> {
    public DailyListTask(Context context) {
        super(context);
    }

    @Override
    protected Result<DateReportData> onHttpRequest(Object... params) {
        HashMap<String,String> paramMap = new HashMap<String, String>();

        paramMap.put("personId" , (String)params[0]);
        paramMap.put("lastTime" , (String)params[1]);
        paramMap.put("mode"  , (String)params[2]);
        paramMap.put("date"  , (String)params[3]);


        Result<DateReportData> result = postCommon(UrlConstants.DAILYLIST , paramMap);
        if (result.isSuccess()){
            DateReportData dateReport = JSON.parseObject(result.getMessage() , DateReportData.class);
            result.setMessage(dateReport.getMessage());
            if (dateReport.codeOk()){
                result.setValue(dateReport.getResult());
            }else{
                result.setSuccess(false);
            }
        }
        return result;
    }
}
