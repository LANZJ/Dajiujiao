package com.zjyeshi.dajiujiao.buyer.task.my;

import android.content.Context;

import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.utils.DataCleanUtil;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.xuan.bigapple.lib.asynctask.helper.Result;

/**
 * Created by wuhk on 2015/11/22.
 */
public class CleanCachTask extends BaseTask<NoResultData> {
    public CleanCachTask(Context context) {
        super(context);
    }

    @Override
    protected Result<NoResultData> onHttpRequest(Object... params) {
        Result<NoResultData> result = new Result<NoResultData>();
        DataCleanUtil.clearAllCache(context);
        result.setSuccess(true);
        return result;
    }
}
