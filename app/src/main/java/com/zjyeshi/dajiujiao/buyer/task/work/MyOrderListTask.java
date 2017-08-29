package com.zjyeshi.dajiujiao.buyer.task.work;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.MyOrderListData;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/6/22.
 */
public class MyOrderListTask extends BaseTask<MyOrderListData> {
    public MyOrderListTask(Context context) {
        super(context);
    }

    @Override
    protected Result<MyOrderListData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("memberId" , (String)params[0]);
        Result<MyOrderListData> result = postCommon(UrlConstants.MYORDERLISTV2 , paramMap);
        if (result.isSuccess()){
            MyOrderListData retData = JSON.parseObject(result.getMessage() , MyOrderListData.class);
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
