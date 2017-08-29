package com.jopool.crow.imkit.delegate;

/**
 * Conversation相关的代理
 *
 * Created by xuan on 15/10/28.
 */
public class CustomUIDelegate {
    /**
     * 头像是否圆角
     */
    private boolean headIconCircleCorner = true;

    public boolean isHeadIconCircleCorner() {
        return headIconCircleCorner;
    }

    public void setHeadIconCircleCorner(boolean headIconCircleCorner) {
        this.headIconCircleCorner = headIconCircleCorner;
    }

}
