package com.jopool.crow.imapi;

import com.jopool.crow.CWChat;
import com.jopool.crow.imkit.listeners.GetConversationTitleRightViewProvider;
import com.jopool.crow.imkit.listeners.TopNoticeProvider;

/**
 * UI定制
 * <p/>
 * Created by xuan on 16/11/7.
 */
public class CWApiUi {

    /**
     * 设置头像是否圆角
     *
     * @param isCircleCorner 是表示使用圆角,否者使用直角
     * @return
     */
    public CWApiUi setHeadIconCircleCorner(boolean isCircleCorner) {
        CWChat.getInstance().getCustomUIDelegate().setHeadIconCircleCorner(isCircleCorner);
        return this;
    }

    /**
     * 设置聊天界面右边布局
     *
     * @param provider
     */
    public CWApiUi setCVTitleRightViewProvider(GetConversationTitleRightViewProvider provider){
        CWChat.getInstance().getProviderDelegate().setGetConversationTitleRightViewProvider(provider);
        return this;
    }

    /**
     * 设置聊天界面顶部通知提示布局
     *
     * @param provider
     */
    public CWApiUi setCVTopNoticeProvider(TopNoticeProvider provider){
        CWChat.getInstance().getProviderDelegate().setTopNoticeProvider(provider);
        return this;
    }

}
