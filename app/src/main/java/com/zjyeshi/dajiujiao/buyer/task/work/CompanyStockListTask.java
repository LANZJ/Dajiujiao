package com.zjyeshi.dajiujiao.buyer.task.work;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.CompanyStockListData;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/6/23.
 */
public class CompanyStockListTask extends BaseTask<CompanyStockListData> {
    public CompanyStockListTask(Context context) {
        super(context);
    }

    @Override
    protected Result<CompanyStockListData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("memberId" , (String)params[0]);
        paramMap.put("categoryId" , (String)params[1]);

        Result<CompanyStockListData> result = postCommon(UrlConstants.PRODUCTSINVENTORY , paramMap);
        if (result.isSuccess()){
            CompanyStockListData retData = JSON.parseObject(result.getMessage() , CompanyStockListData.class);
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
