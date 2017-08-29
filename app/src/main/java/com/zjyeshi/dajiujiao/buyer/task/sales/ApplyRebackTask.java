package com.zjyeshi.dajiujiao.buyer.task.sales;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.entity.sales.RebackOrderRequest;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.HashMap;

/**
 * Created by wuhk on 2017/5/14.
 */

public class ApplyRebackTask extends BaseTask<NoResultData> {
    public ApplyRebackTask(Context context) {
        super(context);
    }

    @Override
    protected Result<NoResultData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        RebackOrderRequest request = (RebackOrderRequest)params[0];
        paramMap.put("orderId" , request.getOrderId());
        paramMap.put("type" , String.valueOf(request.getType()));
        paramMap.put("returnReason" , request.getReturnReason());
        if(!Validators.isEmpty(request.getPics())){
            paramMap.put("pics" , request.getPics());
        }
        paramMap.put("orderProductIds" , request.getOrderProductIds());
        paramMap.put("nums" , request.getNums());
        paramMap.put("boxTypes" , String.valueOf(request.getBoxType()));
        paramMap.put("markCostOrderProductIds" , request.getMarkCostOrderProductIds());
        paramMap.put("markCostNums" , request.getMarkCostNums());
        paramMap.put("markCostBoxTypes" , String.valueOf(request.getMarkCostBoxTypes()));

        Result<NoResultData> result = postCommon(UrlConstants.APPLYREBACKORDER , paramMap);
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

    public static void applyRebackOrder(Context context , final RebackOrderRequest request , AsyncTaskSuccessCallback<NoResultData> successCallback){
        ApplyRebackTask applyRebackTask = new ApplyRebackTask(context);
        applyRebackTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        applyRebackTask.setAsyncTaskSuccessCallback(successCallback);

        applyRebackTask.execute(request);
    }
}
