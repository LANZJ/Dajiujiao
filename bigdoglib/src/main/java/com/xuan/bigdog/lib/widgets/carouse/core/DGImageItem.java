package com.xuan.bigdog.lib.widgets.carouse.core;

import android.view.View;

/**
 * 图片对象
 *
 * Created by xuan on 15/9/16.
 */
public class DGImageItem {
    private String url;//图片下载地址
    private View.OnClickListener onClickListener;//点击事件

    public DGImageItem(String url){
        this.url = url;
    }

    public DGImageItem(String url, View.OnClickListener l){
        this.url = url;
        this.onClickListener = l;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
