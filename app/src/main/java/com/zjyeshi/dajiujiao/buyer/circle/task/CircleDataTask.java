package com.zjyeshi.dajiujiao.buyer.circle.task;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.zjyeshi.dajiujiao.buyer.circle.task.data.CircleData;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.xuan.bigapple.lib.asynctask.helper.Result;

import java.util.HashMap;

/**
 * 获取圈子列表
 *
 * Created by xuan on 15/11/5.
 */
public class CircleDataTask extends BaseTask<CircleData> {
    public CircleDataTask(Context context) {
        super(context);
    }

    @Override
    protected Result<CircleData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("lastTime" , (String)params[0]);
        paramMap.put("mode" , (String)params[1]);//lt(加载更多)||gt(返回最新)

        Result<CircleData> result = postCommon(UrlConstants.CIRCLE_CIRCLES , paramMap);

        if(result.isSuccess()){
            CircleData retData = JSON.parseObject(result.getMessage(), CircleData.class);
            result.setMessage(retData.getMessage());
            if (retData.codeOk()){
                if("0".equals(paramMap.get("lastTime"))){
                    //表示一次下拉刷新了,需要删除本地数据
                    DaoFactory.getCircleDao().deleteAll();
                }

                //入库
                DaoFactory.getCircleDao().replaceIntoBatch(retData.getResult().getList());

                result.setValue(retData.getResult());
            }else{
                result.setSuccess(false);
            }
        }
        return result;
    }

    public enum Mode{
        //lt(加载更多)||gt(返回最新)
        LT("lt"), GT("gt");
        private String value;

        Mode(String value){
            this.value = value;
        }

        public String getValue(){
            return value;
        }
    }

}
