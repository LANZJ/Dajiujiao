package com.xuan.bigdog.lib.update.core;

import android.os.Environment;
import android.os.Handler;

import com.xuan.bigdog.lib.update.listener.OnUpdateListener;

/**
 * 配置参数
 *
 * @author xuan
 */
public class UpdateConfig {
    public static final int DOWNLOAD_NOTIFICATION_ID = 0x3;

    private String checkUrl;// 检查url
    private String saveFileName = Environment.getExternalStorageDirectory()
            .getPath() + "/bigdog/update.apk";// apk存放路径
    private String noticeMessage = "APP正在下载...";

    private PasreUpdateInfoHandler pasreUpdateInfoHandler = new DefaultPasreUpdateInfoHandler();// 服务器返回解析器
    private OnUpdateListener onUpdateListener;// 整个更新过程监听
    private Handler handler;// 与UI交互用的

    /**
     * 检查到当前已是最新版本的提示,如果不需要设置成null
     */
    private String noUpateTips = "当前已是最新版本";

    public String getNoUpateTips() {
        return noUpateTips;
    }

    public void setNoUpateTips(String noUpateTips) {
        this.noUpateTips = noUpateTips;
    }

    public String getCheckUrl() {
        return checkUrl;
    }
//更新url
    public UpdateConfig setCheckUrl(String checkUrl) {
        this.checkUrl = checkUrl;
        return this;
    }

    public String getSaveFileName() {
        return saveFileName;
    }
//更新那个APK
    public UpdateConfig setSaveFileName(String saveFileName) {
        this.saveFileName = saveFileName;
        return this;
    }

    public PasreUpdateInfoHandler getPasreUpdateInfoHandler() {
        return pasreUpdateInfoHandler;
    }

    public UpdateConfig setPasreUpdateInfoHandler(
            PasreUpdateInfoHandler pasreUpdateInfoHandler) {
        this.pasreUpdateInfoHandler = pasreUpdateInfoHandler;
        return this;
    }

    public OnUpdateListener getOnUpdateListener() {
        return onUpdateListener;
    }

    public UpdateConfig setOnUpdateListener(OnUpdateListener onUpdateListener) {
        this.onUpdateListener = onUpdateListener;
        return this;
    }

    public Handler getHandler() {
        return handler;
    }

    public UpdateConfig setHandler(Handler handler) {
        this.handler = handler;
        return this;
    }

    public String getNoticeMessage() {
        return noticeMessage;
    }

    public UpdateConfig setNoticeMessage(String noticeMessage) {
        this.noticeMessage = noticeMessage;
        return this;
    }

}
