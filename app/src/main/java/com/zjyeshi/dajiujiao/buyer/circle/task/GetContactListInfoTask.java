package com.zjyeshi.dajiujiao.buyer.circle.task;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.circle.task.data.ContactListData;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/9/11.
 */
public class GetContactListInfoTask extends BaseTask<ContactListData> {
    public GetContactListInfoTask(Context context) {
        super(context);
    }

    @Override
    protected Result<ContactListData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();

        Result<ContactListData> result = postCommon(UrlConstants.CONTACTLISTINFO , paramMap);
        if (result.isSuccess()){
            ContactListData retData = JSON.parseObject(result.getMessage() , ContactListData.class);
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
