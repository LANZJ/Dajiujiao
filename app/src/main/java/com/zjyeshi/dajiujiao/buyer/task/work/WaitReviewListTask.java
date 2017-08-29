package com.zjyeshi.dajiujiao.buyer.task.work;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.WaitReviewListData;

import java.util.HashMap;

/**
 * 待我审批
 *
 * Created by zhum on 2016/6/23.
 */
public class WaitReviewListTask extends BaseTask<WaitReviewListData>{
    public WaitReviewListTask(Context context) {
        super(context);
    }

    @Override
    protected Result<WaitReviewListData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("status" , (String)params[0]);

        Result<WaitReviewListData> result = postCommon(UrlConstants.WAITREVIEWLIST , paramMap);
        if (result.isSuccess()){
            WaitReviewListData waitReviewListData = JSON.parseObject(result.getMessage() , WaitReviewListData.class);
            result.setMessage(waitReviewListData.getMessage());
            if (waitReviewListData.codeOk()){
                result.setValue(waitReviewListData.getResult());
            }else{
                result.setSuccess(false);
            }
        }
        return result;
    }
}
