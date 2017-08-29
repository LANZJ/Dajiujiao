package com.jopool.crow.imkit.listeners.impl;

import com.jopool.crow.imkit.listeners.TopNoticeProvider;

/**
 * Created by 巨柏网络 on 2016/5/13.
 */
public class DefaulTopNoticeProvider implements TopNoticeProvider{

    @Override
    public void getNotice(String toId, ShowNoticeCallback showNoticeCallback) {
        showNoticeCallback.show(new TopNoticeProvider.Notice());
    }

}
