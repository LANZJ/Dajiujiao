package com.jopool.crow.imkit.listeners.impl;

import com.jopool.crow.imkit.listeners.CWConversationPriorityProvider;
import com.jopool.crow.imlib.enums.CWConversationType;

/**
 * Created by wuhk on 2017/4/11.
 */

public class DefaultConversationPriorityProvider implements CWConversationPriorityProvider {
    @Override
    public int getConversationPriority(CWConversationType conversationType, String userId) {
        return 0;
    }
}
