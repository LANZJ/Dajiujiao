package com.zjyeshi.dajiujiao.buyer.task.pay;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.pay.PrePareData;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;

import java.util.HashMap;

/**
 * 支付宝支付预处理
 * Created by wuhk on 2016/1/6.
 */
public class PayPrepareTask extends BaseTask<PrePareData> {
    public PayPrepareTask(Context context) {
        super(context);
    }

    @Override
    protected Result<PrePareData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String , String>();
        paramMap.put("orderId" , (String)params[0]);
        paramMap.put("addressId" , (String)params[1]);
        paramMap.put("signStr" , (String)params[2]);

        Result<PrePareData> result = payPost(UrlConstants.PAYPREPARE , paramMap);
        if (result.isSuccess()){
            PrePareData retData = JSON.parseObject(result.getMessage() , PrePareData.class);
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
