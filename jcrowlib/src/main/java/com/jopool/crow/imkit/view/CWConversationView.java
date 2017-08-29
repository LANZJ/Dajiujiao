package com.jopool.crow.imkit.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.jopool.crow.CWChat;
import com.jopool.crow.R;
import com.jopool.crow.imkit.adapter.ConversationMessageAdapter;
import com.jopool.crow.imkit.listeners.OnRecordVoiceListener.RecordResultListener;
import com.jopool.crow.imkit.listeners.TopNoticeProvider;
import com.jopool.crow.imkit.receiver.CWConversationUnreadNumReceiver;
import com.jopool.crow.imkit.receiver.CWRefreshConversationListReceiver;
import com.jopool.crow.imlib.db.dao.CWChatDaoFactory;
import com.jopool.crow.imlib.entity.CWConversation;
import com.jopool.crow.imlib.entity.CWConversationMessage;
import com.jopool.crow.imlib.entity.CWUser;
import com.jopool.crow.imlib.enums.CWConversationType;
import com.jopool.crow.imlib.enums.CWSendStatus;
import com.jopool.crow.imlib.soket.CWErrorCode;
import com.jopool.crow.imlib.soket.listeners.OnMessageSendListener;
import com.jopool.crow.imlib.task.CWMakeMessageReadedTask;
import com.jopool.crow.imlib.utils.CWToastUtil;
import com.jopool.crow.imlib.utils.CWValidator;
import com.jopool.crow.imlib.utils.ChatFileUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 会话聊天布局
 *
 * @author xuan
 */
public class CWConversationView extends CWBaseLayout {
    private final Activity activity;
    private CWTitleLayout titleLayout;// 标题栏
    private CWConversationNoticeView noticeView;//顶部通告栏
    private CWConversationBottomBarView bottomBarView;// 底部操作栏
    private CWConversationVolumeView volumeView;// 录音提示

    private ListView listView;// 消息内容listView
    private ConversationMessageAdapter chatAdapter;// 适配器
    private List<CWConversationMessage> dataList;// 数据

    private String toId;
    private CWConversationType conversationType;
    private String ownerUserId;

    private Handler handler;

    /**
     * 发送消息回调
     */
    private OnMessageSendListener onSendMessageListener;

    public CWConversationView(Context context) {
        super(context);
        activity = (Activity) context;
    }

    public CWConversationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        activity = (Activity) context;
    }

    @Override
    protected void onViewInit() {
        loadView();
        initWidgits();
    }

    /**
     * Load view
     */
    private void loadView() {
        inflate(getContext(), R.layout.cw_chat_view_conversation, this);
        bottomBarView = (CWConversationBottomBarView) findViewById(R.id.bottomBarView);
        titleLayout = (CWTitleLayout) findViewById(R.id.titleLayout);
        noticeView = (CWConversationNoticeView) findViewById(R.id.noticeView);
        listView = (ListView) findViewById(R.id.listView);
        volumeView = (CWConversationVolumeView) findViewById(R.id.volumeView);
    }

    /**
     * Init view
     */
    private void initWidgits() {
        handler = new Handler();
        ownerUserId = CWUser.getConnectUserId();

        // 初始化列表
        dataList = new ArrayList<CWConversationMessage>();
        chatAdapter = new ConversationMessageAdapter(getContext(), dataList);
        listView.setAdapter(chatAdapter);
        //
        handler.post(new Runnable() {
            @Override
            public void run() {
                listView.setSelection(listView.getAdapter().getCount() - 1);//滚动到底部
            }
        });
        // 标题
        titleLayout.configTitle("聊天").configReturn(new OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
        // 点击发送文本
        bottomBarView.getSendBtn().setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomBarView.getSendBtn().setClickable(false);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                bottomBarView.getSendBtn().setClickable(true);// 防止重复发送
                            }
                        }, 1000);

//                        sendTextMessage(bottomBarView.getInputEt().getText()
//                                .toString());
                        CWChat.getInstance().getSendMsgDelegate().sendTextMessage(getContext(), toId, bottomBarView.getInputEt().getText()
                                .toString(), conversationType);
                        bottomBarView.getInputEt().setText("");
                    }
                });
        // 录音结果监听
        CWChat.getInstance().getOnRecordVoiceListener()
                .setRecordResultListener(new RecordResultListener() {
                    @Override
                    public void onVolume(int v) {
                        volumeView.setVolume(v);
                    }

                    @Override
                    public void onTooShort() {
                        volumeView.interruptPrepare();
                        volumeView.toastTooShot();
                    }

                    @Override
                    public void onRecordStoped(boolean isCancel,
                                               boolean success, String fileName, long voiceLength) {
                        if (!isCancel && success) {
                            // 非取消，并且是录音成功的
//                            sendVoiceMessage(
//                                    ChatFileUtils.getVoiceFileName(fileName),
//                                    voiceLength);
                            CWChat.getInstance().getSendMsgDelegate().sendVoiceMessage(getContext(), toId, ChatFileUtils.getVoiceFileName(fileName), voiceLength, conversationType);
                        }
                    }
                });

        // 录音按下按钮
        final Button voiceBtn = bottomBarView.getVoiceBtn();
        voiceBtn.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        volumeView.showPrePare();
                        voiceBtn.setBackgroundResource(R.drawable.cw_chat_conversation_bottom_bar_voice_btn_pressed);
                        CWChat.getInstance().getOnRecordVoiceListener()
                                .startRecord();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (moveIsOutVoiceBtn(event)) {
                            volumeView.showReleaseUpCancel();
                        } else {
                            volumeView.showSlideUpCancel();
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        volumeView.hide();
                        voiceBtn.setBackgroundResource(R.drawable.cw_chat_conversation_bottom_bar_voice_btn_normal);
                        if (moveIsOutVoiceBtn(event)) {
                            CWChat.getInstance().getOnRecordVoiceListener()
                                    .cancelRecord();
                        } else {
                            CWChat.getInstance().getOnRecordVoiceListener()
                                    .finishRecord();
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        // 发送消息回调
        onSendMessageListener = new OnMessageSendListener() {
            @Override
            public void onSuccess(CWConversationMessage message) {
                modifySendStatus(message.getId(),
                        CWSendStatus.SUCCESS.getValue());
            }

            @Override
            public void onError(CWConversationMessage message,
                                CWErrorCode errorCode) {
                modifySendStatus(message.getId(), CWSendStatus.FAIL.getValue());
                CWToastUtil.displayTextShort(errorCode.getMessage());
            }
        };

        //初始化通知
        CWChat.getInstance().getProviderDelegate().getTopNoticeProvider().getNotice(toId, new TopNoticeProvider.ShowNoticeCallback() {
            @Override
            public void show(TopNoticeProvider.Notice notice) {
                if (null == notice) {
                    notice = new TopNoticeProvider.Notice();
                }
                noticeView.initNotice(notice);
            }
        });
    }

    /**
     * 是否移出了
     */
    private boolean moveIsOutVoiceBtn(MotionEvent event) {
        if (event.getY() < -50) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 装载数据
     *
     * @param toId
     * @param conversationType
     */
    public void loadData(String toId, CWConversationType conversationType) {
        this.toId = toId;
        this.conversationType = conversationType;
        setupData();
        // 设置改会话所有消息为已读，除了语音
        CWConversation conversation = CWChatDaoFactory.getConversationDao()
                .findByToId(toId);
        if (null != conversation) {
            //进入聊天界面的时候，将服务器未读消息置为已读
            List<CWConversationMessage> unreadList = CWChatDaoFactory.getConversationMessageDao().findUnReadMessage(conversation.getToId());
            CWMakeMessageReadedTask.makeMessageReaded(getContext() , unreadList , null);

            CWChatDaoFactory.getConversationMessageDao()
                    .updateAllUnReadedStatusToReadedStatusByConversationToId(
                            conversation.getToId());
            // 通知他们一下
            CWConversationUnreadNumReceiver.notifyReceiver(getContext(),
                    conversation.getToId());
            //通知刷新会话列表
            CWRefreshConversationListReceiver.notifyReceiver(getContext());
        }
    }

    public CWTitleLayout getTitleLayout() {
        return titleLayout;
    }

    public ListView getListView() {
        return listView;
    }

    public CWConversationBottomBarView getBottomBarView() {
        return bottomBarView;
    }

    public ConversationMessageAdapter getChatAdapter() {
        return chatAdapter;
    }

    public List<CWConversationMessage> getDataList() {
        return dataList;
    }

    /**
     * 发送语音，语音会直接录音到指定目录下的
     *
     * @param voiceFileName
     * @param voiceLength
     */
//    public void sendVoiceMessage(String voiceFileName, long voiceLength) {
//        if (!CWChat.getInstance().getImClient().isLogined()) {
//            CWToastUtil.displayTextShort("微信服务器未连接");
//            return;
//        }
//
//        if (CWValidator.isEmpty(voiceFileName)) {
//            CWToastUtil.displayTextShort("语音不存在～");
//            return;
//        }
//        // 检查会话，不存在就插入
//        CWConversation conversation = CWConversationModel.getInstance()
//                .addOrUpdateConversation(toId, conversationType);
//
//        // 先入本地数据库
//        CWConversationMessage message = saveToDatabase(CWUUIDUtils.createId(),
//                CWMessageContentType.VOICE, voiceFileName, "",
//                (int) (voiceLength / 1000));
//        // 数据列表添加
//        dataList.add(message);
//        chatAdapter.notifyDataSetChanged();
//
//        // 发送消息
//        CWChat.getInstance().getImClient()
//                .sendMessage(getContext(), message, onSendMessageListener, conversationType);
//    }

    /**
     * 发送图片，图片需要copy指定目录下
     *
     * @param fromFileName
     */
//    public void sendImageMessage(String fromFileName) {
//        if (!CWChat.getInstance().getImClient().isLogined()) {
//            CWToastUtil.displayTextShort("微信服务器未连接");
//            return;
//        }
//
//        if (CWValidator.isEmpty(fromFileName)) {
//            CWToastUtil.displayTextShort("图片不存在～");
//            return;
//        }
//
//        // 检查会话，不存在就插入
//        CWConversation conversation = CWConversationModel.getInstance()
//                .addOrUpdateConversation(toId, conversationType);
//
//        // copy图片
//        String uuid = CWUUIDUtils.createId();
//        String toBigFileName = ChatFileUtils.getImageFileNameForBig(uuid);
//        String toSmallFileName = ChatFileUtils.getImageFileNameForSmall(uuid);
//        ChatFileUtils.copyImageForBig(fromFileName, toBigFileName);
//        ChatFileUtils.copyImageForSmall(fromFileName, toSmallFileName);
//        // 先入本地数据库
//        CWConversationMessage message = saveToDatabase(uuid,
//                CWMessageContentType.IMAGE, toSmallFileName, toBigFileName, 0);
//        // 数据列表添加
//        dataList.add(message);
//        chatAdapter.notifyDataSetChanged();
//
//        // 发送消息
//        CWChat.getInstance().getImClient()
//                .sendMessage(getContext(), message, onSendMessageListener, conversationType);
//    }

    /**
     * 发送文本消息
     *
     * @param inputText
     */
//    public void sendTextMessage(String inputText) {
//        if (!CWChat.getInstance().getImClient().isLogined()) {
//            CWToastUtil.displayTextShort("通讯服务器未连接");
//            return;
//        }
//
//        if (CWValidator.isEmpty(inputText)) {
//            CWToastUtil.displayTextShort("输入点什么呗～");
//            return;
//        }
//        // 检查会话，不存在就插入
//        CWConversation conversation = CWConversationModel.getInstance()
//                .addOrUpdateConversation(toId, conversationType);
//
//        // 先入本地数据库
//        CWConversationMessage message = saveToDatabase(CWUUIDUtils.createId(),
//                CWMessageContentType.TEXT, inputText, "", 0);
//        // 数据列表添加
//        dataList.add(message);
//        chatAdapter.notifyDataSetChanged();
//
//        bottomBarView.getInputEt().setText("");
//
//        // 发送消息
//        CWChat.getInstance().getImClient()
//                .sendMessage(getContext(), message, onSendMessageListener, conversationType);
//    }

    /**
     * 保存消息到数据库
     */
//    private CWConversationMessage saveToDatabase(String uuid,
//                                                 CWMessageContentType messageContentType, String content,
//                                                 String downloadUrl, int voiceLenght) {
//        CWConversationMessage insertMessage = CWConversationMessage.obtain(
//                ownerUserId, conversationType, toId, CWMessageType.RIGHT,
//                messageContentType, content, downloadUrl, voiceLenght,
//                CWReadStatus.READED, CWSendStatus.ING);
//        CWChatDaoFactory.getConversationMessageDao().insert(insertMessage);
//        return insertMessage;
//    }

    /**
     * 刷新数据
     */
    private void setupData() {
        dataList.clear();
        List<CWConversationMessage> temp = CWChatDaoFactory
                .getConversationMessageDao().findByConversationToId(
                        ownerUserId, toId);
        dataList.addAll(temp);
    }

    /**
     * 修改发送状态
     */
    private void modifySendStatus(String messageId, int sendStatus) {
        // 修改数据库
        CWChatDaoFactory.getConversationMessageDao().updateSendStatusById(
                messageId, sendStatus);
        // 修改内存
        if (!CWValidator.isEmpty(dataList)) {
            // 遍历设置消息发送状态
            for (CWConversationMessage message : dataList) {
                if (message.getId().equals(messageId)) {
                    message.setSendStatus(CWSendStatus.valueOf(sendStatus));
                    break;// 找到一条消息后就返回
                }
            }
        }
        chatAdapter.notifyDataSetChanged();
    }

    /**
     * 刷新数据
     */
    public void refreshChatList() {
        dataList.clear();
        List<CWConversationMessage> temp = CWChatDaoFactory
                .getConversationMessageDao().findByConversationToId(
                        ownerUserId, toId);
        dataList.addAll(temp);
        //
        chatAdapter.notifyDataSetChanged();
        //滚动到底部
        handler.post(new Runnable() {
            @Override
            public void run() {
                listView.setSelection(listView.getAdapter().getCount() - 1);
            }
        });
    }
}
