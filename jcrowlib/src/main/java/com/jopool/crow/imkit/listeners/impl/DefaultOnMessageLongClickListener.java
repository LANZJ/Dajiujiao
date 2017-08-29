package com.jopool.crow.imkit.listeners.impl;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.jopool.crow.imkit.dialog.CWSingleSelectDialog;
import com.jopool.crow.imkit.listeners.OnMessageLongClickListener;
import com.jopool.crow.imkit.receiver.CWConversationMessageReceiver;
import com.jopool.crow.imkit.receiver.CWConversationUnreadNumReceiver;
import com.jopool.crow.imlib.db.dao.CWChatDaoFactory;
import com.jopool.crow.imlib.entity.CWConversationMessage;
import com.jopool.crow.imlib.enums.CWConversationType;
import com.jopool.crow.imlib.enums.CWMessageContentType;
import com.jopool.crow.imlib.task.RemoveChatMessageTask;
import com.jopool.crow.imlib.utils.CWClipboardUtil;
import com.jopool.crow.imlib.utils.CWToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息长按默认实现
 *
 * @author xuan
 */
public class DefaultOnMessageLongClickListener implements
        OnMessageLongClickListener {

    @Override
    public boolean onMessageLongClick(final Context context, View view,
                                      final CWConversationMessage message) {
        List<String> itemList = new ArrayList<String>();
        List<OnClickListener> onClickListenerList = new ArrayList<OnClickListener>();

        itemList.add("删除消息");
        onClickListenerList.add(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CWChatDaoFactory.getConversationMessageDao().deleteById(
                        message.getId());
                // 通知有消息删除
                CWConversationMessageReceiver.notifyMessageRemove(context,
                        message.getId());
                // 通知有消息数量变动
                CWConversationUnreadNumReceiver.notifyReceiver(context,
                        message.getConversationToId());
                //删除服务器上的
                RemoveChatMessageTask.removeById(context, message.getId());
            }

        });

        if (CWMessageContentType.TEXT.equals(message.getMessageContentType())) {
            itemList.add("复制消息");
            onClickListenerList.add(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    CWClipboardUtil.copyText(message.getContent());
                    CWToastUtil.displayTextShort("已复制");
                }
            });
        }

        CWSingleSelectDialog dialog = new CWSingleSelectDialog.Builder(context)
                .setItemTextAndOnClickListener(
                        itemList.toArray(new String[itemList.size()]),
                        onClickListenerList
                                .toArray(new OnClickListener[onClickListenerList
                                        .size()])).create();
        dialog.show();
        return true;
    }

}
