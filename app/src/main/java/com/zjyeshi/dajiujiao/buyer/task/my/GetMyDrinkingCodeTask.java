package com.zjyeshi.dajiujiao.buyer.task.my;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.utils.sharepreference.BPPreferences;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.my.DrinkingCodeData;
import com.xuan.bigapple.lib.asynctask.helper.Result;

import java.util.HashMap;

/**
 * 获取我的酒友码
 * Created by wuhk on 2015/11/30.
 */
public class GetMyDrinkingCodeTask extends BaseTask<DrinkingCodeData> {
    public GetMyDrinkingCodeTask(Context context) {
        super(context);
    }

    @Override
    protected Result<DrinkingCodeData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String , String>();

        Result<DrinkingCodeData> result = postCommon(UrlConstants.DRINKINGCODE_BY_SHOPID , paramMap);
        if (result.isSuccess()){
            DrinkingCodeData retData = JSON.parseObject(result.getMessage() , DrinkingCodeData.class);
            result.setMessage(retData.getMessage());
            if (retData.codeOk()){
                result.setValue(retData.getResult());
                BPPreferences.instance().putString("drinkCode" , result.getValue().getDrinkingCode());
            }else{
                result.setSuccess(false);
            }
        }
        return result;
    }
}
