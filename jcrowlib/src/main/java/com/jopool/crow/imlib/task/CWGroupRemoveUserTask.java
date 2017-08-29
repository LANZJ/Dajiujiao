package com.jopool.crow.imlib.task;

import android.content.Context;

import com.jopool.crow.CWChat;
import com.jopool.crow.imlib.constants.UrlConstants;
import com.jopool.crow.imlib.entity.CWUser;
import com.jopool.crow.imlib.utils.CWHttpUtil;
import com.jopool.crow.imlib.utils.CWToastUtil;
import com.jopool.crow.imlib.utils.asynctask.NetAbstractTask;
import com.jopool.crow.imlib.utils.asynctask.callback.AsyncTaskFailCallback;
import com.jopool.crow.imlib.utils.asynctask.callback.AsyncTaskSuccessCallback;
import com.jopool.crow.imlib.utils.asynctask.helper.Result;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by wuhk on 2016/11/8.
 */
public class CWGroupRemoveUserTask extends NetAbstractTask<String> {
    public CWGroupRemoveUserTask(Context context) {
        super(context);
        setShowProgressDialog(false);
    }

    @Override
    protected Result<String> onHttpRequest(Object... params) {
        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("appId", CWChat.getInstance().getConfig().getAppId());
        paramMap.put("appGroupId", (String) params[0]);
        paramMap.put("userIds", (String) params[1]);
        paramMap.put("ownerUserId", CWUser.getConnectUserId());

        Result<String> result = null;
        try {
            CWHttpUtil.CWResponse response = CWHttpUtil.post(CWChat.getInstance().getConfig().getApiPrefix() + UrlConstants.REMOVE_USER_FROM_GROUP
                    , paramMap);
            if (200 == response.getStatusCode()) {
                JSONObject retObj = new JSONObject(response.getResultStr());
                int code = retObj.getInt("code");
                if (1 == code) {
                    JSONObject resultObj = retObj.getJSONObject("result");
                    result = new Result<String>(true, retObj.optString("message"));
                } else {
                    // code不对
                    result = new Result<String>(false, retObj.getString("message"));
                }
            } else {
                result = new Result<String>(false, response.getReasonPhrase());
            }
        } catch (Exception e) {
            result = new Result<String>(false, e.getMessage());
        }
        return result;
    }

    /**
     * 从群组中移除群成员
     *
     * @param context
     * @param appGroupId
     * @param userIds
     * @param successCallback
     */
    public static void removeUserFromGroup(Context context, String appGroupId, String userIds, final AsyncTaskSuccessCallback<String> successCallback) {
        CWGroupRemoveUserTask cwGroupRemoveUserTask = new CWGroupRemoveUserTask(context);

        cwGroupRemoveUserTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<String>() {
            @Override
            public void failCallback(Result<String> result) {
                CWToastUtil.displayTextShort(result.getMessage());
            }
        });

        cwGroupRemoveUserTask.setAsyncTaskSuccessCallback(successCallback);

        cwGroupRemoveUserTask.execute(appGroupId, userIds);

    }

}
