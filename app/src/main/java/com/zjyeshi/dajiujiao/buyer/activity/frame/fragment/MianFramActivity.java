package com.zjyeshi.dajiujiao.buyer.activity.frame.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jopool.crow.CWChat;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.bservice.bversioncheck.BDVersionCheckModel;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.account.BasMianActivity;
import com.zjyeshi.dajiujiao.buyer.activity.rong.ap.ConversationListAdapterEx;
import com.zjyeshi.dajiujiao.buyer.activity.rong.broadcast.CWTotalUnreadNumReceiver;
import com.zjyeshi.dajiujiao.buyer.activity.rong.get.GettokenTask;
import com.zjyeshi.dajiujiao.buyer.activity.rong.sversp.HomeWatcherReceiver;
import com.zjyeshi.dajiujiao.buyer.activity.rong.ui.DragPointView;
import com.zjyeshi.dajiujiao.buyer.activity.rong.ui.LoadDialog;
import com.zjyeshi.dajiujiao.buyer.activity.rong.ui.NToast;
import com.zjyeshi.dajiujiao.buyer.circle.CircleFragment;
import com.zjyeshi.dajiujiao.buyer.common.Constants;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.dao.Gettoken;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.NewRemind;
import com.zjyeshi.dajiujiao.buyer.entity.enums.UserEnum;
import com.zjyeshi.dajiujiao.buyer.receiver.ChangeRemindShowReceiver;
import com.zjyeshi.dajiujiao.buyer.receiver.ContactsChangeReceiver;
import com.zjyeshi.dajiujiao.buyer.receiver.ToLoginReceiver;
import com.zjyeshi.dajiujiao.buyer.receiver.ToLogoutReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.my.FriendListData;
import com.zjyeshi.dajiujiao.buyer.task.data.pay.Wallet;
import com.zjyeshi.dajiujiao.buyer.task.data.store.homepage.GetNearbyShopList;
import com.zjyeshi.dajiujiao.buyer.task.my.FriendListTask;
import com.zjyeshi.dajiujiao.buyer.task.my.MyAccountTask;
import com.zjyeshi.dajiujiao.buyer.task.seller.GetProductBuyTask;
import com.zjyeshi.dajiujiao.buyer.task.work.NewRemindTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.NewRemindData;
import com.zjyeshi.dajiujiao.buyer.utils.AppUnreadUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import io.rong.common.RLog;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.message.ContactNotificationMessage;

/**
 * Created by lan on 2017/7/6.
 */
@SuppressWarnings("deprecation")
public class MianFramActivity extends BasMianActivity implements ViewPager.OnPageChangeListener,
        View.OnClickListener,
        DragPointView.OnDragListencer,
        IUnReadMessageObserver
        {

            private CWTotalUnreadNumReceiver totalUnreadNumReceiver;
    private ToLoginReceiver toLoginReceiver;//登录广播,未登录的情况下点击需要登录的地方会触发
    private ToLogoutReceiver toLogoutReceiver;//多账号登录退出登录广播,非业务员登录的时候注册
    private ChangeRemindShowReceiver changeRemindShowReceiver;//新消息变动改变显示广播
    private ContactsChangeReceiver contactsChangeReceiver;//酒友通讯录改变广播
    public static ViewPager mViewPager;
    private List<Fragment> mFragment = new ArrayList<>();
    private ImageView moreImage, mImageChats, mImageContact, mImageFind, mImageMe, mMineRed;
    private TextView mTextChats, mTextContact, mTextFind, mTextMe;
    private DragPointView mUnreadNumView,my_view;
    private ImageView mSearchImageView;
    private LinearLayout top;
            private DGTitleLayout titleLayout;
            private Handler handler=new Handler();
            private   int  allNum;
            /**
     * 会话列表的fragment
     */
            Handler mHandler = new Handler() {
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    loadRemind();
                }
            };
    private ConversationListFragment mConversationListFragment = null;
    private boolean isDebug=false;
    private Context mContext;
    private Conversation.ConversationType[] mConversationsTypes = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mains);
        mContext = this;
        isDebug = getSharedPreferences("config", MODE_PRIVATE).getBoolean("isDebug", false);

        initViews();
        initMainViewPager();
        changeTextViewColor();
        changeSelectedTabState(0);
        getToke();
        registerHomeKeyReceiver(this);

        //获取通讯录
        loadFriend();
        //获取新消息
        loadRemind();
        //获取上级店铺
        if (LoginedUser.getLoginedUser().isLogined()) {
            getTopShop();
        }
        if (LoginedUser.getLoginedUser().isLogined()) {
            loadAccount();
        }
        //广播注册
        brodcastRegister();
        BDVersionCheckModel.getInstance().doCheckDefault(MianFramActivity.this, UrlConstants.WEB_SITE + UrlConstants.CHECKVERSION, Constants.APK_FILENAME);

        new Thread(mRunnable).start();

    }
    private void initViews() {
        top= (LinearLayout) findViewById(R.id.tops);
        RelativeLayout chatRLayout = (RelativeLayout) findViewById(R.id.seal_chat);
        RelativeLayout contactRLayout = (RelativeLayout) findViewById(R.id.seal_contact_list);
        RelativeLayout foundRLayout = (RelativeLayout) findViewById(R.id.seal_find);
        RelativeLayout mineRLayout = (RelativeLayout) findViewById(R.id.seal_me);
        titleLayout= (DGTitleLayout) findViewById(R.id.titleLayout);
        mImageChats = (ImageView) findViewById(R.id.tab_img_chats);
        mImageContact = (ImageView) findViewById(R.id.tab_img_contact);
        mImageFind = (ImageView) findViewById(R.id.tab_img_find);
        mImageMe = (ImageView) findViewById(R.id.tab_img_me);
        mTextChats = (TextView) findViewById(R.id.tab_text_chats);
        mTextContact = (TextView) findViewById(R.id.tab_text_contact);
        mTextFind = (TextView) findViewById(R.id.tab_text_find);
        mTextMe = (TextView) findViewById(R.id.tab_text_me);
       chatRLayout.setOnClickListener(this);
        contactRLayout.setOnClickListener(this);
        foundRLayout.setOnClickListener(this);
        mineRLayout.setOnClickListener(this);
    }

            private void initMainViewPager() {
        Fragment conversationList = initConversationList();
        mViewPager = (ViewPager) findViewById(R.id.main_viewpager);

        mUnreadNumView = (DragPointView) findViewById(R.id.seal_num);
       mUnreadNumView.setOnClickListener(this);
       mUnreadNumView.setDragListencer(this);
        my_view= (DragPointView) findViewById(R.id.my_view);
        my_view.setOnClickListener(this);
        my_view.setDragListencer(this);

        mFragment.add(new Fragment1Seller());
        mFragment.add(conversationList);
        mFragment.add(new CircleFragment());
        mFragment.add(new Fragment4());

        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }
        };
        titleLayout.configTitle("聊天");
        titleLayout.configRightText("发起聊天", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CWChat.getInstance().getGroupDelegate().startSelectWhenCreate(mContext);
            }
        });
        mViewPager.setAdapter(fragmentPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setOnPageChangeListener(this);
        initData();
    }


    private Fragment initConversationList() {
        if (mConversationListFragment == null) {
            ConversationListFragment listFragment = new ConversationListFragment();
            listFragment.setAdapter(new ConversationListAdapterEx(RongContext.getInstance()));
            Uri uri;
            if (isDebug) {
                uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                        .appendPath("conversationlist")
                        .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "true") //设置私聊会话是否聚合显示
                        .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//群组
                        .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "true")//公共服务号
                        .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "true")//订阅号
                        .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                       // .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "true")
                        .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
                        .build();
                mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
                        Conversation.ConversationType.GROUP,
                        Conversation.ConversationType.PUBLIC_SERVICE,
                        Conversation.ConversationType.APP_PUBLIC_SERVICE,
                        Conversation.ConversationType.SYSTEM,
                        Conversation.ConversationType.DISCUSSION
                };

            } else {
                uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                        .appendPath("conversationlist")
                        .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                        .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                        .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                        .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                        .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                        .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
                        .build();
                mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
                        Conversation.ConversationType.GROUP,
                        Conversation.ConversationType.PUBLIC_SERVICE,
                        Conversation.ConversationType.APP_PUBLIC_SERVICE,
                        Conversation.ConversationType.SYSTEM,
                        Conversation.ConversationType.DISCUSSION
                };
            }
            listFragment.setUri(uri);
            mConversationListFragment = listFragment;
            return listFragment;
        } else {
            return mConversationListFragment;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        changeTextViewColor();
        changeSelectedTabState(position);
    }

    private void changeTextViewColor() {
        mImageChats.setBackgroundDrawable(getResources().getDrawable(R.drawable.tabbtn_top_normal1));
        mImageContact.setBackgroundDrawable(getResources().getDrawable(R.drawable.tabbtn_top_normal2));
        mImageFind.setBackgroundDrawable(getResources().getDrawable(R.drawable.tabbtn_top_normal3));
        mImageMe.setBackgroundDrawable(getResources().getDrawable(R.drawable.tabbtn_top_normal4));
        mTextChats.setTextColor(Color.parseColor("#E4CCB0"));
        mTextContact.setTextColor(Color.parseColor("#E4CCB0"));
        mTextFind.setTextColor(Color.parseColor("#E4CCB0"));
        mTextMe.setTextColor(Color.parseColor("#E4CCB0"));
    }

    private void changeSelectedTabState(int position) {
        switch (position) {
            case 0:
                top.setVisibility(View.GONE);
                mTextChats.setTextColor(Color.parseColor("#E4CCB0"));
                mImageChats.setBackgroundDrawable(getResources().getDrawable(R.drawable.tabbtn_top_sel1));

                break;
            case 1:
                top.setVisibility(View.VISIBLE);
                mTextContact.setTextColor(Color.parseColor("#E4CCB0"));
                mImageContact.setBackgroundDrawable(getResources().getDrawable(R.drawable.tabbtn_top_sel2));
                break;
            case 2:
                top.setVisibility(View.GONE);
                mTextFind.setTextColor(Color.parseColor("#E4CCB0"));
                mImageFind.setBackgroundDrawable(getResources().getDrawable(R.drawable.tabbtn_top_sel3));
                break;
            case 3:
                top.setVisibility(View.GONE);
                mTextMe.setTextColor(Color.parseColor("#E4CCB0"));
                mImageMe.setBackgroundDrawable(getResources().getDrawable(R.drawable.tabbtn_top_sel4));
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    long firstClick = 0;
    long secondClick = 0;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.seal_chat:
                if (mViewPager.getCurrentItem() == 0) {
                    if (firstClick == 0) {
                        firstClick = System.currentTimeMillis();
                    } else {
                        secondClick = System.currentTimeMillis();
                    }
                    RLog.i("MainActivity", "time = " + (secondClick - firstClick));
                    if (secondClick - firstClick > 0 && secondClick - firstClick <= 800) {
                        mConversationListFragment.focusUnreadItem();
                        firstClick = 0;
                        secondClick = 0;
                    } else if (firstClick != 0 && secondClick != 0) {
                        firstClick = 0;
                        secondClick = 0;
                    }
                }
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.seal_contact_list:
                mViewPager.setCurrentItem(1, false);
                top.setVisibility(View.VISIBLE);
                break;
            case R.id.seal_find:
                mViewPager.setCurrentItem(2, false);
                break;
            case R.id.seal_me:
                mViewPager.setCurrentItem(3, false);
       //         mMineRed.setVisibility(View.GONE);
                break;
//            case R.id.seal_more:
////                MorePopWindow morePopWindow = new MorePopWindow(MainActivity.this);
////                morePopWindow.showPopupWindow(moreImage);
//                break;
//            case R.id.ac_iv_search:
//         //       startActivity(new Intent(MainActivity.this, SealSearchActivity.class));
//                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra("systemconversation", false)) {
            mViewPager.setCurrentItem(0, false);
        }
    }

    protected void initData() {

        final Conversation.ConversationType[] conversationTypes = {
                Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP, Conversation.ConversationType.SYSTEM,
                Conversation.ConversationType.PUBLIC_SERVICE, Conversation.ConversationType.APP_PUBLIC_SERVICE,
                Conversation.ConversationType.DISCUSSION
        };

        RongIM.getInstance().addUnReadMessageCountChangedObserver(this, conversationTypes);
        getConversationPush();// 获取 push 的 id 和 target
        getPushMessage();
    }

    private void getConversationPush() {
        if (getIntent() != null && getIntent().hasExtra("PUSH_CONVERSATIONTYPE") && getIntent().hasExtra("PUSH_TARGETID")) {

            final String conversationType = getIntent().getStringExtra("PUSH_CONVERSATIONTYPE");
            final String targetId = getIntent().getStringExtra("PUSH_TARGETID");


            RongIM.getInstance().getConversation(Conversation.ConversationType.valueOf(conversationType), targetId, new RongIMClient.ResultCallback<Conversation>() {
                @Override
                public void onSuccess(Conversation conversation) {

                    if (conversation != null) {

                        if (conversation.getLatestMessage() instanceof ContactNotificationMessage) { //好友消息的push
                            //startActivity(new Intent(MainActivity.this, NewFriendListActivity.class));
                        } else {
                            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon().appendPath("conversation")
                                    .appendPath(conversationType).appendQueryParameter("targetId", targetId).build();
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onError(RongIMClient.ErrorCode e) {

                }
            });
        }
    }

    /**
     * 得到不落地 push 消息
     */
    private void getPushMessage() {
        Intent intent = getIntent();
        if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong")) {
            String path = intent.getData().getPath();
            if (path.contains("push_message")) {
                SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
                String cacheToken = sharedPreferences.getString("loginToken", "");
                if (TextUtils.isEmpty(cacheToken)) {
                  //  startActivity(new Intent(MainActivity.this, LoginActivity.class));
                } else {
                    if (!RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
                        LoadDialog.show(mContext);
                        RongIM.connect(cacheToken, new RongIMClient.ConnectCallback() {
                            @Override
                            public void onTokenIncorrect() {
                                LoadDialog.dismiss(mContext);
                            }

                            @Override
                            public void onSuccess(String s) {
                                LoadDialog.dismiss(mContext);
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode e) {
                                LoadDialog.dismiss(mContext);
                            }
                        });
                    }
                }
            }
        }
    }

    @Override
    public void onCountChanged(int count) {
        if (count == 0) {
            mUnreadNumView.setVisibility(View.GONE);
        } else if (count > 0 && count < 100) {
            mUnreadNumView.setVisibility(View.VISIBLE);
            mUnreadNumView.setText(String.valueOf(count));
        } else {
            mUnreadNumView.setVisibility(View.VISIBLE);
            mUnreadNumView.setText(R.string.no_read_message);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        RongIM.getInstance().removeUnReadMessageCountChangedObserver(this);
        if (mHomeKeyReceiver != null)
            this.unregisterReceiver(mHomeKeyReceiver);
        super.onDestroy();
        if (!LoginedUser.getLoginedUser().getUserEnum().equals(UserEnum.SALESMAN)) {
            toLogoutReceiver.unregister();
        }
        changeRemindShowReceiver.unregister();
        contactsChangeReceiver.unregister();
    }

    @Override
    public void onDragOut() {
      //  mUnreadNumView.setVisibility(View.GONE);
        NToast.shortToast(mContext, getString(R.string.clear_success));
        RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                if (conversations != null && conversations.size() > 0) {
                    for (Conversation c : conversations) {
                        RongIM.getInstance().clearMessagesUnreadStatus(c.getConversationType(), c.getTargetId(), null);
                    }
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode e) {

            }
        }, mConversationsTypes);

    }

    private HomeWatcherReceiver mHomeKeyReceiver = null;
    //如果遇见 Android 7.0 系统切换到后台回来无效的情况 把下面注册广播相关代码注释或者删除即可解决。下面广播重写 home 键是为了解决三星 note3 按 home 键花屏的一个问题
    private void registerHomeKeyReceiver(Context context) {
        if (mHomeKeyReceiver == null) {
            mHomeKeyReceiver = new HomeWatcherReceiver();
            final IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            try {
                context.registerReceiver(mHomeKeyReceiver, homeFilter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void connect(String token) {

        // if (getApplicationInfo().packageName.equals(App.getCurProcessName(getApplicationContext()))) {

        RongIM.connect(token, new RongIMClient.ConnectCallback() {

            /**
             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
             *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
             */
            @Override
            public void onTokenIncorrect() {
                ToastUtil.toast("token错误");
            }

            /**
             * 连接融云成功
             * @param userid 当前 token 对应的用户 id
             */
            @Override
            public void onSuccess(String userid) {
                Log.d("LoginActivity", "--onSuccess" + userid);

                //消息体携带用户信息
                RongIM.getInstance().setMessageAttachedUserInfo(true);
            }

            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                ToastUtil.toast("onError");
            }
        });
    }



    /**
     * 新消息
     */
    private void loadRemind() {
        NewRemindTask newRemindTask = new NewRemindTask(this);
        newRemindTask.setShowProgressDialog(false);
        newRemindTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NewRemindData>() {
            @Override
            public void failCallback(Result<NewRemindData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        newRemindTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NewRemindData>() {
            @Override
            public void successCallback(Result<NewRemindData> result) {
              changeMyNum();

            }
        });

        newRemindTask.execute();
    }
    /**
     * 获取上级店铺
     */
    private void getTopShop() {
        GetProductBuyTask getProductBuyTask = new GetProductBuyTask(MianFramActivity.this);
        getProductBuyTask.setShowProgressDialog(false);
        getProductBuyTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<GetNearbyShopList>() {
            @Override
            public void failCallback(Result<GetNearbyShopList> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        getProductBuyTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<GetNearbyShopList>() {
            @Override
            public void successCallback(Result<GetNearbyShopList> result) {

            }
        });
    }



    /**
     * 获取好友列表
     */
    private void loadFriend() {
        FriendListTask friendListTask = new FriendListTask(MianFramActivity.this);
        friendListTask.setShowProgressDialog(false);

        friendListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<FriendListData>() {
            @Override
            public void failCallback(Result<FriendListData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        friendListTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<FriendListData>() {
            @Override
            public void successCallback(Result<FriendListData> result) {
            }
        });

        friendListTask.execute();
    }

    /**
     * 获取帐户市场支持费用
     */
    private void loadAccount() {
        MyAccountTask myAccountTask = new MyAccountTask(MianFramActivity.this);
        myAccountTask.setShowProgressDialog(false);
        myAccountTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<Wallet>() {
            @Override
            public void failCallback(Result<Wallet> result) {

            }
        });
        myAccountTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<Wallet>() {
            @Override
            public void successCallback(Result<Wallet> result) {

            }
        });

        myAccountTask.execute(LoginedUser.getLastLoginedUserInfo().getId());
    }
            public void getToke() {
                GettokenTask gettokenTask=new GettokenTask(MianFramActivity.this);
                gettokenTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<Gettoken>() {
                    @Override
                    public void failCallback(Result<Gettoken> result) {
                        ToastUtil.toast(result.getMessage());
                    }
                });
                gettokenTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<Gettoken>() {
                    @Override
                    public void successCallback(Result<Gettoken> result) {
                        if(result.getValue().getToken()!=null) {
                            connect(result.getValue().getToken());
                        }
                    }
                });
                gettokenTask.execute(LoginedUser.getLastLoginedUserInfo().getId(),LoginedUser.getLastLoginedUserInfo().getName(),LoginedUser.getLastLoginedUserInfo().getPic());
            }


            /**
             * 注册广播
             */
            private void brodcastRegister() {

                //跳转登录,操作前判断是已登录用的
                toLoginReceiver = new ToLoginReceiver(this);
                toLoginReceiver.register();
                //非业务员在注册退出登录广播，业务员去工作台注册
                if (!LoginedUser.getLoginedUser().getUserEnum().equals(UserEnum.SALESMAN)) {
                    //收到退出登录广播
                    toLogoutReceiver = new ToLogoutReceiver() {
                        @Override
                        public void onLogout() {
                            ExtraUtil.logoutAndUnBindPush(MianFramActivity.this);

                        }
                    };
                    toLogoutReceiver.register();
                }
//                //我的tab页数量显示
                changeRemindShowReceiver = new ChangeRemindShowReceiver() {
                    @Override
                    public void change() {
                        changeMyNum();
                    }
                };
                changeRemindShowReceiver.register();

                //通讯录改变广播
                contactsChangeReceiver = new ContactsChangeReceiver() {
                    @Override
                    public void change() {
                        loadFriend();
                    }
                };
                contactsChangeReceiver.register();

                totalUnreadNumReceiver = new CWTotalUnreadNumReceiver() {
                    @Override
                    public void totalUnreadNum(int totalUnreadNum) {
                        AppUnreadUtil.sendBadgeNumber(MianFramActivity.this);
                    }
                };
                totalUnreadNumReceiver.register(MianFramActivity.this);
                CWTotalUnreadNumReceiver.notifyReceiver(MianFramActivity.this);
            }

            /**
             * 改变数量
             */
            private void changeMyNum() {
                NewRemind data = NewRemind.getNewRemind();
                //加两遍订单数量
               allNum = data.getFeeApplyChangeCount() + data.getLeaveUnAuditCount() +
                        data.getFeeUnAuditCount() + data.getLeaveChangeCount() + data.getFeeReimbursementCount()
                        + data.getOrderCount() + data.getOrderCount() + data.getOrderWaitingForReceived() + data.getRebackPipeliningCount();
                if (!Validators.isEmpty(data.getUnReadDailyList())) {
                    for (NewRemindData.UnReadDaily unReadDaily : data.getUnReadDailyList()) {
                        allNum += unReadDaily.getUnreadCount();
                    }
                }
                if (!LoginedUser.getLoginedUser().isMaxLeavel()) {
                    //非boss，计算日报评论未读数量
                    allNum += data.getUnreadDailyCommentCount();
                }

                if(allNum==0){
                    my_view.setVisibility(View.GONE);
                }else {
                    my_view.setVisibility(View.VISIBLE);
                    my_view.setText(String.valueOf(allNum));
                }
            }

            @Override
            protected void onPostCreate(Bundle savedInstanceState) {
                super.onPostCreate(savedInstanceState);
                //获取新消息
                loadRemind();
           }

            @Override
            protected void onResume() {
                super.onResume();
                //获取新消息
                loadRemind();
            }


              private   Runnable mRunnable = new Runnable() {
                  public void run() {
                      while (true) {
                          try {
                              Thread.sleep(1000);
                              mHandler.sendMessage(mHandler.obtainMessage());
                          } catch (InterruptedException e) {
                              e.printStackTrace();
                          }
                      }
                  }
            };



        }

