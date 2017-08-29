package com.jopool.crow.imlib.task;

import android.content.Context;

import com.jopool.crow.CWChat;
import com.jopool.crow.imlib.constants.UrlConstants;
import com.jopool.crow.imlib.entity.CWUser;
import com.jopool.crow.imlib.utils.CWHttpUtil;
import com.jopool.crow.imlib.utils.asynctask.AbstractTask;
import com.jopool.crow.imlib.utils.asynctask.callback.AsyncTaskFailCallback;
import com.jopool.crow.imlib.utils.asynctask.callback.AsyncTaskSuccessCallback;
import com.jopool.crow.imlib.utils.asynctask.helper.Result;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 绑定channelId
 * 
 * @author xuan
 */
public class RemoveChatMessageTask extends AbstractTask<String> {
	public RemoveChatMessageTask(final Context context) {
		super(context);
		setShowProgressDialog(false);
		setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<String>() {
			@Override
			public void successCallback(Result<String> result) {
				//Ignore
			}
		});
		setAsyncTaskFailCallback(new AsyncTaskFailCallback<String>() {
			@Override
			public void failCallback(Result<String> result) {
				//Ignore
			}
		});
	}

	@Override
	protected Result<String> doHttpRequest(Object... params) {
		String id = (String) params[0];

		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);
		paramMap.put("ownerUserId", CWUser.getConnectUserId());
		Result<String> result = null;
		try {
			CWHttpUtil.CWResponse response = CWHttpUtil.post(CWChat.getInstance().getConfig().getApiPrefix() + UrlConstants.REMOVW_CHAT_MESSAGE
					, paramMap);
			if(200 == response.getStatusCode()){
				JSONObject retObj = new JSONObject(response.getResultStr());
				int code = retObj.getInt("code");
				if (1 == code) {
					JSONObject resultObj = retObj.getJSONObject("result");
					result = new Result<String>(true, retObj.optString("message"));
				} else {
					// code不对
					result = new Result<String>(false, retObj.getString("message"));
				}
			}else{
				result = new Result<String>(false, response.getReasonPhrase());
			}
		} catch (Exception e) {
			result = new Result<String>(false, e.getMessage());
		}
		return result;
	}

	public static void removeById(Context context, String id){
		RemoveChatMessageTask task = new RemoveChatMessageTask(context);
		task.setAsyncTaskFailCallback(new AsyncTaskFailCallback<String>() {
			@Override
			public void failCallback(Result<String> result) {
				//Ignore
			}
		});
		task.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<String>() {
			@Override
			public void successCallback(Result<String> result) {
				//Ignore
			}
		});
		task.execute(id);
	}

}
