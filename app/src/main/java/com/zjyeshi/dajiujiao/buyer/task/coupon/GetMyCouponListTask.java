package com.zjyeshi.dajiujiao.buyer.task.coupon;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.my.CouponListData;

import java.util.HashMap;

/**
 * 我的优惠券
 * Created by wuhk on 2016/9/27.
 */
public class GetMyCouponListTask extends BaseTask<CouponListData> {
    public GetMyCouponListTask(Context context) {
        super(context);
    }

    @Override
    protected Result<CouponListData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();

        Result<CouponListData> result = postCommon(UrlConstants.GETMYCOUPONLIST , paramMap);
        if (result.isSuccess()){
            CouponListData retData = JSON.parseObject(result.getMessage() , CouponListData.class);
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
