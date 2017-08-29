package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.R;

/**
 * 选择付款方式
 *
 * Created by zhum on 2016/6/22.
 */
public class ChosePayWayActivity extends BaseActivity {

    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;//标题栏
    @InjectView(R.id.layout1)
    private RelativeLayout layout1;//现金
    @InjectView(R.id.layout2)
    private RelativeLayout layout2;//打卡

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chose_pay_way);

        initWidgets();
    }

    private void initWidgets() {
        titleLayout.configTitle("联系").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //数据是使用Intent返回
                Intent intent = new Intent();
                //把返回数据存入Intent
                intent.putExtra("PayWay", "1");
                //设置返回数据
                ChosePayWayActivity.this.setResult(RESULT_OK, intent);
                //关闭Activity
                ChosePayWayActivity.this.finish();
            }
        });

        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //数据是使用Intent返回
                Intent intent = new Intent();
                //把返回数据存入Intent
                intent.putExtra("PayWay", "2");
                //设置返回数据
                ChosePayWayActivity.this.setResult(RESULT_OK, intent);
                //关闭Activity
                ChosePayWayActivity.this.finish();
            }
        });
    }
}