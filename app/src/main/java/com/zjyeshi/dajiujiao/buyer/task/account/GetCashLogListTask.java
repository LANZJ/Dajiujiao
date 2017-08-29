package com.zjyeshi.dajiujiao.buyer.task.account;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.account.data.CashLogList;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/7/4.
 */
public class GetCashLogListTask extends BaseTask<CashLogList> {
    public GetCashLogListTask(Context context) {
        super(context);
    }

    @Override
    protected Result<CashLogList> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("lastTime" , (String)params[0]);
        paramMap.put("mode" , (String)params[1]);

        Result<CashLogList> result = postCommon(UrlConstants.GETCASHLOGLIST , paramMap);
        if (result.isSuccess()){
            CashLogList retData = JSON.parseObject(result.getMessage() , CashLogList.class);
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
