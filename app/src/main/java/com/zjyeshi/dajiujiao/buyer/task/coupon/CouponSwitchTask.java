package com.zjyeshi.dajiujiao.buyer.task.coupon;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;

import java.util.HashMap;

/**
 * 是否启用优惠券
 * Created by wuhk on 2016/9/30.
 */
public class CouponSwitchTask extends BaseTask<NoResultData> {
    public CouponSwitchTask(Context context) {
        super(context);
    }

    @Override
    protected Result<NoResultData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("couponsId" ,  (String)params[0]);
        paramMap.put("couponsSwitch" , (String)params[1]);

        Result<NoResultData> result = postCommon(UrlConstants.COUPONSWITCH , paramMap);
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
