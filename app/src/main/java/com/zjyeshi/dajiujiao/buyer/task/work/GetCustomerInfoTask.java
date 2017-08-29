package com.zjyeshi.dajiujiao.buyer.task.work;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.task.work.data.CustomerInfoData;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;

import java.util.HashMap;

/**
 * 获取客户详细信息
 *
 * Created by zhum on 2016/6/22.
 */
public class GetCustomerInfoTask extends BaseTask<CustomerInfoData>{
    public GetCustomerInfoTask(Context context) {
        super(context);
    }

    @Override
    protected Result<CustomerInfoData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("id" , (String)params[0]);

        Result<CustomerInfoData> result = postCommon(UrlConstants.CUSTOMERINFO , paramMap);
        if (result.isSuccess()){
            CustomerInfoData data = JSON.parseObject(result.getMessage() , CustomerInfoData.class);
            result.setMessage(data.getMessage());
            if (data.codeOk()){
                result.setValue(data.getResult());
            }else{
                result.setSuccess(false);
            }
        }
        return result;
    }
}
