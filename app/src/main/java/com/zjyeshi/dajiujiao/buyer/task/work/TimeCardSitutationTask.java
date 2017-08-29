package com.zjyeshi.dajiujiao.buyer.task.work;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.task.work.data.TimeCardSitutaionData;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/6/20.
 */
public class TimeCardSitutationTask extends BaseTask<TimeCardSitutaionData> {
    public TimeCardSitutationTask(Context context) {
        super(context);
    }

    @Override
    protected Result<TimeCardSitutaionData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();

        Result<TimeCardSitutaionData> result = postCommon(UrlConstants.TIMECARDSITUATION , paramMap);
        if (result.isSuccess()){
            TimeCardSitutaionData retData = JSON.parseObject(result.getMessage() , TimeCardSitutaionData.class);
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
