package com.xuan.bigdog.lib.bservice.blogin;

/**
 * 登录操作的回调
 *
 * Created by xuan on 16/2/16.
 */
public interface BDLoginListener {
    /**
     * 登录成功
     *
     * @param loginUser
     */
    void success(BDLoginUser loginUser);

    /**
     * 登录失败
     *
     * @param message
     */
    void fail(String message);
}
