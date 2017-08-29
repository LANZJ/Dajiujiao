package com.jopool.crow;

import android.os.Environment;

import com.jopool.crow.imlib.gettui.CWGeTuiDefaultIntentService;
import com.jopool.crow.imlib.utils.CWContextUtil;


/**
 * 默认配置
 *
 * Created by xuan on 15/11/30.
 */
public class CWDefaultChatConfig extends CWChatConfig {

    /**
     * 数据库名称
     *
     * @return
     */
    @Override
    public String getDbName() {
        return "crow_demo_db";
    }

    /**
     * 聊天等文件保存地址
     *
     * @return
     */
    @Override
    public String getFileSavePath() {
        String externalPath;
        if (CWContextUtil.hasSdCard()) {
            externalPath = Environment.getExternalStorageDirectory().getPath();
        } else {
            externalPath = CWContextUtil.getCacheDirPath();
        }

        return externalPath + "/crow_demo_file/";
    }

    /**
     * Socket连接地址
     *
     * @return
     */
    @Override
    public String getSocketHost() {
        return "api.jopool.net";
    }

    /**
     * Socket连接端口
     *
     * @return
     */
    @Override
    public int getSocketPort() {
        return 8880;
    }

    @Override
    public String getApiPrefix() {
        return "http://api.jopool.net/jppush";
    }

    /**
     * APPID
     *
     * @return
     */
    @Override
    public String getAppId() {
        return "00000000000000000000000000000001";
    }

    /**
     * APP秘钥
     *
     * @return
     */
    @Override
    public String getAppSecret() {
        return "00000000000000000000000000000001";
    }

//    /**
//     * 文件上传地址
//     *
//     * @return
//     */
//    @Override
//    public String getFileUploadUrl() {
//        return "http://api.jopool.net/jppush/files/upload.htm";
//    }

    /**
     * 百度推送KEY
     *
     * @return
     */
    @Override
    public String getPushApiKey() {
        return "1m5ZYQBnLmL2uqRAtprPkhAe";
    }

    @Override
    public Class getGTServiceClass() {
        return CWGeTuiDefaultIntentService.class;
    }
}
