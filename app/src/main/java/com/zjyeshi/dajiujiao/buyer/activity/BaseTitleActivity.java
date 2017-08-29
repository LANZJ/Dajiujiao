package com.zjyeshi.dajiujiao.buyer.activity;

import com.zjyeshi.dajiujiao.R;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;

/**
 * 带有标题栏的界面
 * <p/>
 * Created by xuan on 15/10/29.
 */
public class BaseTitleActivity extends BaseActivity {
    protected DGTitleLayout titleLayout;

    @Override
    public void setContentView(int i) {
        super.setContentView(i);
        titleLayout = (DGTitleLayout) findViewById(R.id.titleLayout);
    }

}
