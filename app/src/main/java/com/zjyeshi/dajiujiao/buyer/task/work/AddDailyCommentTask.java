package com.zjyeshi.dajiujiao.buyer.task.work;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.views.comment.listener.CommentListener;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/7/18.
 */
public class AddDailyCommentTask extends BaseTask<NoResultData> {
    public AddDailyCommentTask(Context context) {
        super(context);
    }

    @Override
    protected Result<NoResultData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("dailyId" , (String)params[0]);
        paramMap.put("dailyCommentId" , (String)params[1]);
        paramMap.put("content" , (String)params[2]);
        paramMap.put("pics" , (String)params[3]);
        CommentListener.Voice voice = (CommentListener.Voice)params[4];
        paramMap.put("voice" , voice.getVoiceUrl());
        paramMap.put("voiceLength" , voice.getVoiceLength());

        Result<NoResultData> result = postCommon(UrlConstants.ADDDAILYCOMMENT , paramMap);
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
