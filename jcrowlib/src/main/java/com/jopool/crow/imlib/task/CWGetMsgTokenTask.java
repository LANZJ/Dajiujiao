package com.jopool.crow.imlib.task;

import android.content.Context;

import com.jopool.crow.CWChat;
import com.jopool.crow.imlib.constants.UrlConstants;
import com.jopool.crow.imlib.utils.CWHttpUtil;
import com.jopool.crow.imlib.utils.CWLogUtil;
import com.jopool.crow.imlib.utils.asynctask.NetAbstractTask;
import com.jopool.crow.imlib.utils.asynctask.callback.AsyncTaskFailCallback;
import com.jopool.crow.imlib.utils.asynctask.callback.AsyncTaskResultNullCallback;
import com.jopool.crow.imlib.utils.asynctask.callback.AsyncTaskSuccessCallback;
import com.jopool.crow.imlib.utils.asynctask.helper.Result;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 获取token
 *
 * @author xuan
 */
public class CWGetMsgTokenTask extends NetAbstractTask<String> {
    public CWGetMsgTokenTask(Context context) {
        super(context);
        setShowProgressDialog(false);
        setAsyncTaskResultNullCallback(new AsyncTaskResultNullCallback() {
            @Override
            public void resultNullCallback() {
                CWLogUtil.e("无网络");
            }
        });
        setAsyncTaskFailCallback(new AsyncTaskFailCallback<String>() {
            @Override
            public void failCallback(Result<String> result) {
                CWLogUtil.e(result.getMessage());
            }
        });
    }

    @Override
    protected Result<String> onHttpRequest(Object... params) {
        PostParam pp = (PostParam) params[0];
        Result<String> result = null;
        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("appId", pp.appId);
        paramMap.put("appSecret", pp.appSecret);
        paramMap.put("userId", pp.userId);
        paramMap.put("userName", pp.userName);
        paramMap.put("userLogo", pp.userLogo);
        paramMap.put("osType", pp.osType);
        paramMap.put("bpushChannelId", pp.bpushChannelId);
        paramMap.put("identifier", CWChat.getInstance().getApplication().getPackageName());

        try {
            CWHttpUtil.CWResponse response = CWHttpUtil.post(CWChat.getInstance().getConfig().getApiPrefix() + UrlConstants.GET_TOKEN, paramMap);
            if (200 == response.getStatusCode()) {
                JSONObject retObj = new JSONObject(response.getResultStr());
                int code = retObj.getInt("code");
                if (1 == code) {
                    // 拿到token
                    JSONObject resultObj = retObj.getJSONObject("result");
                    result = new Result<String>(true, "获取成功");
                    result.setValue(resultObj.getString("token"));
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
     * Post数据
     *
     * @author xuan
     */
    public static class PostParam {
        public String appId;// JPPush 分配的appid
        public String appSecret;// JPPush 分配的secret
        public String userId;// 用户id
        public String userName;// 用户名
        public String userLogo;// 头像url
        public String osType = "ANDROID";// 终端类型：ANDROID|IOS
        public String bpushChannelId;// 百度推送channelid

        public static PostParam obtain(String appId, String appSecret,
                                       String userId, String userName, String userLogo,
                                       String bpushChannelId) {
            PostParam pp = new PostParam();
            pp.appId = appId;
            pp.appSecret = appSecret;
            pp.userId = userId;
            pp.userName = userName;
            pp.userLogo = userLogo;
            pp.osType = "ANDROID";
            pp.bpushChannelId = bpushChannelId;
            return pp;
        }
    }

    /**
     * 获取token
     *
     * @param context
     * @param pp
     * @param successCallback
     */
    public static void getToken(Context context, PostParam pp,
                                AsyncTaskSuccessCallback<String> successCallback) {
        CWGetMsgTokenTask getMsgTokenTask = new CWGetMsgTokenTask(context);
        getMsgTokenTask.setAsyncTaskSuccessCallback(successCallback);
        getMsgTokenTask.execute(pp);
    }

}
