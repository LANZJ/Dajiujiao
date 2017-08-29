package com.zjyeshi.dajiujiao.buyer.task.work;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.work.data.AskMoneyDetailData;

import java.util.HashMap;

/**
 * 费用申请详情
 * Created by wuhk on 2016/6/22.
 */
public class AskMoneyDetailTask extends BaseTask<AskMoneyDetailData> {
    public AskMoneyDetailTask(Context context) {
        super(context);
    }

    @Override
    protected Result<AskMoneyDetailData> onHttpRequest(Object... params) {
        HashMap<String ,String> paramMap = new HashMap<String, String>();
        paramMap.put("id" , (String)params[0]);

        Result<AskMoneyDetailData> result = postCommon(UrlConstants.COSTAPPLICATIONINFO , paramMap);
        if (result.isSuccess()){
            AskMoneyDetailData retData = JSON.parseObject(result.getMessage() , AskMoneyDetailData.class);
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
