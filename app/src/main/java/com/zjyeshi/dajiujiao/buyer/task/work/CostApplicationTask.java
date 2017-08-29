package com.zjyeshi.dajiujiao.buyer.task.work;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;

import java.util.HashMap;

/**费用盛情
 * Created by wuhk on 2016/6/22.
 */
public class CostApplicationTask extends BaseTask<NoResultData> {
    public CostApplicationTask(Context context) {
        super(context);
    }

    @Override
    protected Result<NoResultData> onHttpRequest(Object... params) {
        HashMap<String ,String> paramMap = new HashMap<String, String>();
        paramMap.put("approver" , (String)params[0]);
        paramMap.put("applicantType" , (String)params[1]);
        paramMap.put("applicationMoney" , (String)params[2]);
        paramMap.put("remark" , (String)params[3]);
        paramMap.put("pics" , (String)params[4]);

        Result<NoResultData> result = postCommon(UrlConstants.COSTAPPLICATION , paramMap);
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
