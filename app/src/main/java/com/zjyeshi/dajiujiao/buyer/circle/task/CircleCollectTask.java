package com.zjyeshi.dajiujiao.buyer.circle.task;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.entity.CircleCollect;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/8/15.
 */
public class CircleCollectTask extends BaseTask<NoResultData> {
    public CircleCollectTask(Context context) {
        super(context);
    }

    @Override
    protected Result<NoResultData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap =  new HashMap<String, String>();
        CircleCollect collect = (CircleCollect)params[0];
        paramMap.put("fromMemberId" , collect.getFromMemberId());
        paramMap.put("pics" , collect.getPic());
        paramMap.put("content" , collect.getContent());
        paramMap.put("linkType" , String.valueOf(collect.getLink_type()));
        paramMap.put("linkPic" , collect.getLink_pic());
        paramMap.put("linkTitle" , collect.getLink_title());
        paramMap.put("linkContent" , collect.getLink_content());

        Result<NoResultData> result = postCommon(UrlConstants.COLLECTION , paramMap);
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
