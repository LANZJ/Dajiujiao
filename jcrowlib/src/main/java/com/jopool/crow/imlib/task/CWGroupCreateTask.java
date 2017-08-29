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
 * 创建群组
 * Created by wuhk on 2016/11/3.
 */
public class CWGroupCreateTask extends NetAbstractTask<String> {
    public CWGroupCreateTask(Context context) {
        super(context);
        setShowProgressDialog(false);
    }

    @Override
    protected Result<String> onHttpRequest(Object... params) {
        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("appId", CWChat.getInstance().getConfig().getAppId());
        paramMap.put("name", (String) params[0]);
        paramMap.put("creatorUserId", CWUser.getConnectUserId());

        Result<String> result = null;
        try {
            CWHttpUtil.CWResponse response = CWHttpUtil.post(CWChat.getInstance().getConfig().getApiPrefix() + UrlConstants.CREATE_GROUP, paramMap);
            if (200 == response.getStatusCode()) {
                JSONObject retObj = new JSONObject(response.getResultStr());
                int code = retObj.getInt("code");
                if (1 == code) {
                    // 拿到token
                    JSONObject resultObj = retObj.getJSONObject("result");
                    result = new Result<String>(true, "获取成功");
                    result.setValue(resultObj.getString("appGroupId"));
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
     * 创建群组
     *
     * @param context
     * @param groupName
     * @param successCallback
     */
    public static void creatGroup(Context context, String groupName, AsyncTaskSuccessCallback<String> successCallback) {
        CWGroupCreateTask cwGroupCreateTask = new CWGroupCreateTask(context);
        cwGroupCreateTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<String>() {
            @Override
            public void failCallback(Result<String> result) {
                CWToastUtil.displayTextShort(result.getMessage());
            }
        });

        cwGroupCreateTask.setAsyncTaskSuccessCallback(successCallback);

        cwGroupCreateTask.execute(groupName);

    }
}
