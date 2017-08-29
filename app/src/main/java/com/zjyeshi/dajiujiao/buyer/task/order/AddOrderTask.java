package com.zjyeshi.dajiujiao.buyer.task.order;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.utils.StringUtils;
import com.xuan.bigapple.lib.utils.sharepreference.BPPreferences;
import com.zjyeshi.dajiujiao.buyer.common.PreferenceConstants;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.order.GetAddOrderData;
import com.zjyeshi.dajiujiao.buyer.entity.order.AddOrderRequest;
import com.xuan.bigapple.lib.asynctask.helper.Result;

import java.util.HashMap;
import java.util.List;

/**
 * 增加订单任务类
 *
 * Created by wuhk on 2015/11/3.
 */
public class AddOrderTask extends BaseTask<GetAddOrderData> {

    public AddOrderTask(Context context) {
        super(context);
    }

    @Override
    protected Result<GetAddOrderData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String , String>();
        AddOrderRequest request  = (AddOrderRequest) params[0];

        paramMap.put("memberId" , request.getMemberId());

        paramMap.put("productIds" , request.getProductIds());
        paramMap.put("prices" , request.getPrices());
        paramMap.put("counts" , request.getCounts());
        paramMap.put("boxType" , request.getBoxType());

        paramMap.put("markCostProductIds" , request.getMarkCostProductIds());
        paramMap.put("markCostPrices" , request.getMarkCostPrices());
        paramMap.put("markCostCounts" , request.getMarkCostCounts());
        paramMap.put("markCostBoxType" , request.getMarkCostBoxType());

        paramMap.put("sellerIds" , request.getSellerIds());
        paramMap.put("addressId" , request.getAddressId());
        paramMap.put("orderType" , request.getOrderType());
        paramMap.put("remark" , request.getRemark());

        String[] shopIds  = StringUtils.split(request.getSellerIds() , ",");
        StringBuilder activities = new StringBuilder();
        String nowShopId = "";
        for (int i = 0 ;  i < shopIds.length ; i ++){
            if (!nowShopId.equals(shopIds[i])){
                activities.append(BPPreferences.instance().getString(PreferenceConstants.getShopActivitiesKey(shopIds[i]) , ""));
                activities.append(",");
                nowShopId = shopIds[i];
            }
        }
        if (activities.length() > 1){
            activities.deleteCharAt(activities.length() - 1);
        }
        paramMap.put("activities" , activities.toString());

        Result<GetAddOrderData> result = postCommon(UrlConstants.ADDORDERV2 , paramMap);

        if (result.isSuccess()){
            GetAddOrderData retData = JSON.parseObject(result.getMessage() , GetAddOrderData.class);
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
