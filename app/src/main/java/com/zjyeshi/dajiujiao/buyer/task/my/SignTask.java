package com.zjyeshi.dajiujiao.buyer.task.my;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/3/2.
 */
public class SignTask extends BaseTask<NoResultData> {
    public SignTask(Context context) {
        super(context);
    }

    @Override
    protected Result<NoResultData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String , String>();
        paramMap.put("lngE5" , (String)params[0]);
        paramMap.put("latE5" , (String)params[1]);
        paramMap.put("address" , (String)params[2]);
        paramMap.put("status" , (String)params[3]);

        Result<NoResultData> result = postCommon(UrlConstants.SIGNRECORD , paramMap);
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
