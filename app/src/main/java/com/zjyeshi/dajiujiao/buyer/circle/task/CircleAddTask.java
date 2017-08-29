package com.zjyeshi.dajiujiao.buyer.circle.task;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.circle.task.data.CircleAddData;
import com.xuan.bigapple.lib.asynctask.helper.Result;

import java.util.HashMap;

/**
 * 添加朋友圈
 *
 * Created by xuan on 15/11/9.
 */
public class CircleAddTask extends BaseTask<CircleAddData> {
    public CircleAddTask(Context context) {
        super(context);
    }

    @Override
    protected Result<CircleAddData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("content" , (String)params[0]);
        paramMap.put("pics" , (String)params[1]);
        if (params.length > 2){
            paramMap.put("linkType" , (String)params[2]);
            paramMap.put("linkPic" , (String)params[3]);
            paramMap.put("linkTitle" , (String)params[4]);
            paramMap.put("linkContent" , (String)params[5]);
        }

        Result<CircleAddData> result = postCommon(UrlConstants.CIRCLE_ADD_CIRCLE , paramMap);

        if(result.isSuccess()){
            CircleAddData retData = JSON.parseObject(result.getMessage(), CircleAddData.class);
            result.setMessage(retData.getMessage());
            if (retData.codeOk()){
                //发送成功,需要修改圈子的状态
                result.setValue(retData.getResult());
            }else{
                //发送失败也需要修改圈子的状态
                result.setSuccess(false);
            }
        }
        return result;
    }

}
