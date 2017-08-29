package com.zjyeshi.dajiujiao.buyer.task.seller;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.task.data.seller.CustomerBuyInfoData;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/4/7.
 */
public class GetCustomerBuyInfoTask extends BaseTask<CustomerBuyInfoData> {
    public GetCustomerBuyInfoTask(Context context) {
        super(context);
    }

    @Override
    protected Result<CustomerBuyInfoData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String ,String>();
        paramMap.put("id" , (String)params[0]);

        Result<CustomerBuyInfoData> result = postCommon(UrlConstants.GETMYCUSTOMERBUYINFO , paramMap);
        if (result.isSuccess()){
            CustomerBuyInfoData retData = JSON.parseObject(result.getMessage() , CustomerBuyInfoData.class);
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
