package com.jopool.crow.imlib.soket;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;

import com.jopool.crow.CWChat;
import com.jopool.crow.R;
import com.jopool.crow.imkit.receiver.CWConversationMessageReceiver;
import com.jopool.crow.imkit.receiver.CWConversationUnreadNumReceiver;
import com.jopool.crow.imlib.db.dao.CWChatDaoFactory;
import com.jopool.crow.imlib.entity.CWConversationMessage;
import com.jopool.crow.imlib.entity.CWUser;
import com.jopool.crow.imlib.enums.CWConversationType;
import com.jopool.crow.imlib.enums.CWMessageContentType;
import com.jopool.crow.imlib.model.CWConversationModel;
import com.jopool.crow.imlib.soket.listeners.OnMessageSendListener;
import com.jopool.crow.imlib.task.CWGetMsgTask;
import com.jopool.crow.imlib.task.CWUploadFileTask;
import com.jopool.crow.imlib.utils.CWLogUtil;
import com.jopool.crow.imlib.utils.CWSettingUtil;
import com.jopool.crow.imlib.utils.CWValidator;
import com.jopool.crow.imlib.utils.asynctask.callback.AsyncTaskFailCallback;
import com.jopool.crow.imlib.utils.asynctask.callback.AsyncTaskResultNullCallback;
import com.jopool.crow.imlib.utils.asynctask.callback.AsyncTaskSuccessCallback;
import com.jopool.crow.imlib.utils.asynctask.helper.Result;
import com.jopool.jppush.client.JPPushClient;
import com.jopool.jppush.client.listener.ConnectListener;
import com.jopool.jppush.client.listener.OnReceiveMessageListener;
import com.jopool.jppush.client.listener.OnSendMessageListener;
import com.jopool.jppush.common.message.CommonMessage;
import com.jopool.jppush.common.protocol.Message;
import com.jopool.jppush.common.protocol.MessageContent;
import com.jopool.jppush.remoting.protocol.RemotingCommand;

/**
 * socket发消息端
 *
 * @author xuan
 */
public class JPImClient extends CWImClient {
    public static final String JP_CHAT_MESSAGE = "JP:CHATMESSAGE";//聊天消息类型

    private final Handler mHandler;
    private MediaPlayer mMediaPlayer;

    public JPImClient(Context context) {
        super(context);
        mHandler = new Handler(context.getMainLooper());
        mMediaPlayer = MediaPlayer.create(context, R.raw.cw_receive_message);
        try {
            mMediaPlayer.prepare();
        } catch (Exception e) {
            CWLogUtil.e(e.getMessage());
        }
    }

    @Override
    public void connect(final CWImConfig config) {
        JPPushClient
                .getInstance()
                .init(config.getHost() + ":" + config.getPort(),
                        config.getUser().getToken(), config.getAppKey())
                .setConnectListener(new MyConnectListener(config))
                .setOnReceiveMessageListener(new MyOnReceiveMessageListener())
                .start();
    }

    @Override
    public void sendMessage(Context context,
                            final CWConversationMessage message, final OnMessageSendListener l, final CWConversationType toType) {
        CWLogUtil.d("发送给用户：" + message.getConversationToId());

        // 如果是图片或者语音先上传文件
        if (CWMessageContentType.IMAGE.equals(message.getMessageContentType())
                || CWMessageContentType.VOICE.equals(message
                .getMessageContentType())) {
            // 语音和图片先上传文件
            uploadFile(message, l, new AsyncTaskSuccessCallback<String>() {
                @Override
                public void successCallback(Result<String> result) {
                    String filePath = result.getValue();
                    if (CWMessageContentType.VOICE.equals(message
                            .getMessageContentType())) {
                        // 语音直接发送
                        message.setDownloadUrl(filePath);
                        doSend(message, l, toType);
                    } else {
                        // 图片还要上传大图的
                        String localFileNameSmall = message.getContent();
                        message.setContent(filePath);
                        uploadFile(message, l,
                                new AsyncTaskSuccessCallback<String>() {
                                    @Override
                                    public void successCallback(
                                            Result<String> result) {
                                        // 终于可以发送了
                                        String bigFilePath = result.getValue();
                                        message.setDownloadUrl(bigFilePath);
                                        doSend(message, l, toType);
                                    }
                                }, localFileNameSmall.replaceAll("_small.jpg",
                                        ".jpg"));
                    }
                }
            }, message.getContent());
        } else {
            // 非语音、图片直接发送好了
            doSend(message, l, toType);
        }
    }

    @Override
    public boolean isConnected() {
        return JPPushClient.getInstance().isOpen();
    }

    /**
     * 是否是登陆状态
     *
     * @return
     */
    @Override
    public boolean isLogined() {
        return !CWValidator.isEmpty(CWUser.getConnectUserId()) && isConnected();
    }

    @Override
    public void disConnect() {
        if (JPPushClient.getInstance().isOpen()) {
            JPPushClient.getInstance().close();
        }
    }

    /**
     * 发送
     *
     * @param cwMessage
     * @param l
     * @param toType
     */
    private void doSend(final CWConversationMessage cwMessage,
                        final OnMessageSendListener l, CWConversationType toType) {
        CommonMessage cm = new CommonMessage();
        cm.setMessageType(JPImClient.JP_CHAT_MESSAGE);
        cm.setContent(cwMessage.encode());
        //
        if (CWConversationType.USER.equals(toType)) {
            //单聊
            JPPushClient.getInstance().sendMessage(cwMessage.getConversationToId(),
                    cm, new JPOnMessageSendListener(l, cwMessage));
        } else if (CWConversationType.GROUP.equals(toType)) {
            //群聊
            JPPushClient.getInstance().sendGroupMessage(cwMessage.getConversationToId(), cm,
                    new JPOnMessageSendListener(l, cwMessage));
        }
    }

    /**
     * 这个回调一般可以用来缓存用户信息
     *
     * @param message
     * @return
     */
    private boolean fireBeforeConsumeConversationMesssgeListener(CWConversationMessage message) {
        boolean canConsume = true;
        if (null != getBeforeConsumeConversationMesssgeListener()) {
            canConsume = getBeforeConsumeConversationMesssgeListener().onBeforeConsumeConversationMesssge(message);
        }
        if (!canConsume) {
            CWLogUtil.d("消息不处理,OnBeforeConsumeConversationMesssgeListener用户返回false");
        }
        return canConsume;
    }

    /**
     * 触发震动或者铃声
     *
     * @param conversationToId
     */
    private void fireRingOrVibrate(String conversationToId) {
        if (!CWSettingUtil.isToIdInUnDisturbList(conversationToId)) {
            //非免打扰需要判断设置中的:是否语音+振动提醒
            if (CWSettingUtil.isReceiveMessageRing()) {
                ring();
            }
            if (CWSettingUtil.isReceiveMessageVibrate()) {
                vibrate();
            }
        }//else 免打扰不提示语音和振动
    }

    /**
     * 通知消息变动
     *
     * @param messageId
     * @param conversationToId
     */
    private void noticeMessageChange(String messageId, String conversationToId) {
        // 通知有消息添加
        CWConversationMessageReceiver.notifyMessageAdd(context, messageId);
        // 通知会话消息有变更
        CWConversationUnreadNumReceiver.notifyReceiver(context, conversationToId);
    }

    /**
     * 处理接收到的聊天消息,注意这里有可能是自己发送的消息
     *
     * @param netMessage
     */
    public void consumeConversationMessage(CWConversationMessage netMessage , String realStatus , boolean replace) {
         if (!fireBeforeConsumeConversationMesssgeListener(netMessage)) {
                return;
            }
            CWConversationMessage localMessage = CWConversationMessage.adapterLocalFromNet(netMessage , realStatus);
            // 检查会话是否存在
            CWConversationModel.getInstance().addOrUpdateConversation(localMessage.getConversationToId(), localMessage.getConversationType());
            // 保存消息
            if (replace){
                CWChatDaoFactory.getConversationMessageDao().insertOrReplace(localMessage);
            }else{
                CWChatDaoFactory.getConversationMessageDao().insert(localMessage);
        }
        // 通知消息有变动
        noticeMessageChange(localMessage.getId(), localMessage.getConversationToId());
        //收到消息的震动和语音
        fireRingOrVibrate(localMessage.getConversationToId());
    }

    /**
     * 处理别人的消息到本地
     *
     * @param cwMessage
     */
//    private void consumeMessageForOtherSend(CWConversationMessage cwMessage) {
//        if (!fireBeforeConsumeConversationMesssgeListener(cwMessage)) {
//            return;
//        }
//        //如果单聊,那么toId就要改成那个人发送的userId,toId在本地的意思就是对方的id
//        String toId = null;
//        if (CWConversationType.USER.equals(cwMessage.getConversationType())) {
//            toId = cwMessage.getSenderUserId();
//        } else {
//            //如果群聊,toId是群组的id
//            toId = cwMessage.getConversationToId();
//        }
//        // 修正消息数据
//        cwMessage.setOwnerUserId(CWUser.getConnectUserId());
//        cwMessage.setMessageType(CWMessageType.LEFT);
//        cwMessage.setReadStatus(CWReadStatus.UNREAD);
//        cwMessage.setSendStatus(CWSendStatus.SUCCESS);
//        cwMessage.setConversationToId(toId);
//        //如果是语音，清空content，因为content存的是对方的本地路径
//        if (CWMessageContentType.VOICE.equals(cwMessage.getMessageContentType())) {
//            cwMessage.setContent("");
//        }
//        // 检查会话是否存在
//        CWConversationModel.getInstance().addOrUpdateConversation(toId, cwMessage.getConversationType());
//        // 保存消息
//        CWChatDaoFactory.getConversationMessageDao().insert(cwMessage);
//        // 通知消息有变动
//        noticeMessageChange(cwMessage.getId(), toId);
//        //收到消息的震动和语音
//        fireRingOrVibrate(cwMessage.getConversationToId());
//    }

    /**
     * 处理自己的消息到本地
     *
     * @param message
     */
//    private void consumeMessageForSelfSend(CWConversationMessage message) {
//        if (!fireBeforeConsumeConversationMesssgeListener(message)) {
//            return;
//        }
//        // 修正消息数据
//        message.setOwnerUserId(CWUser.getConnectUserId());
//        message.setMessageType(CWMessageType.RIGHT);
//        message.setReadStatus(CWReadStatus.READED);
//        message.setSendStatus(CWSendStatus.SUCCESS);
//        //
//        if (CWMessageContentType.VOICE.equals(message.getMessageContentType())) {
//            // 如果是语音，清空content，因为content存的是对方的本地路径
//            message.setContent("");
//        }
//        // 检查会话是否存在
//        CWConversationModel.getInstance().addOrUpdateConversation(message.getConversationToId(), message.getConversationType());
//        // 保存消息
//        CWChatDaoFactory.getConversationMessageDao().insert(message);
//        //通知消息有变动
//        noticeMessageChange(message.getId(), message.getConversationToId());
//        //收到消息的震动和语音
//        fireRingOrVibrate(message.getConversationToId());
//    }

    /**
     * 震动
     */
    private void vibrate() {
        Vibrator vibrator = (Vibrator) CWChat.getApplication().getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 400};   // 停止 开启 停止 开启
        vibrator.vibrate(pattern, -1);
    }

    /**
     * 响铃
     */
    private void ring() {
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
        }
    }

    /**
     * 上传文件
     *
     * @param message
     * @param l
     * @param success
     * @param fileName
     */
    private void uploadFile(final CWConversationMessage message,
                            final OnMessageSendListener l,
                            AsyncTaskSuccessCallback<String> success, String fileName) {
        CWUploadFileTask uploadFileTask = new CWUploadFileTask(context);
        uploadFileTask
                .setAsyncTaskResultNullCallback(new AsyncTaskResultNullCallback() {
                    @Override
                    public void resultNullCallback() {
                        l.onError(message, CWErrorCode.obtain(
                                CWErrorCode.Code.NONETWORK, "无网络"));
                    }
                });
        uploadFileTask
                .setAsyncTaskFailCallback(new AsyncTaskFailCallback<String>() {
                    @Override
                    public void failCallback(Result<String> result) {
                        l.onError(message, CWErrorCode.obtain(
                                CWErrorCode.Code.UNKOWN, result.getMessage()));
                    }
                });
        uploadFileTask.setAsyncTaskSuccessCallback(success);

        // 判断文件类型
        String type;
        if (CWMessageContentType.VOICE.equals(message.getMessageContentType())) {
            type = "voice";
        } else {
            type = "image";
        }
        uploadFileTask.execute(type, fileName);
    }

    /**
     * 连接监听实现
     *
     * @author xuan
     */
    private static class MyConnectListener implements ConnectListener {
        private final CWImConfig config;

        public MyConnectListener(CWImConfig config) {
            this.config = config;
        }

        @Override
        public void onSuccsss() {
            // 登陆成功
            if (null != config.getOnConnectListener()) {
                config.getOnConnectListener().onSuccess(config.getUser());
            }
        }

        @Override
        public void onClose(int i, String s, boolean b) {

        }

        @Override
        public void onError(Exception e) {
            if (null != config.getOnConnectListener()) {
                CWErrorCode ec = CWErrorCode.obtain(CWErrorCode.Code.EXCEPTION,
                        "异常错误");
                ec.setE(e);
                config.getOnConnectListener().onError(ec);
            }
        }
    }

    /**
     * 消息接受监听
     *
     * @author xuan
     */
    private class MyOnReceiveMessageListener implements
            OnReceiveMessageListener {
        @Override
        public boolean consumeMessage(Message jpMessage) {
            MessageContent messageContent = jpMessage.getContent();
            if (messageContent instanceof CommonMessage) {
                CommonMessage commonMessage = (CommonMessage) messageContent;
                if (JP_CHAT_MESSAGE.equals(commonMessage.getMessageType())) {
                    // 聊天消息，框架处理
                    //consumeConversationMessage(commonMessage);
                    CWConversationMessage cwMessage = CWConversationMessage.decode(commonMessage.getContent());
                    String toId = "";
                    if (cwMessage.isGroup()) {
                        toId = cwMessage.getConversationToId();
                    } else {
                        toId = cwMessage.getSenderUserId();
                    }
                    CWGetMsgTask.getNewMsg(context, toId, cwMessage.getConversationType());
                } else {
                    // 其他消息，具体APP自行处理
                    if (null != getMessageReceiveListener()) {
                        getMessageReceiveListener().onReceive(commonMessage);
                    }
                }
            } else {
                CWLogUtil.e("[" + jpMessage.getClass().getSimpleName()
                        + "]该消息不支持处理");
            }

            return true;
        }
    }

    /**
     * 发送监听实现
     *
     * @author xuan
     */
    private class JPOnMessageSendListener implements OnSendMessageListener {
        private OnMessageSendListener l;
        private CWConversationMessage cwMessage;

        public JPOnMessageSendListener(OnMessageSendListener l, CWConversationMessage cwMessage) {
            this.l = l;
            this.cwMessage = cwMessage;
        }

        public void beforeSend(Message message) {
            CWLogUtil.d("我要发送了哦");
        }

        public void onSuccess(final Message message) {
            if (null != l) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        l.onSuccess(cwMessage);
                    }
                });
            }
        }

        public void onError(RemotingCommand rc) {
            if (null != l) {
                String retError = "";
                if (null != rc) {
                    retError = String.valueOf(rc.getCode());
                }

                final String error = retError;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        l.onError(cwMessage, CWErrorCode.obtain(
                                CWErrorCode.Code.UNKOWN, error));
                    }
                });
            }
        }
    }

}
