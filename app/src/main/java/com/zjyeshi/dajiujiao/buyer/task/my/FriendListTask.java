package com.zjyeshi.dajiujiao.buyer.task.my;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.task.data.my.FriendListData;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/8/16.
 */
public class FriendListTask extends BaseTask<FriendListData> {
    public FriendListTask(Context context) {
        super(context);
    }

    @Override
    protected Result<FriendListData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();

        Result<FriendListData> result = postCommon(UrlConstants.FRIENDLIST , paramMap);
        if (result.isSuccess()){
            FriendListData retData = JSON.parseObject(result.getMessage() , FriendListData.class);
            result.setMessage(retData.getMessage());
            if (retData.codeOk()){
                result.setValue(retData.getResult());
                for (FriendListData.Friend friend : result.getValue().getList()){
                    friend.setOwnerUserId(LoginedUser.getLoginedUser().getId());
                }
                DaoFactory.getContactDao().deleteAll();
                DaoFactory.getContactDao().insertBatch(result.getValue().getList());
            }else{
                result.setSuccess(false);
            }
        }
        return result;
    }
}
