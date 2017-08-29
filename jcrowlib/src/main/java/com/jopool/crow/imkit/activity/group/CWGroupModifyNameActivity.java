package com.jopool.crow.imkit.activity.group;

import android.os.Bundle;
import android.view.View;

import com.jopool.crow.R;
import com.jopool.crow.imkit.view.CWTitleLayout;

/**
 * 修改群聊名称
 * Created by wuhk on 2016/11/3.
 */
public class CWGroupModifyNameActivity extends CWBaseFragmentActivity {
    private CWTitleLayout titleLayout;
    private CWGroupModifyNameFragment groupModifyNameFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cw_group_layout_modify_name);
        titleLayout = (CWTitleLayout) findViewById(R.id.titleLayout);
        groupModifyNameFragment = (CWGroupModifyNameFragment) getSupportFragmentManager().findFragmentById(R.id.groupModifyNameFragment);

        initWidget();
    }

    private void initWidget() {
        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle("群聊名称").configRightText("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupModifyNameFragment.saveGroupName();
            }
        });
    }
}
