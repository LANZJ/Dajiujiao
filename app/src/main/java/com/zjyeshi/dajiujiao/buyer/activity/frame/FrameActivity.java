package com.zjyeshi.dajiujiao.buyer.activity.frame;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.widget.RadioGroup;

import com.jopool.crow.CWChat;
import com.zjyeshi.dajiujiao.buyer.activity.rong.broadcast.CWTotalUnreadNumReceiver;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigappleui.lib.view.BUNumRadioButton;
import com.xuan.bigdog.lib.bservice.bversioncheck.BDVersionCheckModel;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.frame.fragment.Fragment1Seller;
import com.zjyeshi.dajiujiao.buyer.activity.frame.fragment.Fragment4;
import com.zjyeshi.dajiujiao.buyer.activity.login.LoginActivity;
import com.zjyeshi.dajiujiao.buyer.activity.order.MyOrderNewActivity;
import com.zjyeshi.dajiujiao.buyer.chat.consumer.BasePush;
import com.zjyeshi.dajiujiao.buyer.circle.CircleFragment;
import com.zjyeshi.dajiujiao.buyer.common.Constants;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.NewRemind;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LoginEnum;
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
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

/**
 * 主界面
 * Created by xuan on 15/9/16.
 */
public class FrameActivity extends BaseFrameActivity {

       private ToLoginReceiver toLoginReceiver;//登录广播,未登录的情况下点击需要登录的地方会触发
    private CWTotalUnreadNumReceiver totalUnreadNumReceiver;//聊天未读消息广播
    private ToLogoutReceiver toLogoutReceiver;//多账号登录退出登录广播,非业务员登录的时候注册
    private ChangeRemindShowReceiver changeRemindShowReceiver;//新消息变动改变显示广播
    private ContactsChangeReceiver contactsChangeReceiver;//酒友通讯录改变广播




    Fragment conversationList;
    private BUNumRadioButton tabBtn1;//消息tab页
    private BUNumRadioButton tabBtn2;//圈子tab页
    private BUNumRadioButton tabBtn3;//我的tab页
   String g="pzR3b+sGy4M97PevGUaRJ10A6zeTyWWXcxY+xW8A3NrkM2zXtoobwaL4YfRzXL430q/L5qQOHajuvYlvsdvmxA==";
    private boolean backPressed;
    private Handler handler = new Handler();//再按一次退出

    private String type;//点击推送传过来的type

    public static boolean tab3Checked = false;//我的
    public static boolean tab1Checked = false;//消息

    @Override
    protected Class<?>[] initFragment() {
        return new Class<?>[]{Fragment1Seller.class,
             //   ConversationListActivity.class,
                CircleFragment.class, Fragment4.class};
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  mConversationListFragment = (ConversationListFragment) initConversationList();
        tabBtn1 = (BUNumRadioButton) tabBtns[1];
        tabBtn2 = (BUNumRadioButton) tabBtns[2];
        tabBtn3 = (BUNumRadioButton) tabBtns[3];
        tabBtn3.setOffsetWidthByDp(10);
        Constants.isAlive = true;




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
        // 广播注册成功后发消息通知数量刷新一下
        CWTotalUnreadNumReceiver.notifyReceiver(FrameActivity.this);
        //自动检测新版本
        BDVersionCheckModel.getInstance().doCheckDefault(FrameActivity.this, UrlConstants.WEB_SITE + UrlConstants.CHECKVERSION, Constants.APK_FILENAME);

        //登录状态下才显示未读消息数量
        if (LoginedUser.getLoginedUser().isLogined()) {
            tabBtn1.setNum(CWChat.getInstance().getTotalUnreadNum(), R.drawable.dg_icon_red, R.drawable.dg_icon_red2);
        }

        //点击推送过来的处理
        type = getIntent().getStringExtra(BasePush.PARAM_NOTICE_TYPE);
        if (!Validators.isEmpty(type)) {
            if (type.equals(BasePush.PUSH_NEW_ORDER)) {
                //新订单提醒，进入我的订单
                Intent intent = new Intent();
                intent.setClass(FrameActivity.this, MyOrderNewActivity.class);
                intent.putExtra(MyOrderNewActivity.USER_TYPE, LoginEnum.SELLER.toString());
                if (LoginedUser.getLoginedUser().getUserEnum().equals(UserEnum.SALESMAN)) {
                    intent.putExtra(MyOrderNewActivity.ROLE, "new");
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else if (type.equals(BasePush.PUSH_NEW_CHAT_MESSAGE)) {
                //新消息，进入聊天界面
                //先打开主界面，进入聊天tab页
               // tabBtn1.setChecked(true);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (tab3Checked) {
            tabBtn3.setChecked(true);
            tab3Checked = false;
        } else if (tab1Checked) {
            tabBtn1.setChecked(true);
            tab1Checked = false;
        }

    }

    @Override
    public void onBackPressed() {
        if (backPressed) {
            Constants.isAlive = false;
            CWChat.getInstance().getImClient().disConnect();
            FrameActivity.this.finish();
        } else {
            ToastUtil.toast("再按一次退出");
            backPressed = true;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    backPressed = false;
                }
            }, 2000L);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        totalUnreadNumReceiver.unregister(this);
        toLoginReceiver.unRegister();
        if (!LoginedUser.getLoginedUser().getUserEnum().equals(UserEnum.SALESMAN)) {
            toLogoutReceiver.unregister();
        }
        changeRemindShowReceiver.unregister();
        contactsChangeReceiver.unregister();
    }

    @Override
    protected boolean onTabChecked(RadioGroup group, int checkedId) {
        //未登录情况的点击处理
        if (checkedId == ((BUNumRadioButton) tabBtns[1]).getId() || checkedId == ((BUNumRadioButton) tabBtns[2]).getId()) {
            if (!LoginedUser.getLoginedUser().isLogined()) {
                Intent intent = new Intent();
                intent.setClass(FrameActivity.this, LoginActivity.class);
                startActivity(intent);
                ((BUNumRadioButton) tabBtns[1]).setChecked(false);
                ((BUNumRadioButton) tabBtns[2]).setChecked(false);
                lastCheckedBtn.setChecked(true);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 注册广播
     */
    private void brodcastRegister() {
        //收到未读消消息数量通知后改变数量，并保存
//        totalUnreadNumReceiver = new CWTotalUnreadNumReceiver() {
//            @Override
//            public void totalUnreadNum(int totalUnreadNum) {
//                tabBtn1.setNum(totalUnreadNum, R.drawable.dg_icon_red, R.drawable.dg_icon_red2);
//                AppUnreadUtil.sendBadgeNumber(FrameActivity.this);
//                changeMyNum();
//            }
//        };
        totalUnreadNumReceiver.register(this);
        //跳转登录,操作前判断是已登录用的
        toLoginReceiver = new ToLoginReceiver(this);
        toLoginReceiver.register();
        //非业务员在注册退出登录广播，业务员去工作台注册
        if (!LoginedUser.getLoginedUser().getUserEnum().equals(UserEnum.SALESMAN)) {
            //收到退出登录广播
            toLogoutReceiver = new ToLogoutReceiver() {
                @Override
                public void onLogout() {
                    ExtraUtil.logoutAndUnBindPush(FrameActivity.this);

                }
            };
            toLogoutReceiver.register();
        }
        //我的tab页数量显示
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
    }

    /**
     * 获取上级店铺
     */
    private void getTopShop() {
        GetProductBuyTask getProductBuyTask = new GetProductBuyTask(FrameActivity.this);
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
     * 改变数量
     */
    private void changeMyNum() {
        NewRemind data = NewRemind.getNewRemind();
        //加两遍订单数量
        int allNum = data.getFeeApplyChangeCount() + data.getLeaveUnAuditCount() +
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
        //第四个tab页的总数
        tabBtn3.setNum(allNum, R.drawable.dg_icon_red, R.drawable.dg_icon_red2);
        //好友申请数量
        tabBtn2.setNum(data.getFriendApplyCount(), R.drawable.dg_icon_red, R.drawable.dg_icon_red2);
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
     * 获取好友列表
     */
    private void loadFriend() {
        FriendListTask friendListTask = new FriendListTask(FrameActivity.this);
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
        MyAccountTask myAccountTask = new MyAccountTask(FrameActivity.this);
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

        myAccountTask.execute();
    }



}
