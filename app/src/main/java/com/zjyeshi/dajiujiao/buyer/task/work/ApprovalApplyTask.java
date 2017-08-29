package com.zjyeshi.dajiujiao.buyer.task.work;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;

import java.util.HashMap;

/**
 * 审核通过拒绝，URL不同
 * Created by wuhk on 2016/6/23.
 */
public class ApprovalApplyTask extends BaseTask<NoResultData> {
    private String url;
    public ApprovalApplyTask(Context context , String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected Result<NoResultData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("id" , (String)params[0]);
        paramMap.put("type" , (String)params[1]);

        Result<NoResultData> result = postCommon(url , paramMap);
        if (result.isSuccess()){
            NoResultData retData = JSON.parseObject(result.getMessage(), NoResultData.class);
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
