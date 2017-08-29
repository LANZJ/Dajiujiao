package com.zjyeshi.dajiujiao.buyer.task.work;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.data.order.PathResData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/12/29.
 */
public class GetFeeApplyPathTask extends BaseTask<PathResData> {
    public GetFeeApplyPathTask(Context context) {
        super(context);
    }

    @Override
    protected Result<PathResData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<>();
        FeeApplyReq req = (FeeApplyReq)params[0];
        paramMap.put("costId" , req.getCostId());
        paramMap.put("paidId" , req.getPaidId());

        Result<PathResData> result = postCommon(UrlConstants.GETFEEAPPLYPATH , paramMap);
        if (result.isSuccess()){
            PathResData retData = JSON.parseObject(result.getMessage() , PathResData.class);
            result.setMessage(retData.getMessage());
            if (retData.codeOk()){
                result.setValue(retData.getResult());
            }else{
                result.setSuccess(false);
            }
        }
        return result;
    }


    public static void getFeePath(Context context , FeeApplyReq req , AsyncTaskSuccessCallback<PathResData> successCallback){
        GetFeeApplyPathTask getFeeApplyPathTask  = new GetFeeApplyPathTask(context);
        getFeeApplyPathTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<PathResData>() {
            @Override
            public void failCallback(Result<PathResData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });
        getFeeApplyPathTask.setAsyncTaskSuccessCallback(successCallback);

        getFeeApplyPathTask.execute(req);
    }


    public static class FeeApplyReq{
        private String costId;
        private String paidId;

        public FeeApplyReq(String costId, String paidId) {
            if (Validators.isEmpty(costId)){
                this.costId = "";
            }else{
                this.costId = costId;
            }

            if (Validators.isEmpty(paidId)){
                this.paidId = "";
            }else{
                this.paidId = paidId;
            }
        }

        public String getCostId() {
            return costId;
        }

        public void setCostId(String costId) {
            this.costId = costId;
        }

        public String getPaidId() {
            return paidId;
        }

        public void setPaidId(String paidId) {
            this.paidId = paidId;
        }
    }
}
