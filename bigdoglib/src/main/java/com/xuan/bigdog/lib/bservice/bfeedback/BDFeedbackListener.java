package com.xuan.bigdog.lib.bservice.bfeedback;

/**
 * 意见反馈回调
 * Created by wuhk on 2016/3/14.
 */
public interface BDFeedbackListener {
    void success();

    void fail(String message);
}
