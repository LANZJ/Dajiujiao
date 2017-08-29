package com.zjyeshi.dajiujiao.buyer.circle.task;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.circle.task.data.GetPraiseData;
import com.xuan.bigapple.lib.asynctask.helper.Result;

import java.util.HashMap;

/**
 * 酒友圈点赞（取消赞）
 * Created by wuhk on 2015/11/17.
 */
public class CirclePraiseTask extends BaseTask<GetPraiseData> {
    public CirclePraiseTask(Context context) {
        super(context);
    }

    @Override
    protected Result<GetPraiseData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String , String>();
        paramMap.put("circleId" , (String)params[0]);
        paramMap.put("operateType" , (String)params[1]);

        Result<GetPraiseData> result = postCommon(UrlConstants.PRAISE , paramMap);

        if (result.isSuccess()){
            GetPraiseData retData = JSON.parseObject(result.getMessage() , GetPraiseData.class);
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
