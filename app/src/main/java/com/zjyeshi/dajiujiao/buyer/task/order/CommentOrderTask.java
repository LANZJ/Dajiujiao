package com.zjyeshi.dajiujiao.buyer.task.order;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.xuan.bigapple.lib.asynctask.helper.Result;

import java.util.HashMap;

/**
 * 订单评价
 * Created by wuhk on 2015/11/22.
 */
public class CommentOrderTask extends BaseTask<NoResultData> {
    public CommentOrderTask(Context context) {
        super(context);
    }

    @Override
    protected Result<NoResultData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String , String>();

        paramMap.put("orderId" , (String)params[0]);
        paramMap.put("productId" , (String)params[1]);
        paramMap.put("level" , (String)params[2]);
        paramMap.put("content" , (String)params[3]);

        Result<NoResultData> result = postCommon(UrlConstants.ADDORDEREVALUATE , paramMap);
        if (result.isSuccess()){
            NoResultData retData = JSON.parseObject(result.getMessage() , NoResultData.class);
            result.setMessage(retData.getMessage());
            if(retData.codeOk()){
                result.setValue(retData.getResult());
            }else{
                result.setSuccess(false);
            }
        }
        return result;
    }
}
