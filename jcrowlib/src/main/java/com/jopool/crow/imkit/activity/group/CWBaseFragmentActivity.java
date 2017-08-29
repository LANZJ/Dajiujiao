package com.jopool.crow.imkit.activity.group;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.jopool.crow.imkit.utils.CWActivityManager;

/**
 * Created by wuhk on 2016/11/8.
 */
public class CWBaseFragmentActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CWActivityManager.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CWActivityManager.removeActivity(this);
    }
}
