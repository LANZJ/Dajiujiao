package com.zjyeshi.dajiujiao.buyer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.sharepreference.BPPreferences;
import com.xuan.bigappleui.lib.viewpage.BUViewPage;
import com.xuan.bigappleui.lib.viewpage.listeners.OnScrollCompleteListener;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.frame.fragment.MianFramActivity;
import com.zjyeshi.dajiujiao.buyer.activity.login.LoginActivity;
import com.zjyeshi.dajiujiao.buyer.activity.my.work.WorkActivity;
import com.zjyeshi.dajiujiao.buyer.common.Constants;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LoginEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.UserEnum;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 欢迎页
 * Created by wuhk on 2015/11/24.
 */
public class WelcomeActivity extends BaseActivity {
    @InjectView(R.id.viewPage)
    private BUViewPage viewPage;

    private final List<Integer> dataList = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_welcome);
        boolean isFirst = BPPreferences.instance().getBoolean("isFirst" , false);
        if (isFirst){
            if (LoginedUser.getLoginedUser().isLogined()){
                //自动登陆,连接下微信
               ExtraUtil.connectChat(WelcomeActivity.this);

                if (LoginedUser.getLoginedUser().getUserEnum() == UserEnum.SALESMAN){
                    //如果是业务员直接进入工作台
                    Constants.loginEnum = LoginEnum.SELLER;
                    Intent intent = new Intent();
                    intent.setClass(WelcomeActivity.this , WorkActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                 //   startActivity(new Intent(WelcomeActivity.this, FrameActivity.class));
                    startActivity(new Intent(WelcomeActivity.this, MianFramActivity.class));
                    finish();
                }
            }else{
                goFram();
            }
        }else{
            //设置欢迎页
            initWidgets();
        }
    }

    private void initWidgets() {
        dataList.add(R.drawable.welcome_page1);
        dataList.add(R.drawable.welcome_page2);
        dataList.add(R.drawable.welcome_page3);
        dataList.add(R.drawable.welcome_page5);

        // 设置偏移量

        viewPage.setOffset(0);

        // 设置滚动后的监听器
        viewPage.setOnScrollCompleteListener(new OnScrollCompleteListener() {
            @Override
            public void onScrollComplete(int toPosition) {

            }
        });

        viewPage.setAdapter(new BaseAdapter() {
            @Override
            public View getView(int position, View arg1, ViewGroup arg2) {
                ImageView view = (ImageView) LayoutInflater.from(WelcomeActivity.this).inflate(R.layout.layout_viewpage_image, null);
                view.setImageResource(dataList.get(position));
                if (3 == position){
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BPPreferences.instance().putBoolean("isFirst" , true);
                            goFram();
                        }
                    });
                }
                return view;
            }

            @Override
            public long getItemId(int arg0) {
                return 0;
            }

            @Override
            public Object getItem(int arg0) {
                return null;
            }

            @Override
            public int getCount() {
                return dataList.size();
            }
        });
    }

    private void goFram() {
        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
        finish();
    }
}