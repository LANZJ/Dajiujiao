package com.zjyeshi.dajiujiao.buyer.task.store;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.CanBuyData;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/12/14.
 */
public class GoodSellListTask extends BaseTask<CanBuyData> {
    public GoodSellListTask(Context context) {
        super(context);
    }

    @Override
    protected Result<CanBuyData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String , String>();
        paramMap.put("shopId" , (String)params[0]);

        Result<CanBuyData> result = postCommon(UrlConstants.CANBUYPRODUCT , paramMap);

        if (result.isSuccess()){
            CanBuyData retData = JSON.parseObject(result.getMessage() , CanBuyData.class);
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