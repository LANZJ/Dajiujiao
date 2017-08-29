package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.os.Bundle;
import android.view.View;

import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;

/**
 * 公司库存 筛选
 *
 * Created by zhum on 2016/6/15.
 */
public class StockScreenActivity extends BaseActivity {

    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;//标题栏

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_stock_screen);

        initWidgets();
    }

    private void initWidgets(){
        titleLayout.configTitle("筛选").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}