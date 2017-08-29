package com.zjyeshi.dajiujiao.buyer.task.work;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.ApproverListData;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/6/21.
 */
public class ApproverListTask extends BaseTask<ApproverListData> {
    public ApproverListTask(Context context) {
        super(context);
    }

    @Override
    protected Result<ApproverListData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("type" , (String)params[0]);

        Result<ApproverListData> result = postCommon(UrlConstants.APPROVERLIST , paramMap);
        if (result.isSuccess()){
            ApproverListData retData = JSON.parseObject(result.getMessage() , ApproverListData.class);
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
