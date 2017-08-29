package extended;

import android.content.Context;

import com.igexin.sdk.PushManager;
import com.jopool.crow.CWChat;
import com.jopool.crow.imlib.entity.CWUser;
import com.jopool.crow.imlib.gettui.CWGetTuiPushService;
import com.jopool.crow.imlib.push.CWPushManager;
import com.jopool.crow.imlib.soket.CWImConfig;
import com.jopool.crow.imlib.soket.listeners.OnConnectListener;
import com.jopool.crow.imlib.soket.listeners.OnMessageReceiveListener;
import com.jopool.crow.imlib.task.CWGetMsgTokenTask;
import com.jopool.crow.imlib.utils.CWLogUtil;
import com.jopool.crow.imlib.utils.CWValidator;
import com.jopool.crow.imlib.utils.asynctask.callback.AsyncTaskSuccessCallback;
import com.jopool.crow.imlib.utils.asynctask.helper.Result;

/**
 * 微信服务器使用类
 *
 * @author xuan
 */
public class Chat {
    private static Chat instance;

    public static Chat getInstance() {
        if (null == instance) {
            instance = new Chat();
        }

        return instance;
    }

    private OnConnectListener connectListener;
    private OnMessageReceiveListener onMessageReceiveListener;

    public OnMessageReceiveListener getOnMessageReceiveListener() {
        return onMessageReceiveListener;
    }

    public void setOnMessageReceiveListener(OnMessageReceiveListener onMessageReceiveListener) {
        this.onMessageReceiveListener = onMessageReceiveListener;
    }

    public OnConnectListener getConnectListener() {
        return connectListener;
    }

    public void setConnectListener(OnConnectListener connectListener) {
        this.connectListener = connectListener;
    }

    /**
     * 是否处于微信登陆状态
     *
     * @return
     */
    public boolean isLogined() {
        return !CWValidator.isEmpty(CWUser.getConnectUserId())
                && CWChat.getInstance().getImClient().isConnected();
    }

    /**
     * 初始化微信
     *
     * @param context
     * @param channelId
     */
    public void startChat(final Context context, String channelId) {
        // 设置消息接受监听
        CWChat.getInstance().getImClient()
                .setMessageReceiveListener(onMessageReceiveListener);

        CWGetMsgTokenTask.PostParam pp = CWGetMsgTokenTask.PostParam
                .obtain(CWChat.getInstance().getConfig().getAppId(), CWChat.getInstance().getConfig().getAppSecret(),
                        CWUser.getUser().getUserId(), CWUser.getUser().getName(), CWUser.getUser().getUrl(),
                        channelId);
        CWGetMsgTokenTask.getToken(context, pp,
                new AsyncTaskSuccessCallback<String>() {
                    @Override
                    public void successCallback(Result<String> result) {
                        CWUser.getUser().setToken(result.getValue());
                        CWLogUtil.d("拿到token[" + CWUser.getUser().getToken() + "]");
                        connectSocket(context, result.getValue(), CWUser.getUser());
//                        //启动百度推送去绑定channelId
//                        CWPushManager.startWork(context, CWChat.getInstance().getConfig().getPushApiKey());
                        //初始化个推并注册服务
                        PushManager.getInstance().initialize(context, CWGetTuiPushService.class);
                        PushManager.getInstance().registerPushIntentService(context , CWChat.getInstance().getConfig().getGTServiceClass());
                    }
                });
    }

    /**
     * 连接微信服务器
     *
     * @param context
     * @param msgToken
     * @param user
     */
    private void connectSocket(final Context context, String msgToken,
                               final CWUser user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CWChat.getInstance()
                        .getImClient()
                        .connect(
                                CWImConfig.obtainJpImConfig(
                                        CWChat.getInstance().getConfig().getSocketHost(),
                                        CWChat.getInstance().getConfig().getSocketPort(),
                                        CWChat.getInstance().getConfig().getAppId(), connectListener,
                                        user));
            }
        }).start();
    }

}
