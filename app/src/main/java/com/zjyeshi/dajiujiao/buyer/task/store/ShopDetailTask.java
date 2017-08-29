package com.zjyeshi.dajiujiao.buyer.task.store;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigdog.lib.location.DGLocationUtils;
import com.zjyeshi.dajiujiao.buyer.activity.store.ShopDetailActivity;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.store.shopdetails.ShopDetailData;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.HashMap;

/**
 * 商品详情任务类
 * <p>
 * Created by wuhk on 2015/10/28.
 */
public class ShopDetailTask extends BaseTask<ShopDetailData> {

    public ShopDetailTask(Context context) {
        super(context);
    }

    @Override
    protected Result<ShopDetailData> onHttpRequest(Object... params) {
        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("shopId", (String) params[0]);
        paramMap.put("latE5", (String) params[1]);
        paramMap.put("lngE5", (String) params[2]);

        Result<ShopDetailData> result = postCommon(UrlConstants.SHOPDETAILS, paramMap);

        if (result.isSuccess()) {
            ShopDetailData retData = JSON.parseObject(result.getMessage(), ShopDetailData.class);
            result.setMessage(retData.getMessage());
            if (retData.codeOk()) {
                result.setValue(retData.getResult());
            } else {
                result.setSuccess(false);
            }
        }
        return result;
    }


    /**
     * 获取店铺详情
     *
     * @param context
     * @param shopId
     * @param successCallback
     */
    public static void getShopDetail(final Context context, final String shopId, final AsyncTaskSuccessCallback<ShopDetailData> successCallback) {
        //首次进来的时候，先请求完店铺详情，然后再去请求商品列表
        DGLocationUtils.init(context, new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                String lat = String.valueOf((int) (bdLocation.getLatitude() * 100000));
                String lng = String.valueOf((int) (bdLocation.getLongitude() * 100000));

                final ShopDetailTask shopDetailTask = new ShopDetailTask(context);
                shopDetailTask.setShowProgressDialog(true);
                shopDetailTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<ShopDetailData>() {
                    @Override
                    public void failCallback(Result<ShopDetailData> result) {
                        ToastUtil.toast(result.getMessage());
                    }
                });

                shopDetailTask.setAsyncTaskSuccessCallback(successCallback);

                shopDetailTask.execute(shopId, lat, lng);
                DGLocationUtils.stop();
            }
        });

        DGLocationUtils.start();

    }
}
