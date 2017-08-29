package com.zjyeshi.dajiujiao.buyer.task.work;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;

import java.util.HashMap;

/**
 * 添加工作日报
 *
 * Created by zhum on 2016/6/23.
 */
public class AddDailyTask extends BaseTask<NoResultData> {
    public AddDailyTask(Context context) {
        super(context);
    }

    @Override
    protected Result<NoResultData> onHttpRequest(Object... params) {
        HashMap<String , String> paraMap = new HashMap<String , String>();
        paraMap.put("recordTime" , (String)params[0]);
        paraMap.put("trip" , (String)params[1]);
        paraMap.put("summary" , (String)params[2]);
        paraMap.put("todayCost" , (String)params[3]);
        paraMap.put("tomorrowPlan" , (String)params[4]);
        paraMap.put("supInfos" , (String)params[5]);
        paraMap.put("copyMemberIds",(String)params[6]);

        Result<NoResultData> result = postCommon(UrlConstants.ADDDAILY , paraMap);
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
