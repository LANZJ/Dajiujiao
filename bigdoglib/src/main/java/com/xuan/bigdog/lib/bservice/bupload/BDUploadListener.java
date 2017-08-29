package com.xuan.bigdog.lib.bservice.bupload;

/**
 * 文件上传接口
 * <p/>
 * Created by xuan on 16/2/16.
 */
public interface BDUploadListener {
    /**
     * 成功
     *
     * @param path
     */
    void success(String path);

    /**
     * 失败
     *
     * @param message
     */
    void fail(String message);

}
