package com.jopool.crow.imlib.task;

import android.content.Context;

import com.jopool.crow.CWChat;
import com.jopool.crow.imlib.constants.UrlConstants;
import com.jopool.crow.imlib.entity.CWUser;
import com.jopool.crow.imlib.utils.CWHttpUtil;
import com.jopool.crow.imlib.utils.CWToastUtil;
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
public class BindChannelIdTask extends AbstractTask<String> {
	public BindChannelIdTask(final Context context) {
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
		String bpushChannelId = (String) params[0];

		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("bpushChannelId", bpushChannelId);
		paramMap.put("userId", CWUser.getConnectUserId());
		paramMap.put("osType", "ANDROID");
		paramMap.put("identifier", CWChat.getInstance().getApplication().getPackageName());
		Result<String> result = null;
		try {
			CWHttpUtil.CWResponse response = CWHttpUtil.post(CWChat.getInstance().getConfig().getApiPrefix() + UrlConstants.URL_BIND_CHANNELID, paramMap);
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

}
