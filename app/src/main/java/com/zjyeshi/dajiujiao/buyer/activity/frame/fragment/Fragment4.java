package com.zjyeshi.dajiujiao.buyer.activity.frame.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.my.CouponActivity;
import com.zjyeshi.dajiujiao.buyer.activity.my.PersonalInfoActivity;
import com.zjyeshi.dajiujiao.buyer.activity.my.SettingActivity;
import com.zjyeshi.dajiujiao.buyer.activity.my.ShopMaActivity;
import com.zjyeshi.dajiujiao.buyer.activity.my.work.Pyoductactivity;
import com.zjyeshi.dajiujiao.buyer.activity.my.work.WorkActivity;
import com.zjyeshi.dajiujiao.buyer.activity.order.MyOrderNewActivity;
import com.zjyeshi.dajiujiao.buyer.activity.rong.broadcast.CWTotalUnreadNumReceiver;
import com.zjyeshi.dajiujiao.buyer.activity.seller.ShopManageActivity;
import com.zjyeshi.dajiujiao.buyer.activity.store.BuyCarActivity;
import com.zjyeshi.dajiujiao.buyer.common.Constants;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.NewRemind;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LoginEnum;
import com.zjyeshi.dajiujiao.buyer.model.UploadHeadModel;
import com.zjyeshi.dajiujiao.buyer.receiver.ChangeRemindShowReceiver;
import com.zjyeshi.dajiujiao.buyer.receiver.LoadNewRemindReceiver;
import com.zjyeshi.dajiujiao.buyer.receiver.info.ChangeAvatorReceiver;
import com.zjyeshi.dajiujiao.buyer.receiver.info.ChangeNameReceiver;
import com.zjyeshi.dajiujiao.buyer.task.work.NewRemindTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.NewRemindData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.views.UnReadNumView;

/**
 * 我的
 * Created by xuan on 15/9/16.
 */
public class Fragment4 extends BaseFragment {
    @InjectView(R.id.loginedLayout)
    private RelativeLayout loginedLayout;//已登录
    @InjectView(R.id.sellerLayout)
    private LinearLayout sellerLayout;//卖家增加模块
    @InjectView(R.id.shopManangeLayout)
    private RelativeLayout shopManangeLayout;//商家管理
    @InjectView(R.id.myCouponLayout)
    private RelativeLayout myCouponLayout;//我的优惠券
    @InjectView(R.id.shopMaLayout)
    private RelativeLayout shopMaLayout;//店铺码
    @InjectView(R.id.workLayout)
    private RelativeLayout workLayout;//工作台
    @InjectView(R.id.orderLayout)
    private RelativeLayout orderLayout;//我的订单
    @InjectView(R.id.sellerOrderLayout)
    private RelativeLayout sellerOrderLayout;//销售订单
    @InjectView(R.id.goodCarLayout)
    private RelativeLayout goodCarLayout;//我的购物车
    @InjectView(R.id.settingLayout)
    private RelativeLayout settingLayout;//设置

    @InjectView(R.id.photoIv)
    private ImageView photoIv;//头像
    @InjectView(R.id.nameTv)
    private TextView nameTv;

    @InjectView(R.id.workUnread)
    private UnReadNumView workUnread;//工作台未读数量
    @InjectView(R.id.buyOrderUnread)
    private UnReadNumView buyOrderUnread;//买家待收货数量
    @InjectView(R.id.orderCountUnRead)
    private UnReadNumView orderCountUnRead;//订单待处理数量


    private ChangeAvatorReceiver changeAvatorReceiver;//改变头像
    private ChangeNameReceiver changeNameReceiver;//修改名字
    private ChangeRemindShowReceiver changeRemindShowReceiver;//改变工作台新消息显示
    private CWTotalUnreadNumReceiver unreadNumReceiver;//消息未读数量

    private UploadHeadModel uploadHeadModel = new UploadHeadModel();
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            loadRemind();
        }
    };
    @Override
    protected int initFragmentView() {
        return R.layout.fragment4;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        register();
        new Thread(mRunnable).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRigister();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != NewRemind.getNewRemind()) {
            showWorkInfo();
        }
        LoadNewRemindReceiver.notifyReceiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        uploadHeadModel.refreshAvator(photoIv);//更新头像
        return v;
    }

    @Override
    protected void initFragmentWidgets(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (LoginEnum.BURER == Constants.loginEnum) {
            sellerLayout.setVisibility(View.GONE);
        } else {
            sellerLayout.setVisibility(View.VISIBLE);
        }

        loadRemind();

        LoginedUser loginedUser = LoginedUser.getLoginedUser();

        //个人信息
        loginedLayout.setVisibility(View.VISIBLE);
        uploadHeadModel.refreshAvator(photoIv);//更新头像
        initTextView(nameTv, LoginedUser.getLoginedUser().getName());
        loginedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), PersonalInfoActivity.class);
                startActivity(intent);
            }
        });

        //商家管理
        shopManangeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginedUser.checkLogined()) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), ShopManageActivity.class);
                    startActivity(intent);
                }

            }
        });

        //我的优惠券
        myCouponLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CouponActivity.startActivity(getActivity());
            }
        });

        //店铺码
        shopMaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ShopMaActivity.class));
            }
        });

        //我的工作台
        workLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginedUser.checkLogined()) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), WorkActivity.class);
                    startActivity(intent);
                }
            }
        });

        //采购订单
        orderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginedUser.checkLogined()) {
                    MyOrderNewActivity.startOrderActivity(getActivity(), LoginEnum.BURER.toString());
                }
            }
        });

        //销售订单
        sellerOrderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginedUser.checkLogined()) {
                    MyOrderNewActivity.startOrderActivity(getActivity(), LoginEnum.SELLER.toString());
                }
            }
        });

        //我的购物车
        goodCarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(getActivity(), BuyCarActivity.class);
//                startActivity(intent);
                Intent intent = new Intent();
                intent.setClass(getActivity(), Pyoductactivity.class);
                startActivity(intent);
            }
        });

        //设置
        settingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginedUser.checkLogined()) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), SettingActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void register() {
        //修改名字
        changeNameReceiver = new ChangeNameReceiver() {
            @Override
            public void changeName() {
                nameTv.setText(LoginedUser.getLoginedUser().getName());
            }
        };
        changeNameReceiver.register();
        //修改头像
        changeAvatorReceiver = new ChangeAvatorReceiver() {
            @Override
            public void changeAvator() {
                uploadHeadModel.refreshAvator(photoIv);//更新头像
            }
        };
        changeAvatorReceiver.register();
        //提醒数量
        changeRemindShowReceiver = new ChangeRemindShowReceiver() {
            @Override
            public void change() {
                showWorkInfo();
            }
        };
        changeRemindShowReceiver.register();
        //消息未读数量
        unreadNumReceiver = new CWTotalUnreadNumReceiver() {
            @Override
            public void totalUnreadNum(int totalUnreadNum) {
                showWorkInfo();
            }
        };
        unreadNumReceiver.register(getActivity());
    }

    private void unRigister() {
        changeAvatorReceiver.unRegister();
        changeNameReceiver.unRegister();
        changeRemindShowReceiver.unregister();
        unreadNumReceiver.unregister(getActivity());
    }

    //显示工作台里是否有新信息
    private void loadRemind() {
        NewRemindTask newRemindTask = new NewRemindTask(getActivity());
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
                showWorkInfo();
            }
        });

        newRemindTask.execute();
    }

    private void showWorkInfo() {
        NewRemind data = NewRemind.getNewRemind();
        int workNum = data.getFeeApplyChangeCount() + data.getLeaveUnAuditCount() +
                data.getFeeUnAuditCount() + data.getLeaveChangeCount() + data.getFeeReimbursementCount()
                + data.getOrderCount() + data.getRebackPipeliningCount();
        if (!Validators.isEmpty(data.getUnReadDailyList())) {
            for (NewRemindData.UnReadDaily unReadDaily : data.getUnReadDailyList()) {
                workNum += unReadDaily.getUnreadCount();
            }
        }
        if (!LoginedUser.getLoginedUser().isMaxLeavel()){
            //非boss，计算日报评论未读数量
            workNum += data.getUnreadDailyCommentCount();
        }
        workUnread.setNum(workNum);
        orderCountUnRead.setNum(data.getOrderCount());
        buyOrderUnread.setNum(data.getOrderWaitingForReceived());
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
