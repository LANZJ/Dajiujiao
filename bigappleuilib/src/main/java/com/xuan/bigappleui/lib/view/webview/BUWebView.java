package com.xuan.bigappleui.lib.view.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.xuan.bigappleui.lib.view.webview.core.BUWebChromeClient;
import com.xuan.bigappleui.lib.view.webview.core.BUWebViewClient;
import com.xuan.bigappleui.lib.view.webview.jscall.JsCallback;
import com.xuan.bigappleui.lib.view.webview.progress.BUProgress;

/**
 * 自定义一个WebView，方便实用
 *
 * @author xuan
 */
public class BUWebView extends WebView {
    private JsCallback mJsCallback;

    private BUWebViewClient mWebViewClient;
    private BUWebChromeClient mWebChromeClient;

    public BUWebView(Context context) {
        super(context);
        init();
    }

    public BUWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BUWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        initWebView();
    }

    private void initWebView() {
        /** 滚动条在页面之上,这样不会留空白边 */
        setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

        WebSettings webSettings = getSettings();
        /** 支持缩放 */
        webSettings.setSupportZoom(true);
        /** 隐藏缩放按钮 */
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        /** 支持JS执行 */
        webSettings.setJavaScriptEnabled(true);
        /** 设置点击页面上的连接时，使用原webView显示（默认启动第三方浏览器显示） */
        mWebViewClient = new BUWebViewClient();
        setWebViewClient(mWebViewClient);
        /** 获取页面标题title、加载进度设置 */
        mWebChromeClient = new BUWebChromeClient();
        setWebChromeClient(mWebChromeClient);
    }

    /**
     * 设置JS调用回调
     *
     * @param jsCallback
     */
    public void setBuJsCallback(JsCallback jsCallback) {
        mWebViewClient.setJsCallback(jsCallback);
        this.mJsCallback = jsCallback;
    }

    /**
     * 设置ChromeClient
     *
     * @param buWebChromeClient
     */
    public void setBuWebChromeClient(BUWebChromeClient buWebChromeClient) {
        this.mWebChromeClient = buWebChromeClient;
        setWebChromeClient(this.mWebChromeClient);
    }

    /**
     * 设置ViewClient
     *
     * @param buWebViewClient
     */
    public void setBuWebViewClient(BUWebViewClient buWebViewClient) {
        if (null != buWebViewClient) {
            buWebViewClient.setJsCallback(mJsCallback);
        }
        this.mWebViewClient = buWebViewClient;
        setWebViewClient(this.mWebViewClient);
    }

    /**
     * 设置进度条
     *
     * @param progress
     */
    public void setBuProgress(BUProgress progress) {
        this.mWebViewClient.setProgress(progress);
        this.mWebChromeClient.setProgress(progress);
    }

    /**
     * 调用JS
     *
     * @param message
     */
    public void callJs(String message) {
        loadUrl("javascript:beCalledByBigappleui('" + message + "');");
    }

    /**
     * 调用JS的方法
     *
     * @param method
     * @param message
     */
    public void callJs(String method, String message) {
        loadUrl("javascript:" + method + "('" + message + "');");
    }

}
