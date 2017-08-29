package com.zjyeshi.dajiujiao.buyer.task.work;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.DailyInfoData;

import java.util.HashMap;

/**
 * 日报详情
 *
 * Created by zhum on 2016/6/23.
 */
public class DailyInfoTask extends BaseTask<DailyInfoData> {
    public DailyInfoTask(Context context) {
        super(context);
    }

    @Override
    protected Result<DailyInfoData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("id" , (String)params[0]);

        Result<DailyInfoData> result = postCommon(UrlConstants.DAILYINFO , paramMap);
        if (result.isSuccess()){
            DailyInfoData dailyInfoData = JSON.parseObject(result.getMessage() , DailyInfoData.class);
            result.setMessage(dailyInfoData.getMessage());
            if (dailyInfoData.codeOk()){
                result.setValue(dailyInfoData.getResult());
            }else{
                result.setSuccess(false);
            }
        }
        return result;
    }
}
