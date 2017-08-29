package com.zjyeshi.dajiujiao.buyer.task.work;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.CostApplyRecordListData;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;

import java.util.HashMap;

/**
 * 费用盛情记录
 * Created by wuhk on 2016/6/22.
 */
public class CostApplyRecordListTask extends BaseTask<CostApplyRecordListData> {
    public CostApplyRecordListTask(Context context) {
        super(context);
    }

    @Override
    protected Result<CostApplyRecordListData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
//        paramMap.put("memberId" , (String)params[0]);

        Result<CostApplyRecordListData> result = postCommon(UrlConstants.COSTAPPLICATIONRECORDLIST , paramMap);
        if (result.isSuccess()){
            CostApplyRecordListData retData = JSON.parseObject(result.getMessage() , CostApplyRecordListData.class);
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
