package com.jopool.crow.imlib.constants;

/**
 * Created by xuan on 16/8/17.
 */
public class UrlConstants {
    /**
     * 绑定百度推送
     */
    public static final String URL_BIND_CHANNELID = "/rest/bpush/bindBpushChannelId.htm";

    /**
     * 取消百度推送的绑定
     */
    public static final String URL_UNBIND_CHANNELID = "/rest/bpush/unBindBpushChannelId.htm";

    /**
     * 获取单聊消息
     */
    public static final String GET_USER_MSG_URL = "/rest/message/getMessageHistory.htm";

    /**
     * 获取群聊消息
     */
    public static final String GET_GROUP_MSG_URL = "/rest/message/getGroupMessageHistory.htm";

    /**
     * 获取token
     */
    public static final String GET_TOKEN = "/rest/token.htm";

    /**
     * 删除消息
     */
    public static final String REMOVW_CHAT_MESSAGE = "/rest/message/removeChatMessage.htm";

    /**
     * 获取用户信息
     */
    public static final String GET_USER = "/rest/user/getUserIfNotExsitWillAdd.htm";

    /**
     * 创建群组
     */
    public static final String CREATE_GROUP = "/rest/group/createGroup.htm";

    /**
     * 用户添加到群组
     */
    public static final String ADD_USER_TO_GROUP = "/rest/group/addUserToGroup.htm";

    /**
     * 获取群组基本信息
     */
    public static final String GET_GROUP = "/rest/group/getGroup.htm";

    /**
     * 修改群组名称
     */
    public static final String MODIFY_GROUP_NAME = "/rest/group/modifyGroupName.htm";

    /**
     * 从群组移除
     */
    public static final String REMOVE_USER_FROM_GROUP = "/rest/group/removeUserFromGroup.htm";

    /**
     * 文件上传接口
     */
    public static final String FILE_UPLOAD = "/files/upload.htm";

    /**
     * 设置消息已读
     */
    public static final String MAKE_MESSAGE_READED = "/rest/message/makeMessageReaded.htm";

    /**
     * 获取会话列表
     */
    public static final String GET_MESSAGE_CONVERSATION_LIST = "/rest/message/getMessageConversationList.htm";

    /**
     * 删除会话
     */
    public static final String REMOVE_MESSAGE_CONVERSATION = "/rest/message/removeMessageConversation.htm";


}
