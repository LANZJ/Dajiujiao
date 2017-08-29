package com.jopool.crow.imlib.model;

import android.content.Context;

import com.jopool.crow.CWChat;
import com.jopool.crow.imlib.db.dao.CWChatDaoFactory;
import com.jopool.crow.imlib.entity.CWConversation;
import com.jopool.crow.imlib.entity.CWUser;
import com.jopool.crow.imlib.enums.CWConversationType;
import com.jopool.crow.imlib.task.CWRemoveMessageConversationTask;

import java.util.Date;

/**
 * 会话
 *
 * @author xuan
 */
public class CWConversationModel {
    private static CWConversationModel instance = new CWConversationModel();

    private CWConversationModel() {
    }

    public static CWConversationModel getInstance() {
        return instance;
    }

    /**
     * 如果会话不存在添加
     *
     * @param toId
     * @param conversationType
     * @return
     */
    public synchronized CWConversation addOrUpdateConversation(String toId,
                                                               CWConversationType conversationType) {
        CWConversation conversation = CWChatDaoFactory.getConversationDao().findByToId(
                toId);
        if (null == conversation) {
            // 不存在，插入
            conversation = CWConversation.obtain(toId, conversationType);
            //根据用户优先级设置一下会话的优先级
            conversation.setPriority(CWChat.getInstance().getConversationDelegate().getConversationPriorityProvider().getConversationPriority(conversationType , toId));

            CWChatDaoFactory.getConversationDao().insert(conversation);
        } else {
            //存在的话修改时间
            CWChatDaoFactory.getConversationDao().updateModify(toId, new Date());
        }
        return conversation;
    }


    /**
     * 删除会话
     *
     * @param context
     * @param toId
     * @param conversationType
     */
    public void deleteConversation(Context context, String toId, CWConversationType conversationType) {
        CWRemoveMessageConversationTask.removeConversation(context, toId, conversationType.getValueForTras());
    }
}
