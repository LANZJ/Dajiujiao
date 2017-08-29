package com.zjyeshi.dajiujiao.buyer.task.my;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.my.FriendApplyListData;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/8/16.
 */
public class FriendApplyListTask extends BaseTask<FriendApplyListData> {
    public FriendApplyListTask(Context context) {
        super(context);
    }

    @Override
    protected Result<FriendApplyListData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();

        Result<FriendApplyListData> result  = postCommon(UrlConstants.FRIENDAPPLYLIST , paramMap);
        if (result.isSuccess()){
            FriendApplyListData retData = JSON.parseObject(result.getMessage() , FriendApplyListData.class);
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
