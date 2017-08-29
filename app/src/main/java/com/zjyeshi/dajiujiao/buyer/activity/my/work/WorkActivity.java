package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zjyeshi.dajiujiao.buyer.activity.rong.broadcast.CWTotalUnreadNumReceiver;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigapple.lib.utils.sharepreference.BPPreferences;
import com.xuan.bigdog.lib.bservice.bversioncheck.BDVersionCheckModel;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.activity.my.PersonalInfoActivity;
import com.zjyeshi.dajiujiao.buyer.activity.my.SettingActivity;
import com.zjyeshi.dajiujiao.buyer.activity.order.MyOrderNewActivity;
import com.zjyeshi.dajiujiao.buyer.activity.rong.get.GettokenTask;
import com.zjyeshi.dajiujiao.buyer.activity.rong.ui.DragPointView;
import com.zjyeshi.dajiujiao.buyer.activity.rong.ui.LoadDialog;
import com.zjyeshi.dajiujiao.buyer.activity.sales.RebackManagerListActivity;
import com.zjyeshi.dajiujiao.buyer.activity.sales.SalesActivity;
import com.zjyeshi.dajiujiao.buyer.activity.store.CourseWebActivity;
import com.zjyeshi.dajiujiao.buyer.chat.consumer.BasePush;
import com.zjyeshi.dajiujiao.buyer.common.Constants;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.dao.Gettoken;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.NewRemind;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LoginEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.UserEnum;
import com.zjyeshi.dajiujiao.buyer.receiver.ChangeRemindShowReceiver;
import com.zjyeshi.dajiujiao.buyer.receiver.LoadNewRemindReceiver;
import com.zjyeshi.dajiujiao.buyer.receiver.ToLogoutReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.my.WorkTimeData;
import com.zjyeshi.dajiujiao.buyer.task.my.GetWorkTimeTask;
import com.zjyeshi.dajiujiao.buyer.task.work.NewRemindTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.NewRemindData;
import com.zjyeshi.dajiujiao.buyer.utils.AppUnreadUtil;
import com.zjyeshi.dajiujiao.buyer.utils.AuthUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.views.UnReadNumView;

import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.message.ContactNotificationMessage;

/**
 * 工作台主界面
 * Created by zhum on 2016/6/12.
 */
public class WorkActivity extends BaseActivity implements DragPointView.OnDragListencer,
        IUnReadMessageObserver {
    private SharedPreferences.Editor editor;
    private SharedPreferences sp;
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;//标题栏
    @InjectView(R.id.infoLayout)
    private RelativeLayout infoLayout;
    @InjectView(R.id.checkLayout)
    private RelativeLayout checkLayout;//考勤
    @InjectView(R.id.khLayout)
    private RelativeLayout khLayout;//客户管理
    @InjectView(R.id.spLayout)
    private RelativeLayout spLayout;//审批
    @InjectView(R.id.hbLayout)
    private RelativeLayout hbLayout;//工作汇报
    @InjectView(R.id.contactsLayout)
    private RelativeLayout contactsLayout;//联系
    @InjectView(R.id.messageLayout)
    private RelativeLayout messageLayout;//我的消息
    @InjectView(R.id.messageUnReadView)
    private UnReadNumView messageUnReadView;
    @InjectView(R.id.dailyUnReadNumView)
    private UnReadNumView dailyUnReadNumView;
    @InjectView(R.id.settingTv)
    private TextView settingTv;
    @InjectView(R.id.checkUnread)
    private UnReadNumView checkUnread;//考勤数量
    @InjectView(R.id.feeUnread)
    private UnReadNumView feeUnread;//费用数量
    @InjectView(R.id.khUnread)
    private UnReadNumView khUnread;//客户管理数量
    @InjectView(R.id.circleUnReadView)
    private UnReadNumView circleUnRead;//酒友圈好友申请数量
    @InjectView(R.id.salesQrCodeLayout)
    private RelativeLayout salesQrCodeLayout;
    @InjectView(R.id.circleLayout)
    private RelativeLayout circleLayout;
    @InjectView(R.id.salesLayout)
    private RelativeLayout salesLayout;//优惠活动
    @InjectView(R.id.rebackLayout)
    private RelativeLayout rebackLayout;
    @InjectView(R.id.rebackUnReadView)
    private UnReadNumView rebackUnReadView;

    private CWTotalUnreadNumReceiver totalUnreadNumReceiver;
    private ToLogoutReceiver toLogoutReceiver;//退出登录广播，业务员登录时才注册;
    private ChangeRemindShowReceiver changeRemindShowReceiver;//改变显示广播

    private boolean backPressed;
    private Handler handler = new Handler();

    private String type; //点击推送传入的type

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_work);


        initWidgets();
        getToke();
        //注册广播
        broadcastRegister();
        //更新提醒
        refreshRemind();
        initData();
        //自动检测版本
        if (LoginedUser.getLoginedUser().getUserEnum().equals(UserEnum.SALESMAN)) {
            BDVersionCheckModel.getInstance().doCheckDefault(WorkActivity.this, UrlConstants.WEB_SITE + UrlConstants.CHECKVERSION, Constants.APK_FILENAME);
        }

        //左上角的设置，业务员才有
        if (LoginedUser.getLoginedUser().getUserEnum().equals(UserEnum.SALESMAN)) {
            settingTv.setVisibility(View.VISIBLE);
            //获取上下班时间
            getWorkTime();
        } else {
            settingTv.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != NewRemind.getNewRemind()) {
            changeRemindShow();
        }
        LoadNewRemindReceiver.notifyReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        totalUnreadNumReceiver.unregister(WorkActivity.this);
        if (LoginedUser.getLoginedUser().getUserEnum().equals(UserEnum.SALESMAN)) {
            toLogoutReceiver.unregister();
        }
        changeRemindShowReceiver.unregister();
    }

    private void initWidgets() {

        titleLayout.configTitle("工作台");
        if (!LoginedUser.getLoginedUser().getUserEnum().equals(UserEnum.SALESMAN)) {
            titleLayout.configReturn(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            Constants.isAlive = true;
        }

        //聊天消息未读数量
      //  messageUnReadView.setNum(CWChat.getInstance().getTotalUnreadNum());

        //个人信息 , 业务员才显示个人信息
        infoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(WorkActivity.this, PersonalInfoActivity.class);
                startActivity(intent);
            }
        });
        if (LoginedUser.getLoginedUser().getUserEnum().equals(UserEnum.SALESMAN)) {
            infoLayout.setVisibility(View.VISIBLE);
        } else {
            infoLayout.setVisibility(View.GONE);
        }

        //出勤记录
        checkLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WorkActivity.this, CheckActivity.class);
                startActivity(intent);
            }
        });


        //工作日报
        if (AuthUtil.showDateReport()) {
            hbLayout.setVisibility(View.VISIBLE);
        } else {
            hbLayout.setVisibility(View.GONE);
        }
        hbLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AuthUtil.dateGoFilterDirect()) {
                    String name;
                    if (!Validators.isEmpty(LoginedUser.getLoginedUser().getShopName())) {
                        name = LoginedUser.getLoginedUser().getShopName();
                    } else {
                        name = LoginedUser.getLoginedUser().getName();
                    }
                    MemberFilterActivity.startActivity(WorkActivity.this, AuthUtil.getFilterRole()
                            , name, "", false);
                } else {
                    Intent intent = new Intent(WorkActivity.this, DateReportActivity.class);
                    startActivity(intent);
                }
            }
        });

        //特殊费用，里面就是费用申请和费用审批
        spLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WorkActivity.this, ApproveActivity.class);
                startActivity(intent);
            }
        });

        //客户管理
        khLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WorkActivity.this, KhManagerActivity.class);
                startActivity(intent);
            }
        });


        //联系列表
        contactsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WorkActivity.this, ContactActivity.class);
                startActivity(intent);
            }
        });

        //聊天记录, 业务员才显示聊天记录
        messageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //消息
                Intent intent = new Intent(WorkActivity.this, MyMessageActivity.class);
                startActivity(intent);

            }
        });
        if (LoginedUser.getLoginedUser().getUserEnum().equals(UserEnum.SALESMAN)) {
            messageLayout.setVisibility(View.VISIBLE);
        } else {
            messageLayout.setVisibility(View.GONE);
        }

        //酒友圈，业务员才显示酒友圈
        if (LoginedUser.getLoginedUser().getUserEnum().equals(UserEnum.SALESMAN)) {
            circleLayout.setVisibility(View.VISIBLE);
        } else {
            circleLayout.setVisibility(View.GONE);
        }
        circleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(WorkActivity.this, SalesManCircleActivity.class);
                startActivity(intent);
            }
        });

        //二维码
        if (LoginedUser.getLoginedUser().getUserEnum().equals(UserEnum.SALESMAN)) {
            salesQrCodeLayout.setVisibility(View.VISIBLE);
        } else {
            salesQrCodeLayout.setVisibility(View.GONE);
        }
        //业务员二维码
        salesQrCodeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://i.yeshiwine.com/app/show_erwma.html?type=2&memberId=" + LoginedUser.getLoginedUser().getId();
                CourseWebActivity.startWebActivity(WorkActivity.this, url, "我的二维码");
            }
        });

        //优惠活动
        salesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SalesActivity.startSalesActivity(WorkActivity.this);
            }
        });
        if (AuthUtil.showSalesLayout()){
            salesLayout.setVisibility(View.VISIBLE);
        }else{
            salesLayout.setVisibility(View.GONE);
        }

        //退货管理
        rebackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RebackManagerListActivity.startReBackActivity(WorkActivity.this);
            }
        });


        //业务员左上角设置
        settingTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(WorkActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        // 广播注册成功后发消息通知数量刷新一下
        CWTotalUnreadNumReceiver.notifyReceiver(WorkActivity.this);

        //点击推送处理
        type = getIntent().getStringExtra(BasePush.PARAM_NOTICE_TYPE);
        if (!Validators.isEmpty(type)) {
//            //业务员没启动程序，点击推送进入WorkActivity,首先连接聊天服务器
//            ExtraUtil.connectChat(WorkActivity.this);
            if (type.equals(BasePush.PUSH_NEW_ORDER)) {
                //新订单
                Intent intent = new Intent();
                intent.setClass(WorkActivity.this, MyOrderNewActivity.class);
                intent.putExtra(MyOrderNewActivity.USER_TYPE, LoginEnum.SELLER.toString());
                if (LoginedUser.getLoginedUser().getUserEnum().equals(UserEnum.SALESMAN)) {
                    intent.putExtra(MyOrderNewActivity.ROLE, "new");
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else if (type.equals(BasePush.PUSH_NEW_CHAT_MESSAGE)) {
                //新消息，先打开主界面，跳转聊天列表
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(WorkActivity.this, MyMessageActivity.class);
                startActivity(intent);
            }
        }
    }

    /**
     * 更新提醒
     */
    private void refreshRemind() {
        NewRemindTask newRemindTask = new NewRemindTask(WorkActivity.this);
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
                changeRemindShow();
            }
        });

        newRemindTask.execute();
    }

    //改变提醒显示
    private void changeRemindShow() {
        NewRemind data = NewRemind.getNewRemind();
        //考勤未读数量 = 请假状态变动数量 + 请假待审批数量
        int checkUnreadNum = data.getLeaveChangeCount() + data.getLeaveUnAuditCount();
        checkUnread.setNum(checkUnreadNum);

        //特殊费用数量 = 费用申请装填变动数量 +　费用报销状态变动数量　+ 费用申请报销待审批数量
        int feeUnreadNum = data.getFeeApplyChangeCount() + data.getFeeReimbursementCount() + data.getFeeUnAuditCount();
        feeUnread.setNum(feeUnreadNum);

        //工作日报未读数量 = 列表unreadCount数量之和
        int totalUnRead = 0;
        if (!Validators.isEmpty(data.getUnReadDailyList())) {
            for (NewRemindData.UnReadDaily unReadDaily : data.getUnReadDailyList()) {
                totalUnRead += unReadDaily.getUnreadCount(); //这个unreadCount表示未读日报和未读评论之和
            }
        }
        //非boss的业务员 ，日报未读数是未读的回复评论数量
        if (!LoginedUser.getLoginedUser().isMaxLeavel()) {
            totalUnRead += data.getUnreadDailyCommentCount();
        }
        dailyUnReadNumView.setNum(totalUnRead);

        //客户订单管理数量
        khUnread.setNum(data.getOrderCount());

        //好友申请数量
        circleUnRead.setNum(data.getFriendApplyCount());

        //退货管理待处理数量
        rebackUnReadView.setNum(data.getRebackPipeliningCount());
    }

    @Override
    public void onBackPressed() {
        if (LoginedUser.getLoginedUser().getUserEnum().equals(UserEnum.SALESMAN)) {
            if (backPressed) {
                Constants.isAlive = false;
               // CWChat.getInstance().getImClient().disConnect();
                RongIM.getInstance().disconnect();
                finish();
            } else {
                ToastUtil.toast("再按一次退出程序");
                backPressed = true;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        backPressed = false;
                    }
                }, 2000L);
            }
        } else {
            super.onBackPressed();
        }
    }

    //广播注册
    private void broadcastRegister() {
        //未读消息数量广播
        totalUnreadNumReceiver = new CWTotalUnreadNumReceiver() {
            @Override
            public void totalUnreadNum(int totalUnreadNum) {
                AppUnreadUtil.sendBadgeNumber(WorkActivity.this);
              // messageUnReadView.setNum(totalUnreadNum);

            }
        };
        totalUnreadNumReceiver.register(WorkActivity.this);

        //退出登录广播 , 业务员才注册
        if (LoginedUser.getLoginedUser().getUserEnum().equals(UserEnum.SALESMAN)) {
            toLogoutReceiver = new ToLogoutReceiver() {
                @Override
                public void onLogout() {
                    ExtraUtil.logoutAndUnBindPush(WorkActivity.this);
                }
            };
            toLogoutReceiver.register();
        }

        //改变显示
        changeRemindShowReceiver = new ChangeRemindShowReceiver() {
            @Override
            public void change() {
                changeRemindShow();
            }
        };
        changeRemindShowReceiver.register();
    }

    //业务员获取上下班时间
    private void getWorkTime() {
        GetWorkTimeTask getWorkTimeTask = new GetWorkTimeTask(WorkActivity.this);
        getWorkTimeTask.setShowProgressDialog(false);
        getWorkTimeTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<WorkTimeData>() {
            @Override
            public void failCallback(Result<WorkTimeData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });
        getWorkTimeTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<WorkTimeData>() {
            @Override
            public void successCallback(Result<WorkTimeData> result) {
                BPPreferences.instance().putString("workTime" + LoginedUser.getLoginedUser().getId(), JSON.toJSONString(result.getValue()));
            }
        });

        getWorkTimeTask.execute();
    }
    public void getToke() {
        GettokenTask gettokenTask=new GettokenTask(WorkActivity.this);
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
                // startActivity(new Intent(LoginActivity.this, MainActivity.class));
                //   ToastUtil.toast("聊天服务器连接成功");
                //     finish();

                if(RongIM.getInstance() != null)

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
                        LoadDialog.show(getApplication());
                        RongIM.connect(cacheToken, new RongIMClient.ConnectCallback() {
                            @Override
                            public void onTokenIncorrect() {
                                LoadDialog.dismiss(getApplication());
                            }

                            @Override
                            public void onSuccess(String s) {
                                LoadDialog.dismiss(getApplication());
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode e) {
                                LoadDialog.dismiss(getApplication());
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
            messageUnReadView.setVisibility(View.GONE);
        } else if (count > 0 && count < 100) {
            messageUnReadView.setVisibility(View.VISIBLE);
            messageUnReadView.setNum(count);
        } else {
            messageUnReadView.setVisibility(View.VISIBLE);
            messageUnReadView.setNum(R.string.no_read_message);
        }
    }

    @Override
    public void onDragOut() {

    }
}