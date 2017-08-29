package com.xuan.bigdog.lib.bservice.bbanner;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigdog.lib.bservice.bcommon.BServiceBaseTask;

import java.util.HashMap;

/**
 * 轮播图
 * Created by wuhk on 2016/3/14.
 */
public class BDBannerModel {
    private static BDBannerModel instance;

    public BDBannerModel() {
    }

    public static BDBannerModel getInstance() {
        if (null == instance){
            instance = new BDBannerModel();
        }
        return instance;
    }

    /**获取轮播条
     *
     * @param context
     * @param website
     * @param position
     * @param l
     */
    public void getBanner(Context context , String website ,String position , final BDGetBannerListener l){
        String url = website + "common/getBanners.htm";
        GetBannerTask getBannerTask = new GetBannerTask(context , url);
        getBannerTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<BDGetBannerData>() {
            @Override
            public void successCallback(Result<BDGetBannerData> result) {
                l.fail(result.getMessage());
            }
        });

        getBannerTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<BDGetBannerData>() {
            @Override
            public void successCallback(Result<BDGetBannerData> result) {
                l.success(result.getValue());
            }
        });

        getBannerTask.execute(position);

    }

    /**获取轮播条Task
     *
     */
    private class GetBannerTask extends BServiceBaseTask<BDGetBannerData>{
        private String url;

        public GetBannerTask(Context context, String url) {
            super(context);
            this.url = url;
        }

        @Override
        protected Result<BDGetBannerData> onHttpRequest(Object... params) {
            HashMap<String ,String> paramMap = new HashMap<String , String >();
            paramMap.put("position" , (String)params[0]);

            Result<BDGetBannerData> result = bPost(url , paramMap);
            if(result.isSuccess()){
                BDGetBannerData retData = JSON.parseObject(result.getMessage() , BDGetBannerData.class);
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
}
