package com.zjyeshi.dajiujiao.buyer.task.work;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.SalemanListData;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/8/25.
 */
public class MemberListTask extends BaseTask<SalemanListData> {

    public MemberListTask(Context context) {
        super(context);
    }

    @Override
    protected Result<SalemanListData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();

        Result<SalemanListData> result = postCommon(UrlConstants.SALESMANLISTANDME , paramMap);
        if (result.isSuccess()){
            SalemanListData retData = JSON.parseObject(result.getMessage() , SalemanListData.class);
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