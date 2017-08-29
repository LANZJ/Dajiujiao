package com.zjyeshi.dajiujiao.buyer.activity.rong.get;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.dao.Gettoken;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;

import java.util.HashMap;

/**
 * Created by lan on 2017/7/6.
 */
public class GettokenTask extends BaseTask<Gettoken> {
    public GettokenTask(Context context) {
        super(context);
    }

    @Override
    protected Result<Gettoken> onHttpRequest(Object... params) {
        HashMap<String,String> paramMap = new HashMap<String , String>();
        String userId = (String)params[0];
        String name  = (String)params[1];
        String portraitUri= (String) params[2];
        paramMap.put("userId" ,userId);
        paramMap.put("name" ,name);
        paramMap.put("portraitUri" ,portraitUri);


        Result<Gettoken> result = post(UrlConstants.RUN ,paramMap);
        if (result.isSuccess()){
            Gettoken retData = JSON.parseObject(result.getMessage() , Gettoken.class);
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
