package com.zjyeshi.dajiujiao.buyer.task.store;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.store.homepage.CourseImageData;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.xuan.bigapple.lib.asynctask.helper.Result;

import java.util.HashMap;

/**
 * 首页轮播图
 * Created by wuhk on 2015/11/23.
 */
public class GetCourseImageTask extends BaseTask<CourseImageData> {
    public GetCourseImageTask(Context context) {
        super(context);
    }

    @Override
    protected Result<CourseImageData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String , String>();

        Result<CourseImageData> result = post(UrlConstants.ACTIVITYLIST , paramMap);
        if (result.isSuccess()){
            CourseImageData retData = JSON.parseObject(result.getMessage() , CourseImageData.class);
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
