package com.zjyeshi.dajiujiao.buyer.common;

import android.os.Environment;

import com.jopool.crow.CWChat;
import com.xuan.bigapple.lib.utils.ContextUtils;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LoginEnum;

/**
 * Created by wuhk on 2017/1/16.
 */

public class Constants {
    /**
     * 本地数据库版本
     */
    public static final int DB_VERSION = 7;

    /**
     * 本地数据库名称
     */
    public static String DB_NAME;

    /**
     * 卖家买家
     */
    public static LoginEnum loginEnum = LoginEnum.SELLER;

    /**
     * 判断应用是否启动着
     */
    public static boolean isAlive = false;
    /**
     * 是否是开发状态
     */
    public static boolean isDebug = false;
    /**
     * 是否是连接状态
     */
    public static boolean connecting = false;
    /**
     * 文件存储目录
     */
    public static String SDCARD;
    public static String SDCARD_DJJBUYER;
    public static String DJJBUYER;

    public static String APK_FILENAME;

    static {
        if (ContextUtils.hasSdCard()) {
            SDCARD = Environment.getExternalStorageDirectory().getPath();
        } else {
            SDCARD = ContextUtils.getFileDirPath();
        }
        //根据类型区分,这个包肯定是卖家，不用区分了
        DB_NAME = "djj1_db";
        SDCARD_DJJBUYER = SDCARD + "/djj1/";
        DJJBUYER = "/djj1/";
        APK_FILENAME = SDCARD_DJJBUYER + "Dajiujiao1.apk";
    }

    public static final String SDCARD_DJJBUYER_CIRCLE = SDCARD_DJJBUYER + "circle/";
    public static final String SDCARD_DJJBUYER_PERSON = SDCARD_DJJBUYER + "person/";
    public static final String SDCARD_DJJBUYER_CACHE = SDCARD_DJJBUYER + "cache/";
    public static final String SDCARD_DJJBUYER_COMMENT = SDCARD_DJJBUYER + "comment/voice/";

    public static final String SDCARD_DJJBUYER_SHOTSCREEN = SDCARD_DJJBUYER + "drink.jpg";
    public static final String SDCARD_DJJBUYER_ERCODEIMAGE = SDCARD_DJJBUYER + "ercode.jpg";

    public static final String SDCARD_DJJBUYER_CIRCLE_EDIT = SDCARD_DJJBUYER + "circle/edit/";
    public static final String SDCARD_DJJBUYER_WORK_EDIT = SDCARD_DJJBUYER + "work/edit/";

    /**
     * 拍照临时存放图片位置
     */
    public static final String SDCARD_DJJBUYER_CIRCLE_TEMP_CAMREA = SDCARD_DJJBUYER + "circle/edit/camrea.jpg";
    public static final String SDCARD_DJJBUYER_WORK_TEMP_CAMREA = SDCARD_DJJBUYER + "work/edit/camrea.jpg";
    public static final String SDCARD_DJJBUYER_COMMENT_TEMP_CAMREA = SDCARD_DJJBUYER + "comment/edit/camrea.jpg";

    /**
     * 酒友圈转发图片
     */
    public static final String SDCARD_DJJBUYER_CIRCLE_PIC = CWChat.getInstance().getConfig().getFileSavePath()
            + "/temp/circle_send.jpg";

    public static final String VOICE_EXT = "amr";

    /**
     * 图片限制宽高
     */
    public static final int IMAGE_LIMIT_SIZE = 900;
}
