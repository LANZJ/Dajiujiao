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
 * 报销评论
 *
 * Created by zhum on 2016/6/23.
 */
public class AddPaidCommentTask extends BaseTask<NoResultData> {
    public AddPaidCommentTask(Context context) {
        super(context);
    }

    @Override
    protected Result<NoResultData> onHttpRequest(Object... params) {
        HashMap<String , String> paraMap = new HashMap<String , String>();
        paraMap.put("paidThemeId" , (String)params[0]);
        if (params[1]!=null){
            paraMap.put("paidCommentId" , (String)params[1]);
        }
        paraMap.put("content" , (String)params[2]);
        paraMap.put("pics" , (String)params[3]);
        CommentListener.Voice voice  = (CommentListener.Voice)params[4];
        paraMap.put("voice" , voice.getVoiceUrl());
        paraMap.put("voiceLength" , voice.getVoiceLength());


        Result<NoResultData> result = postCommon(UrlConstants.ADDPAIDCOMMENT , paraMap);
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
