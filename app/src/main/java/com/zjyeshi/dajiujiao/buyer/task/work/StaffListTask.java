package com.zjyeshi.dajiujiao.buyer.task.work;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.entity.enums.ApproveEnum;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.StaffListData;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/6/20.
 */
public class StaffListTask extends BaseTask<StaffListData> {
    public StaffListTask(Context context) {
        super(context);
    }

    @Override
    protected Result<StaffListData> onHttpRequest(Object... params) {
        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("type", String.valueOf(ApproveEnum.ALL.getValue()));

        Result<StaffListData> result = postCommon(UrlConstants.STAFFLIST, paramMap);
        if (result.isSuccess()) {
            StaffListData retData = JSON.parseObject(result.getMessage(), StaffListData.class);
            result.setMessage(retData.getMessage());
            if (retData.codeOk()) {
                result.setValue(retData.getResult());
            } else {
                result.setSuccess(false);
            }
        }
        return result;
    }
}
