package com.zjyeshi.dajiujiao.buyer.task.my;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.my.MemberMoreData;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/9/8.
 */
public class MemberMoreInfoTask extends BaseTask<MemberMoreData> {
    public MemberMoreInfoTask(Context context) {
        super(context);
    }

    @Override
    protected Result<MemberMoreData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("memberId" , (String)params[0]);

        Result<MemberMoreData> result = postCommon(UrlConstants.MEMBERMOREINFO , paramMap);
        if (result.isSuccess()){
            MemberMoreData retData = JSON.parseObject(result.getMessage() , MemberMoreData.class);
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
