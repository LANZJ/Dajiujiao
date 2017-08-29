package com.zjyeshi.dajiujiao.buyer.task.store;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.store.homepage.ALLStoreData;
import com.zjyeshi.dajiujiao.buyer.task.data.store.homepage.GetNearbyShopList;
import com.xuan.bigapple.lib.asynctask.helper.Result;

import java.util.HashMap;
import java.util.List;

/**
 * 查看收藏店铺列表任务类
 *
 * Created by wuhk on 2015/11/11.
 */
public class FollowShopTask extends BaseTask<GetNearbyShopList> {
    public FollowShopTask(Context context) {
        super(context);
    }

    @Override
    protected Result<GetNearbyShopList> onHttpRequest(Object... params) {
        HashMap<String ,String> paramMap = new HashMap<String , String>();
        paramMap.put("lngE5" , (String)params[0]);
        paramMap.put("latE5" , (String)params[1]);

        Result<GetNearbyShopList> result = postCommon(UrlConstants.FOLLOWSHOPS, paramMap);

        if (result.isSuccess()){
            GetNearbyShopList retData = JSON.parseObject(result.getMessage(), GetNearbyShopList.class);
            result.setMessage(retData.getMessage());
            if (retData.codeOk()){
                //删除之前的数据
                DaoFactory.getShopsDao().deleteAll();
                result.setValue(retData.getResult());
                //插入新数据
                List<ALLStoreData> followList = result.getValue().getList();
                for (ALLStoreData allStoreData : followList){
                    allStoreData.setFollowed(true);
                }
                DaoFactory.getShopsDao().insertBatch(followList);
            }else{
                result.setSuccess(false);
            }
        }
        return result;
    }
}
