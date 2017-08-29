package com.zjyeshi.dajiujiao.buyer.task.seller;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.data.seller.ShopManageData;
import com.xuan.bigapple.lib.asynctask.helper.Result;

import java.util.HashMap;

/**
 * 卖家查看店铺信息
 * Created by wuhk on 2015/11/13.
 */
public class GetShopInfoTask extends BaseTask<ShopManageData> {
    public GetShopInfoTask(Context context) {
        super(context);
    }

    @Override
    protected Result<ShopManageData> onHttpRequest(Object... params) {
        HashMap<String  , String > paramMap = new HashMap<String  ,String >();

        Result<ShopManageData> result = postCommon(UrlConstants.GETSHOPDETAILBYB , paramMap);
        if (result.isSuccess()){
            ShopManageData retData = JSON.parseObject(result.getMessage() , ShopManageData.class);
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
