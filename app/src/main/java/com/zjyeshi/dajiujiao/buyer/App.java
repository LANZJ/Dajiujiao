package com.zjyeshi.dajiujiao.buyer;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.baidu.mapapi.SDKInitializer;
import com.github.moduth.blockcanary.BlockCanary;
import com.jopool.crow.CWChat;
import com.jopool.crow.imapi.CWApi;
import com.jopool.crow.imkit.listeners.CWConversationPriorityProvider;
import com.jopool.crow.imkit.listeners.GetGroupUserSelectListProvider;
import com.jopool.crow.imkit.listeners.GetUserInfoProvider;
import com.jopool.crow.imkit.listeners.OnGroupUserClickListener;
import com.jopool.crow.imkit.listeners.OnPortraitClickListener;
import com.jopool.crow.imkit.view.CWTitleLayout;
import com.jopool.crow.imlib.entity.CWSelectUser;
import com.jopool.crow.imlib.entity.CWUser;
import com.jopool.crow.imlib.enums.CWConversationType;
import com.xuan.bigapple.lib.Bigapple;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.bitmap.BPBitmapLoader;
import com.xuan.bigapple.lib.bitmap.core.impl.net.NetBitmapLoader;
import com.xuan.bigapple.lib.db.DBHelper;
import com.xuan.bigapple.lib.http.BPResponse;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigappleui.lib.BigappleUI;
import com.xuan.bigappleui.lib.utils.ui.M;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.my.UserInfoActivity;
import com.zjyeshi.dajiujiao.buyer.activity.rong.SealAppContext;
import com.zjyeshi.dajiujiao.buyer.activity.rong.SealUserInfoManager;
import com.zjyeshi.dajiujiao.buyer.activity.rong.broadcast.CWTotalUnreadNumReceiver;
import com.zjyeshi.dajiujiao.buyer.activity.rong.message.TestMessage;
import com.zjyeshi.dajiujiao.buyer.activity.rong.message.provider.ContactNotificationMessageProvider;
import com.zjyeshi.dajiujiao.buyer.activity.rong.message.provider.TestMessageProvider;
import com.zjyeshi.dajiujiao.buyer.activity.rong.utils.SharedPreferencesContext;
import com.zjyeshi.dajiujiao.buyer.chat.MyChatConfig;
import com.zjyeshi.dajiujiao.buyer.chat.MyChatWebViewActivity;
import com.zjyeshi.dajiujiao.buyer.circle.task.CircleMemberInfoTask;
import com.zjyeshi.dajiujiao.buyer.common.Constants;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.contact.AddressUser;
import com.zjyeshi.dajiujiao.buyer.task.data.UserData;
import com.zjyeshi.dajiujiao.buyer.task.my.GetAreaDataTask;
import com.zjyeshi.dajiujiao.buyer.task.work.GetAllStaffListTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.StaffListData;
import com.zjyeshi.dajiujiao.buyer.utils.AppUnreadUtil;
import com.zjyeshi.dajiujiao.buyer.utils.BitmapDisplayConfigFactory;
import com.zjyeshi.dajiujiao.buyer.utils.CrashHandler;
import com.zjyeshi.dajiujiao.buyer.utils.FileUtil;
import com.zjyeshi.dajiujiao.buyer.utils.HttpUtil;
import com.zjyeshi.dajiujiao.buyer.utils.LogUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import io.rong.imageloader.core.DisplayImageOptions;
import io.rong.imageloader.core.display.FadeInBitmapDisplayer;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imkit.widget.provider.RealTimeLocationMessageProvider;
import io.rong.imlib.ipc.RongExceptionHandler;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Discussion;
import io.rong.push.RongPushClient;

/**
 * 程序入口
 * <p/>
 * Created by xuan on 15/9/16.
 */
public class App extends MultiDexApplication implements IUnReadMessageObserver {
    public static Bitmap defaultBitmap;
    public static Bitmap defaultBitmapGray;
    public static App instance;
    private static final String APP_KEY = "x4vkb1qpx78yk";
    private static DisplayImageOptions options;
    private SharedPreferences.Editor editor;
    private SharedPreferences sp;
    final List<String>muer=new ArrayList<String>();
    private CWTotalUnreadNumReceiver totalUnreadNumReceiver;

    @Override
    public void onCreate() {
        super.onCreate();

        defaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_img);
        defaultBitmapGray = BitmapFactory.decodeResource(getResources(), R.drawable.default_image_gray);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        editor = sp.edit();
        String loginToken="DmZtplypGJCi/a/Y3JPSH8Ldl8teHonyztxu2Umd1bBHVqCJREqVYHUXlozuac1c7X5kHnBCMn4=";
        editor.putString("loginToken", loginToken);
        editor.apply();
        instance = this;
        initData();

        //个推中需要用到,否则会出问题
        try {
            // 这个的作用就是将 AsyncTask初始化到主线程
            Class.forName("com.jopool.crow.imlib.utils.asynctask.helper.CompatibleAsyncTask");
        } catch (ClassNotFoundException e) {

        }
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {


            RongPushClient.registerHWPush(this);
            RongPushClient.registerMiPush(this, "2882303761517597959", "5801759735959");

            //设置R文件名,资源路径,多个包名使用同一个项目准备
            M.RFileName = "com.zjyeshi.dajiujiao";
            RongPushClient.registerHWPush(this);
            //框架引用初始化
            initChat();

            RongIM.init(this, APP_KEY);
            Bigapple.init(this);
            SealAppContext.init(this);
            SharedPreferencesContext.init(this);
            BigappleUI.init(this);
            DBHelper.init(Constants.DB_VERSION, Constants.DB_NAME, this);
            initBitmapLoader();
            initTitle();
            initBaiDuMap();
            loadArea();
            Thread.setDefaultUncaughtExceptionHandler(new RongExceptionHandler(this));
            //在这里为应用设置异常处理程序，然后我们的程序才能捕获未处理的异常
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(this);

            //设置点击链接跳转的页面
            MyChatWebViewActivity.configOpenWebviewClass(MyChatWebViewActivity.class);

            try {
                RongIM.registerMessageTemplate(new ContactNotificationMessageProvider());
                RongIM.registerMessageTemplate(new RealTimeLocationMessageProvider());
                RongIM.registerMessageType(TestMessage.class);
                RongIM.registerMessageTemplate(new TestMessageProvider());


            } catch (Exception e) {
                e.printStackTrace();
            }
            openSealDBIfHasCachedToken();

            options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.de_default_portrait)
                    .showImageOnFail(R.drawable.de_default_portrait)
                    .showImageOnLoading(R.drawable.de_default_portrait)
                    .displayer(new FadeInBitmapDisplayer(300))
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();

        }
        // 在主进程初始化调用哈
        BlockCanary.install(this, new AppBlockCanaryContext()).start();

    }


    public static void ods(Discussion discussion) {
        RongIM.getInstance().refreshDiscussionCache(discussion);
    }

    /**
     * 聊天相关
     */
    private void initChat() {
        //聊天配置
        MyChatConfig myChatConfig = new MyChatConfig();
        CWChat.init(this, myChatConfig);
//        //接受消息之前
//        CWChat.getInstance().getImClient().setBeforeConsumeConversationMesssgeListener(new OnBeforeConsumeConversationMesssgeListener() {
//            @Override
//            public boolean onBeforeConsumeConversationMesssge(CWConversationMessage cwMessage) {
//                final String memberId = cwMessage.getSenderUserId();
//                AddressUser addressUser = DaoFactory.getAddressUserDao().findUserById(LoginedUser.getLoginedUser().getId(), memberId);
//                if (null == addressUser) {
//                    addAddressUser(memberId);
//                }
//                return true;
//            }
//        });
        //获取用户信息外部提供
        CWChat.getInstance().setGetUserInfoProvider(new GetUserInfoProvider() {
            @Override
            public CWUser getUserById(String userId) {
                AddressUser au = DaoFactory.getAddressUserDao().findUserById(LoginedUser.getLoginedUser().getId(), userId);
                if (null != au) {
                    CWUser user = new CWUser();
                    user.setName(au.getName());
                    user.setUrl(au.getAvatar());
                    user.setUserId(userId);
                    return user;
                } else {
                    addAddressUser(userId);
                    AddressUser nowUser = DaoFactory.getAddressUserDao().findUserById(LoginedUser.getLoginedUser().getId(), userId);
                    CWUser user = new CWUser();
                    user.setUserId(userId);
                    if (null != nowUser){
                        user.setName(nowUser.getName());
                        user.setUrl(nowUser.getAvatar());
                    }else{
                        user.setName(" ");
                    }
                    return user;

                }

            }
        });

        //头像点击事件
        CWChat.getInstance().setPortraitClickListener(new OnPortraitClickListener() {
            @Override
            public void onPortaitClick(final Context context, View view, final String userId) {
                if (!Validators.isEmpty(userId)) {
                    CircleMemberInfoTask task = new CircleMemberInfoTask(instance);
                    task.setAsyncTaskFailCallback(new AsyncTaskFailCallback<CircleMemberInfoTask.CircleMember>() {
                        @Override
                        public void failCallback(Result<CircleMemberInfoTask.CircleMember> result) {

                        }
                    });
                    task.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<CircleMemberInfoTask.CircleMember>() {
                        @Override
                        public void successCallback(Result<CircleMemberInfoTask.CircleMember> result) {
                            CircleMemberInfoTask.CircleMember member = result.getValue();
                            UserInfoActivity.startActivity(context, userId);
                        }
                    });

                    task.execute(userId);
                }
            }
        });

        //群组信息人员头像点击初始化
        CWApi.getInstance().getApiGroup().setOnGroupUserClickListener(new OnGroupUserClickListener() {
            @Override
            public void onGroupUserClick(Context context, String userId) {
                UserInfoActivity.startActivity(context, userId);
            }
        });

        //发起群聊是的人员选择列表
        CWApi.getInstance().getApiGroup().setGetGroupUserSelectListProvider(new GetGroupUserSelectListProvider() {
            @Override
            public void getSelectUserList(final GetListCallback getCallback) {
                GetAllStaffListTask.getAllStaffList(instance, new AsyncTaskSuccessCallback<StaffListData>() {
                    @Override
                    public void successCallback(Result<StaffListData> result) {
                        List<StaffListData.Staff> staffList = result.getValue().getList();
                        final List<CWSelectUser> userList = new ArrayList<CWSelectUser>();

                        for (StaffListData.Staff staff : staffList) {
                            CWSelectUser user = new CWSelectUser();
                            user.setUserId(staff.getId());
                            user.setUserName(staff.getName());
                            user.setUserLogo(staff.getPic());
                            userList.add(user);
                            muer.add(staff.getId());
                        }


                        getCallback.callBack(userList);
                    }
                });
            }
        });

        //会话优先级设置
        CWChat.getInstance().getConversationDelegate().setConversationPriorityProvider(new CWConversationPriorityProvider() {
            @Override
            public int getConversationPriority(CWConversationType conversationType, String userId) {
                if (conversationType.equals(CWConversationType.GROUP)){
                    return 6;
                }else{
                    AddressUser addressUser = DaoFactory.getAddressUserDao().findUserById(LoginedUser.getLoginedUser().getId(), userId);
                    if (null == addressUser){
                        addAddressUser(userId);
                        AddressUser au = DaoFactory.getAddressUserDao().findUserById(LoginedUser.getLoginedUser().getId() , userId);
                        if (null != au){
                            return au.getPriority();
                        }
                    }else{
                        return addressUser.getPriority();
                    }
                }
                return 0;
            }
        });
    }

    /**
     * 图片加载器初始化
     */
    private void initBitmapLoader() {
        BPBitmapLoader.init(this);
        BPBitmapLoader.getInstance().getDefaultBitmapDisplayConfig().setLoadFailedBitmap(App.defaultBitmap);
        BPBitmapLoader.getInstance().getDefaultBitmapDisplayConfig().setLoadingBitmap(App.defaultBitmap);
        NetBitmapLoader.getInstance().getDefaultBitmapGlobalConfig().setMemCacheSizePercent(0.8f);//80%的内存缓存
        NetBitmapLoader.getInstance().getDefaultBitmapGlobalConfig().setDiskCacheSize(1024 * 1024 * 500);//500M磁盘缓存
        NetBitmapLoader.getInstance().getDefaultBitmapGlobalConfig().setThreadPoolSize(30);//30个线程喽,面子很大了
        NetBitmapLoader.getInstance().getDefaultBitmapGlobalConfig().setDiskCachePath(FileUtil.getCacheDir());

        //config
        BitmapDisplayConfigFactory.init();
    }

    /**
     * 框架标题颜色
     */
    private void initTitle() {
        DGTitleLayout.UIConfig dgUiConfig = new DGTitleLayout.UIConfig();
        dgUiConfig.DEFAULT_BG_COLOR = R.color.color_theme;
        DGTitleLayout.setUiConfig(dgUiConfig);

        CWTitleLayout.UIConfig cwUiConfig = new CWTitleLayout.UIConfig();
        cwUiConfig.DEFAULT_BG_COLOR = R.color.color_theme;
        CWTitleLayout.setUiConfig(cwUiConfig);
    }

    /**
     * 从服务器获得用户数据插入
     *
     * @param memberId
     */
    private void addAddressUser(final String memberId) {

        final CountDownLatch latch = new CountDownLatch(1);
        new Thread() {
            @Override
            public void run() {
                LogUtil.e("消息接受之前取了个人信息------------------------------");
                HashMap<String, String> paramMap = new HashMap<String, String>();
                paramMap.put("memberId", memberId);
                BPResponse response = HttpUtil.postCommom(instance, UrlConstants.MEMBERDETAILS, paramMap);
                if (-1 == response.getStatusCode()) {
                    ToastUtil.toast(response.getReasonPhrase());
                } else if (HttpURLConnection.HTTP_OK == response.getStatusCode()) {
                    // 正常200
                    UserData retData = JSON.parseObject(response.getResultStr(), UserData.class);
                    if (retData.codeOk()) {
                        Result<UserData> result = new Result<UserData>();
                        result.setValue(retData.getResult());
                        DaoFactory.getAddressUserDao().insert(memberId ,result);
                    } else {
//                        ToastUtil.toast("获取用户信息出错");
                    }
                } else {
                    // 非200的状态码
                    ToastUtil.toast("返回状态码错误" + response.toString());
                }
                //执行完成之后减去计数器
                latch.countDown();
            }
        }.start();

        try {
            // 等待，直到获取用户信息的线程完成再执行
            latch.await();
//            Thread.sleep(2000);
        } catch (InterruptedException e1) {
            // Ignore
        }
    }

    /**
     * 百度地图初始化
     */
    private void initBaiDuMap() {
        SDKInitializer.initialize(this);
    }

    /**
     * 加载省市区地区信息并缓存
     */
    private void loadArea() {
        if (null == DaoFactory.getAreaDao().findByCode("0")) {
            GetAreaDataTask getAreaDataTask = new GetAreaDataTask(instance);
            getAreaDataTask.setShowProgressDialog(false);
            getAreaDataTask.execute();
        }
    }
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
    private void openSealDBIfHasCachedToken() {

        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        String cachedToken = sp.getString("loginToken", "");
        if (!TextUtils.isEmpty(cachedToken)) {
            String current = getCurProcessName(this);
            String mainProcessName = getPackageName();
            if (mainProcessName.equals(current)) {
                SealUserInfoManager.getInstance().openDB();
            }
        }
    }
    public static DisplayImageOptions getOptions() {
        return options;
    }

    protected static void initData() {

        final Conversation.ConversationType[] conversationTypes = {
                Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP, Conversation.ConversationType.SYSTEM,
                Conversation.ConversationType.PUBLIC_SERVICE, Conversation.ConversationType.APP_PUBLIC_SERVICE,
                Conversation.ConversationType.DISCUSSION
        };

        RongIM.getInstance().addUnReadMessageCountChangedObserver(instance, conversationTypes);

    }
    @Override
    public void onCountChanged(int comdi) {
        AppUnreadUtil.om(comdi);
       CWTotalUnreadNumReceiver.J(comdi);
       // ToastUtil.toast(comdi+"");
        totalUnreadNumReceiver = new CWTotalUnreadNumReceiver() {
            @Override
            public void totalUnreadNum(int totalUnreadNum) {
                AppUnreadUtil.sendBadgeNumber(App.this);
            }
        };
        totalUnreadNumReceiver.register(App.this);
        CWTotalUnreadNumReceiver.notifyReceiver(App.this);
    }

}
