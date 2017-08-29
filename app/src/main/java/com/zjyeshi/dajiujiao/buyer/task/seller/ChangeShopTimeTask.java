package com.zjyeshi.dajiujiao.buyer.task.seller;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.xuan.bigapple.lib.asynctask.helper.Result;

import java.util.HashMap;

/**
 * Created by wuhk on 2015/11/30.
 */
public class ChangeShopTimeTask extends BaseTask<NoResultData> {

    public ChangeShopTimeTask(Context context) {
        super(context);
    }

    @Override
    protected Result<NoResultData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String , String>();
//        paramMap.put("name" , (String)params[0]);
//        paramMap.put("lngE5" , (String)params[1]);
//        paramMap.put("latE5" , (String)params[2]);
//        paramMap.put("address" , (String)params[3]);
        paramMap.put("openTime" , (String)params[0]);
        paramMap.put("closeTime" , (String)params[1]);
//        paramMap.put("status" , (String)params[6]);

        Result<NoResultData> result = postCommon(UrlConstants.MODIFYSHOP , paramMap);

        if (result.isSuccess()){
            NoResultData retData = JSON.parseObject(result.getMessage(), NoResultData.class);
            result.setMessage(retData.getMessage());
            if(retData.codeOk()){
                result.setValue(retData.getResult());
            }else{
                result.setSuccess(false);
            }
        }
        return result;
    }
}
