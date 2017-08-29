package com.zjyeshi.dajiujiao.buyer.task.store;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.store.homepage.GetNearbyShopList;
import com.xuan.bigapple.lib.asynctask.helper.Result;

import java.util.HashMap;

/**
 * 附近店铺
 *
 * Created by wuhk on 2015/10/26.
 */
public class NearShopTask extends BaseTask<GetNearbyShopList> {
    public NearShopTask(Context context) {
        super(context);
    }

    @Override
    protected Result<GetNearbyShopList> onHttpRequest(Object... params) {
        HashMap<String ,String> paramMap = new HashMap<String , String>();
        paramMap.put("lngE5" , (String)params[0]);
        paramMap.put("latE5" , (String)params[1]);

        Result<GetNearbyShopList> result = post(UrlConstants.NEARBYSHOPS, paramMap);

        if (result.isSuccess()){
            GetNearbyShopList retData = JSON.parseObject(result.getMessage(), GetNearbyShopList.class);
            result.setMessage(retData.getMessage());
            if (retData.codeOk()){
                result.setValue(retData.getResult());
                //将原来的数据清掉
                DaoFactory.getUnLoginShopDao().deleteAll();
                //将新数据插入数据库中
                DaoFactory.getUnLoginShopDao().insertBatch(result.getValue().getList());
            }else{
                result.setSuccess(false);
            }
        }
        return result;
    }
}
