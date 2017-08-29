package com.zjyeshi.dajiujiao.buyer.task.my;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/9/8.
 */
public class ChangeDeliverStateTask extends BaseTask<NoResultData> {

    public ChangeDeliverStateTask(Context context) {
        super(context);
    }

    @Override
    protected Result<NoResultData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String , String>();
        paramMap.put("delivery" , (String)params[0]);

        Result<NoResultData> result = postCommon(UrlConstants.MODIFYSHOP , paramMap);

        if (result.isSuccess()){
            NoResultData retData = JSON.parseObject(result.getMessage(), NoResultData.class);
            result.setMessage(retData.getMessage());
            if(retData.codeOk()){
                result.setValue(retData.getResult());
            }else{
                result.setSuccess(false);
            }
        }
        return result;
    }
}
