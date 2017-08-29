package com.jopool.crow.imkit.activity.group;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.jopool.crow.R;
import com.jopool.crow.imkit.view.CWTitleLayout;

/**
 * 群聊信息
 * Created by wuhk on 2016/11/3.
 */
public class CWGroupDetailActivity extends CWBaseFragmentActivity {
    private CWTitleLayout titleLayout;
    private CWGroupDetailFragment groupDetailFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cw_group_layout_detail);
        titleLayout = (CWTitleLayout) findViewById(R.id.titleLayout);
        groupDetailFragment = (CWGroupDetailFragment) getSupportFragmentManager().findFragmentById(R.id.groupDetailFragment);

        initWidgets();
    }

    private void initWidgets() {
        titleLayout.configTitle("群聊信息").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
