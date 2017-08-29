package com.zjyeshi.dajiujiao.buyer.circle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.sharepreference.BPPreferences;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.model.GetContactsModel;
import com.zjyeshi.dajiujiao.R;

/**
 * Created by wuhk on 2016/9/11.
 */
public class UpLoadPhoneActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.sendBtn)
    private Button sendBtn;
    public static final String PHONE_UPLOAD_KEY = LoginedUser.getLoginedUser().getId() + "phone_up_load";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_upload_phone);
        initWidgets();
    }

    private void initWidgets(){


        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle("手机联系人");

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetContactsModel.saveAndUpLoadPhoneContacts(UpLoadPhoneActivity.this, new GetContactsModel.UpLoadSuccess() {
                    @Override
                    public void success() {
                        BPPreferences.instance().putBoolean(PHONE_UPLOAD_KEY , true);
                        startActivity(new Intent(UpLoadPhoneActivity.this , PhoneActivity.class));
                        finish();
                    }
                });
//                //上传
//                GetContactsModel.upLoadContacts(UpLoadPhoneActivity.this, true , new GetContactsModel.UpLoadSuccess() {
//                    @Override
//                    public void success() {
//                        BPPreferences.instance().putBoolean(PHONE_UPLOAD_KEY , true);
//                        startActivity(new Intent(UpLoadPhoneActivity.this , PhoneActivity.class));
//                        finish();
//                    }
//                });
            }
        });
    }
}
