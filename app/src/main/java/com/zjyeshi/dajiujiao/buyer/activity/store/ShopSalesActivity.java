package com.zjyeshi.dajiujiao.buyer.activity.store;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.task.data.store.homepage.ShopActivity;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.common.PassConstans;
import com.xuan.bigapple.lib.ioc.InjectView;

/**
 * 店铺活动
 * Created by wuhk on 2015/11/30.
 */
public class ShopSalesActivity extends BaseActivity {
    @InjectView(R.id.allLayout)
    private RelativeLayout allLayout;

    @InjectView(R.id.photoIv)
    private ImageView photoIv;

    @InjectView(R.id.activityTv)
    private TextView activityTv;

    @InjectView(R.id.shopNameTv)
    private TextView shopNameTv;

    @InjectView(R.id.activityLayout)
    private LinearLayout activityLayout;

    @InjectView(R.id.linkLayout)
    private RelativeLayout linkLayout;

    private ShopActivity shopActivity;
    private String imageUrl;
    private String shopName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_shop_sales);
        initWidgets();
    }

    private void initWidgets(){
        shopActivity = (ShopActivity)getIntent().getSerializableExtra(PassConstans.SHOPACTIVITY);
        imageUrl = getIntent().getStringExtra("shopPic");
        shopName = getIntent().getStringExtra("shopName");

//        initImageViewDefault(photoIv, imageUrl, R.drawable.default_img);
        GlideImageUtil.glidImage(photoIv , imageUrl , R.drawable.default_img);
        shopNameTv.setText(shopName);

        activityLayout.setClickable(true);
        linkLayout.setClickable(true);

        //跳转连接网页
        linkLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ShopSalesActivity.this , ShopWebActivity.class);
                intent.putExtra("url", shopActivity.getLink());
                startActivity(intent);
            }
        });
        //点击外部消失
        allLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                finish();
                return true;
            }
        });

        if (null != shopActivity.getTitle()){
            activityTv.setText(shopActivity.getTitle());
        }else{
            activityTv.setText("该商店暂无活动");
        }
    }
}
