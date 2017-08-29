package com.jopool.crow.imlib.model;

import com.jopool.crow.imlib.db.dao.CWChatDaoFactory;
import com.jopool.crow.imlib.entity.CWConversationMessage;
import com.jopool.crow.imlib.enums.CWReadStatus;

/**
 * 聊天消息
 *
 * Created by xuan on 15/9/25.
 */
public class CWConversationMessageModel {
    /**
     * 修改会话中的最后一条消息为未读
     *
     * @param toId
     */
    public void modifyLastMessageUnReadByToId(String toId){
        CWConversationMessage lastMessage = CWChatDaoFactory.getConversationMessageDao().findLastMessageByConversationToId(toId);
        if(null != lastMessage){
            String messageId = lastMessage.getId();
            CWChatDaoFactory.getConversationMessageDao().updateReadStatusById(messageId, CWReadStatus.UNREAD);
        }
    }

}
