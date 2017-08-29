package com.zjyeshi.dajiujiao.buyer.circle.task;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.circle.task.data.CircleCollectionData;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/8/16.
 */
public class MyCircleCollectionTask extends BaseTask<CircleCollectionData> {
    public MyCircleCollectionTask(Context context) {
        super(context);
    }

    @Override
    protected Result<CircleCollectionData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("lastTime" , (String)params[0]);
        paramMap.put("mode"  , (String)params[1]);

        Result<CircleCollectionData> result = postCommon(UrlConstants.MYCOLLECTION , paramMap);
        if (result.isSuccess()){
            CircleCollectionData retData = JSON.parseObject(result.getMessage() , CircleCollectionData.class);
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
