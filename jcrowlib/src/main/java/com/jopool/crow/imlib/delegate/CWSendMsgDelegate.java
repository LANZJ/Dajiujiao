package com.jopool.crow.imlib.delegate;

import android.content.Context;
import android.util.Log;

import com.jopool.crow.CWChat;
import com.jopool.crow.imkit.receiver.CWConversationMessageReceiver;
import com.jopool.crow.imlib.db.dao.CWChatDaoFactory;
import com.jopool.crow.imlib.entity.CWConversation;
import com.jopool.crow.imlib.entity.CWConversationMessage;
import com.jopool.crow.imlib.entity.CWUser;
import com.jopool.crow.imlib.enums.CWConversationType;
import com.jopool.crow.imlib.enums.CWMessageContentType;
import com.jopool.crow.imlib.enums.CWMessageType;
import com.jopool.crow.imlib.enums.CWReadStatus;
import com.jopool.crow.imlib.enums.CWSendStatus;
import com.jopool.crow.imlib.model.CWConversationModel;
import com.jopool.crow.imlib.soket.CWErrorCode;
import com.jopool.crow.imlib.soket.listeners.OnMessageSendListener;
import com.jopool.crow.imlib.utils.CWToastUtil;
import com.jopool.crow.imlib.utils.CWValidator;
import com.jopool.crow.imlib.utils.ChatFileUtils;
import com.jopool.crow.imlib.utils.uuid.CWUUIDUtils;

/**
 * 发送消息代理
 * <p/>
 * Created by xuan on 16/8/16.
 */
public class CWSendMsgDelegate {
    private static final String TAG = "CWSendMsgDelegate";

    /**
     * 发送文本消息
     *
     * @param context
     * @param toId
     * @param text
     * @param conversationType
     */
    public void sendTextMessage(Context context, String toId, String text, CWConversationType conversationType) {
        if (!CWChat.getInstance().getImClient().isLogined()) {
            CWToastUtil.displayTextShort("通讯服务器未连接");
            return;
        }
        if (CWValidator.isEmpty(text)) {
            CWToastUtil.displayTextShort("说点什么呗～");
            return;
        }
        // 检查会话，不存在就插入
        CWConversation conversation = CWConversationModel.getInstance()
                .addOrUpdateConversation(toId, conversationType);
        // 先入本地数据库
        CWConversationMessage msg = CWConversationMessage.obtain(
                CWUser.getConnectUserId(), conversationType, toId, CWMessageType.RIGHT,
                CWMessageContentType.TEXT, text, "", 0,
                CWReadStatus.READED, CWSendStatus.ING);
        CWChatDaoFactory.getConversationMessageDao().insert(msg);
        // 发送消息
        CWConversationMessageReceiver.notifyRefreshList(context);
        CWChat.getInstance().getImClient()
                .sendMessage(context, msg, new OnMessageSendListenerImpl(context), conversationType);
    }

    /**
     * 发送图片消息
     *
     * @param context
     * @param toId
     * @param fromFileName
     * @param conversationType
     */
    public void sendImageMessage(Context context, String toId, String fromFileName, CWConversationType conversationType) {
        if (!CWChat.getInstance().getImClient().isLogined()) {
            CWToastUtil.displayTextShort("微信服务器未连接");
            return;
        }
        if (CWValidator.isEmpty(fromFileName)) {
            CWToastUtil.displayTextShort("图片不存在～");
            return;
        }
        // 检查会话，不存在就插入
        CWConversation conversation = CWConversationModel.getInstance()
                .addOrUpdateConversation(toId, conversationType);
        // copy图片
        String uuid = CWUUIDUtils.createId();
        String toBigFileName = ChatFileUtils.getImageFileNameForBig(uuid);
        String toSmallFileName = ChatFileUtils.getImageFileNameForSmall(uuid);
        ChatFileUtils.copyImageForBig(fromFileName, toBigFileName);
        ChatFileUtils.copyImageForSmall(fromFileName, toSmallFileName);
        // 先入本地数据库
        CWConversationMessage msg = CWConversationMessage.obtain(
                CWUser.getConnectUserId(), conversationType, toId, CWMessageType.RIGHT,
                CWMessageContentType.IMAGE, toSmallFileName, toBigFileName, 0,
                CWReadStatus.READED, CWSendStatus.ING);
        CWChatDaoFactory.getConversationMessageDao().insert(msg);
        // 发送消息
        CWConversationMessageReceiver.notifyRefreshList(context);
        CWChat.getInstance().getImClient()
                .sendMessage(context, msg, new OnMessageSendListenerImpl(context), conversationType);
    }

    /**
     * 发送语音消息
     *
     * @param context
     * @param toId
     * @param voiceFileName
     * @param voiceLength
     * @param conversationType
     */
    public void sendVoiceMessage(Context context, String toId, String voiceFileName, long voiceLength, CWConversationType conversationType) {
        if (!CWChat.getInstance().getImClient().isLogined()) {
            CWToastUtil.displayTextShort("微信服务器未连接");
            return;
        }
        if (CWValidator.isEmpty(voiceFileName)) {
            CWToastUtil.displayTextShort("语音不存在～");
            return;
        }
        // 检查会话，不存在就插入
        CWConversation conversation = CWConversationModel.getInstance()
                .addOrUpdateConversation(toId, conversationType);
        // 先入本地数据库
        CWConversationMessage msg = CWConversationMessage.obtain(
                CWUser.getConnectUserId(), conversationType, toId, CWMessageType.RIGHT,
                CWMessageContentType.VOICE, voiceFileName, "", (int) (voiceLength / 1000),
                CWReadStatus.READED, CWSendStatus.ING);
        CWChatDaoFactory.getConversationMessageDao().insert(msg);
        // 发送消息
        CWConversationMessageReceiver.notifyRefreshList(context);
        CWChat.getInstance().getImClient()
                .sendMessage(context, msg, new OnMessageSendListenerImpl(context), conversationType);
    }

    /**
     * 发送网址消息
     *
     * @param context
     * @param toId
     * @param title
     * @param url
     * @param conversationType
     */
    public void sendUrlMessage(Context context, String toId, String title, String url, CWConversationType conversationType) {
        if (!CWChat.getInstance().getImClient().isLogined()) {
            CWToastUtil.displayTextShort("通讯服务器未连接");
            return;
        }
        if (CWValidator.isEmpty(url)) {
            CWToastUtil.displayTextShort("网址不能空～");
            return;
        }
        // 检查会话，不存在就插入
        CWConversation conversation = CWConversationModel.getInstance()
                .addOrUpdateConversation(toId, conversationType);
        // 先入本地数据库
        CWConversationMessage msg = CWConversationMessage.obtain(
                CWUser.getConnectUserId(), conversationType, toId, CWMessageType.RIGHT,
                CWMessageContentType.URL, url, "", 0,
                CWReadStatus.READED, CWSendStatus.ING);
        msg.setExt(title);//网页标题
        CWChatDaoFactory.getConversationMessageDao().insert(msg);
        // 发送消息
        CWConversationMessageReceiver.notifyRefreshList(context);
        CWChat.getInstance().getImClient()
                .sendMessage(context, msg, new OnMessageSendListenerImpl(context), conversationType);
    }

    private class OnMessageSendListenerImpl implements OnMessageSendListener {
        private Context context;

        public OnMessageSendListenerImpl(Context context) {
            this.context = context;
        }

        @Override
        public void onError(CWConversationMessage message, CWErrorCode errorCode) {
            Log.d(TAG, "发送失败: " + errorCode.getMessage());
            CWChatDaoFactory.getConversationMessageDao().updateSendStatusById(message.getId(), CWSendStatus.FAIL.getValue());
            CWConversationMessageReceiver.notifyRefreshList(context);
        }

        @Override
        public void onSuccess(CWConversationMessage message) {
            Log.d(TAG, "发送成功");
            CWChatDaoFactory.getConversationMessageDao().updateSendStatusById(message.getId(), CWSendStatus.SUCCESS.getValue());
            CWConversationMessageReceiver.notifyRefreshList(context);
        }
    }

}
