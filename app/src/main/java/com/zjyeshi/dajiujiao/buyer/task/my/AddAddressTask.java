package com.zjyeshi.dajiujiao.buyer.task.my;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.my.AddData;
import com.xuan.bigapple.lib.asynctask.helper.Result;

import java.util.HashMap;

/**
 * 增加收货地址
 * Created by wuhk on 2015/11/13.
 */
public class AddAddressTask extends BaseTask<AddData> {
    public AddAddressTask(Context context) {
        super(context);
    }

    @Override
    protected Result<AddData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String , String>();
        paramMap.put("name" , (String)params[0]);
        paramMap.put("phone" , (String)params[1]);
        paramMap.put("area" , (String)params[2]);
        paramMap.put("address" , (String)params[3]);

        Result<AddData> result = postCommon(UrlConstants.ADDADDRESS , paramMap);

        if (result.isSuccess()){
            AddData retData = JSON.parseObject(result.getMessage() , AddData.class);
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
