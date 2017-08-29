package com.zjyeshi.dajiujiao.buyer.task.work;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.SubSetListData;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/6/24.
 */
public class SubSetListTask extends BaseTask<SubSetListData> {
    public SubSetListTask(Context context) {
        super(context);
    }

    @Override
    protected Result<SubSetListData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("memberId"  , (String)params[0]);

        Result<SubSetListData> result = postCommon(UrlConstants.SUBSETLIST , paramMap);
        if (result.isSuccess()){
            SubSetListData retData = JSON.parseObject(result.getMessage() , SubSetListData.class);
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
