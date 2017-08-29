package com.zjyeshi.dajiujiao.buyer.task.order;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.order.GetOrderListData;
import com.xuan.bigapple.lib.asynctask.helper.Result;

import java.util.HashMap;

/**
 * 查看订单列表任务类
 *
 * Created by wuhk on 2015/11/3.
 */
public class GetOrderListTask extends BaseTask<GetOrderListData> {
    private String url;
    public GetOrderListTask(Context context , String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected Result<GetOrderListData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String , String>();
        paramMap.put("tabTitle"  , (String)params[0]);
        paramMap.put("lastTime" , (String)params[1]);
        paramMap.put("mode" , (String)params[2]);

        if (params.length == 5){
            paramMap.put("shopId" , (String)params[3]);
            paramMap.put("memberId" , (String)params[4]);
        }
        Result<GetOrderListData> result = postCommon(url , paramMap);
        if(result.isSuccess()){
            GetOrderListData retData = JSON.parseObject(result.getMessage() , GetOrderListData.class);
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
