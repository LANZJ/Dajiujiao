package com.jopool.crow.imkit.ui;

import com.jopool.crow.imkit.ui.uientity.CWUiConversation;
import com.jopool.crow.imlib.db.dao.CWChatDaoFactory;
import com.jopool.crow.imlib.entity.CWConversation;
import com.jopool.crow.imlib.entity.CWConversationMessage;
import com.jopool.crow.imlib.entity.CWGroup;
import com.jopool.crow.imlib.entity.CWUser;
import com.jopool.crow.imlib.utils.CWValidator;

import java.util.ArrayList;
import java.util.List;

/**
 * UI数据model
 * <p/>
 * Created by xuan on 15/11/30.
 */
public class CWUiModel {
    private static final CWUiModel instance = new CWUiModel();

    public static CWUiModel getInstance() {
        return instance;
    }

    /**
     * 获取会话列表
     *
     * @return
     */
    public List<CWUiConversation> getUiConversationList() {
        List<CWConversation> conversationList = CWChatDaoFactory.getConversationDao().findAllDGConversation();
        if (CWValidator.isEmpty(conversationList)) {
            return new ArrayList<CWUiConversation>();
        }

        List<CWUiConversation> uiConversationList = new ArrayList<CWUiConversation>();
        for (CWConversation conversation : conversationList) {
            CWUiConversation uiConversation = new CWUiConversation();

            //头像和标题
            if (conversation.isUser()) {
                //单聊
                CWUser toUser = conversation.getToUser();
                uiConversation.setTitle(toUser.getName());
                uiConversation.setUrl(toUser.getUrl());
            } else {
                //群聊
                CWGroup toGroup = conversation.getToGroup();
                uiConversation.setTitle(toGroup.getName());
                uiConversation.setUrl(toGroup.getUrl());
            }

            //最后detail和时间
            CWConversationMessage lastMessage = conversation.getLastMessage();
            if (null != lastMessage) {
                if (lastMessage.isText()) {
                    uiConversation.setDetail(lastMessage.getContent());
                } else if (lastMessage.isImage()) {
                    uiConversation.setDetail("[图片]");
                } else if (lastMessage.isVoice()) {
                    uiConversation.setDetail("[语音]");
                } else if (lastMessage.isSystem()) {
                    uiConversation.setDetail(lastMessage.getContent());
                } else {
                    uiConversation.setDetail("");
                }
                uiConversation.setTime(lastMessage.getModifyTime());
            }

            //未读消息和置顶标题
            uiConversation.setUnreadNum(conversation.getUnreadNum());
            uiConversation.setPriority(conversation.getPriority());
            uiConversation.setToId(conversation.getToId());
            uiConversation.setToType(conversation.getConversationType());
            uiConversationList.add(uiConversation);
        }

        return uiConversationList;
    }

}
