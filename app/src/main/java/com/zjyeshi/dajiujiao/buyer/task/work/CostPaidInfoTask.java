package com.zjyeshi.dajiujiao.buyer.task.work;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.CostPaidInfoData;

import java.util.HashMap;

/**
 * 费用报销
 *
 * Created by zhum on 2016/6/23.
 */
public class CostPaidInfoTask extends BaseTask<CostPaidInfoData> {
    public CostPaidInfoTask(Context context) {
        super(context);
    }

    @Override
    protected Result<CostPaidInfoData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("id" , (String)params[0]);

        Result<CostPaidInfoData> result = postCommon(UrlConstants.COSTPAIDINFO , paramMap);
        if (result.isSuccess()){
            CostPaidInfoData costPaidInfoData = JSON.parseObject(result.getMessage() , CostPaidInfoData.class);
            result.setMessage(costPaidInfoData.getMessage());
            if (costPaidInfoData.codeOk()){
                result.setValue(costPaidInfoData.getResult());
            }else{
                result.setSuccess(false);
            }
        }
        return result;
    }
}
