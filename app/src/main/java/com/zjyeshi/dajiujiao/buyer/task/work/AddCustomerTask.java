package com.zjyeshi.dajiujiao.buyer.task.work;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;

import java.util.HashMap;

/**
 * 添加客户
 *
 * Created by zhum on 2016/6/20.
 */
public class AddCustomerTask extends BaseTask<NoResultData> {
    public AddCustomerTask(Context context) {
        super(context);
    }

    @Override
    protected Result<NoResultData> onHttpRequest(Object... params) {
        HashMap<String , String> paraMap = new HashMap<String , String>();
        paraMap.put("name" , (String)params[0]);
        paraMap.put("phone" , (String)params[1]);
        paraMap.put("legalPerson" , (String)params[2]);
        paraMap.put("turnover" , (String)params[3]);
        paraMap.put("rightPerson" , (String)params[4]);
        paraMap.put("salemensQuantity" , (String)params[5]);
        paraMap.put("personQuantity" , (String)params[6]);
        paraMap.put("area" , (String)params[7]);
        paraMap.put("address" , ((String)params[8]).trim());
        paraMap.put("businessArea" , (String)params[9]);
        paraMap.put("warehouseArea" , (String)params[10]);
        paraMap.put("paymentMethod" , (String)params[11]);
        paraMap.put("productStatus" , (String)params[12]);
        paraMap.put("competingProducts" , (String)params[13]);
        paraMap.put("remark" , (String)params[14]);
        paraMap.put("pic" , (String)params[15]);
        paraMap.put("id" , (String)params[16]);
        paraMap.put("memberId" , (String)params[17]);
        paraMap.put("landlinePhone" , (String)params[18]);

        Result<NoResultData> result = postCommon(UrlConstants.ADDCUSTOMER , paraMap);
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
