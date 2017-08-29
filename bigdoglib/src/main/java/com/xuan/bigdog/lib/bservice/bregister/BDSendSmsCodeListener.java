package com.xuan.bigdog.lib.bservice.bregister;

/**
 * 发送手机验证码回调
 *
 * Created by xuan on 16/2/16.
 */
public interface BDSendSmsCodeListener {
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
