package com.zjyeshi.dajiujiao.buyer.task.pay;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.pay.WxPayData;

import java.util.HashMap;

/**
 * 微信支付预处理
 * Created by wuhk on 2016/1/13.
 */
public class WxPayPrepareTask extends BaseTask<WxPayData> {
    public WxPayPrepareTask(Context context) {
        super(context);
    }

    @Override
    protected Result<WxPayData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String , String>();
        paramMap.put("orderId" , (String)params[0]);
        paramMap.put("addressId" , (String)params[1]);
        paramMap.put("totalFee" , (String)params[2]);
        paramMap.put("role" , "SELLER");

        Result<WxPayData> result = payPost(UrlConstants.WXPAYPREPARE, paramMap);

        if (result.isSuccess()){
            WxPayData  retData = JSON.parseObject(result.getMessage(), WxPayData.class);
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
