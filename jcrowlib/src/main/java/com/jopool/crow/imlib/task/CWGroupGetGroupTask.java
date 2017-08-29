package com.jopool.crow.imlib.task;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.jopool.crow.CWChat;
import com.jopool.crow.imlib.constants.UrlConstants;
import com.jopool.crow.imlib.entity.CWSelectUser;
import com.jopool.crow.imlib.model.CWGroupModel;
import com.jopool.crow.imlib.model.CWUserModel;
import com.jopool.crow.imlib.utils.CWHttpUtil;
import com.jopool.crow.imlib.utils.CWToastUtil;
import com.jopool.crow.imlib.utils.asynctask.NetAbstractTask;
import com.jopool.crow.imlib.utils.asynctask.callback.AsyncTaskFailCallback;
import com.jopool.crow.imlib.utils.asynctask.callback.AsyncTaskSuccessCallback;
import com.jopool.crow.imlib.utils.asynctask.helper.Result;

import java.util.HashMap;
import java.util.List;

/**
 * 获取去群组基本信息
 * Created by wuhk on 2016/11/4.
 */
public class CWGroupGetGroupTask extends NetAbstractTask<CWGroupGetGroupTask.GetGroupData> {
    public CWGroupGetGroupTask(Context context) {
        super(context);
        setShowProgressDialog(false);

    }

    @Override
    protected Result onHttpRequest(Object... params) {
        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("appGroupId", (String) params[0]);
        paramMap.put("appId", CWChat.getInstance().getConfig().getAppId());

        Result<GetGroupData> result = null;
        try {
            CWHttpUtil.CWResponse response = CWHttpUtil.post(CWChat.getInstance().getConfig().getApiPrefix() + UrlConstants.GET_GROUP, paramMap);
            if (200 == response.getStatusCode()) {
                GetGroupData getGroupData = JSON.parseObject(response.getResultStr(), GetGroupData.class);

                if (getGroupData.isOk()) {
//                    CWUserModel.getInstance().cacheUserByGroup(getGroupData.getResult());//缓存用户
//                    CWGroupModel.getInstance().cacheGroupByGroup(getGroupData.getResult());//缓存群组
                    result = new Result<GetGroupData>(true, getGroupData.getMessage(), getGroupData.getResult());
                } else {
                    result = new Result<GetGroupData>(false, getGroupData.getMessage());
                }
            } else {
                // 响应非200
                result = new Result<GetGroupData>(false, response.getReasonPhrase());
            }
        } catch (Exception e) {
            result = new Result<GetGroupData>(false, e.getMessage());
        }

        return result;
    }

    /**
     * 获取群组信息
     *
     * @param context
     * @param groupId
     * @param successCallback
     */
    public static void getGroupInfo(Context context, String groupId, AsyncTaskSuccessCallback<GetGroupData> successCallback) {
        CWGroupGetGroupTask cwGroupGetGroupTask = new CWGroupGetGroupTask(context);
        cwGroupGetGroupTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<GetGroupData>() {
            @Override
            public void failCallback(Result<GetGroupData> result) {
                CWToastUtil.displayTextShort(result.getMessage());
            }
        });

        cwGroupGetGroupTask.setAsyncTaskSuccessCallback(successCallback);

        cwGroupGetGroupTask.execute(groupId);
    }

    public static class GetGroupData extends BaseRes<GetGroupData> {
        private String id;
        private String name;
        private String md5;
        private long modifyTime;
        private long creationTime;
        private List<GetGroupUserData> groupUserList;
        private CWSelectUser creatorUser;

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

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        public long getModifyTime() {
            return modifyTime;
        }

        public void setModifyTime(long modifyTime) {
            this.modifyTime = modifyTime;
        }

        public long getCreationTime() {
            return creationTime;
        }

        public void setCreationTime(long creationTime) {
            this.creationTime = creationTime;
        }

        public List<GetGroupUserData> getGroupUserList() {
            return groupUserList;
        }

        public void setGroupUserList(List<GetGroupUserData> groupUserList) {
            this.groupUserList = groupUserList;
        }

        public CWSelectUser getCreatorUser() {
            return creatorUser;
        }

        public void setCreatorUser(CWSelectUser creatorUser) {
            this.creatorUser = creatorUser;
        }
    }

    public static class GetGroupUserData {
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
}


