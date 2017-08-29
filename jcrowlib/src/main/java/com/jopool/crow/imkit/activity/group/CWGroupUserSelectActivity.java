package com.jopool.crow.imkit.activity.group;

import android.os.Bundle;
import android.view.View;

import com.jopool.crow.R;
import com.jopool.crow.imkit.listeners.CWGroupUserSelectTitleListener;
import com.jopool.crow.imkit.view.CWTitleLayout;
import com.jopool.crow.imlib.enums.CWGroupSelectUserType;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认的用户选择界面
 * Created by wuhk on 2016/11/3.
 */
public class CWGroupUserSelectActivity extends CWBaseFragmentActivity implements CWGroupUserSelectTitleListener {
    private CWTitleLayout titleLayout;
    private CWGroupUserSelectFragment userSelectFragment;

    List mLists = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cw_group_layout_select_user);
        titleLayout = (CWTitleLayout) findViewById(R.id.titleLayout);
        userSelectFragment = (CWGroupUserSelectFragment) getSupportFragmentManager().findFragmentById(R.id.userSelectFragment);

        userSelectFragment.configActivityTitle();

    }

    @Override
    public void configTitle(int type) {
        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        CWGroupSelectUserType selectUserType = CWGroupSelectUserType.valueOf(type);
        if (selectUserType.equals(CWGroupSelectUserType.CREATE)) {
            //新建群聊
            titleLayout.configTitle("发起群聊").configRightText("确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  userSelectFragment.createGroup();
                }
            });
        } else if (selectUserType.equals(CWGroupSelectUserType.ADD)) {
            //添加成员进群
            titleLayout.configTitle("选择添加人").configRightText("确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userSelectFragment.addUserToGroupNotCreate();
                    CWGroupUserSelectActivity.this.finish();
                }
            });
        }
        else if (selectUserType.equals(CWGroupSelectUserType.REMOVE)) {
            //移除群成员
            titleLayout.configTitle("聊天成员").configRightText("移除", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userSelectFragment.removeUserFromGroup();

                }
            });
        }
    }
}
