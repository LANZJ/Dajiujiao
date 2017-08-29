package com.zjyeshi.dajiujiao.buyer.task.account;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/7/4.
 */
public class AddCashAccountTask extends BaseTask<NoResultData> {
    public AddCashAccountTask(Context context) {
        super(context);
    }

    @Override
    protected Result<NoResultData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        if (!Validators.isEmpty((String)params[0])){
            paramMap.put("id" , (String)params[0]);
        }
        paramMap.put("type" , (String)params[1]);
        paramMap.put("number" , (String)params[2]);
        paramMap.put("name" , (String)params[3]);
        if (!Validators.isEmpty((String)params[4])){
            paramMap.put("bankName" , (String)params[4]);
        }

        Result<NoResultData> result = postCommon(UrlConstants.ADDCASHACCOUNT , paramMap);
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
