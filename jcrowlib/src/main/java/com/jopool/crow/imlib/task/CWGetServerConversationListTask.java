package com.jopool.crow.imlib.task;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.jopool.crow.CWChat;
import com.jopool.crow.imkit.receiver.CWTotalUnreadNumReceiver;
import com.jopool.crow.imlib.constants.UrlConstants;
import com.jopool.crow.imlib.db.dao.CWChatDaoFactory;
import com.jopool.crow.imlib.entity.CWConversation;
import com.jopool.crow.imlib.entity.CWUser;
import com.jopool.crow.imlib.enums.CWConversationType;
import com.jopool.crow.imlib.model.CWConversationModel;
import com.jopool.crow.imlib.model.CWGroupModel;
import com.jopool.crow.imlib.model.CWUserModel;
import com.jopool.crow.imlib.utils.CWHttpUtil;
import com.jopool.crow.imlib.utils.CWLogUtil;
import com.jopool.crow.imlib.utils.asynctask.NetAbstractTask;
import com.jopool.crow.imlib.utils.asynctask.callback.AsyncTaskFailCallback;
import com.jopool.crow.imlib.utils.asynctask.callback.AsyncTaskSuccessCallback;
import com.jopool.crow.imlib.utils.asynctask.helper.Result;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 获取服务器上的会话列表
 * Created by wuhk on 2017/1/10.
 */

public class CWGetServerConversationListTask extends NetAbstractTask<CWGetServerConversationListTask.CWServerConversationList> {

    public CWGetServerConversationListTask(Context context) {
        super(context);
        setShowProgressDialog(false);
        setAsyncTaskFailCallback(new AsyncTaskFailCallback<CWServerConversationList>() {
            @Override
            public void failCallback(Result<CWServerConversationList> result) {
                //Ignore
            }
        });
    }

    @Override
    protected Result<CWServerConversationList> onHttpRequest(Object... params) {
        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("ownerUserId", CWUser.getConnectUserId());

        Result<CWServerConversationList> result = null;
        try {
            CWHttpUtil.CWResponse response = CWHttpUtil.post(CWChat.getInstance().getConfig().getApiPrefix() + UrlConstants.GET_MESSAGE_CONVERSATION_LIST, paramMap);
            if (200 == response.getStatusCode()) {
                CWServerConversationList retData = JSON.parseObject(response.getResultStr(), CWServerConversationList.class);
                CWLogUtil.e("会话列表请求成功了----" + response.getResultStr());
                if (retData.isOk()) {
                    result = new Result<CWServerConversationList>(true, retData.getMessage(), retData.getResult());

                    List<CWServerConversationList.CWServerConversation> conversationList = result.getValue().getList();
                    //存服务器列表会话Id
                    Set<String> serverToIdSet = new HashSet<String>();
                    //缓存群组,用户，会话
                    for (CWServerConversationList.CWServerConversation csc : conversationList) {

                        String conversationToId = "";
                        if (csc.getToType().equals(CWConversationType.USER)) {
                            CWUserModel.getInstance().cacheUserByConversation(csc.getToAppUser());
                            conversationToId = csc.getToAppUser().getUserId();
                        } else if (csc.getToType().equals(CWConversationType.GROUP)) {
                            CWGroupModel.getInstance().cacheGroupByConversation(csc.getToAppGroup());
                            conversationToId = csc.getToAppGroup().getId();
                        }
                        //存入set
                        serverToIdSet.add(conversationToId);

                        // 检查会话是否存在
                        CWConversationModel.getInstance().addOrUpdateConversation(conversationToId, csc.getToType());
                        //拉取该会话的未读消息消息
                        //拉取所有未读消息之前，先将本地的所有未读消息置为已读
                        CWChatDaoFactory.getConversationMessageDao().updateAllUnReadedStatusToReadedStatusByConversationToId(conversationToId);
                        CWGetMsgTask.getUnreadMsg(context, conversationToId, csc.getToType());
                    }
                    //删除本地存在但是服务器上不存在的会话
                    List<CWConversation> localConversationList = CWChatDaoFactory.getConversationDao().findAllDGConversation();
                    for (CWConversation conversation : localConversationList) {
                        if (conversation.getConversationType().equals(CWConversationType.GROUP)){
                            if (!serverToIdSet.contains(conversation.getToGroup().getId())){
                                CWChat.getInstance().removeConversationByToId(conversation.getToGroup().getId());
                            }
                        }else{
                            if (!serverToIdSet.contains(conversation.getToUser().getUserId())){
                                CWChat.getInstance().removeConversationByToId(conversation.getToUser().getUserId());
                            }
                        }
                    }
                    CWTotalUnreadNumReceiver.notifyReceiver(context);
                } else {
                    result = new Result<CWServerConversationList>(false, retData.getMessage());
                }
            } else {
                // 响应非200
                result = new Result<CWServerConversationList>(false, response.getReasonPhrase());
            }
        } catch (Exception e) {
            result = new Result<CWServerConversationList>(false, e.getMessage());
        }

        return result;
    }

    public static class CWServerConversationList extends BaseRes<CWServerConversationList> {

        private List<CWServerConversation> list;

        public List<CWServerConversation> getList() {
            return list;
        }

        public void setList(List<CWServerConversation> list) {
            this.list = list;
        }

        public static class CWServerConversation {
            private String toId;
            private CWConversationType toType;
            private ToAppUser toAppUser;
            private ToAppGroup toAppGroup;
            private LastMessageObj lastMessageObj;

            public String getToId() {
                return toId;
            }

            public void setToId(String toId) {
                this.toId = toId;
            }

            public CWConversationType getToType() {
                return toType;
            }

            public void setToType(CWConversationType toType) {
                this.toType = toType;
            }

            public ToAppUser getToAppUser() {
                return toAppUser;
            }

            public void setToAppUser(ToAppUser toAppUser) {
                this.toAppUser = toAppUser;
            }

            public ToAppGroup getToAppGroup() {
                return toAppGroup;
            }

            public void setToAppGroup(ToAppGroup toAppGroup) {
                this.toAppGroup = toAppGroup;
            }

            public LastMessageObj getLastMessageObj() {
                return lastMessageObj;
            }

            public void setLastMessageObj(LastMessageObj lastMessageObj) {
                this.lastMessageObj = lastMessageObj;
            }

            public static class ToAppUser {
                private String userId;
                private String userName;
                private String userLogo;

                public String getUserId() {
                    return userId;
                }

                public void setUserId(String userId) {
                    this.userId = userId;
                }

                public String getUserName() {
                    return userName;
                }

                public void setUserName(String userName) {
                    this.userName = userName;
                }

                public String getUserLogo() {
                    return userLogo;
                }

                public void setUserLogo(String userLogo) {
                    this.userLogo = userLogo;
                }
            }

            public static class ToAppGroup {
                private String id;
                private String name;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public static class LastMessageObj {
                private String extra;
                private int versionCode;
                private String messageType;
                private String content;
                private String messageCode;

                public String getExtra() {
                    return extra;
                }

                public void setExtra(String extra) {
                    this.extra = extra;
                }

                public int getVersionCode() {
                    return versionCode;
                }

                public void setVersionCode(int versionCode) {
                    this.versionCode = versionCode;
                }

                public String getMessageType() {
                    return messageType;
                }

                public void setMessageType(String messageType) {
                    this.messageType = messageType;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getMessageCode() {
                    return messageCode;
                }

                public void setMessageCode(String messageCode) {
                    this.messageCode = messageCode;
                }
            }
        }
    }

    public static void getServerConversationList(final Context context) {
        CWGetServerConversationListTask getServerConversationListTask = new CWGetServerConversationListTask(context);
        getServerConversationListTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<CWServerConversationList>() {
            @Override
            public void successCallback(Result<CWServerConversationList> result) {
//                List<CWServerConversationList.CWServerConversation> conversationList = result.getValue().getList();
//                //存服务器列表会话Id
//                Set<String> serverToIdSet = new HashSet<String>();
//                //缓存群组,用户，会话
//                for (CWServerConversationList.CWServerConversation csc : conversationList) {
//
//                    String conversationToId = "";
//                    if (csc.getToType().equals(CWConversationType.USER)) {
//                        CWUserModel.getInstance().cacheUserByConversation(csc.getToAppUser());
//                        conversationToId = csc.getToAppUser().getUserId();
//                    } else if (csc.getToType().equals(CWConversationType.GROUP)) {
//                        CWGroupModel.getInstance().cacheGroupByConversation(csc.getToAppGroup());
//                        conversationToId = csc.getToAppGroup().getId();
//                    }
//                    //存入set
//                    serverToIdSet.add(conversationToId);
//
//                    // 检查会话是否存在
//                    CWConversationModel.getInstance().addOrUpdateConversation(conversationToId, csc.getToType());
//                    //拉取该会话的未读消息消息
//                    //拉取所有未读消息之前，先将本地的所有未读消息置为已读
//                    CWChatDaoFactory.getConversationMessageDao().updateAllUnReadedStatusToReadedStatusByConversationToId(conversationToId);
//                    CWGetMsgTask.getUnreadMsg(context, conversationToId, csc.getToType());
//                }
//                //删除本地存在但是服务器上不存在的会话
//                List<CWConversation> localConversationList = CWChatDaoFactory.getConversationDao().findAllDGConversation();
//                for (CWConversation conversation : localConversationList) {
//                    if (conversation.getConversationType().equals(CWConversationType.GROUP)){
//                        if (!serverToIdSet.contains(conversation.getToGroup().getId())){
//                            CWChat.getInstance().removeConversationByToId(conversation.getToGroup().getId());
//                        }
//                    }else{
//                        if (!serverToIdSet.contains(conversation.getToUser().getUserId())){
//                            CWChat.getInstance().removeConversationByToId(conversation.getToUser().getUserId());
//                        }
//                    }
//                }
                //会话删除之后，通知消息刷新
//                CWTotalUnreadNumReceiver.notifyReceiver(context);
            }
        });

        getServerConversationListTask.execute();
    }
}
