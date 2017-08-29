package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.utils.AuthUtil;

/**
 * 库存
 *
 * Created by zhum on 2016/6/15.
 */
public class StockActivity extends BaseActivity {

    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;//标题栏
    @InjectView(R.id.companyLayout)
    private RelativeLayout companyLayout;//公司库存
    @InjectView(R.id.khLayout)
    private RelativeLayout khLayout;//客户库存

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_kc);

        initWidgets();
    }

    private void initWidgets(){
        titleLayout.configTitle("库存").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //终端业务员和终端只显示公司库存
        if (AuthUtil.customerStockShow()){
            khLayout.setVisibility(View.VISIBLE);
        }else{
            khLayout.setVisibility(View.GONE);
        }

        //公司库存
        companyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StockActivity.this,CompanyStockActivity.class);
                startActivity(intent);
            }
        });

        //客户库存
        khLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StockActivity.this, CustomerStockActivity.class);
                startActivity(intent);
            }
        });
    }
}