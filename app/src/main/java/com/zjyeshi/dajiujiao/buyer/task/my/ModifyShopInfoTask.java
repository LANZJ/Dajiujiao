package com.zjyeshi.dajiujiao.buyer.task.my;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.data.seller.ShopManageData;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/9/19.
 */
public class ModifyShopInfoTask extends BaseTask<NoResultData> {
    public ModifyShopInfoTask(Context context) {
        super(context);
    }

    @Override
    protected Result<NoResultData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        ShopManageData data = (ShopManageData)params[0];

        configParamMap("name" , data.getName() , paramMap);
        configParamMap("lngE5" , data.getLngE5(), paramMap);
        configParamMap("latE5" , data.getLatE5() , paramMap);
        configParamMap("address" , data.getAddress() , paramMap);
        configParamMap("openTime" , data.getOpenTime() , paramMap);
        configParamMap("closeTime" , data.getCloseTime() , paramMap);
        configParamMap("status" , data.getStatus() , paramMap);
        configParamMap("delivery" , String.valueOf(data.isDelivery()) , paramMap);
        configParamMap("deliverMinCost" , data.getDeliverMinCost() , paramMap);
        configParamMap("marketCost" , data.getMarketCost() , paramMap);

        Result<NoResultData> result = postCommon(UrlConstants.MODIFYSHOP , paramMap);
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

    private void configParamMap (String key , String value , HashMap<String , String> paramMap){
        if (Validators.isEmpty(value)){
            value = "";
        }
        paramMap.put(key , value);

    }
}
