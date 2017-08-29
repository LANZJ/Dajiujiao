package com.zjyeshi.dajiujiao.buyer.task.order;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/7/20.
 */
public class XianxiaOrderTask extends BaseTask<NoResultData> {
    public XianxiaOrderTask(Context context) {
        super(context);
    }

    @Override
    protected Result<NoResultData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();

        paramMap.put("orderId" , (String)params[0]);
        paramMap.put("addressId" , (String)params[1]);
        paramMap.put("useMarketCost" , (String)params[2]);
        paramMap.put("orderPreferentialActivities",(String)params[3]);

        Result<NoResultData> result = postCommon(UrlConstants.LINEPAYMENTV2 , paramMap);
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
