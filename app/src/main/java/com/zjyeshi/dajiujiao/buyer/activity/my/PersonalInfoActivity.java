package com.zjyeshi.dajiujiao.buyer.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.activity.my.personal.MyAddressActivity;
import com.zjyeshi.dajiujiao.buyer.activity.store.CourseWebActivity;
import com.zjyeshi.dajiujiao.buyer.receiver.info.ChangeAvatorReceiver;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.my.personal.ChangeNameActivity;
import com.zjyeshi.dajiujiao.buyer.activity.my.personal.ShowImageActivity;
import com.zjyeshi.dajiujiao.buyer.common.PassConstans;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.enums.UserEnum;
import com.zjyeshi.dajiujiao.buyer.model.UploadHeadModel;
import com.zjyeshi.dajiujiao.buyer.receiver.info.ChangeNameReceiver;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;

/**
 * 个人信息
 * Created by wuhk on 2015/9/28.
 */
public class PersonalInfoActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;

    @InjectView(R.id.photoLayout)
    private RelativeLayout photoLayout;//布局

    @InjectView(R.id.headIv)
    private ImageView headIv; //头像

    @InjectView(R.id.nameLayout)
    private RelativeLayout nameLayout; //姓名

    @InjectView(R.id.nameTv)
    private TextView nameTv;

    @InjectView(R.id.phoneLayout)
    private RelativeLayout phoneLayout;//手机号码

    @InjectView(R.id.phoneTv)
    private TextView phoneTv;

    @InjectView(R.id.qrCodeLayout)
    private RelativeLayout qrCodeLayout;

    @InjectView(R.id.addressLayout)
    private RelativeLayout addressLayout;//我的地址

    private UploadHeadModel uploadHeadModel = new UploadHeadModel();

    private ChangeNameReceiver changeNameReceiver;//改变姓名广播
    private ChangeAvatorReceiver changeAvatorReceiver;//修改头像

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_my_info);
        initWidgets();

        registerNotice();

        //上传头像初始化
        uploadHeadModel.initForActivity(photoLayout, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterNotice();
    }

    private void initWidgets() {
        titleLayout.configReturn("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleLayout.configTitle("个人信息");
        //手机号
        phoneTv.setText(LoginedUser.getLoginedUser().getPhone());

        headIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(PassConstans.IMAGEURL , LoginedUser.getLoginedUser().getPic());
                intent.setClass(PersonalInfoActivity.this , ShowImageActivity.class);
                startActivity(intent);
            }
        });
        //头像
        uploadHeadModel.refreshAvator(headIv);

        //姓名
        nameTv.setText(LoginedUser.getLoginedUser().getName());
        nameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(ChangeNameActivity.PARAM_NAME, nameTv.getText().toString());
                intent.setClass(PersonalInfoActivity.this, ChangeNameActivity.class);
                startActivity(intent);
            }
        });
        //手机号码
        phoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.toast("手机号不能修改");
            }
        });
        //我的地址
        addressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(PassConstans.ISSELECTADD, "No");
                intent.setClass(PersonalInfoActivity.this, MyAddressActivity.class);
                startActivity(intent);
            }
        });

        //我的二维码
        qrCodeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://i.yeshiwine.com/app/show_erwma.html?type=1&sellerId=" + LoginedUser.getLoginedUser().getShopId();
                CourseWebActivity.startWebActivity(PersonalInfoActivity.this , url , "我的二维码");
            }
        });

        if (LoginedUser.getLoginedUser().getUserEnum().equals(UserEnum.SALESMAN)){
            addressLayout.setVisibility(View.GONE);
            qrCodeLayout.setVisibility(View.GONE);
        }else{
            addressLayout.setVisibility(View.VISIBLE);
            qrCodeLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }

        uploadHeadModel.onActivityResult(requestCode, resultCode, data);
    }

    private void registerNotice() {
        //更改名字之后改变名字
        changeNameReceiver = new ChangeNameReceiver() {
            @Override
            public void changeName() {
                nameTv.setText(LoginedUser.getLoginedUser().getName());
            }
        };
        changeNameReceiver.register();

        changeAvatorReceiver = new ChangeAvatorReceiver() {
            @Override
            public void changeAvator() {
                uploadHeadModel.refreshAvator(headIv);
            }
        };
        changeAvatorReceiver.register();
    }

    private void unRegisterNotice() {
        changeNameReceiver.unRegister();
        changeAvatorReceiver.unRegister();
    }

}
