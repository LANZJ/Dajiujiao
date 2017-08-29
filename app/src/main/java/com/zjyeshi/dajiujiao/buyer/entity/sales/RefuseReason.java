package com.zjyeshi.dajiujiao.buyer.entity.sales;

/**
 * Created by wuhk on 2017/5/15.
 */

public class RefuseReason {
    private String content;
    private boolean checked;

    public RefuseReason(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
