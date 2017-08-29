package com.zjyeshi.dajiujiao.buyer.utils;

import com.zjyeshi.dajiujiao.buyer.common.Constants;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.xuan.bigapple.lib.utils.Validators;

import java.io.File;

/**
 * 文件工具类
 *
 * Created by xuan on 15/11/6.
 */
public abstract class FileUtil {
    /**
     * 确保文件的父文件夹存在
     *
     * @param filePathName
     */
    public static void checkParentFile(String filePathName){
        if(Validators.isEmpty(filePathName)){
            return;
        }

        File file = new File(filePathName);
        File parentFile = file.getParentFile();
        if(!parentFile.exists()){
            parentFile.mkdirs();
        }
    }

    /**
     * 获取头像地址
     *
     * @return
     */
    public static String getLocalAvatorFilePath(){
        String avatorPath = Constants.SDCARD_DJJBUYER_PERSON + LoginedUser.getLoginedUser().getId() +"/avator/";

        File filePath = new File(avatorPath);
        if(!filePath.exists()){
            filePath.mkdirs();
        }

        return avatorPath;
    }

    /**
     * 获取头像地址
     *
     * @return
     */
    public static String getCirlceBgFilePath(){
        String circlePath = Constants.SDCARD_DJJBUYER_PERSON + LoginedUser.getLoginedUser().getId() +"/circleBg/";

        File filePath = new File(circlePath);
        if(!filePath.exists()){
            filePath.mkdirs();
        }

        return circlePath;
    }

    /**
     * 获取缓存
     *
     * @return
     */
    public static String getCacheDir(){
        String cacheDir = Constants.SDCARD_DJJBUYER_CACHE;

        File filePath = new File(cacheDir);
        if(!filePath.exists()){
            filePath.mkdirs();
        }

        return cacheDir;
    }

    public static String getLocalVoiceFileName(String fileName){
        return Constants.SDCARD_DJJBUYER_COMMENT + fileName + "." + Constants.VOICE_EXT;
    }

    public static String getUrlVoiceFileName(String fileName){
        return Constants.SDCARD_DJJBUYER_COMMENT + fileName;
    }

    public static String getUrlWithoutamr(String fileName){
        return fileName.substring(0 , fileName.length() - 4);
    }

}
