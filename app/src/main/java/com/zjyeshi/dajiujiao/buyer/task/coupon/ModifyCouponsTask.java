package com.zjyeshi.dajiujiao.buyer.task.coupon;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;

import java.util.HashMap;

/**
 * 修改店铺优惠券
 * Created by wuhk on 2016/9/30.
 */
public class ModifyCouponsTask extends BaseTask<NoResultData> {
    public ModifyCouponsTask(Context context) {
        super(context);
    }

    @Override
    protected Result<NoResultData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("id" , (String)params[0]);
        paramMap.put("fulfilMoney" , (String)params[1]);
        paramMap.put("minusMoney" , (String)params[2]);
        paramMap.put("startTime" , (String)params[3]);
        paramMap.put("endTime" , (String)params[4]);
        paramMap.put("applicationType" , (String)params[5]);

        Result<NoResultData> result = postCommon(UrlConstants.MODIFYCOUPONS , paramMap);
        if (result.isSuccess()){
            NoResultData retData = JSON.parseObject(result.getMessage() , NoResultData.class);
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
