package com.jopool.crow;

import android.os.Environment;

import com.jopool.crow.imlib.utils.CWContextUtil;

/**
 * 配置参数
 *
 * @author xuan
 */
public abstract class CWChatConfig {
    /**
     * 免打扰名单存放key
     */
    public static final String CW_PREFERENCES_NOT_DISTURB_LIST = "CW_PREFERENCES_NOT_DISTURB_LIST";

    /**
     * Socket连接地址
     *
     * @return
     */
    public abstract String getSocketHost();

    /**
     * API访问前缀
     *
     * @return
     */
    public abstract String getApiPrefix();

    /**
     * Socket连接端口
     *
     * @return
     */
    public abstract int getSocketPort();

    /**
     * APPID
     *
     * @return
     */
    public abstract String getAppId();

    /**
     * APP秘钥
     *
     * @return
     */
    public abstract String getAppSecret();

    /**
     * 文件上传地址
     *
     * @return
     */
//    public abstract String getFileUploadUrl();

    /**
     * 百度推送KEY
     *
     * @return
     */
    public abstract String getPushApiKey();

    public abstract Class getGTServiceClass();
    /**
     * 数据库名称(默认可不实现)
     *
     * @return
     */
    public String getDbName() {
        return getDefaultDbName();
    }

    /**
     * 聊天等文件保存地址(默认可不实现)
     *
     * @return
     */
    public String getFileSavePath() {
        return getDefaultFileSavePath();
    }

    private String getDefaultDbName() {
        String defaultDbName = CWChat.getInstance().getApplication().getPackageName();
        defaultDbName = defaultDbName.replace('.', '_');
        defaultDbName += "_jcrow_db";
        return defaultDbName;
    }

    private String getDefaultFileSavePath() {
        String externalPath;
        if (CWContextUtil.hasSdCard()) {
            externalPath = Environment.getExternalStorageDirectory().getPath();
        } else {
            externalPath = CWContextUtil.getCacheDirPath();
        }
        //
        String defaultFileSavePath = CWChat.getInstance().getApplication().getPackageName();
        defaultFileSavePath = defaultFileSavePath.replace('.', '_');
        defaultFileSavePath += "_jcrow_files";
        //
        return externalPath + "/" + defaultFileSavePath + "/";
    }

}
