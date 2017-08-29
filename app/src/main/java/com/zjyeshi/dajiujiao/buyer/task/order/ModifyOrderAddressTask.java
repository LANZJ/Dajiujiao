package com.zjyeshi.dajiujiao.buyer.task.order;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/9/10.
 */
public class ModifyOrderAddressTask extends BaseTask<NoResultData> {
    public ModifyOrderAddressTask(Context context) {
        super(context);
    }

    @Override
    protected Result<NoResultData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("orderId" , (String)params[0]);
        paramMap.put("name" , (String)params[1]);
        paramMap.put("phone" , (String)params[2]);
        paramMap.put("area" , (String)params[3]);
        paramMap.put("address" , (String)params[4]);

        Result<NoResultData> result = postCommon(UrlConstants.MODIFYORDERADDRESS , paramMap);
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
