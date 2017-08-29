package com.jopool.crow.imlib.task;

import android.content.Context;

import com.jopool.crow.CWChat;
import com.jopool.crow.imlib.constants.UrlConstants;
import com.jopool.crow.imlib.utils.CWHttpUtil;
import com.jopool.crow.imlib.utils.CWToastUtil;
import com.jopool.crow.imlib.utils.asynctask.NetAbstractTask;
import com.jopool.crow.imlib.utils.asynctask.callback.AsyncTaskFailCallback;
import com.jopool.crow.imlib.utils.asynctask.callback.AsyncTaskSuccessCallback;
import com.jopool.crow.imlib.utils.asynctask.helper.Result;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 修改群聊名称
 * Created by wuhk on 2016/11/4.
 */
public class CWGroupModifyGroupNameTask extends NetAbstractTask<String> {
    public CWGroupModifyGroupNameTask(Context context) {
        super(context);
        setShowProgressDialog(false);
    }

    @Override
    protected Result<String> onHttpRequest(Object... params) {
        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("appId", CWChat.getInstance().getConfig().getAppId());
        paramMap.put("appGroupId", (String) params[0]);
        paramMap.put("name", (String) params[1]);

        Result<String> result = null;
        try {
            CWHttpUtil.CWResponse response = CWHttpUtil.post(CWChat.getInstance().getConfig().getApiPrefix() + UrlConstants.MODIFY_GROUP_NAME
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
     * 修改群名
     *
     * @param context
     * @param groupId
     * @param name
     * @param successCallback
     */
    public static void modifyGroupName(Context context, String groupId, String name, AsyncTaskSuccessCallback<String> successCallback) {
        CWGroupModifyGroupNameTask cwGroupModifyGroupNameTask = new CWGroupModifyGroupNameTask(context);
        cwGroupModifyGroupNameTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<String>() {
            @Override
            public void failCallback(Result<String> result) {
                CWToastUtil.displayTextShort(result.getMessage());
            }
        });

        cwGroupModifyGroupNameTask.setAsyncTaskSuccessCallback(successCallback);

        cwGroupModifyGroupNameTask.execute(groupId, name);
    }
}
