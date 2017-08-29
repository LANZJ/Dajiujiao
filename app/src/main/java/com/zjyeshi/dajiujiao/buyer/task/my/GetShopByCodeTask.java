package com.zjyeshi.dajiujiao.buyer.task.my;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.my.ShopData;
import com.xuan.bigapple.lib.asynctask.helper.Result;

import java.util.HashMap;

/**
 * 根据酒友码返回商铺信息
 *
 * Created by xuan on 15/11/27.
 */
public class GetShopByCodeTask extends BaseTask<ShopData> {
    public GetShopByCodeTask(Context context) {
        super(context);
    }

    @Override
    protected Result<ShopData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("drinkingcode" , (String)params[0]);

        Result<ShopData> result = postCommon(UrlConstants.DRINKINGCODE_SHOP, paramMap);

        if(result.isSuccess()){
            ShopData retData = JSON.parseObject(result.getMessage() , ShopData.class);
            retData.setMessage(result.getMessage());
            if (retData.codeOk()){
                result.setValue(retData.getResult());
            }else{
                result.setSuccess(false);
            }
        }
        return result;
    }

}
