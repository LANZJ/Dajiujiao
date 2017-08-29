package com.jopool.crow.imlib.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jopool.crow.CWChat;
import com.jopool.crow.imlib.db.core.CWMultiRowMapper;
import com.jopool.crow.imlib.db.core.CWSingleRowMapper;
import com.jopool.crow.imlib.entity.CWConversation;
import com.jopool.crow.imlib.entity.CWConversationMessage;
import com.jopool.crow.imlib.entity.CWGroup;
import com.jopool.crow.imlib.entity.CWUser;
import com.jopool.crow.imlib.enums.CWConversationType;
import com.jopool.crow.imlib.enums.CWReadStatus;
import com.jopool.crow.imlib.utils.CWDateUtil;
import com.jopool.crow.imlib.utils.CWLogUtil;
import com.jopool.crow.imlib.utils.CWValidator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 会话Dao
 *
 * @author xuan
 */
public class CWConversationDao extends CWBaseDao {
    //select
    private static final String SQL_FIND_BY_TOID = "SELECT * FROM cw_conversation WHERE to_id=? AND owner_user_id=?";
    private static final String SQL_FIND_ALL = "SELECT dc.*, "
            + "(SELECT count(1) FROM cw_conversation_message dcm WHERE "
            + "dcm.owner_user_id=dc.owner_user_id AND dc.conversation_type=dcm.conversation_type AND dc.to_id=dcm.conversation_to_id AND dcm.read_status=?) num "
            + "FROM cw_conversation dc WHERE dc.owner_user_id=? ORDER BY dc.priority DESC, dc.modify_time DESC";

    //insert
    private static final String SQL_INSERT = "INSERT INTO cw_conversation(to_id,conversation_type,owner_user_id,priority,ext,modify_time,creation_time) VALUES(?,?,?,?,?,?,?)";

    //update
    private static final String SQL_UPDATE_PRIORITY = "UPDATE cw_conversation SET priority=?, modify_time=? WHERE owner_user_id=? AND to_id=?";
    private static final String SQL_UPDATE_MODIFYTIME = "UPDATE cw_conversation SET modify_time=? WHERE owner_user_id=? AND to_id=?";

    //delete
    private static final String SQL_DELETE_BY_TOID = "DELETE FROM cw_conversation WHERE to_id=? AND owner_user_id=?";



    /**
     * 查询--会话记录
     *
     * @param toId
     * @return
     */
    public CWConversation findByToId(String toId) {
        String ownerUserId = getOwnerUserId();
        if (CWValidator.isEmpty(ownerUserId) || CWValidator.isEmpty(toId)) {
            return null;
        }

        return bpQuery(SQL_FIND_BY_TOID, new String[]{toId, ownerUserId},
                new MSingleRowMapper());
    }

    /**
     * 查找--所有会话列表
     *
     * @return
     */
    public List<CWConversation> findAllDGConversation() {
        String ownerUserId = getOwnerUserId();
        if (CWValidator.isEmpty(ownerUserId)) {
            return new ArrayList<CWConversation>();
        }

        List<CWConversation> ret = new ArrayList<CWConversation>();
        SQLiteDatabase sqliteDatabase = null;

        try {
            sqliteDatabase = openSQLiteDatabase();

            Cursor cursor = sqliteDatabase.rawQuery(SQL_FIND_ALL, new String[]{
                    CWReadStatus.UNREAD.getValueStr(), ownerUserId});
            while (cursor.moveToNext()) {
                CWConversation conversation = new MSingleRowMapper()
                        .mapRow(cursor);
                conversation.setUnreadNum(cursor.getInt(cursor
                        .getColumnIndex("num")));
                ret.add(conversation);
            }
            cursor.close();

        } catch (Exception e) {
            CWLogUtil.e(e.getMessage(), e);
        } finally {
            closeSQLiteDatabase();// 关闭数据库连接
        }

        //设置会话对方信息和最后一条消息
        for (CWConversation conversation : ret) {
            // 如果是单独界面，查找被聊天人的名字
            if (conversation.isUser()) {
                CWUser user = CWChat.getInstance().getGetUserInfoProvider()
                        .getUserById(conversation.getToId());
                conversation.setToUser(user);
            }
            // 如果是群组聊天，查找群组的名称
            else if (conversation.isGroup()) {
                CWGroup group = CWChat.getInstance().getProviderDelegate().getGetGroupInfoProvider()
                        .getGroupById(conversation.getToId());
                conversation.setToGroup(group);
            }

            // 设置会话中最近一条消息
            CWConversationMessage message = CWChatDaoFactory
                    .getConversationMessageDao()
                    .findLastMessageByConversationToId(
                            conversation.getToId());
            conversation.setLastMessage(message);
        }

        return ret;
    }

    /**
     * 插入
     *
     * @param conversaion
     */
    public void insert(CWConversation conversaion) {
        if (null == conversaion) {
            return;
        }

        bpUpdate(
                SQL_INSERT,
                new Object[]{
                        conversaion.getToId(),
                        conversaion.getConversationType().getValue(),
                        conversaion.getOwnerUserId(),
                        conversaion.getPriority(),
                        conversaion.getExt(),
                        CWDateUtil.date2StringBySecond(conversaion
                                .getModifyTime()),
                        CWDateUtil.date2StringBySecond(conversaion
                                .getCreationTime())});
    }


    /**
     * 修改--设置会话的优先级
     *
     * @param toId
     * @param priority
     */
    public void updatePriority(String toId, int priority) {
        if (CWValidator.isEmpty(toId)) {
            return;
        }

        String ownerUserId = getOwnerUserId();
        bpUpdate(SQL_UPDATE_PRIORITY, new String[]{String.valueOf(priority), CWDateUtil.date2StringBySecond(new Date()), ownerUserId, toId});
    }

    /**
     * 修改--更新修改时间
     *
     * @param toId
     * @param modifyTime
     */
    public void updateModify(String toId, Date modifyTime) {
        if (CWValidator.isEmpty(toId) || null == modifyTime) {
            return;
        }
        //
        String ownerUserId = getOwnerUserId();
        String[] PARAMS = new String[]{CWDateUtil.date2StringBySecond(new Date()), ownerUserId, toId};
        bpUpdate(SQL_UPDATE_MODIFYTIME, PARAMS);
    }


    /**
     * 根据toId删除会话
     *
     * @param toId
     */
    public void deleteByToId(String toId) {
        String ownerUserId = getOwnerUserId();
        if (CWValidator.isEmpty(ownerUserId) || CWValidator.isEmpty(toId)) {
            return;
        }
        bpUpdate(SQL_DELETE_BY_TOID, new String[]{toId, ownerUserId});
    }

    /**
     * 单条结果集
     *
     * @author xuan
     */
    private static class MSingleRowMapper implements
            CWSingleRowMapper<CWConversation> {
        @Override
        public CWConversation mapRow(Cursor cursor) throws SQLException {
            return new MMultiRowMapper().mapRow(cursor, 0);
        }
    }

    /**
     * 多条结果集
     *
     * @author xuan
     * @version $Revision: 1.0 $, $Date: 2014-3-25 下午2:29:46 $
     */
    private static class MMultiRowMapper implements
            CWMultiRowMapper<CWConversation> {
        @Override
        public CWConversation mapRow(Cursor cursor, int n) throws SQLException {
            CWConversation conversaion = new CWConversation();
            conversaion.setToId(cursor.getString(cursor
                    .getColumnIndex(CWConversation.TO_ID)));
            conversaion
                    .setConversationType(CWConversationType.valueOf(cursor.getInt(cursor
                            .getColumnIndex(CWConversation.CONVERSATION_TYPE))));
            conversaion.setOwnerUserId(cursor.getString(cursor
                    .getColumnIndex(CWConversation.OWNER_USER_ID)));
            conversaion.setPriority(cursor.getInt(cursor
                    .getColumnIndex(CWConversation.PRIORITY)));
            conversaion.setExt(cursor.getString(cursor
                    .getColumnIndex(CWConversation.EXT)));
            conversaion.setModifyTime(CWDateUtil.string2DateTime(cursor
                    .getString(cursor
                            .getColumnIndex(CWConversation.MODIFY_TIME))));
            conversaion.setCreationTime(CWDateUtil.string2DateTime(cursor
                    .getString(cursor
                            .getColumnIndex(CWConversation.CREATION_TIME))));
            return conversaion;
        }
    }

}
