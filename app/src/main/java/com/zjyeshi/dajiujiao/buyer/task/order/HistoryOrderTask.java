package com.zjyeshi.dajiujiao.buyer.task.order;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.order.GetOrderListData;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/9/12.
 */
public class HistoryOrderTask extends BaseTask<GetOrderListData> {
    public HistoryOrderTask(Context context) {
        super(context);
    }

    @Override
    protected Result<GetOrderListData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("month"  , (String)params[0]);
        paramMap.put("type" , (String)params[1]);

        Result<GetOrderListData> result = postCommon(UrlConstants.ORDERHISTORY , paramMap);
        if (result.isSuccess()){
            GetOrderListData retData = JSON.parseObject(result.getMessage() , GetOrderListData.class);
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
