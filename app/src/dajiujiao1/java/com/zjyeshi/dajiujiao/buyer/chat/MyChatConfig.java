package com.zjyeshi.dajiujiao.buyer.chat;

import android.os.Environment;

import com.jopool.crow.CWChatConfig;
import com.xuan.bigapple.lib.utils.ContextUtils;
import com.zjyeshi.dajiujiao.buyer.common.Constants;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LoginEnum;

/**
 * 配置
 * 
 * @author xuan
 */
public class MyChatConfig extends CWChatConfig {

    @Override
    public String getDbName() {
        return "seller1_crow_db";
    }

    @Override
    public String getFileSavePath() {
        String externalPath;
        if (ContextUtils.hasSdCard()) {
            externalPath = Environment.getExternalStorageDirectory().getPath();
        } else {
            externalPath = ContextUtils.getCacheDirPath();
        }

        return externalPath + Constants.DJJBUYER;
    }

    @Override
    public String getSocketHost() {
        return "jcrow.yeshiwine.com";
//        return "api.jopool.net";
//        return "192.168.199.156";
    }

    @Override
    public int getSocketPort() {
        return 8880;
    }

    @Override
    public String getAppId() {
        return "00000000000000000000000000000001";
    }

    @Override
    public String getAppSecret() {
        return "00000000000000000000000000000001";
    }

    @Override
    public String getPushApiKey() {
//        大酒窖正式
        return "nuu6P159ABARwPbMW0HiVj8p";
    }

    @Override
    public String getApiPrefix() {
//        return "http://jcrow.yeshiwine.com";
//        return "http://api.jopool.net/jppush";
        return UrlConstants.JCROW_WEB_SITE;
    }

    @Override
    public Class getGTServiceClass() {
        return MyGTIntentService.class;
    }
}
