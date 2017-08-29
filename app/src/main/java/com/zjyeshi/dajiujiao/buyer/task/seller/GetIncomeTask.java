package com.zjyeshi.dajiujiao.buyer.task.seller;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.seller.IncomeData;
import com.xuan.bigapple.lib.asynctask.helper.Result;

import java.util.HashMap;

/**
 * 支付收入流水任务类
 * Created by wuhk on 2015/11/10.
 */
public class GetIncomeTask extends BaseTask<IncomeData> {
    public GetIncomeTask(Context context) {
        super(context);
    }

    @Override
    protected Result<IncomeData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String , String>();
        Result<IncomeData> result = postCommon(UrlConstants.ACCOUNTLOG , paramMap);

        if (result.isSuccess()){
            IncomeData retData = JSON.parseObject(result.getMessage() , IncomeData.class);
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
