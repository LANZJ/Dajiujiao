package com.zjyeshi.dajiujiao.buyer.task.order;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.pay.Wallet;

import java.util.HashMap;

/**
 * 优惠券和钱包支付
 * Created by wuhk on 2016/9/27.
 */
public class CouponAndWalletTask extends BaseTask<Wallet> {
    public static final String USE_WALLLET = "1";//使用钱包
    public static final String NO_USE = "2";//不适用钱包

    public CouponAndWalletTask(Context context) {
        super(context);
    }

    @Override
    protected Result<Wallet> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("orderId" , (String)params[0]);
        paramMap.put("addressId" , (String)params[1]);
        paramMap.put("myCouponsId" , (String)params[2]);
        paramMap.put("type" , (String)params[3]);
        paramMap.put("useMarketCost" , (String)params[4]);
        paramMap.put("orderPreferentialActivities",(String)params[5]);
        Result<Wallet> result = postCommon(UrlConstants.COUPONSANDWALLET , paramMap);
        if (result.isSuccess()){
            Wallet retData = JSON.parseObject(result.getMessage() , Wallet.class);
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
