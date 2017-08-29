package com.zjyeshi.dajiujiao.buyer.task.login;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.xuan.bigapple.lib.asynctask.helper.Result;

import java.util.HashMap;

/**
 * 找回密码任务类
 *
 * Created by wuhk on 2015/10/22.
 */
public class FindPwdTask extends BaseTask<NoResultData> {
    public FindPwdTask(Context context) {
        super(context);
    }

    @Override
    protected Result<NoResultData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String , String>();
        paramMap.put("phone" , (String)params[0]);
        paramMap.put("password" , (String)params[1]);
        paramMap.put("smsCode" , (String)params[2]);

        Result<NoResultData> result = post(UrlConstants.FINDPASSWORD , paramMap);

        if (result.isSuccess()){
            NoResultData retData = JSON.parseObject(result.getMessage() , NoResultData.class);
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
