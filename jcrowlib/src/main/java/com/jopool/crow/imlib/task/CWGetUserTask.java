package com.jopool.crow.imlib.task;

import android.content.Context;

import com.jopool.crow.imapi.CWApi;
import com.jopool.crow.imlib.entity.CWCacheUser;
import com.jopool.crow.imlib.model.CWUserModel;
import com.jopool.crow.imlib.utils.CWLogUtil;
import com.jopool.crow.imlib.utils.asynctask.NetAbstractTask;
import com.jopool.crow.imlib.utils.asynctask.callback.AsyncTaskFailCallback;
import com.jopool.crow.imlib.utils.asynctask.callback.AsyncTaskSuccessCallback;
import com.jopool.crow.imlib.utils.asynctask.helper.Result;

/**
 * 获取用户信息接口
 *
 * @author xuan
 */
public class CWGetUserTask extends NetAbstractTask<CWCacheUser> {
    public CWGetUserTask(Context context) {
        super(context);
        setShowProgressDialog(false);
    }

    @Override
    protected Result<CWCacheUser> onHttpRequest(Object... params) {
        String userId = (String) params[0];
        //
        Result<CWCacheUser> result = null;
        CWCacheUser cacheUser = CWApi.getInstance().getApiUser().getLoadUserInfoProvider().loadUserById(userId);
        if(null != cacheUser){
            CWUserModel.getInstance().cacheUser(cacheUser);
            result = new Result<CWCacheUser>(true, "成功", cacheUser);
        }else{
            result = new Result<CWCacheUser>(false, "加载用户失败");
        }
        return result;
    }

    /**
     * 获取用户信息
     *
     * @param context
     * @param userId
     * @param callback
     */
    public static void getUserInfo(Context context, String userId, final AsyncTaskSuccessCallback<CWCacheUser> callback) {
        CWGetUserTask task = new CWGetUserTask(context);
        task.setAsyncTaskFailCallback(new AsyncTaskFailCallback<CWCacheUser>() {
            @Override
            public void failCallback(Result<CWCacheUser> result) {
                CWLogUtil.e(result.getMessage());
            }
        });
        task.setAsyncTaskSuccessCallback(callback);
        task.execute(userId);
    }

}
