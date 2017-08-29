package com.zjyeshi.dajiujiao.buyer.task.work;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.StockListData;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/6/23.
 */
public class CustomerInventoryTask extends BaseTask<StockListData> {
    public CustomerInventoryTask(Context context) {
        super(context);
    }

    @Override
    protected Result<StockListData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();

        Result<StockListData> result = postCommon(UrlConstants.CUSTOMERINVENTORY , paramMap);

        if (result.isSuccess()){
            StockListData retData = JSON.parseObject(result.getMessage() , StockListData.class);
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
