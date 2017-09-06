package com.zjyeshi.dajiujiao.buyer.task.store;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.store.goods.GoodList;
import com.xuan.bigapple.lib.asynctask.helper.Result;

import java.util.HashMap;

/**
 * Created by wuhk on 2015/10/28.
 */
public class GoodListTask extends BaseTask<GoodList> {
    public GoodListTask(Context context) {
        super(context);
    }

    @Override
    protected Result<GoodList> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String , String>();
        paramMap.put("shopId" , (String)params[0]);
        paramMap.put("categoryId" ,(String)params[1]);
        paramMap.put("orderId" , (String) params[2]);
        String memberId = (String) params[3];
        paramMap.put("page", (String) params[4]);
        if (Validators.isEmpty(memberId)){
            paramMap.put("userId" , LoginedUser.getLoginedUser().getId());
        }else{
            paramMap.put("userId" , memberId);
        }


        Result<GoodList> result = post(UrlConstants.LISTPRODUCTS , paramMap);

        if (result.isSuccess()){
            GoodList retData = JSON.parseObject(result.getMessage() , GoodList.class);
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
