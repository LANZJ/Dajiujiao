package com.jopool.crow.imlib.task;

import android.content.Context;

import com.jopool.crow.CWChat;
import com.jopool.crow.imlib.constants.UrlConstants;
import com.jopool.crow.imlib.entity.CWConversationMessage;
import com.jopool.crow.imlib.entity.CWUser;
import com.jopool.crow.imlib.utils.CWHttpUtil;
import com.jopool.crow.imlib.utils.CWToastUtil;
import com.jopool.crow.imlib.utils.CWValidator;
import com.jopool.crow.imlib.utils.asynctask.NetAbstractTask;
import com.jopool.crow.imlib.utils.asynctask.callback.AsyncTaskFailCallback;
import com.jopool.crow.imlib.utils.asynctask.callback.AsyncTaskSuccessCallback;
import com.jopool.crow.imlib.utils.asynctask.helper.Result;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * 设置消息已读
 * Created by wuhk on 2017/1/10.
 */

public class CWMakeMessageReadedTask extends NetAbstractTask<String> {
    public CWMakeMessageReadedTask(Context context) {
        super(context);
        setShowProgressDialog(false);
    }

    @Override
    protected Result<String> onHttpRequest(Object... params) {
        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("messageIds", (String) params[0]);
        paramMap.put("ownerUserId" , CWUser.getConnectUserId());

        Result<String> result = null;
        try {
            CWHttpUtil.CWResponse response = CWHttpUtil.post(CWChat.getInstance().getConfig().getApiPrefix() + UrlConstants.MAKE_MESSAGE_READED
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
     * 设置消息已读
     *
     * @param context
     * @param messageList
     */
    public static void makeMessageReaded(Context context, List<CWConversationMessage> messageList , String messageId) {
        CWMakeMessageReadedTask cwMakeMessageReadedTask = new CWMakeMessageReadedTask(context);
        cwMakeMessageReadedTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<String>() {
            @Override
            public void failCallback(Result<String> result) {
                CWToastUtil.displayTextShort(result.getMessage());
            }
        });

        cwMakeMessageReadedTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<String>() {
            @Override
            public void successCallback(Result<String> result) {

            }
        });

        if (!CWValidator.isEmpty(messageList) && CWValidator.isEmpty(messageId)){
            StringBuilder messageIds  = new StringBuilder();
            for(CWConversationMessage message : messageList){
                messageIds.append(message.getId());
                messageIds.append(",");
            }
            messageIds.deleteCharAt(messageIds.length()-1);
            cwMakeMessageReadedTask.execute(messageIds.toString());
        }else if (CWValidator.isEmpty(messageList) && !CWValidator.isEmpty(messageId)){
            cwMakeMessageReadedTask.execute(messageId);
        }
    }
}
