package com.zjyeshi.dajiujiao.buyer.task.my;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.my.FaqData;

import java.util.HashMap;

/**
 * 获取常见问题任务类
 * Created by wuhk on 2015/12/15.
 */
public class GetFaqLinkTask extends BaseTask<FaqData> {
    public GetFaqLinkTask(Context context) {
        super(context);
    }

    @Override
    protected Result<FaqData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String , String>();
        Result<FaqData> result = post(UrlConstants.FAQ , paramMap);
        if (result.isSuccess()){
            FaqData retData = JSON.parseObject(result.getMessage() , FaqData.class);
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
