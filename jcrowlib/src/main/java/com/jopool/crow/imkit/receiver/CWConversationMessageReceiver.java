package com.jopool.crow.imkit.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.jopool.crow.imlib.db.dao.CWChatDaoFactory;
import com.jopool.crow.imlib.entity.CWConversationMessage;
import com.jopool.crow.imlib.enums.CWReadStatus;
import com.jopool.crow.imlib.enums.CWSendStatus;

/**
 * 消息接受器
 *
 * @author xuan
 */
public abstract class CWConversationMessageReceiver extends BroadcastReceiver {
    /**
     * 用来注册的key
     */
    public static final String ACTION = CWConversationMessageReceiver.class.getSimpleName();

    public static final String PARAM_MESSAGE_ID = "param.message.id";
    public static final String PARAM_SEND_STATUS = "param.send.status";
    public static final String PARAM_READ_STATUS = "param.read.status";

    public static final String PARAM_TYPE = "param.type";// 业务类型
    public static final int TYPE_MESSAGE_SENDSTATUS_MODIFY = 1;// 消息发送状态修改
    public static final int TYPE_MESSAGE_REMOVE = 2;// 消息删除
    public static final int TYPE_REFRESH_LIST = 3;// 刷新数据列表
    public static final int TYPE_MESSAGE_ADD = 4;// 消息添加
    public static final int TYPE_MESSAGE_READSTATUS_MODIFY = 5;// 已读状态修改

    @Override
    public void onReceive(Context context, Intent intent) {
        int type = intent.getIntExtra(PARAM_TYPE, -1);
        switch (type) {
            case TYPE_MESSAGE_SENDSTATUS_MODIFY:
                String modifyId = intent.getStringExtra(PARAM_MESSAGE_ID);
                int sendStatus = intent.getIntExtra(PARAM_SEND_STATUS,
                        CWSendStatus.FAIL.getValue());
                onModifyMessageSendStatus(modifyId, sendStatus);
                break;
            case TYPE_MESSAGE_REMOVE:
                String removeId = intent.getStringExtra(PARAM_MESSAGE_ID);
                onRemoveMessage(removeId);
                break;
            case TYPE_REFRESH_LIST:
                onRefreshList();
                break;
            case TYPE_MESSAGE_ADD:
                String addMessageId = intent.getStringExtra(PARAM_MESSAGE_ID);
                CWConversationMessage addMessage = CWChatDaoFactory
                        .getConversationMessageDao().findById(addMessageId);
                onAddMessage(addMessage);
                break;
            case TYPE_MESSAGE_READSTATUS_MODIFY:
                String readModifyId = intent.getStringExtra(PARAM_MESSAGE_ID);
                int readStatus = intent.getIntExtra(PARAM_READ_STATUS,
                        CWReadStatus.UNREAD.getValue());
                onModifyMessageReadStatus(readModifyId, readStatus);
                break;
            default:
                break;
        }
    }

    // ==========================注册/取消方法==========================

    /**
     * 注册广播接受
     *
     * @param context
     */
    public void register(Context context) {
        context.registerReceiver(this, new IntentFilter(
                ACTION));
    }

    /**
     * 取消广播接受
     *
     * @param context
     */
    public void unregister(Context context) {
        context.unregisterReceiver(this);
    }

    // ==========================发送通知方法==========================

    /**
     * 已读状态修改
     *
     * @param context
     * @param messageId
     * @param readStatus
     */
    public static void notifyMessageReadStatusModify(Context context,
                                                     String messageId, int readStatus) {
        Intent intent = new Intent(ACTION);
        intent.putExtra(PARAM_TYPE, TYPE_MESSAGE_READSTATUS_MODIFY);
        intent.putExtra(PARAM_MESSAGE_ID, messageId);
        intent.putExtra(PARAM_READ_STATUS, readStatus);
        context.sendBroadcast(intent);
    }

    /**
     * 发送状态修改
     *
     * @param context
     * @param messageId
     * @param sendStatus
     */
    public static void notifyMessageSendStatusModify(Context context,
                                                     String messageId, int sendStatus) {
        Intent intent = new Intent(ACTION);
        intent.putExtra(PARAM_TYPE, TYPE_MESSAGE_SENDSTATUS_MODIFY);
        intent.putExtra(PARAM_MESSAGE_ID, messageId);
        intent.putExtra(PARAM_SEND_STATUS, sendStatus);
        context.sendBroadcast(intent);
    }

    /**
     * 通知消息有删除
     *
     * @param context
     * @param messageId
     */
    public static void notifyMessageRemove(Context context, String messageId) {
        Intent intent = new Intent(ACTION);
        intent.putExtra(PARAM_TYPE, TYPE_MESSAGE_REMOVE);
        intent.putExtra(PARAM_MESSAGE_ID, messageId);
        context.sendBroadcast(intent);
    }

    /**
     * 通知消息有删除
     *
     * @param context
     */
    public static void notifyRefreshList(Context context) {
        Intent intent = new Intent(ACTION);
        intent.putExtra(PARAM_TYPE, TYPE_REFRESH_LIST);
        context.sendBroadcast(intent);
    }

    /**
     * 通知消息有添加
     *
     * @param context
     * @param messageId
     */
    public static void notifyMessageAdd(Context context, String messageId) {
        Intent intent = new Intent(ACTION);
        intent.putExtra(PARAM_TYPE, TYPE_MESSAGE_ADD);
        intent.putExtra(PARAM_MESSAGE_ID, messageId);
        context.sendBroadcast(intent);
    }

    // ==========================子类实现方法=====================================

    /**
     * 子类实现：修改消息发送状态
     *
     * @param messageId
     * @param sendStatus
     */
    protected abstract void onModifyMessageSendStatus(String messageId,
                                                      int sendStatus);

    /**
     * 子类实现：修改消息未读已读状态
     *
     * @param messageId
     * @param readStatus
     */
    protected abstract void onModifyMessageReadStatus(String messageId,
                                                      int readStatus);

    /**
     * 子类实现：删除消息
     *
     * @param messageId
     */
    protected abstract void onRemoveMessage(String messageId);

    /**
     * 子类实现：刷新数据列表
     */
    protected abstract void onRefreshList();

    /**
     * 子类实现：添加消息
     *
     * @param addMessage
     */
    protected abstract void onAddMessage(CWConversationMessage addMessage);

}
