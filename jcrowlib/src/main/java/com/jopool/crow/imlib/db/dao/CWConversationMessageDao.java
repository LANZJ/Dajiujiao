package com.jopool.crow.imlib.db.dao;

import android.database.Cursor;

import com.jopool.crow.imlib.db.core.CWMultiRowMapper;
import com.jopool.crow.imlib.db.core.CWSingleRowMapper;
import com.jopool.crow.imlib.entity.CWConversationMessage;
import com.jopool.crow.imlib.enums.CWConversationType;
import com.jopool.crow.imlib.enums.CWMessageContentType;
import com.jopool.crow.imlib.enums.CWMessageType;
import com.jopool.crow.imlib.enums.CWReadStatus;
import com.jopool.crow.imlib.enums.CWSendStatus;
import com.jopool.crow.imlib.utils.CWDateUtil;
import com.jopool.crow.imlib.utils.CWValidator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 聊天消息dao操作类
 *
 * @author xuan
 */
public class CWConversationMessageDao extends CWBaseDao {
    //select
    private static final String SQL_FIND_BY_ID = "SELECT * FROM cw_conversation_message WHERE id=? AND owner_user_id=?";
    private static final String SQL_FIND_LAST_MESSAGE_FOR_MESSAGETYPE = "SELECT * FROM cw_conversation_message WHERE owner_user_id=? AND conversation_to_id=? AND message_type=? ORDER BY modify_time DESC LIMIT 0,1";
    private static final String SQL_FIND_LAST_MESSAGE = "SELECT * FROM cw_conversation_message WHERE owner_user_id=? AND conversation_to_id=? ORDER BY modify_time DESC LIMIT 0,1";
    private static final String SQL_COUNT_UNREAD_NUM_BY_CONVERSATIONTOID = "SELECT count(*) num FROM cw_conversation_message WHERE owner_user_id=? AND conversation_to_id=? AND read_status=?";
    private static final String SQL_FIND_BY_CONVERSATIONTOID = "SELECT * FROM cw_conversation_message WHERE owner_user_id=? AND conversation_to_id=?";
    private static final String SQL_TOTAL_UNREAD_NUM = "SELECT count(*) num FROM cw_conversation_message WHERE owner_user_id=? AND read_status=?";
    private static final String SQL_FIND_UNREAD_MESSAGE_BY_CONVERSATIONTOID = "SELECT * FROM cw_conversation_message WHERE owner_user_id=? AND conversation_to_id=? AND read_status=?";

    //insert
    private static final String SQL_INSERT_OR_IGNORE = "INSERT OR IGNORE INTO cw_conversation_message(id,owner_user_id,sender_user_id,"
            + "conversation_type,conversation_to_id,message_type,message_content_type,content,download_url,voice_length,read_status,"
            + "send_status,ext,modify_time,creation_time) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    private static final String SQL_INSERT_OR_REPLACE = "INSERT OR REPLACE INTO cw_conversation_message(id,owner_user_id,sender_user_id,"
            + "conversation_type,conversation_to_id,message_type,message_content_type,content,download_url,voice_length,read_status,"
            + "send_status,ext,modify_time,creation_time) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    //update
    private static final String SQL_UPDATE_READSTATUS_BY_TOID_AND_READSTATUS = "UPDATE cw_conversation_message SET read_status=? WHERE owner_user_id=? AND conversation_to_id=? AND read_status=?";
    private static final String SQL_UPDATE_READED_BY_ID = "UPDATE cw_conversation_message SET read_status=? WHERE id=? AND owner_user_id=?";
    private static final String SQL_UPDATE_READED_BY_ID_WITH_UNREAD = "UPDATE cw_conversation_message SET read_status=? WHERE id=? AND owner_user_id=? AND read_status=2";
    private static final String SQL_UPDATE_CONTENT_BY_ID = "UPDATE cw_conversation_message SET content=? WHERE id=? AND owner_user_id=?";
    private static final String SQL_UPDATE_SENDSTATUS_BY_ID = "UPDATE cw_conversation_message SET send_status=? WHERE id=? AND owner_user_id=?";

    //delete
    private static final String SQL_DELETE_BY_ID = "DELETE FROM cw_conversation_message WHERE id=? AND owner_user_id=?";
    private static final String SQL_DELETE_BY_CONVERSATIONTOID = "DELETE FROM cw_conversation_message WHERE owner_user_id=? AND conversation_to_id=?";

    /**
     * 查找--消息记录
     *
     * @param id
     * @return
     */
    public CWConversationMessage findById(String id) {
        String ownerUserId = getOwnerUserId();
        if (CWValidator.isEmpty(id) || CWValidator.isEmpty(ownerUserId)) {
            return null;
        }

        return bpQuery(SQL_FIND_BY_ID, new String[]{id, ownerUserId},
                new MSingleRowMapper());
    }

    /**
     * 查询--会话左边的最后一条消息(用来加载最新消息,所以删除的消息也需要算在内的)
     *
     * @param conversationToId
     * @return
     */
    public CWConversationMessage findLastMessageByConversationToIdForLeft(
            String conversationToId) {
        String ownerUserId = getOwnerUserId();
        if (CWValidator.isEmpty(conversationToId)
                || CWValidator.isEmpty(ownerUserId)) {
            return null;
        }

        return bpQuery(SQL_FIND_LAST_MESSAGE_FOR_MESSAGETYPE, new String[]{ownerUserId,
                conversationToId, String.valueOf(CWMessageType.LEFT.getValue())}, new MSingleRowMapper());
    }

    /**
     * 查询--一个会话的最后一条消息
     *
     * @param conversationToId
     * @return
     */
    public CWConversationMessage findLastMessageByConversationToId(
            String conversationToId) {
        String ownerUserId = getOwnerUserId();
        if (CWValidator.isEmpty(conversationToId)
                || CWValidator.isEmpty(ownerUserId)) {
            return null;
        }

        return bpQuery(SQL_FIND_LAST_MESSAGE, new String[]{ownerUserId,
                conversationToId}, new MSingleRowMapper());
    }

    /**
     * 查询--获取会话中的未读消息
     *
     * @param conversationToId
     * @return
     */
    public int countUnreadNumByConversationToId(String conversationToId) {
        String ownerUserId = getOwnerUserId();
        if (CWValidator.isEmpty(conversationToId)
                || CWValidator.isEmpty(ownerUserId)) {
            return 0;
        }

        return bpQuery(SQL_COUNT_UNREAD_NUM_BY_CONVERSATIONTOID,
                new String[]{ownerUserId, conversationToId,
                        CWReadStatus.UNREAD.getValueStr()},
                new CWSingleRowMapper<Integer>() {
                    @Override
                    public Integer mapRow(Cursor cursor) throws SQLException {
                        return cursor.getInt(cursor.getColumnIndex("num"));
                    }
                });
    }

    /**
     * 查找--所有未读消息
     *
     * @return
     */
    public int countTotalUnreadNum() {
        String ownerUserId = getOwnerUserId();
        if (CWValidator.isEmpty(ownerUserId)) {
            return 0;
        }

        return bpQuery(SQL_TOTAL_UNREAD_NUM, new String[]{ownerUserId,
                        CWReadStatus.UNREAD.getValueStr()},
                new CWSingleRowMapper<Integer>() {
                    @Override
                    public Integer mapRow(Cursor cursor) throws SQLException {
                        return cursor.getInt(cursor.getColumnIndex("num"));
                    }
                });
    }

    /**
     * 查找--所有未读消息
     *
     * @return
     */
    public List<CWConversationMessage> findUnReadMessage(
            String conversationToId) {
        String ownerUserId = getOwnerUserId();
        if (CWValidator.isEmpty(conversationToId)
                || CWValidator.isEmpty(ownerUserId)) {
            return new ArrayList<CWConversationMessage>();
        }

        return bpQuery(SQL_FIND_UNREAD_MESSAGE_BY_CONVERSATIONTOID, new String[]{
                ownerUserId, conversationToId , CWReadStatus.UNREAD.getValueStr()}, new MMultiRowMapper());
    }


    /**
     * 查询--会话中的消息
     *
     * @param conversationToId
     * @return
     */
    public List<CWConversationMessage> findByConversationToId(
            String ownerUserId, String conversationToId) {
        if (CWValidator.isEmpty(ownerUserId)
                || CWValidator.isEmpty(conversationToId)) {
            return new ArrayList<CWConversationMessage>();
        }

        return bpQuery(SQL_FIND_BY_CONVERSATIONTOID, new String[]{
                ownerUserId, conversationToId}, new MMultiRowMapper());
    }

    /**
     * 插入或忽略--单条记录
     *
     * @param message
     */
    public void insert(CWConversationMessage message) {
        if (null == message) {
            return;
        }

        bpUpdate(
                SQL_INSERT_OR_IGNORE,
                new Object[]{
                        message.getId(),
                        message.getOwnerUserId(),
                        message.getSenderUserId(),
                        message.getConversationType().getValue(),
                        message.getConversationToId(),
                        message.getMessageType().getValue(),
                        message.getMessageContentType().getValue(),
                        message.getContent(),
                        message.getDownloadUrl(),
                        message.getVoiceLength(),
                        message.getReadStatus().getValue(),
                        message.getSendStatus().getValue(),
                        message.getExt(),
                        CWDateUtil.date2StringBySecond(message.getModifyTime()),
                        CWDateUtil.date2StringBySecond(message.getCreationTime())});
    }


    /**
     * 插入或替换--单条记录
     *
     * @param message
     */
    public void insertOrReplace(CWConversationMessage message) {
        if (null == message) {
            return;
        }

        bpUpdate(
                SQL_INSERT_OR_REPLACE,
                new Object[]{
                        message.getId(),
                        message.getOwnerUserId(),
                        message.getSenderUserId(),
                        message.getConversationType().getValue(),
                        message.getConversationToId(),
                        message.getMessageType().getValue(),
                        message.getMessageContentType().getValue(),
                        message.getContent(),
                        message.getDownloadUrl(),
                        message.getVoiceLength(),
                        message.getReadStatus().getValue(),
                        message.getSendStatus().getValue(),
                        message.getExt(),
                        CWDateUtil.date2StringBySecond(message.getModifyTime()),
                        CWDateUtil.date2StringBySecond(message.getCreationTime())});
    }

    /**
     * 更新--设置所有UNREAD为READED
     *
     * @param conversationToId
     */
    public void updateAllUnReadedStatusToReadedStatusByConversationToId(
            String conversationToId) {
        String ownerUserId = getOwnerUserId();
        if (CWValidator.isEmpty(conversationToId)
                || CWValidator.isEmpty(ownerUserId)) {
            return;
        }
        bpUpdate(
                SQL_UPDATE_READSTATUS_BY_TOID_AND_READSTATUS,
                new String[]{CWReadStatus.READED.getValueStr(), ownerUserId,
                        conversationToId,
                        CWReadStatus.UNREAD.getValueStr()});
    }

    /**
     * 更新--修改消息未读标识位
     *
     * @param id
     * @param readStatus
     */
    public void updateReadStatusById(String id, CWReadStatus readStatus) {
        String ownerUserId = getOwnerUserId();
        if (CWValidator.isEmpty(id) || CWValidator.isEmpty(ownerUserId) || null == readStatus) {
            return;
        }

        bpUpdate(SQL_UPDATE_READED_BY_ID,
                new String[]{readStatus.getValueStr(), id,
                        ownerUserId});
    }

    /**
     * 更新--修改消息未读标识位
     *
     * @param id
     * @param readStatus
     */
    public void updateReadStatusByIdWidthUnRead(String id, CWReadStatus readStatus) {
        String ownerUserId = getOwnerUserId();
        if (CWValidator.isEmpty(id) || CWValidator.isEmpty(ownerUserId) || null == readStatus) {
            return;
        }

        bpUpdate(SQL_UPDATE_READED_BY_ID_WITH_UNREAD,
                new String[]{readStatus.getValueStr(), id,
                        ownerUserId});
    }

    /**
     * 更新--content内容
     *
     * @param id
     * @param content
     */
    public void updateContentById(String id, String content) {
        String ownerUserId = getOwnerUserId();
        if (CWValidator.isEmpty(ownerUserId) || CWValidator.isEmpty(id)) {
            return;
        }

        bpUpdate(SQL_UPDATE_CONTENT_BY_ID, new Object[]{content, id,
                ownerUserId});
    }

    /**
     * 更新--修改发送状态
     *
     * @param id
     */
    public void updateSendStatusById(String id, int sendStatus) {
        String ownerUserId = getOwnerUserId();
        if (CWValidator.isEmpty(ownerUserId) || CWValidator.isEmpty(id)) {
            return;
        }

        bpUpdate(SQL_UPDATE_SENDSTATUS_BY_ID, new Object[]{sendStatus, id,
                ownerUserId});
    }

    /**
     * 删除--某个会话中的所有消息
     *
     * @param conversationToId
     */
    public void deleteByConversationToId(String conversationToId) {
        String ownerUserId = getOwnerUserId();
        if (CWValidator.isEmpty(conversationToId)
                || CWValidator.isEmpty(ownerUserId)) {
            return;
        }

        bpUpdate(SQL_DELETE_BY_CONVERSATIONTOID, new String[]{ownerUserId,
                conversationToId});
    }

    /**
     * 删除--指定id的消息
     *
     * @param id
     */
    public void deleteById(String id) {
        String ownerUserId = getOwnerUserId();
        if (CWValidator.isEmpty(id) || CWValidator.isEmpty(ownerUserId)) {
            return;
        }

        bpUpdate(SQL_DELETE_BY_ID, new String[]{id, ownerUserId});
    }

    /**
     * 单条结果集
     *
     * @author xuan
     */
    public static class MSingleRowMapper implements
            CWSingleRowMapper<CWConversationMessage> {
        @Override
        public CWConversationMessage mapRow(Cursor cursor) throws SQLException {
            return new MMultiRowMapper().mapRow(cursor, 0);
        }
    }

    /**
     * 多条结果集
     *
     * @author xuan
     * @version $Revision: 1.0 $, $Date: 2014-3-25 下午2:29:46 $
     */
    public static class MMultiRowMapper implements
            CWMultiRowMapper<CWConversationMessage> {
        @Override
        public CWConversationMessage mapRow(Cursor cursor, int n)
                throws SQLException {
            CWConversationMessage m = new CWConversationMessage();
            m.setId(cursor.getString(cursor
                    .getColumnIndex(CWConversationMessage.ID)));
            m.setOwnerUserId(cursor.getString(cursor
                    .getColumnIndex(CWConversationMessage.OWNER_USER_ID)));
            m.setSenderUserId(cursor.getString(cursor
                    .getColumnIndex(CWConversationMessage.SENDER_USER_ID)));
            m.setConversationType(CWConversationType.valueOf(cursor.getInt(cursor
                    .getColumnIndex(CWConversationMessage.CONVERSATION_TYPE))));
            m.setConversationToId(cursor.getString(cursor
                    .getColumnIndex(CWConversationMessage.CONVERSATION_TO_ID)));
            m.setMessageType(CWMessageType.valueOf(cursor.getInt(cursor
                    .getColumnIndex(CWConversationMessage.MESSAGE_TYPE))));
            m.setMessageContentType(CWMessageContentType.valueOf(cursor.getInt(cursor
                    .getColumnIndex(CWConversationMessage.MESSAGE_CONTENT_TYPE))));
            m.setContent(cursor.getString(cursor
                    .getColumnIndex(CWConversationMessage.CONTENT)));
            m.setDownloadUrl(cursor.getString(cursor
                    .getColumnIndex(CWConversationMessage.DOWNLOAD_URL)));
            m.setVoiceLength(cursor.getInt(cursor
                    .getColumnIndex(CWConversationMessage.VOICE_LENGTH)));
            m.setReadStatus(CWReadStatus.valueOf(cursor.getInt(cursor
                    .getColumnIndex(CWConversationMessage.READ_STATUS))));
            m.setSendStatus(CWSendStatus.valueOf(cursor.getInt(cursor
                    .getColumnIndex(CWConversationMessage.SEND_STATUS))));
            m.setExt(cursor.getString(cursor
                    .getColumnIndex(CWConversationMessage.EXT)));
            m.setModifyTime(CWDateUtil.string2DateTime(cursor.getString(cursor
                    .getColumnIndex(CWConversationMessage.MODIFY_TIME))));
            m.setCreationTime(CWDateUtil.string2DateTime(cursor.getString(cursor
                    .getColumnIndex(CWConversationMessage.CREATION_TIME))));
            return m;
        }
    }

}
