package com.zjyeshi.dajiujiao.buyer.activity.store;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.utils.LogUtil;

/**
 * 首页轮播图点击网页
 * Created by wuhk on 2015/12/8.
 */
public class CourseWebActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;

    @InjectView(R.id.webView)
    private WebView webView;

    public static final String WEBURL = "web_url";
    public static final String TITLE = "title";

    private String url;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_hompage_web);
        initWidgets();
    }

    private void initWidgets(){

        url = getIntent().getStringExtra(WEBURL);
        title = getIntent().getStringExtra(TITLE);

        LogUtil.e("二维码地址:" + url);

        titleLayout.configReturn( "返回" , new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle(title);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 根据传入的参数再去加载新的网页
                view.loadUrl(url);
                // 表示当前WebView可以处理打开新网页的请求，不用借助系统浏览器
                return true;
            }
        });
        webView.loadUrl(url);
    }

    public static void startWebActivity(Context context , String url , String title){
        Intent intent = new Intent();
        intent.setClass(context , CourseWebActivity.class);
        intent.putExtra(WEBURL , url);
        intent.putExtra(TITLE , title);
        context.startActivity(intent);

    }
}
