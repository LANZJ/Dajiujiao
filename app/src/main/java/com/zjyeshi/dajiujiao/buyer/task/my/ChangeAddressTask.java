package com.zjyeshi.dajiujiao.buyer.task.my;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.xuan.bigapple.lib.asynctask.helper.Result;

import java.util.HashMap;

/**
 * 修改地址
 * Created by wuhk on 2015/11/16.
 */
public class ChangeAddressTask extends BaseTask<NoResultData> {
    public ChangeAddressTask(Context context) {
        super(context);
    }

    @Override
    protected Result<NoResultData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String , String>();
        paramMap.put("id",(String)params[0]);
        paramMap.put("name" , (String)params[1]);
        paramMap.put("phone" , (String)params[2]);
        paramMap.put("area" , (String)params[3]);
        paramMap.put("address" , (String)params[4]);

        Result<NoResultData> result = postCommon(UrlConstants.MODIFYADDRESS , paramMap);

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
