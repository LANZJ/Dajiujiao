package com.xuan.bigappleui.lib.view.webview.core;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.xuan.bigappleui.lib.view.webview.progress.BUProgress;

/**
 * Created by xuan on 16/2/29.
 */
public class BUWebChromeClient extends WebChromeClient {
    private BUProgress mProgress;

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if(null != mProgress){
            mProgress.updateProgress(newProgress);
            if(newProgress >= 100){
                mProgress.dismiss();
            }
        }
    }

    public BUProgress getProgress() {
        return mProgress;
    }

    public void setProgress(BUProgress progress) {
        this.mProgress = progress;
    }

}
