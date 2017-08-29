package com.xuan.bigdog.lib.bservice.bregister;

/**
 * 注册回调
 * Created by wuhk on 2016/3/14.
 */
public interface BDRegisterListener {
    void success(String userId);

    void fail(String message);
}
