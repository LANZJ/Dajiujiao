package com.zjyeshi.dajiujiao.buyer.task.order;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;

import java.util.HashMap;

/**
 * 修改发货备注
 * Created by wuhk on 2016/12/14.
 */
public class ModifyDellivertRemarkTask extends BaseTask<NoResultData> {
    public ModifyDellivertRemarkTask(Context context) {
        super(context);
    }

    @Override
    protected Result<NoResultData> onHttpRequest(Object... params) {
        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("orderId", (String) params[0]);
        paramMap.put("deliveryRemark", (String) params[1]);

        Result<NoResultData> result = postCommon(UrlConstants.MODIFYDELIVERYREMARK, paramMap);
        if (result.isSuccess()) {
            NoResultData retData = JSON.parseObject(result.getMessage(), NoResultData.class);
            result.setMessage(retData.getMessage());
            if (retData.codeOk()){
                result.setValue(retData.getResult());
            } else {
                result.setSuccess(false);
            }
        }

        return result;
    }
}
