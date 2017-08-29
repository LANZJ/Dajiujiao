package com.zjyeshi.dajiujiao.buyer.task.sales;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.entity.sales.AddSalesRequest;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.sales.data.SalesDetailData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.HashMap;

/**
 * Created by wuhk on 2017/5/8.
 */

public class AddSalesInfoTask extends BaseTask<NoResultData> {
    public AddSalesInfoTask(Context context) {
        super(context);
    }

    @Override
    protected Result<NoResultData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        AddSalesRequest request = (AddSalesRequest)params[0];
        paramMap.put("id" , getNotNullValue(request.getId()));
        paramMap.put("shopId" , getNotNullValue(request.getShopId()));
        paramMap.put("shopIds" , getNotNullValue(request.getShopIds()));
        paramMap.put("productIds" , getNotNullValue(request.getProductIds()));
        paramMap.put("satisfyType" , getNotNullValue(String.valueOf(request.getSatisfyType())));
        paramMap.put("favouredType" , getNotNullValue(String.valueOf(request.getFavouredType())));
        paramMap.put("satisfyCondition" , getNotNullValue(request.getSatisfyCondition()));
        paramMap.put("favouredPolicy" , getNotNullValue(request.getFavouredPolicy()));
        paramMap.put("startTime" , getNotNullValue(request.getStartTime()));
        paramMap.put("formType" , getNotNullValue(String.valueOf(request.getFormType())));
        paramMap.put("endTime" , getNotNullValue(request.getEndTime()));
        paramMap.put("giftId" , getNotNullValue(request.getGiftId()));
        paramMap.put("url" , getNotNullValue(request.getUrl()));
        paramMap.put("superposition" , getNotNullValue(String.valueOf(request.isSuperposition())));
        paramMap.put("priority" , getNotNullValue(String.valueOf(request.getPriority())));
        paramMap.put("giveProductId" , getNotNullValue(request.getGiveProductId()));
        paramMap.put("giveProductNum" , getNotNullValue(request.getGiveProductNum()));


        Result<NoResultData> result = postCommon(UrlConstants.ADDORMODIFYSALES, paramMap);
        if (result.isSuccess()) {
            NoResultData retData = JSON.parseObject(result.getMessage(), NoResultData.class);
            result.setMessage(retData.getMessage());
            if (retData.codeOk()) {
                result.setValue(retData.getResult());
            } else {
                result.setSuccess(false);
            }
        }
        return result;
    }

    private String getNotNullValue(String param){
        if (null == param){
            param = "";
        }
        return param;
    }


    /**
     * 添加或修改活动
     *
     * @param context
     * @param request
     * @param successCallback
     */
    public static void addSalesInfo(Context context, final AddSalesRequest request, AsyncTaskSuccessCallback<NoResultData> successCallback) {
        AddSalesInfoTask addSalesInfoTask = new AddSalesInfoTask(context);

        addSalesInfoTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        addSalesInfoTask.setAsyncTaskSuccessCallback(successCallback);

        addSalesInfoTask.execute(request);
    }
}
