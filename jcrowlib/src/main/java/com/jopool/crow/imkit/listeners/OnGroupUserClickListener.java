package com.jopool.crow.imkit.listeners;

import android.content.Context;

/**
 * 群聊成员头像点击事件
 * Created by wuhk on 2016/11/4.
 */
public interface OnGroupUserClickListener {

    /**
     * 点击成员头像
     *
     * @param userId
     */
    void onGroupUserClick(Context context , String userId);
}
