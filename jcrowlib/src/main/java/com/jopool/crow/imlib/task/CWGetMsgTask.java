package com.jopool.crow.imlib.task;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.jopool.crow.CWChat;
import com.jopool.crow.imlib.constants.UrlConstants;
import com.jopool.crow.imlib.db.dao.CWChatDaoFactory;
import com.jopool.crow.imlib.entity.CWConversationMessage;
import com.jopool.crow.imlib.entity.CWUser;
import com.jopool.crow.imlib.enums.CWConversationType;
import com.jopool.crow.imlib.model.CWGroupModel;
import com.jopool.crow.imlib.model.CWUserModel;
import com.jopool.crow.imlib.utils.CWHttpUtil;
import com.jopool.crow.imlib.utils.CWLogUtil;
import com.jopool.crow.imlib.utils.CWValidator;
import com.jopool.crow.imlib.utils.asynctask.NetAbstractTask;
import com.jopool.crow.imlib.utils.asynctask.callback.AsyncTaskFailCallback;
import com.jopool.crow.imlib.utils.asynctask.callback.AsyncTaskSuccessCallback;
import com.jopool.crow.imlib.utils.asynctask.helper.Result;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuan on 16/8/22.
 */
public class CWGetMsgTask extends NetAbstractTask<CWGetMsgTask.GetMsgRes> {
    private CWGetMsgTask(Context context) {
        super(context);
        setShowProgressDialog(false);
        setAsyncTaskFailCallback(new AsyncTaskFailCallback<GetMsgRes>() {
            @Override
            public void failCallback(Result<GetMsgRes> result) {
                //Ignore
            }
        });
    }

    @Override
    protected Result<GetMsgRes> onHttpRequest(Object... params) {
        Map<String, String> paramMap = new HashMap<String, String>();
        String subUrl = "";
        boolean replace = false;
        if (params[0] instanceof GetGroupMsgReq) {
            GetGroupMsgReq getGroupMsgReq = (GetGroupMsgReq) params[0];
            paramMap.put("lastTime", String.valueOf(getGroupMsgReq.getLastTime()));
            paramMap.put("mode", getGroupMsgReq.getMode());
            paramMap.put("groupId", getGroupMsgReq.getGroupId());
            paramMap.put("ownerUserId", CWUser.getConnectUserId());
            subUrl = UrlConstants.GET_GROUP_MSG_URL;
            if (String.valueOf(getGroupMsgReq.getLastTime()).equals("-2")){
                replace = true;
            }
        } else if (params[0] instanceof GetUserMsgReq) {
            GetUserMsgReq getUserMsgReq = (GetUserMsgReq) params[0];
            paramMap.put("lastTime", String.valueOf(getUserMsgReq.getLastTime()));
            paramMap.put("mode", getUserMsgReq.getMode());
            paramMap.put("userIdOne", getUserMsgReq.getUserIdOne());
            paramMap.put("userIdTwo", getUserMsgReq.getUserIdTwo());
            subUrl = UrlConstants.GET_USER_MSG_URL;
            if (String.valueOf(getUserMsgReq.getLastTime()).equals("-2")){
                replace = true;
            }
        }

        Result<GetMsgRes> result = null;
        try {
            CWHttpUtil.CWResponse response = CWHttpUtil.post(CWChat.getInstance().getConfig().getApiPrefix() + subUrl, paramMap);
            if (200 == response.getStatusCode()) {
                GetMsgRes getMsgRes = JSON.parseObject(response.getResultStr(), GetMsgRes.class);
                if (getMsgRes.isOk()) {
                    List<GetMsgResResultItem> getMsgResList = getMsgRes.getResult().getList();
                    //
                    CWUserModel.getInstance().cacheUserByMsg(getMsgResList);//缓存用户
                    CWGroupModel.getInstance().cacheGroupByMsg(getMsgResList);//如果是群组消息,缓存群组信息
                    //
                    CWLogUtil.d("加载消息数[" + getMsgResList.size() + "]");
                    Collections.reverse(getMsgResList);//翻转
                    if (!CWValidator.isEmpty(getMsgResList)) {
                        for (GetMsgResResultItem getMsgResResultItem : getMsgResList) {
                            CWConversationMessage message = JSON.parseObject(getMsgResResultItem.getMessage(), CWConversationMessage.class);
                            message.setCreationTime(getMsgResResultItem.getCreationTime());//
                            message.setModifyTime(getMsgResResultItem.getCreationTime());//服务器时间为准
                            CWChat.getInstance().getImClient().consumeConversationMessage(message, getMsgResResultItem.getStatus() , replace);
                        }
                    }
                    result = new Result<GetMsgRes>(true, getMsgRes.getMessage(), getMsgRes.getResult());
                } else {
                    result = new Result<GetMsgRes>(false, getMsgRes.getMessage());
                }
            } else {
                // 响应非200
                result = new Result<GetMsgRes>(false, response.getReasonPhrase());
            }
        } catch (Exception e) {
            result = new Result<GetMsgRes>(false, e.getMessage());
        }

        return result;
    }

    /**
     * 两种情况:
     * (1)lastTime = -1,取服务器上的所有未读消息
     * (2)说明会话有消息,并拿到了最新消息的creationTime,去这个时间之后的最新10条,注意这种情况有可能拿不完全所有最新消息,所有里面有递归
     *
     * @param lastTime
     * @param context
     * @param toId
     */
    private static void getNewMsg(final long lastTime, final Context context, final String toId, final CWConversationType toType) {
        Object objReq = null;
        if (CWConversationType.GROUP.equals(toType)) {
            objReq = GetGroupMsgReq.obtainGt(lastTime, toId);
        } else {
            objReq = GetUserMsgReq.obtainGt(lastTime, toId);
        }
        //查询所有未读消息
        GetUserMsgReq getUserMsgReq = GetUserMsgReq.obtainGt(lastTime, toId);
        CWGetMsgTask task = new CWGetMsgTask(context);
        task.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<GetMsgRes>() {
            @Override
            public void successCallback(Result<GetMsgRes> result) {
                //数据库操作放在这里
                List<GetMsgResResultItem> itemList = result.getValue().getList();
                if (lastTime > 0 && itemList.size() >= 10) {
                    //说明服务器上还有最新的消息,再获取一次
                    long l = itemList.get(itemList.size() - 1).getCreationTime().getTime();
                    CWGetMsgTask.getNewMsg(l, context, toId, toType);
                }
                //有两种情况是不需要再递归去查询
                //(1)lastTime=-1,表示只需要获取最新10条
                //(2)lastTime>0,但是获取到的数据少于10条,这就意味没有更多数据
            }
        });
        task.execute(objReq);
    }

    /**
     * 获取消息
     *
     * @param context
     * @param toId
     */
    public static void getNewMsg(final Context context, final String toId, final CWConversationType toType) {
        long lastTime = -1;
        CWConversationMessage lastLeftMessage = CWChatDaoFactory.getConversationMessageDao().findLastMessageByConversationToIdForLeft(toId);
        if (null != lastLeftMessage) {
            lastTime = lastLeftMessage.getCreationTime().getTime();
        }
        //
        CWGetMsgTask.getNewMsg(lastTime, context, toId, toType);
    }

    /**
     * 第一次登陆获取未读消息
     *
     * @param context
     * @param toId
     */
    public static void getUnreadMsg(final Context context, final String toId, final CWConversationType toType) {
        long lastTime = -2;
        CWGetMsgTask.getNewMsg(lastTime, context, toId, toType);
    }

    /**
     * 获取群聊消息请求对象
     */
    public static class GetGroupMsgReq {
        private long lastTime;
        private String mode;
        private String groupId;

        public static GetGroupMsgReq obtain(long lastTime, String mode, String groupId) {
            GetGroupMsgReq pp = new GetGroupMsgReq();
            pp.lastTime = lastTime;
            pp.mode = mode;
            pp.groupId = groupId;
            return pp;
        }

        public static GetGroupMsgReq obtainGt(long lastTime, String groupId) {
            return GetGroupMsgReq.obtain(lastTime, "gt", groupId);
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public long getLastTime() {
            return lastTime;
        }

        public void setLastTime(long lastTime) {
            this.lastTime = lastTime;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }
    }

    /**
     * 单聊消息请求对象
     */
    public static class GetUserMsgReq {
        private long lastTime;
        private String mode;
        private String userIdOne;
        private String userIdTwo;

        public static GetUserMsgReq obtain(long lastTime, String mode, String userIdTwo) {
            GetUserMsgReq pp = new GetUserMsgReq();
            pp.lastTime = lastTime;
            pp.mode = mode;
            pp.userIdOne = CWUser.getConnectUserId();
            pp.userIdTwo = userIdTwo;
            return pp;
        }

        public static GetUserMsgReq obtainGt(long lastTime, String toId) {
            return GetUserMsgReq.obtain(lastTime, "gt", toId);
        }

        public long getLastTime() {
            return lastTime;
        }

        public void setLastTime(long lastTime) {
            this.lastTime = lastTime;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getUserIdOne() {
            return userIdOne;
        }

        public void setUserIdOne(String userIdOne) {
            this.userIdOne = userIdOne;
        }

        public String getUserIdTwo() {
            return userIdTwo;
        }

        public void setUserIdTwo(String userIdTwo) {
            this.userIdTwo = userIdTwo;
        }
    }

    /**
     * 返回结果
     */
    public static class GetMsgRes extends BaseRes<GetMsgRes> {
        private List<GetMsgResResultItem> list;

        public List<GetMsgResResultItem> getList() {
            return list;
        }

        public void setList(List<GetMsgResResultItem> list) {
            this.list = list;
        }
    }

    /**
     * 返回消息对象
     */
    public static class GetMsgResResultItem {

        public static String REAL_STATUS_SUCCESS = "SUCCESS";
        public static String REAL_STATUS_FAIL = "FAIL";
        public static String REAL_STATUS_READED = "READED";

        private String fromAppUserId;
        private String fromUserId;
        private String fromUserName;
        private String fromUserLogo;
        private String toUserId;
        private String message;//Json格式的字符串
        private String status;
        private Date creationTime;

        //
        private String toGroupId;
        private String toGroupName;

        public String getFromUserId() {
            return fromUserId;
        }

        public void setFromUserId(String fromUserId) {
            this.fromUserId = fromUserId;
        }

        public String getFromUserLogo() {
            return fromUserLogo;
        }

        public void setFromUserLogo(String fromUserLogo) {
            this.fromUserLogo = fromUserLogo;
        }

        public String getFromUserName() {
            return fromUserName;
        }

        public void setFromUserName(String fromUserName) {
            this.fromUserName = fromUserName;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getToUserId() {
            return toUserId;
        }

        public void setToUserId(String toUserId) {
            this.toUserId = toUserId;
        }

        public Date getCreationTime() {
            return creationTime;
        }

        public void setCreationTime(Date creationTime) {
            this.creationTime = creationTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getFromAppUserId() {
            return fromAppUserId;
        }

        public void setFromAppUserId(String fromAppUserId) {
            this.fromAppUserId = fromAppUserId;
        }

        public String getToGroupId() {
            return toGroupId;
        }

        public void setToGroupId(String toGroupId) {
            this.toGroupId = toGroupId;
        }

        public String getToGroupName() {
            return toGroupName;
        }

        public void setToGroupName(String toGroupName) {
            this.toGroupName = toGroupName;
        }
    }
}
