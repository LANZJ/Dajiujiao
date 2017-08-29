package com.zjyeshi.dajiujiao.buyer.task.account;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.account.data.CashAccountList;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/7/4.
 */
public class GetCashAccountListTask extends BaseTask<CashAccountList> {
    public GetCashAccountListTask(Context context) {
        super(context);
    }

    @Override
    protected Result<CashAccountList> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();

        Result<CashAccountList> result = postCommon(UrlConstants.CASHACCOUNTLIST , paramMap);
        if (result.isSuccess()){
            CashAccountList retData = JSON.parseObject(result.getMessage() , CashAccountList.class);
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
