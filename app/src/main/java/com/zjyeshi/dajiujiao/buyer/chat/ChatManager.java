package com.zjyeshi.dajiujiao.buyer.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.jopool.crow.CWChat;
import com.jopool.crow.imkit.listeners.GetConversationTitleRightViewProvider;
import com.jopool.crow.imlib.enums.CWConversationType;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.my.UserInfoActivity;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.contact.AddressUser;
import com.zjyeshi.dajiujiao.buyer.task.GetUserInfoTask;
import com.zjyeshi.dajiujiao.buyer.task.data.UserData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.widgets.callback.CheckUserCallback;

/**
 * 启动聊天界面封装
 * Created by wuhk on 2016/3/27.
 */
public class ChatManager {
    private static ChatManager instance;

    /**
     * 获取单例
     *
     * @return
     */
    public static ChatManager getInstance() {
        if (null == instance) {
            instance = new ChatManager();
        }
        return instance;
    }

    /**
     * 启动会话
     *
     * @param context
     * @param userId
     */
    public void startConversion(final Context context, final String userId) {
        if (!Validators.isEmpty(userId)) {
            final AddressUser addressUser = DaoFactory.getAddressUserDao().findUserById(LoginedUser.getLoginedUser().getId(), userId);
            if (null == addressUser) {
                GetUserInfoTask.getUserTodo(context, userId, new AsyncTaskSuccessCallback<UserData>() {
                    @Override
                    public void successCallback(Result<UserData> result) {
                        DaoFactory.getAddressUserDao().insert(userId, result);
                        AddressUser newAu = DaoFactory.getAddressUserDao().findUserById(LoginedUser.getLoginedUser().getId(), userId);
                        openConversation(context, newAu);
                    }
                });
            } else {
                openConversation(context, addressUser);
            }
        } else {
            ToastUtil.toast("该条信息有误，无联系人，建议删除");
        }
    }

    /**
     * 发消息
     *
     * @param context
     * @param userId
     * @param title
     * @param url
     * @param cwConversationType
     */
    public void sendMessage(final Context context, final String userId, final String title, final String url, final CWConversationType cwConversationType) {
        final AddressUser addressUser = DaoFactory.getAddressUserDao().findUserById(LoginedUser.getLoginedUser().getId(), userId);
        if (null == addressUser) {
            GetUserInfoTask.getUserTodo(context, userId, new AsyncTaskSuccessCallback<UserData>() {
                @Override
                public void successCallback(Result<UserData> result) {
                    DaoFactory.getAddressUserDao().insert(userId, result);
                    CWChat.getInstance().getSendMsgDelegate().sendUrlMessage(context,
                            userId, title, url, cwConversationType);
                    ToastUtil.toast("发送成功");
                }
            });
        } else {
            CWChat.getInstance().getSendMsgDelegate().sendUrlMessage(context,
                    userId, title, url, cwConversationType);
            ToastUtil.toast("发送成功");
        }
    }

    /**
     * 检查用户是否存在,转发时用到
     *
     * @param context
     * @param userId
     * @param checkUserCallback
     */
    public void checkUser(Context context, final String userId, final CheckUserCallback checkUserCallback) {
        final AddressUser addressUser = DaoFactory.getAddressUserDao().findUserById(LoginedUser.getLoginedUser().getId(), userId);
        if (null == addressUser) {
            GetUserInfoTask.getUserTodo(context, userId, new AsyncTaskSuccessCallback<UserData>() {
                @Override
                public void successCallback(Result<UserData> result) {
                    DaoFactory.getAddressUserDao().insert(userId, result);
                    checkUserCallback.checkOk();
                }
            });
        } else {
            checkUserCallback.checkOk();
        }
    }

    /**
     * 打开单聊聊天界面，右侧有点击进入人员详情
     *
     * @param context
     * @param addressUser
     */
    private void openConversation(final Context context, AddressUser addressUser) {
        CWChat.getInstance().getConversationDelegate().startConversation(context, CWConversationType.USER
                , addressUser.getUserId(), addressUser.getName(), new GetConversationTitleRightViewProvider() {
                    @Override
                    public View[] getRightViews() {
                        return new View[]{
                                LayoutInflater.from(context).inflate(R.layout.layout_call, null)
                        };
                    }

                    @Override
                    public OnRightViewClickListener[] getRightViewClickListener() {
                        return new OnRightViewClickListener[]{
                                new OnRightViewClickListener() {
                                    @Override
                                    public void onClick(View view, Params params) {
                                        UserInfoActivity.startActivity(context, params.getToId());
                                    }
                                }
                        };
                    }
                });
    }

}
