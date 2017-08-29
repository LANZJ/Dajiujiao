package com.zjyeshi.dajiujiao.buyer.task.coupon;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.my.CouponListData;

import java.util.HashMap;

/**
 * 店铺优惠券
 * Created by wuhk on 2016/9/30.
 */
public class ShopCouponsTask extends BaseTask<CouponListData> {
    public ShopCouponsTask(Context context) {
        super(context);
    }

    @Override
    protected Result<CouponListData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();

        Result<CouponListData> result = postCommon(UrlConstants.SHOPCOUPONS , paramMap);
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
