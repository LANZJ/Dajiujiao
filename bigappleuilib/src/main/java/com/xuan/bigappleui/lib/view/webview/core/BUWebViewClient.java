package com.xuan.bigappleui.lib.view.webview.core;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xuan.bigappleui.lib.view.webview.jscall.JsCall;
import com.xuan.bigappleui.lib.view.webview.jscall.JsCallback;
import com.xuan.bigappleui.lib.view.webview.progress.BUProgress;

/**
 * Created by xuan on 16/2/29.
 */
public class BUWebViewClient extends WebViewClient {
    private JsCallback mJsCallback;
    private BUProgress mProgress;

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        JsCall.call(view, url, mJsCallback);
        return true;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if(null != mProgress){
            mProgress.show();
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if(null != mProgress){
            mProgress.dismiss();
        }
    }

    public JsCallback getJsCallback() {
        return mJsCallback;
    }

    public void setJsCallback(JsCallback jsCallback) {
        this.mJsCallback = jsCallback;
    }

    public BUProgress getProgress() {
        return mProgress;
    }

    public void setProgress(BUProgress progress) {
        this.mProgress = progress;
    }

}
