package com.jopool.crow.imkit.listeners;

import com.jopool.crow.imlib.enums.CWConversationType;

/**
 * Created by wuhk on 2017/4/11.
 */

public interface CWConversationPriorityProvider {

    int getConversationPriority(CWConversationType conversationType , String userId);
}
