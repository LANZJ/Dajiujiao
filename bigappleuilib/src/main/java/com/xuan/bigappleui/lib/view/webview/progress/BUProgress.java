package com.xuan.bigappleui.lib.view.webview.progress;

/**
 * 实现进度条规范接口
 *
 * Created by xuan on 16/2/29.
 */
public interface BUProgress {

    /**
     * 进度值在[1-100]之间
     *
     * @param p
     */
    void updateProgress(int p);

    /**
     * 显示
     */
    void show();

    /**
     * 隐藏
     */
    void dismiss();
}
