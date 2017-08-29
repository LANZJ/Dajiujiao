package com.zjyeshi.dajiujiao.buyer.circle.task;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.circle.task.data.CircleData;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/8/23.
 */
public class UserCircleTask extends BaseTask<CircleData> {
    public UserCircleTask(Context context) {
        super(context);
    }

    @Override
    protected Result<CircleData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("memberId" , (String)params[0]);
        paramMap.put("lastTime" , (String)params[1]);
        paramMap.put("mode" , (String)params[2]);

        Result<CircleData> result = postCommon(UrlConstants.CIRCLE_CIRCLES , paramMap);

        if(result.isSuccess()){
            CircleData retData = JSON.parseObject(result.getMessage(), CircleData.class);
            result.setMessage(retData.getMessage());
            if (retData.codeOk()){
                if("0".equals(paramMap.get("lastTime"))){
                    //表示第一次刷新了,需要删除本地数据
                    DaoFactory.getUserCircleDao().deleteAll(paramMap.get("memberId"));
                }

                //入库
                DaoFactory.getUserCircleDao().replaceIntoBatch(retData.getResult().getList());

                result.setValue(retData.getResult());
            }else{
                result.setSuccess(false);
            }
        }
        return result;
    }
}
