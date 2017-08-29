package com.jopool.crow.imlib.task;

import android.content.Context;

import com.jopool.crow.imlib.utils.CWHttpUtil;
import com.jopool.crow.imlib.utils.asynctask.AbstractTask;
import com.jopool.crow.imlib.utils.asynctask.helper.Result;

/**
 * 下载文件
 * 
 * @author xuan
 */
public class DownloadTask extends AbstractTask<Object> {
	public DownloadTask(Context context) {
		super(context);
		setShowProgressDialog(false);
	}

	@Override
	protected Result<Object> doHttpRequest(Object... params) {
		String downloadUrl = (String) params[0];
		String fileName = (String) params[1];

		Result<Object> result = new Result<Object>();
		try {
			CWHttpUtil.dowloadFile(downloadUrl, fileName);
			result.setSuccess(true);
			result.setMessage(fileName);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMessage(e.getMessage());
		}
		return result;
	}

}
