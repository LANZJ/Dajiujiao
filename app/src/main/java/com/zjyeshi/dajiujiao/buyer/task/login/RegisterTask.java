package com.zjyeshi.dajiujiao.buyer.task.login;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.login.RegisterData;
import com.xuan.bigapple.lib.asynctask.helper.Result;

import java.util.HashMap;

/**
 * 注册
 *
 * Created by wuhk on 2015/10/22.
 */
public class RegisterTask extends BaseTask<RegisterData> {
    public RegisterTask(Context context) {
        super(context);
    }

    @Override
    protected Result<RegisterData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String , String>();
        paramMap.put("phone" , (String)params[0]);
        paramMap.put("password" , (String)params[1]);
        paramMap.put("smsCode" , (String)params[2]);

        Result<RegisterData> result = post(UrlConstants.REGISTER ,paramMap);
        if (result.isSuccess()){
            RegisterData retData = JSON.parseObject(result.getMessage() , RegisterData.class);
            retData.setMessage(result.getMessage());
            if (retData.codeOk()){
                result.setValue(retData.getResult());
            }else{
                result.setSuccess(false);
            }
        }
        return result;
    }
}
