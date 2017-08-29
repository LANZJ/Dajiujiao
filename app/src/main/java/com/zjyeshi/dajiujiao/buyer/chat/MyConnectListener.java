package com.zjyeshi.dajiujiao.buyer.chat;

import com.igexin.sdk.PushManager;
import com.jopool.crow.imlib.entity.CWUser;
import com.jopool.crow.imlib.gettui.CWGetTuiPushService;
import com.jopool.crow.imlib.soket.CWErrorCode;
import com.jopool.crow.imlib.soket.listeners.OnConnectListener;
import com.jopool.crow.imlib.task.CWGetServerConversationListTask;
import com.xuan.bigapple.lib.utils.sharepreference.BPPreferences;
import com.zjyeshi.dajiujiao.buyer.App;
import com.zjyeshi.dajiujiao.buyer.common.Constants;
import com.zjyeshi.dajiujiao.buyer.common.PreferenceConstants;
import com.zjyeshi.dajiujiao.buyer.utils.LogUtil;

/**
 * 连接服务器成功失败的监听
 *
 * @author xuan
 */
public class MyConnectListener implements OnConnectListener {

    @Override
    public void onError(CWErrorCode errorCode) {
        Constants.connecting = false;
        LogUtil.e("失败变false");
    }

    @Override
    public void onSuccess(CWUser user) {
        boolean requestFlag = BPPreferences.instance().getBoolean(PreferenceConstants.REQUEST_MSG , false);
        if (!requestFlag){
            LogUtil.e("登陆连接的聊天，我去请求服务器会话列表了");
            CWGetServerConversationListTask.getServerConversationList(App.instance.getApplicationContext());
            BPPreferences.instance().putBoolean(PreferenceConstants.REQUEST_MSG , true);
        }else{
            LogUtil.e("不是登录连接的聊天，不做请求");
        }
        Constants.connecting = false;
        LogUtil.e("成功变false");

    }

}
