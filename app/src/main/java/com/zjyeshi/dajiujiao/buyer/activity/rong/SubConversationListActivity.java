package com.zjyeshi.dajiujiao.buyer.activity.rong;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.frame.BaseFrameActivity;
import com.zjyeshi.dajiujiao.buyer.activity.rong.ap.SubConversationListAdapterEx;

import io.rong.imkit.RongContext;
import io.rong.imkit.fragment.SubConversationListFragment;

/**
 * Created by Bob on 15/11/3.
 * 聚合会话列表
 */
public class SubConversationListActivity extends BaseFrameActivity {
    private DGTitleLayout titleLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rong);
        titleLayout= (DGTitleLayout) findViewById(R.id.titleLayout);

        SubConversationListFragment fragment = new SubConversationListFragment();
        fragment.setAdapter(new SubConversationListAdapterEx(RongContext.getInstance()));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.rong_content, fragment);
        transaction.commit();

        Intent intent = getIntent();
        if (intent.getData() == null) {
            return;
        }
        //聚合会话参数
        String type = intent.getData().getQueryParameter("type");

        if (type == null)
            return;

        if (type.equals("group")) {
            setTitle(R.string.de_actionbar_sub_group);
            titleLayout.configTitle("群组");
        } else if (type.equals("private")) {
            setTitle(R.string.de_actionbar_sub_private);
            titleLayout.configTitle("我的私人会话");
        } else if (type.equals("discussion")) {
            setTitle(R.string.de_actionbar_sub_discussion);
            titleLayout.configTitle("我的讨论组");
        } else if (type.equals("system")) {
            setTitle(R.string.de_actionbar_sub_system);
            titleLayout.configTitle("系统消息");
        } else {
            setTitle(R.string.de_actionbar_sub_defult);
            titleLayout.configTitle("聊天");
        }
    }
}
