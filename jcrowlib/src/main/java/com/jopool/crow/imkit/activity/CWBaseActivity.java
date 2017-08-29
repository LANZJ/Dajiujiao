package com.jopool.crow.imkit.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.jopool.crow.imkit.utils.CWActivityManager;

/**
 * 所有类的基类
 *
 * @author xuan
 */
public abstract class CWBaseActivity extends Activity {

    /**
     * 找到View
     *
     * @param resid
     * @return
     */
    protected View F(int resid) {
        return findViewById(resid);
    }


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
