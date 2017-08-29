package com.xuan.bigdog.lib.bservice.bregister;

/**
 * 验证手机验证码回调
 * Created by wuhk on 2016/3/14.
 */
public interface BDVerifySmsCodeListener {
    /**
     * 成功
     */
    void success();

    /**
     * 失败
     *
     * @param message
     */
    void fail(String message);
}
