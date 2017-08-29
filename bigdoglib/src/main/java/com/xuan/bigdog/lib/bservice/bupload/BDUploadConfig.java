package com.xuan.bigdog.lib.bservice.bupload;

/**
 * 上传配置
 * Created by wuhk on 2016/8/5.
 */
public class BDUploadConfig {
    private String url;
    private String fileName;
    private String fitMax;
    private String fitCrop;
    private BDUploadListener listener;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFitMax() {
        return fitMax;
    }

    public void setFitMax(String fitMax) {
        this.fitMax = fitMax;
    }

    public String getFitCrop() {
        return fitCrop;
    }

    public void setFitCrop(String fitCrop) {
        this.fitCrop = fitCrop;
    }

    public BDUploadListener getListener() {
        return listener;
    }

    public void setListener(BDUploadListener listener) {
        this.listener = listener;
    }
}
