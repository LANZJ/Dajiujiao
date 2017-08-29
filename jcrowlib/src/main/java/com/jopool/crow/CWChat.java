package com.jopool.crow;

import android.content.Context;

import com.jopool.crow.imkit.delegate.CWGroupDelegate;
import com.jopool.crow.imkit.delegate.ConversationDelegate;
import com.jopool.crow.imkit.delegate.CustomUIDelegate;
import com.jopool.crow.imkit.delegate.GetProviderDelegate;
import com.jopool.crow.imkit.listeners.GetGroupInfoProvider;
import com.jopool.crow.imkit.listeners.GetGroupUserSelectListProvider;
import com.jopool.crow.imkit.listeners.GetUserInfoProvider;
import com.jopool.crow.imkit.listeners.OnMessageClickListener;
import com.jopool.crow.imkit.listeners.OnMessageLongClickListener;
import com.jopool.crow.imkit.listeners.OnPortraitClickListener;
import com.jopool.crow.imkit.listeners.OnRecordVoiceListener;
import com.jopool.crow.imkit.ui.CWUiModel;
import com.jopool.crow.imlib.db.CWDBHelper;
import com.jopool.crow.imlib.db.dao.CWChatDaoFactory;
import com.jopool.crow.imlib.delegate.CWSendMsgDelegate;
import com.jopool.crow.imlib.entity.CWCacheUser;
import com.jopool.crow.imlib.entity.CWConversation;
import com.jopool.crow.imlib.entity.CWConversationMessage;
import com.jopool.crow.imlib.entity.CWGroup;
import com.jopool.crow.imlib.entity.CWUser;
import com.jopool.crow.imlib.enums.CWConversationType;
import com.jopool.crow.imlib.model.CWConversationMessageModel;
import com.jopool.crow.imlib.model.CWUserModel;
import com.jopool.crow.imlib.soket.JPImClient;
import com.jopool.crow.imlib.soket.listeners.OnBeforeConsumeConversationMesssgeListener;
import com.jopool.crow.imlib.soket.listeners.OnConnectListener;
import com.jopool.crow.imlib.soket.listeners.OnMessageReceiveListener;
import com.jopool.crow.imlib.utils.bitmap.BPBitmapLoader;

import java.util.List;

import extended.Chat;

/**
 * 聊天会话
 *
 * @author xuan
 */
public class CWChat {
    public static final String TAG = "CWIM_TAG";

    private static CWChat instance;
    private static Context application;
    private CWChatConfig mConfig;//全局配置参数
    private JPImClient imClient;//消息client

    private GetProviderDelegate providerDelege = new GetProviderDelegate();
    private ConversationDelegate conversationDelegate = new ConversationDelegate();
    private CWSendMsgDelegate sendMsgDelegate = new CWSendMsgDelegate();
    private CustomUIDelegate customUIDelegate = new CustomUIDelegate();
    private CWGroupDelegate groupDelegate = new CWGroupDelegate();

    private CWUiModel uiModel = CWUiModel.getInstance();

    public CWChat(Context context) {
        mConfig = new CWDefaultChatConfig();
        imClient = new JPImClient(context);
    }

    /**
     * 聊天部分初始化
     *
     * @param application
     * @param config
     */
    public static void init(Context application, CWChatConfig config) {
        if (null == instance) {
            instance = new CWChat(application);
        }

        CWChat.getInstance().setConfig(config);
        CWChat.application = application;

        //DB初始化
        CWDBHelper.init(1, config.getDbName(), application);
        //BitmapLoader初始化
        BPBitmapLoader.init(application);
    }

    public static CWChat getInstance() {
        if (null == instance) {
            throw new NullPointerException("Call CWChat.init fisrt.");
        }
        return instance;
    }

    public static Context getApplication() {
        return application;
    }

    public CWChatConfig getConfig() {
        return mConfig;
    }

    public void setConfig(CWChatConfig config) {
        this.mConfig = config;
    }

    public JPImClient getImClient() {
        return imClient;
    }

    public void setImClient(JPImClient imClient) {
        this.imClient = imClient;
    }

    /**
     * 启动微信
     *
     * @param context
     * @param user
     * @param messageReceiveListener
     * @param connectListener
     */
    public void startWork(Context context, CWUser user,
                          OnMessageReceiveListener messageReceiveListener,
                          OnConnectListener connectListener) {
        /**
         * 保存登录用户信息
         */
        CWUser.setUser(user);

        /**
         * 缓存到表
         */
        CWUserModel.getInstance().cacheUser(CWCacheUser.obtainNew(user.getUserId(), user.getName(), user.getUrl()));

        /**
         * 先获取token,然后再拿着token去登录通讯服务器,百度推送的channelId等成功后再设置
         */
        Chat.getInstance().setOnMessageReceiveListener(messageReceiveListener);
        Chat.getInstance().setConnectListener(connectListener);
        Chat.getInstance().startChat(context, "");
    }

    /**
     * 启动会话界面
     *
     * @param context
     * @param conversationType
     * @param toId
     * @param title
     */
    @Deprecated
    public void startConversation(Context context,
                                  CWConversationType conversationType, String toId, String title) {
        getConversationDelegate().startConversation(context, conversationType, toId, title, null);
    }

    // //////////////////////////////各种监听设置////////////////////////////////////////////

    public OnBeforeConsumeConversationMesssgeListener getOnBeforeConsumeConversationMesssgeListener() {
        return getImClient().getBeforeConsumeConversationMesssgeListener();
    }

    public void setOnBeforeConsumeConversationMesssgeListener(OnBeforeConsumeConversationMesssgeListener l) {
        getImClient().setBeforeConsumeConversationMesssgeListener(l);
    }

    ///////////////////////////////////////消息接受///////////////////////////////////
    public OnMessageReceiveListener getOnMessageReceiveListener() {
        return getImClient().getMessageReceiveListener();
    }

    public void setOnMessageReceiveListener(OnMessageReceiveListener l) {
        getImClient().setMessageReceiveListener(l);
    }

    ///////////////////////////////////////录音监听///////////////////////////////////
    public OnRecordVoiceListener getOnRecordVoiceListener() {
        return getConversationDelegate().getOnRecordVoiceListener();
    }

    public void setOnRecordVoiceListener(OnRecordVoiceListener l) {
        getConversationDelegate().setOnRecordVoiceListener(l);
    }

    ///////////////////////////////////////聊天消息点击事件///////////////////////////////////
    public OnMessageClickListener getOnMessageClickListener() {
        return getConversationDelegate().getOnMessageClickListener();
    }

    public void setOnMessageClickListener(OnMessageClickListener l) {
        getConversationDelegate().setOnMessageClickListener(l);
    }

    ///////////////////////////////////////聊天消息长按事件///////////////////////////////////
    public OnMessageLongClickListener getOnMessageLongClickListener() {
        return getConversationDelegate().getOnMessageLongClickListener();
    }

    public void setOnMessageLongClickListener(OnMessageLongClickListener l) {
        getConversationDelegate().setOnMessageLongClickListener(l);
    }

    ///////////////////////////////////////头像点击事件///////////////////////////////////
    public OnPortraitClickListener getPortraitClickListener() {
        return getConversationDelegate().getPortraitClickListener();
    }

    public void setPortraitClickListener(OnPortraitClickListener l) {
        getConversationDelegate().setPortraitClickListener(l);
    }

    ///////////////////////////////////////设置用户信息///////////////////////////////////
    public GetUserInfoProvider getGetUserInfoProvider() {
        return getProviderDelegate().getGetUserInfoProvider();
    }

    public void setGetUserInfoProvider(GetUserInfoProvider getUserInfoProvider) {
        getProviderDelegate().setGetUserInfoProvider(getUserInfoProvider);
    }

    //////////////////////////////设置群组信息////////////////////////////////////
    public GetGroupInfoProvider getGetGroupInfoProvider() {
        return getProviderDelegate().getGetGroupInfoProvider();
    }

    public void setGetGroupInfoProvider(
            GetGroupInfoProvider getGroupInfoProvider) {
        getProviderDelegate().setGetGroupInfoProvider(getGroupInfoProvider);
    }

    //////////////////////////////设置选择成员列表信息///////////////////////////////
    public GetGroupUserSelectListProvider getGroupUserSelectListProvider() {
        return getGroupDelegate().getGetGroupUserSelectListProvider();
    }

    public void setGetGroupUserSelectListProvider(GetGroupUserSelectListProvider getGroupUserSelectListProvider) {
        getGroupDelegate().setGetGroupUserSelectListProvider(getGroupUserSelectListProvider);
    }

    /////////////////////////////获取各种操作的代理/////////////////////////////////////
    public GetProviderDelegate getProviderDelegate() {
        return providerDelege;
    }

    public ConversationDelegate getConversationDelegate() {
        return conversationDelegate;
    }

    public CWSendMsgDelegate getSendMsgDelegate() {
        return sendMsgDelegate;
    }

    public CustomUIDelegate getCustomUIDelegate() {
        return customUIDelegate;
    }

    public CWGroupDelegate getGroupDelegate() {
        return groupDelegate;
    }

    /////////////////////////////UI逻辑/////////////////////////////////////
    public CWUiModel getUiModel() {
        return uiModel;
    }

    // //////////////////////////////对数据库操作//////////////////////////////////////////////////

    /**
     * 修改会话中的最后一条消息为未读
     *
     * @param toId
     */
    public void modifyLastMessageUnreadByToId(String toId) {
        CWConversationMessageModel conversationMessageModel = new CWConversationMessageModel();
        conversationMessageModel.modifyLastMessageUnReadByToId(toId);
    }

    /**
     * 置顶聊天
     *
     * @param toId 单聊就是对方用户userId，群聊就是群组id
     */
    public void priorityUp(String toId) {
        CWChatDaoFactory.getConversationDao().updatePriority(toId, 1);
    }

    /**
     * 取消置顶聊天
     *
     * @param toId
     */
    public void priorityUpCancel(String toId) {
        CWChatDaoFactory.getConversationDao().updatePriority(toId, 0);
    }

    /**
     * 获取所有会话列表
     *
     * @return
     */
    public List<CWConversation> getConversationList() {
        return CWChatDaoFactory.getConversationDao().findAllDGConversation();
    }

    /**
     * 获取会话对象
     *
     * @param toId 单聊就是对方用户userId，群聊就是群组id
     * @return
     */
    public CWConversation getConversationByToId(String toId) {
        CWConversation conversation = CWChatDaoFactory.getConversationDao()
                .findByToId(toId);
        if (null != conversation) {
            // 添加对发送人
            if (CWConversationType.USER.equals(conversation
                    .getConversationType())) {
                // 设置用户信息
                CWUser user = CWChat.getInstance().getProviderDelegate().getGetUserInfoProvider()
                        .getUserById(conversation.getToId());
                conversation.setToUser(user);
            } else if (CWConversationType.GROUP.equals(conversation
                    .getConversationType())) {
                CWGroup group = CWChat.getInstance().getProviderDelegate().getGetGroupInfoProvider()
                        .getGroupById(conversation.getToId());
                conversation.setToGroup(group);
            }

            // 添加未读数量
            int unreadNum = CWChatDaoFactory.getConversationMessageDao()
                    .countUnreadNumByConversationToId(conversation.getToId());
            conversation.setUnreadNum(unreadNum);

            // 添加最后一条消息
            CWConversationMessage lastMessage = CWChatDaoFactory
                    .getConversationMessageDao()
                    .findLastMessageByConversationToId(conversation.getToId());
            conversation.setLastMessage(lastMessage);
        }
        return conversation;
    }

    /**
     * 获取所有未读消息数量
     *
     * @return
     */
    public int getTotalUnreadNum() {
        return CWChatDaoFactory.getConversationMessageDao()
                .countTotalUnreadNum();
    }

    /**
     * 获取某个会话的未读消息数量
     *
     * @param toId 单聊就是对方用户userId，群聊就是群组id
     * @return
     */
    public int getConversationUnreadedNum(String toId) {
        return CWChatDaoFactory.getConversationMessageDao()
                .countUnreadNumByConversationToId(toId);
    }

    /**
     * 查找会话中的最后一条消息
     *
     * @param toId 单聊就是对方用户userId，群聊就是群组id
     * @return
     */
    public CWConversationMessage getLastMessageByToId(String toId) {
        return CWChatDaoFactory.getConversationMessageDao()
                .findLastMessageByConversationToId(toId);
    }

    /**
     * 删除单条消息
     *
     * @param id 消息id
     */
    public void removeMessageById(String id) {
        CWChatDaoFactory.getConversationMessageDao().deleteById(id);
    }

    /**
     * 删除会话，同时也会删除这个会话下的所有消息，本身的会话也会删除
     *
     * @param toId 单聊就是对方用户userId，群聊就是群组id
     */
    public void removeConversationByToId(String toId) {
        CWChatDaoFactory.getConversationDao().deleteByToId(toId);
        CWChatDaoFactory.getConversationMessageDao().deleteByConversationToId(
                toId);
    }

}