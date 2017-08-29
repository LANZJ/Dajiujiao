package com.zjyeshi.dajiujiao.buyer.task.order;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/8/24.
 */
public class ReturnProductTask extends BaseTask<NoResultData> {
    public ReturnProductTask(Context context) {
        super(context);
    }

    @Override
    protected Result<NoResultData> onHttpRequest(Object... params) {
        HashMap<String ,String> paramMap = new HashMap<String, String>();
        paramMap.put("orderId" , (String)params[0]);
        paramMap.put("returnAmount" , (String)params[1]);
        paramMap.put("returnReason" , (String)params[2]);
        paramMap.put("pics" , (String)params[3]);

        Result<NoResultData> result = postCommon(UrlConstants.RETURNPRODUCT , paramMap);
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
