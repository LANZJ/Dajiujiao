package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.utils.AuthUtil;
import com.zjyeshi.dajiujiao.R;

/**
 * 联系
 *
 * Created by zhum on 2016/6/17.
 */
public class ContactActivity extends BaseActivity {

    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;//标题栏
    @InjectView(R.id.customerLayout)
    private RelativeLayout customerLayout;//终端客户
    @InjectView(R.id.employeeLayout)
    private RelativeLayout employeeLayout;//内部员工

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_contact);

        initWidgets();
    }

    private void initWidgets(){
        titleLayout.configTitle("联系").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //终端和终端业务员没有该选项
        if (AuthUtil.isShowTerminal()){
            customerLayout.setVisibility(View.VISIBLE);
        }else{
            customerLayout.setVisibility(View.GONE);
        }
        customerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果是boss或者超管，先看业务员列表，点击业务员在查看对应花名册
                if (LoginedUser.getLoginedUser().isMaxLeavel()){
                    Intent intent = new Intent(ContactActivity.this ,MemberListActivity.class);
                    intent.putExtra(HmcActivity.IS_CONTACT , true);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(ContactActivity.this ,HmcActivity.class);
                    intent.putExtra(HmcActivity.IS_CONTACT , true);
                    startActivity(intent);
                }
            }
        });

        employeeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactActivity.this,ChooseEmployeeActivity.class);
                startActivity(intent);
            }
        });
    }
}