package com.zjyeshi.dajiujiao.buyer.task.store;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.store.goods.GoodCommentList;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/4/7.
 */
public class ListEvaluateTask extends BaseTask<GoodCommentList> {
    public ListEvaluateTask(Context context) {
        super(context);
    }

    @Override
    protected Result<GoodCommentList> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String , String>();
        paramMap.put("productId" , (String)params[0]);
        paramMap.put("lastTime" , (String)params[1]);
        paramMap.put("mode" , (String)params[2]);

        Result<GoodCommentList> result = post(UrlConstants.LISTEVALUATE , paramMap);
        if (result.isSuccess()){
            GoodCommentList retData = JSON.parseObject(result.getMessage() , GoodCommentList.class);
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
