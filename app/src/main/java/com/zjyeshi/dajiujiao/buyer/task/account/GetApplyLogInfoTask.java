package com.zjyeshi.dajiujiao.buyer.task.account;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.account.data.CashLogInfo;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/7/4.
 */
public class GetApplyLogInfoTask extends BaseTask<CashLogInfo> {
    public GetApplyLogInfoTask(Context context) {
        super(context);
    }

    @Override
    protected Result<CashLogInfo> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("id" , (String)params[0]);

        Result<CashLogInfo> result = postCommon(UrlConstants.GETCASHLOGINFO , paramMap);
        if (result.isSuccess()){
            CashLogInfo retData = JSON.parseObject(result.getMessage() , CashLogInfo.class);
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
