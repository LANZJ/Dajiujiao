package com.jopool.crow.imkit.listeners.impl;

import android.content.Context;

import com.jopool.crow.CWChat;
import com.jopool.crow.imkit.listeners.OnGroupUserClickListener;
import com.jopool.crow.imlib.utils.CWToastUtil;

/**
 * Created by wuhk on 2016/11/4.
 */
public class DefaultOnGroupUserClickListner implements OnGroupUserClickListener {
    @Override
    public void onGroupUserClick(Context context, String userId) {
        CWToastUtil.displayTextShort("用户Id:" + userId);
    }
}
