package com.zjyeshi.dajiujiao.buyer.task.pay;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;

import java.util.HashMap;

/**
 * 微信支付状态查询
 * Created by wuhk on 2016/1/13.
 */
public class WxPayOrderQueryTask extends BaseTask<NoResultData> {
    public WxPayOrderQueryTask(Context context) {
        super(context);
    }

    @Override
    protected Result<NoResultData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String , String>();
        paramMap.put("tradeNo" , (String)params[0]);
        paramMap.put("role" , "SELLER");

        Result<NoResultData> result = payPost(UrlConstants.WXPAYORDERQUERY , paramMap);

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
