package com.xuan.bigdog.lib.update.core;

import android.content.Context;
import android.os.AsyncTask;

import com.xuan.bigapple.lib.utils.ToastUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigapple.lib.utils.VersionUtils;
import com.xuan.bigapple.lib.utils.log.LogUtils;
import com.xuan.bigdog.lib.bservice.bversioncheck.UpdateManager;
import com.xuan.bigdog.lib.update.entity.UpdateInfo;

/**
 * 检查更新任务类
 *
 * @author xuan
 */
public class AsyncCheck extends AsyncTask<String, Integer, UpdateInfo> {
    private final Context context;
    private final UpdateConfig mUpdateConfig;

    public AsyncCheck(Context context, UpdateConfig updateConfig) {
        this.context = context;
        this.mUpdateConfig = updateConfig;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (null != mUpdateConfig.getOnUpdateListener()) {
            mUpdateConfig.getOnUpdateListener().onStartCheck();
        }
    }

    @Override
    protected UpdateInfo doInBackground(String... params) {
        UpdateInfo updateInfo = null;
        String url = mUpdateConfig.getCheckUrl();

        if (Validators.isEmpty(url)) {
            LogUtils.e("Url parameter must not be null.");
            return null;
        }

        if (!NetWorkUtils.isNetworkUrl(url)) {
            LogUtils.e("The URL is invalid.");
            return null;
        }

        try {
            updateInfo = mUpdateConfig.getPasreUpdateInfoHandler().toUpdateInfo(
                    HttpRequest.get(url));
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
            return null;
        }
        return updateInfo;
    }

    @Override
    protected void onPostExecute(UpdateInfo updateInfo) {
        if (null == updateInfo){
            return;
        }else{
            if (updateInfo.getLastVersion() > VersionUtils.getVersionCode()) {
               // UpdateUIHelper.showUpdateUI(context, updateInfo, mUpdateConfig);
                UpdateManager mUpdateManager = new UpdateManager(context,updateInfo);
                 mUpdateManager.showNoticeDialog();


            } else {
                if (!Validators.isEmpty(mUpdateConfig.getNoUpateTips())) {
                    ToastUtils.displayTextShort(mUpdateConfig.getNoUpateTips());//提示已是最新版本
                }
            }

            if (null != mUpdateConfig.getOnUpdateListener()) {
                mUpdateConfig.getOnUpdateListener().onFinishCheck(updateInfo);
            }
        }
    }

}
