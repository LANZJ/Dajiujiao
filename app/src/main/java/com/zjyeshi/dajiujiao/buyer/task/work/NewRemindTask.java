package com.zjyeshi.dajiujiao.buyer.task.work;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.NewRemindData;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.entity.NewRemind;
import com.zjyeshi.dajiujiao.buyer.utils.AppUnreadUtil;

import java.util.HashMap;

/**
 * 考勤等新消息变动
 * Created by wuhk on 2016/7/8.
 */
public class NewRemindTask extends BaseTask<NewRemindData> {
    public NewRemindTask(Context context) {
        super(context);
    }

    @Override
    protected Result<NewRemindData> onHttpRequest(Object... params) {
        HashMap<String , String> paramMap = new HashMap<String, String>();

        Result<NewRemindData> result = postCommon(UrlConstants.NEWREMINDV2 , paramMap);
        if (result.isSuccess()){
            NewRemindData retData = JSON.parseObject(result.getMessage() , NewRemindData.class);
            result.setMessage(retData.getMessage());
            if (retData.codeOk()){
                result.setValue(retData.getResult());
                //保存在本地
                NewRemindData data = result.getValue();
                NewRemind newRemind  = new NewRemind();
                newRemind.setFeeApplyChange(data.isFeeApplyChange());
                newRemind.setFeeReimbursement(data.isFeeReimbursement());
                newRemind.setFeeUnAuditCount(data.getFeeUnAuditCount());
                newRemind.setLeaveStatusChange(data.isLeaveStatusChange());
                newRemind.setLeaveUnAuditCount(data.getLeaveUnAuditCount());
                newRemind.setOrderCount(data.getOrderCount());
                newRemind.setUnReadDailyList(data.getUnreadDailyList());
                newRemind.setLeaveChangeCount(data.getLeaveChangeCount());
                newRemind.setFeeReimbursementCount(data.getFeeReimbursementCount());
                newRemind.setFeeApplyChangeCount(data.getFeeApplyChangeCount());
                newRemind.setRebackPipeliningCount(data.getRebackPipeliningCount());
                newRemind.setFriendApplyCount(data.getFriendApplyCount());
                newRemind.setUnreadDailyCommentCount(data.getUnreadDailyCommentCount());
                newRemind.setOrderWaitingForReceived(data.getOrderWaitingForReceived());

                NewRemind.setNewRemind(newRemind);
                AppUnreadUtil.sendBadgeNumber(context);

            }else{
                result.setSuccess(false);
            }
        }
        return result;
    }
}
