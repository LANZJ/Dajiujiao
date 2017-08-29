package com.zjyeshi.dajiujiao.buyer.task.login;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.xuan.bigapple.lib.asynctask.helper.Result;

import java.util.HashMap;

/**
 * 获取短信验证码
 * 
 * Created by wuhk on 2015/10/12.
 */
public class GetSmsCodeTask extends BaseTask<NoResultData> {
    public GetSmsCodeTask(Context context) {
        super(context);
    }

    @Override
    protected Result<NoResultData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String , String>();
        paramMap.put("phone" ,(String)params[0]);

        Result<NoResultData> result = post(UrlConstants.SENDSMSCODE , paramMap);
        if (result.isSuccess()){
            //Http返回成功
            NoResultData retData = JSON.parseObject(result.getMessage() , NoResultData.class);
            retData.setMessage(result.getMessage());
            if (retData.codeOk()){
                //code成功
                result.setValue(retData.getResult());
            }else {
                //code失败
                result.setSuccess(false);
            }
        }
        return result;
    }
}
