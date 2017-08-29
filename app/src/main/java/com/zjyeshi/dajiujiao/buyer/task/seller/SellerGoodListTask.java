package com.zjyeshi.dajiujiao.buyer.task.seller;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.store.goods.GoodList;
import com.xuan.bigapple.lib.asynctask.helper.Result;

import java.util.HashMap;

/**
 * 卖家商品列表
 *
 * Created by wuhk on 2015/11/5.
 */
public class SellerGoodListTask extends BaseTask<GoodList> {
    public SellerGoodListTask(Context context) {
        super(context);
    }

    @Override
    protected Result<GoodList> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String , String>();

        Result<GoodList> result = postCommon(UrlConstants.LISTPRODUCTSBYBUSINESS , paramMap);

        if (result.isSuccess()){
            GoodList retData = JSON.parseObject(result.getMessage(), GoodList.class);
            result.setMessage(retData.getMessage());
            if (retData.codeOk()){
                result.setValue(retData.getResult());
                //将红酒插入数据库
                DaoFactory.getMyWineDao().insertBatch(result.getValue().getList());
            }else{
                result.setSuccess(false);
            }
        }
        return result;
    }
}
