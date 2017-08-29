package com.xuan.bigdog.lib.update.core;

import android.os.AsyncTask;

import com.xuan.bigapple.lib.utils.log.LogUtils;
import com.xuan.bigdog.lib.update.entity.UpdateInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 下载APK任务类
 * 
 * @author xuan
 */
public class AsyncDownLoad extends AsyncTask<Object, Integer, Boolean> {
	private final UpdateConfig mUpdateConfig;
	private final UpdateInfo mUpdateInfo;

	public AsyncDownLoad(UpdateConfig updateConfig, UpdateInfo updateInfo) {
		this.mUpdateConfig = updateConfig;
		this.mUpdateInfo = updateInfo;
	}

//	@Override
//	protected Boolean doInBackground(Object... params) {
//		HttpClient httpClient = new DefaultHttpClient();
//		HttpGet httpGet = new HttpGet(mUpdateInfo.getUrl());
//		try {
//			HttpResponse response = httpClient.execute(httpGet);
//			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
//				LogUtils.e("APK下载失败，请检查服务端配置接口。");
//				return false;
//			} else {
//				HttpEntity entity = response.getEntity();
//				InputStream inputStream = entity.getContent();
//				long total = entity.getContentLength();
//
//				File saveFileName = new File(mUpdateConfig.getSaveFileName());
//				File saveFileNameParent = saveFileName.getParentFile();
//				if (!saveFileNameParent.exists()) {
//					saveFileNameParent.mkdirs();
//				}
//
//				FileOutputStream fos = new FileOutputStream(saveFileName);
//				byte[] buf = new byte[1024];
//				int count = 0;
//				int length = -1;
//				while ((length = inputStream.read(buf)) != -1) {
//					fos.write(buf, 0, length);
//					count += length;
//					int progress = (int) ((count / (float) total) * 100);
//					if (progress % 5 == 0) {
//						mUpdateConfig
//								.getHandler()
//								.obtainMessage(
//										UpdateHandler.UPDATE_NOTIFICATION_PROGRESS,
//										progress, -1, null).sendToTarget();
//					}
//					if (null != mUpdateConfig.getOnUpdateListener()) {
//						mUpdateConfig.getOnUpdateListener().onDownloading(
//								progress);
//					}
//				}
//				inputStream.close();
//				fos.close();
//			}
//
//		} catch (IOException e) {
//			LogUtils.e(e.getMessage(), e);
//			return false;
//		}
//		return true;
//	}

	@Override
	protected Boolean doInBackground(Object... params) {
		try {
			URL url = new URL(mUpdateInfo.getUrl());
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setConnectTimeout(60000);
			conn.setReadTimeout(60000);
			conn .setRequestProperty("Accept-Encoding", "identity");//设置不需要压缩,这样才能获取到文件长度

			int responseCode = conn.getResponseCode();
			if(200 != responseCode){
				LogUtils.e("APK下载失败，请检查服务端配置接口。");
				return false;
			}else{
				InputStream inputStream = conn.getInputStream();
				long total = conn.getContentLength();

				File saveFileName = new File(mUpdateConfig.getSaveFileName());
				File saveFileNameParent = saveFileName.getParentFile();
				if (!saveFileNameParent.exists()) {
					saveFileNameParent.mkdirs();
				}

				FileOutputStream fos = new FileOutputStream(saveFileName);
				byte[] buf = new byte[1024];
				int count = 0;
				int length = -1;
				while ((length = inputStream.read(buf)) != -1) {
					fos.write(buf, 0, length);
					count += length;
					int progress = (int) ((count / (float) total) * 100);
					if (progress % 5 == 0) {
						mUpdateConfig
								.getHandler()
								.obtainMessage(
										UpdateHandler.UPDATE_NOTIFICATION_PROGRESS,
										progress, -1, null).sendToTarget();
					}
					if (null != mUpdateConfig.getOnUpdateListener()) {
						mUpdateConfig.getOnUpdateListener().onDownloading(
								progress);
					}
				}
				inputStream.close();
				fos.close();
			}
		}
		catch (IOException e) {
			LogUtils.e(e.getMessage(), e);
			return false;
		}

		return true;
	}

	@Override
	protected void onPostExecute(Boolean flag) {
		if (flag) {
			mUpdateConfig.getHandler()
					.obtainMessage(UpdateHandler.COMPLETE_DOWNLOAD_APK)
					.sendToTarget();
			if (null != mUpdateConfig.getOnUpdateListener()) {
				mUpdateConfig.getOnUpdateListener().onFinshDownload();

			}
		} else {
			LogUtils.e("下载失败。");
		}
	}

}
