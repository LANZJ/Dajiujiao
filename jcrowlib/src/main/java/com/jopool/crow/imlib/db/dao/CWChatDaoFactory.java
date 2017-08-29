package com.jopool.crow.imlib.db.dao;

/**
 * Dao工厂类
 *
 * @author xuan
 */
public class CWChatDaoFactory {
    private final static CWConversationDao conversationDao = new CWConversationDao();
    private final static CWConversationMessageDao conversationMessageDao = new CWConversationMessageDao();
    private final static CWCacheUserDao cacheUserDao = new CWCacheUserDao();
    private final static CWCacheGroupDao cacheGroupDao = new CWCacheGroupDao();

    public static CWConversationDao getConversationDao() {
        return conversationDao;
    }

    public static CWConversationMessageDao getConversationMessageDao() {
        return conversationMessageDao;
    }

    public static CWCacheUserDao getCacheUserDao() {
        return cacheUserDao;
    }

    public static CWCacheGroupDao getCacheGroupDao() {
        return cacheGroupDao;
    }
}
