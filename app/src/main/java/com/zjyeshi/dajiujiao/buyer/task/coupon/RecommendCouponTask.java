package com.zjyeshi.dajiujiao.buyer.task.coupon;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.my.RecommendCouponData;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;

import java.util.HashMap;

/**
 * 订单推荐优惠券
 * Created by wuhk on 2016/9/29.
 */
public class RecommendCouponTask extends BaseTask<RecommendCouponData> {
    public RecommendCouponTask(Context context) {
        super(context);
    }

    @Override
    protected Result<RecommendCouponData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("orderIds" , (String)params[0]);

        Result<RecommendCouponData> result = postCommon(UrlConstants.RECOMMENDCOUPON , paramMap);
        if (result.isSuccess()){
            RecommendCouponData retData = JSON.parseObject(result.getMessage() , RecommendCouponData.class);
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
