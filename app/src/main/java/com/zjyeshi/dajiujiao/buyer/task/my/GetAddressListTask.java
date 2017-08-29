package com.zjyeshi.dajiujiao.buyer.task.my;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.my.GetAddressListData;
import com.xuan.bigapple.lib.asynctask.helper.Result;

import java.util.HashMap;

/**
 * 获取地址列表
 * Created by wuhk on 2015/11/13.
 */
public class GetAddressListTask extends BaseTask<GetAddressListData> {
    public GetAddressListTask(Context context) {
        super(context);
    }

    @Override
    protected Result<GetAddressListData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String , String>();
        if (!Validators.isEmpty(params)){
            paramMap.put("memberId", (String)params[0]);
        }

        Result<GetAddressListData> result = postCommon(UrlConstants.LISTADDRESS , paramMap);

        if (result.isSuccess()){
            GetAddressListData retData  = JSON.parseObject(result.getMessage() , GetAddressListData.class);
            result.setMessage(retData.getMessage());
            if (retData.codeOk()){
                result.setValue(retData.getResult());
            }else {
                result.setSuccess(false);
            }
        }
        return result;
    }
}
