package com.xuan.bigdog.lib.bservice.bversioncheck;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.xuan.bigdog.lib.update.UpdateHelper;
import com.xuan.bigdog.lib.update.core.UpdateConfig;
import com.xuan.bigdog.lib.update.entity.UpdateInfo;

/**
 * 版本监测
 *
 * Created by xuan on 16/2/16.
 */
public class BDVersionCheckModel {
    private static BDVersionCheckModel instance;
    UpdateInfo updateInfo;
    private BDVersionCheckModel() {

    }

    public static BDVersionCheckModel getInstance() {
        if (null == instance) {
            instance = new BDVersionCheckModel();
        }
        return instance;
    }

    /**
     * 检查版本更新
     *
     * @param context
     * @param checkUrl
     * @param apkSaveFilePath
     */
    public void doCheck(Context context, String checkUrl, String apkSaveFilePath){
        UpdateConfig config = new UpdateConfig();
        config.setCheckUrl(checkUrl);
        config.setSaveFileName(apkSaveFilePath);
        config.setPasreUpdateInfoHandler(new BDVersionCheckPasreHandler());

      //  UpdateHelper updateHelper = new UpdateHelper(context, config);
//        updateHelper.check();
        UpdateManager mUpdateManager = new UpdateManager(context, updateInfo);
        mUpdateManager.showNoticeDialog();
    }

    /**
     * 检查版本更新,自动设置参数
     *
     * @param context
     * @param checkUrl   更新链接
     * @param apkSaveFilePath APK的名字
     */
    public void doCheckDefault(Context context, String checkUrl, String apkSaveFilePath){
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String completeUrl = checkUrl + "?version="+packageInfo.versionCode+"&os=ANDROID&appId="+packageInfo.packageName;
            UpdateConfig config = new UpdateConfig();
            config.setCheckUrl(completeUrl);
            config.setSaveFileName(apkSaveFilePath);
            config.setPasreUpdateInfoHandler(new BDVersionCheckPasreHandler());
            //已经是最新版本,就不提示了
            config.setNoUpateTips("");


           UpdateHelper updateHelper = new UpdateHelper(context, config);
            updateHelper.check();
//
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

}
