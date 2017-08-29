package com.zjyeshi.dajiujiao.buyer.activity.frame;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.jopool.crow.CWChat;
import com.xuan.bigdog.lib.tabframe.mcall.DGFrameActivity;
import com.xuan.bigdog.lib.tabframe.mcall.SystemBarTintManager;
import com.xuan.bigdog.lib.utils.BDActivityManager;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.rong.broadcast.CWTotalUnreadNumReceiver;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.receiver.LoadNewRemindReceiver;
import com.zjyeshi.dajiujiao.buyer.receiver.ScreenBroadcastReceiver;
import com.zjyeshi.dajiujiao.buyer.utils.AppUnreadUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ContextUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.LogUtil;

/**
 * 主界面基类
 * Created by xuan on 15/9/16.
 */
public class BaseFrameActivity extends DGFrameActivity {
    public static boolean fromBackground = false;

    private ScreenBroadcastReceiver screenBroadcastReceiver;
    private CWTotalUnreadNumReceiver totalUnreadNumReceiver;
    private LoadNewRemindReceiver loadNewRemindReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BDActivityManager.addActivity(this);

        screenBroadcastReceiver = new ScreenBroadcastReceiver();
        screenBroadcastReceiver.register();

        loadNewRemindReceiver = new LoadNewRemindReceiver();
        loadNewRemindReceiver.register();

        totalUnreadNumReceiver = new CWTotalUnreadNumReceiver() {
            @Override
            public void totalUnreadNum(int totalUnreadNum) {
                AppUnreadUtil.sendBadgeNumber(BaseFrameActivity.this);
            }
        };
        totalUnreadNumReceiver.register(BaseFrameActivity.this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.color_sign_normal);//通知栏所需颜色
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BDActivityManager.removeActivity(this);
        screenBroadcastReceiver.unRegister();
        loadNewRemindReceiver.unregister();
        totalUnreadNumReceiver.unregister(this);
    }


    @Override
    protected void onStop() {
        super.onStop();

        if (ContextUtil.isBackground()){
            fromBackground = true;
            //退出微信
            if (CWChat.getInstance().getImClient().isConnected()){
                CWChat.getInstance().getImClient().disConnect();
                LogUtil.e("BaseFrameActivity的onStop:--------断开连接" + String.valueOf(CWChat.getInstance().getImClient().isConnected()));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (fromBackground){
            if (ContextUtil.isBackground()){
                //退出微信
                CWChat.getInstance().getImClient().disConnect();
                LogUtil.e("BaseFrameActivity的onResume:--------断开连接");
            }else{
                if (LoginedUser.getLoginedUser().isLogined()){
                    if (!CWChat.getInstance().getImClient().isConnected()){
                        ExtraUtil.connectChat(BaseFrameActivity.this);
                        LogUtil.e("BaseFrameActivity的onResume:--------重连成功");
                    }else{
                        LogUtil.e("BaseFrameActivity的onResume:--------连接着的");
                    }
                }else{
                    LogUtil.e("BaseFrameActivity的onResume:--------未登录的，不重新连接");
                }
            }
        }
        fromBackground = false;
    }
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
