package com.zjyeshi.dajiujiao.buyer.circle.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.utils.sharepreference.BPPreferences;
import com.zjyeshi.dajiujiao.buyer.activity.my.FriendActivity;
import com.zjyeshi.dajiujiao.buyer.circle.CanSeeActivity;
import com.zjyeshi.dajiujiao.buyer.circle.CircleAddActivity;
import com.zjyeshi.dajiujiao.buyer.circle.CircleCollectActivity;
import com.zjyeshi.dajiujiao.buyer.circle.PhoneActivity;
import com.zjyeshi.dajiujiao.buyer.circle.UpLoadPhoneActivity;
import com.zjyeshi.dajiujiao.buyer.entity.NewRemind;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.model.GetContactsModel;

/**
 * Created by wuhk on 2016/8/15.
 */
public class CircleMenuView extends PopupWindow {
    private RelativeLayout fabuLayout;
    private RelativeLayout canSeeLayout;
    private RelativeLayout contactLayout;
    private RelativeLayout collectLayout;
    private RelativeLayout phoneLayout;
    private TextView contactTv;
    private Context context;

    public CircleMenuView(Activity paramActivity, int paramInt1, int paramInt2){
        super(paramActivity);
        context = paramActivity;
        //窗口布局
        View mainView = LayoutInflater.from(paramActivity).inflate(R.layout.layout_circle_menu, null);
        //发布布局
        fabuLayout = (RelativeLayout)mainView.findViewById(R.id.fabuLayout);
        //设置可见布局
        canSeeLayout = (RelativeLayout)mainView.findViewById(R.id.canSeeLayout);
        //通讯录布局
        contactLayout = (RelativeLayout)mainView.findViewById(R.id.contactLayout);
        //我的收藏
        collectLayout = (RelativeLayout)mainView.findViewById(R.id.collectLayout);
        //手机联系人
        phoneLayout = (RelativeLayout)mainView.findViewById(R.id.phoneLayout);

        contactTv = (TextView)mainView.findViewById(R.id.contactTv);

        fabuLayout.setVisibility(View.GONE);

        setContentView(mainView);
        //设置宽度
        setWidth(paramInt1);
        //设置高度
        setHeight(paramInt2);
//        //设置显示隐藏动画
//        setAnimationStyle(R.style.AnimTools);
        //设置背景透明
        setBackgroundDrawable(new ColorDrawable(0));

        init();

    }
//发布圈子
    private void init(){
        fabuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                context.startActivity(new Intent(context, CircleAddActivity.class));
            }
        });
//可见设置
        canSeeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                context.startActivity(new Intent(context , CanSeeActivity.class));
            }
        });
//酒友通讯录
        contactLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                context.startActivity(new Intent(context , FriendActivity.class));
            }
        });
//圈子收藏
        collectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                context.startActivity(new Intent(context , CircleCollectActivity.class));
            }
        });
//手机联系人
        phoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetContactsModel.saveAndUpLoadPhoneContacts(context, new GetContactsModel.UpLoadSuccess() {
                    @Override
                    public void success() {
                        BPPreferences.instance().putBoolean(UpLoadPhoneActivity.PHONE_UPLOAD_KEY , true);
//                        startActivity(new Intent(UpLoadPhoneActivity.this , PhoneActivity.class));
//                        finish();
                    }
                });

                dismiss();
                boolean haveUpload = BPPreferences.instance().getBoolean(UpLoadPhoneActivity.PHONE_UPLOAD_KEY , false);
                if (haveUpload){
                    context.startActivity(new Intent(context , PhoneActivity.class));
                }else{
                    context.startActivity(new Intent(context , UpLoadPhoneActivity.class));
                }

            }
        });
        getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    dismiss();
                }
            }
        });
        NewRemind data = NewRemind.getNewRemind();
        if (data.getFriendApplyCount() == 0){
            contactTv.setText("酒友通讯录");
        }else{
            contactTv.setText("酒友通讯录("+ data.getFriendApplyCount() + ")");
        }
    }


}
