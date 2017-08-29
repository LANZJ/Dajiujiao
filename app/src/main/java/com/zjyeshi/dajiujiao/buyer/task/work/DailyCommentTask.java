package com.zjyeshi.dajiujiao.buyer.task.work;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.DailyCommentInfo;

import java.util.HashMap;

/**
 * 日报评价列表
 * Created by wuhk on 2016/7/18.
 */
public class DailyCommentTask extends BaseTask<DailyCommentInfo> {
    public DailyCommentTask(Context context) {
        super(context);
    }

    @Override
    protected Result<DailyCommentInfo> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("dailyId" , (String)params[0]);

        Result<DailyCommentInfo> result = postCommon(UrlConstants.DAILYCOMMENTINFO , paramMap);
        if (result.isSuccess()){
            DailyCommentInfo retData = JSON.parseObject(result.getMessage() , DailyCommentInfo.class);
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
