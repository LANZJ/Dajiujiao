package com.jopool.crow.imkit.listeners.impl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.jopool.crow.CWChat;
import com.jopool.crow.R;
import com.jopool.crow.imkit.listeners.GetConversationTitleRightViewProvider;

/**
 * 群聊默认右边点击
 * Created by wuhk on 2016/11/8.
 */
public class DefaultGroupRightViewProvider implements GetConversationTitleRightViewProvider {
    private Context context;

    public DefaultGroupRightViewProvider(Context context) {
        this.context = context;
    }

    @Override
    public View[] getRightViews() {
        return new View[]{
                LayoutInflater.from(context).inflate(R.layout.cw_group_conversation_right , null)
        };
    }

    @Override
    public OnRightViewClickListener[] getRightViewClickListener() {
        return new OnRightViewClickListener[]{
            new OnRightViewClickListener() {
                @Override
                public void onClick(View view, Params params) {
                    CWChat.getInstance().getGroupDelegate().startGroupDetailActivity(view.getContext(), params.getToId());
                }
            }
        };
    }
}
