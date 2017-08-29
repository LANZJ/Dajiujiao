package com.zjyeshi.dajiujiao.buyer.circle.task;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.HashMap;

/**
 * 酒友圈用户信息
 * Created by wuhk on 2016/8/23.
 */
public class CircleMemberInfoTask extends BaseTask<CircleMemberInfoTask.CircleMember> {

    public CircleMemberInfoTask(Context context) {
        super(context);
    }

    @Override
    protected Result onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("memberId" , (String)params[0]);

        Result<CircleMember> result = postCommon(UrlConstants.MEMBERINFO , paramMap);
        if (result.isSuccess()){
            CircleMember retData = JSON.parseObject(result.getMessage() , CircleMember.class);
            result.setMessage(retData.getMessage());
            if (retData.codeOk()){
                result.setValue(retData.getResult());
            }else{
                result.setSuccess(false);
            }
        }
        return result;
    }

    public static class CircleMember extends BaseData<CircleMember> {
        private String name;
        private String pic;
        private String circleBackgroundPic;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getCircleBackgroundPic() {
            return circleBackgroundPic;
        }

        public void setCircleBackgroundPic(String circleBackgroundPic) {
            this.circleBackgroundPic = circleBackgroundPic;
        }
    }
}

