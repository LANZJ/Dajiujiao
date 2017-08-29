package com.zjyeshi.dajiujiao.buyer.task.order;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.entity.order.AddOrderRequest;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/7/20.
 */
public class ModifyOrderTask extends BaseTask<NoResultData> {
    public ModifyOrderTask(Context context) {
        super(context);
    }

    @Override
    protected Result<NoResultData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String , String>();
        AddOrderRequest request = (AddOrderRequest)params[0];

        paramMap.put("orderId" , request.getOrderId());

        paramMap.put("productIds" , request.getProductIds());
        paramMap.put("prices" , request.getPrices());
        paramMap.put("counts" , request.getCounts());
        paramMap.put("boxType" , request.getBoxType());

        paramMap.put("markCostProductIds" , request.getMarkCostProductIds());
        paramMap.put("markCostPrices" , request.getMarkCostPrices());
        paramMap.put("markCostCounts" , request.getMarkCostCounts());
        paramMap.put("markCostBoxType" , request.getMarkCostBoxType());

        paramMap.put("sellerIds" , request.getSellerIds());
        paramMap.put("totalPrice" , request.getTotalPrice());

        paramMap.put("useMarketCost" , String.valueOf(request.isUseMarketCost()));
        paramMap.put("editProduct" , String.valueOf(request.isEditProduct()));

        Result<NoResultData> result = postCommon(UrlConstants.MODIFYORDERV2 , paramMap);
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
