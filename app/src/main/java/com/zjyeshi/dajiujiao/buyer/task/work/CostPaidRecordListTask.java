package com.zjyeshi.dajiujiao.buyer.task.work;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.CostPaidRecordListData;

import java.util.HashMap;

/**
 * 费用报销记录
 * Created by wuhk on 2016/6/22.
 */
public class CostPaidRecordListTask extends BaseTask<CostPaidRecordListData> {
    public CostPaidRecordListTask(Context context) {
        super(context);
    }

    @Override
    protected Result<CostPaidRecordListData> onHttpRequest(Object... params) {
        HashMap<String ,String> paramMap = new HashMap<String, String>();

        if (!Validators.isEmpty(params)){
            paramMap.put("memberId" , (String)params[0]);
        }

        Result<CostPaidRecordListData> result = postCommon(UrlConstants.COSTPAIDRECORDLIST , paramMap);
        if (result.isSuccess()){
            CostPaidRecordListData retData = JSON.parseObject(result.getMessage() , CostPaidRecordListData.class);
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
