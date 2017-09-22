package com.zjyeshi.dajiujiao.buyer.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.jopool.crow.CWChat;
import com.zjyeshi.dajiujiao.buyer.activity.rong.broadcast.CWTotalUnreadNumReceiver;
import com.xuan.bigapple.lib.ioc.app.BPActivity;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.utils.BDActivityManager;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.receiver.LoadNewRemindReceiver;
import com.zjyeshi.dajiujiao.buyer.receiver.ScreenBroadcastReceiver;
import com.zjyeshi.dajiujiao.buyer.utils.AppUnreadUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ContextUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.LogUtil;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.utils.SystemBarTintManager;

import io.rong.imkit.RongIM;

/**
 * 所有Activity的基类
 * Created by xuan on 15/9/16.
 */
public class BaseActivity extends BPActivity {
    public static boolean fromBackground = false;

    private ScreenBroadcastReceiver screenBroadcastReceiver;
    private CWTotalUnreadNumReceiver totalUnreadNumReceiver;
    private LoadNewRemindReceiver loadNewRemindReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //添加activity到数组
        BDActivityManager.addActivity(this);
        screenBroadcastReceiver = new ScreenBroadcastReceiver();
        screenBroadcastReceiver.register();

        loadNewRemindReceiver = new LoadNewRemindReceiver();
        loadNewRemindReceiver.register();

        totalUnreadNumReceiver = new CWTotalUnreadNumReceiver() {
            @Override
            public void totalUnreadNum(int totalUnreadNum) {
                AppUnreadUtil.sendBadgeNumber(BaseActivity.this);
            }
        };
        totalUnreadNumReceiver.register(BaseActivity.this);

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
              //  LogUtil.e("BaseActivity的onStop:--------断开连接" + String.valueOf(CWChat.getInstance().getImClient().isConnected()));
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
                RongIM.getInstance().disconnect();
                LogUtil.e("BaseActivity的onResume:--------断开连接");
            }else{
                if (LoginedUser.getLoginedUser().isLogined()){
                    if (!CWChat.getInstance().getImClient().isConnected()){
                        ExtraUtil.connectChat(BaseActivity.this);
                        LogUtil.e("BaseActivity的onResume:--------重连成功");
                    }else{
                        LogUtil.e("BaseActivity的onResume:--------连接着的");
                    }
                }else{
                    LogUtil.e("BaseActivity的onResume:--------未登录的，不重新连接");
                }
            }
        }

        fromBackground = false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

//    /**
//     * 设置图片，图片是空的话就设置默认图片
//     *
//     * @param imageView
//     * @param url
//     * @param resid
//     */
//    public void initImageViewDefault(ImageView imageView, String url, int resid) {
//        imageView.setVisibility(View.VISIBLE);
//        if (!Validators.isEmpty(url)) {
//            BitmapDisplayConfig config = new BitmapDisplayConfig();
//            Bitmap temp = BitmapFactory.decodeResource(App.instance.getResources(), resid);
//            config.setLoadFailedBitmap(temp);
//            config.setLoadingBitmap(temp);
//            config.setBitmapMaxHeight(200);
//            config.setBitmapMaxWidth(200);
//            BPBitmapLoader.getInstance().display(imageView, url, config);
//
//
//        } else {
//            imageView.setImageResource(resid);
//        }
//    }
//
//    /**
//     * 设置图片，图片是空的话就设置默认图片
//     *
//     * @param imageView
//     * @param url
//     * @param resid
//     */
//    public void initBigImageView(ImageView imageView, String url, int resid) {
//        imageView.setVisibility(View.VISIBLE);
//        if (!Validators.isEmpty(url)) {
//            BitmapDisplayConfig config = new BitmapDisplayConfig();
//            Bitmap temp = BitmapFactory.decodeResource(App.instance.getResources(), resid);
//            config.setLoadFailedBitmap(temp);
//            config.setLoadingBitmap(temp);
//            BPBitmapLoader.getInstance().display(imageView, url, config);
//        } else {
//            imageView.setImageResource(resid);
//        }
//    }
//
//    /**
//     * 设置图片，图片是空的话就隐藏图片控件
//     *
//     * @param imageView
//     * @param url
//     */
//    public void initImageView(ImageView imageView, String url) {
//        if (!Validators.isEmpty(url)) {
//            imageView.setVisibility(View.VISIBLE);
//            if (Validators.isNumber(url)) {
//                // 资源id
//                imageView.setImageResource(Integer.valueOf(url));
//            } else if (url.startsWith("http")) {
//                // 加载网络
//                BPBitmapLoader.getInstance().display(imageView, url);
//            } else {
//                // 加载本地
//                BPBitmapLoader.getInstance().display(imageView, url);
//            }
//        } else {
//            imageView.setVisibility(View.GONE);
//        }
////        if (Validators.isEmpty(url)){
////            Glide
////                    .with(BaseActivity.this)
////                    .load(url)
////                    .centerCrop()
////                    .placeholder(resid)
////                    .crossFade()
////                    .into(imageView);
////        }else{
////            imageView.setImageResource(resid);
////        }
//    }

    /**
     * 设置文本
     *
     * @param textView
     * @param text
     */
    public void initTextView(TextView textView, String text) {
        if (!Validators.isEmpty(text)) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
        } else {
            textView.setVisibility(View.GONE);
        }
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
