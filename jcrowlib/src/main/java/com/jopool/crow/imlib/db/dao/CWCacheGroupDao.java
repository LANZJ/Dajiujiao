package com.jopool.crow.imlib.db.dao;

import android.database.Cursor;

import com.jopool.crow.imlib.db.core.CWMultiRowMapper;
import com.jopool.crow.imlib.db.core.CWSingleRowMapper;
import com.jopool.crow.imlib.entity.CWCacheGroup;
import com.jopool.crow.imlib.utils.CWDateUtil;
import com.jopool.crow.imlib.utils.CWValidator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 会话Dao
 *
 * @author xuan
 */
public class CWCacheGroupDao extends CWBaseDao {
    //select
    private static final String SQL_FIND_BY_GROUPID = "SELECT * FROM cw_cache_group WHERE group_id=?";

    //insert
    private static final String SQL_INSERT_OR_REPLACE = "INSERT OR REPLACE INTO cw_cache_group(" +
            "group_id, group_name, last_update_time) VALUES(?,?,?)";

    //update

    //delete

    /**
     * 查找
     *
     * @param groupId
     * @return
     */
    public CWCacheGroup findByGroupId(String groupId) {
        if (CWValidator.isEmpty(groupId)) {
            return null;
        }
        return bpQuery(SQL_FIND_BY_GROUPID, new String[]{groupId},
                new MSingleRowMapper());
    }

    /**
     * 批量插入,如果存在就更新
     *
     * @param cacheGroupList
     */
    public void insertOrReplaceBatch(List<CWCacheGroup> cacheGroupList) {
        if (CWValidator.isEmpty(cacheGroupList)) {
            return;
        }
        List<Object[]> dataList = new ArrayList<Object[]>();
        for (CWCacheGroup cacheGroup : cacheGroupList) {
            dataList.add(new Object[]{
                    cacheGroup.getGroupId(),
                    cacheGroup.getGroupName(),
                    CWDateUtil.date2StringBySecond(cacheGroup.getLastUpdateTime())});
        }
        bpUpdateBatch(SQL_INSERT_OR_REPLACE, dataList);
    }

    /**
     * 单条结果集
     *
     * @author xuan
     */
    private static class MSingleRowMapper implements
            CWSingleRowMapper<CWCacheGroup> {
        @Override
        public CWCacheGroup mapRow(Cursor cursor) throws SQLException {
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
            CWMultiRowMapper<CWCacheGroup> {
        @Override
        public CWCacheGroup mapRow(Cursor cursor, int n) throws SQLException {
            CWCacheGroup cacheGroup = new CWCacheGroup();
            cacheGroup.setGroupId(cursor.getString(cursor.getColumnIndex(CWCacheGroup.GROUP_ID)));
            cacheGroup.setGroupName(cursor.getString(cursor.getColumnIndex(CWCacheGroup.GROUP_NAME)));
            cacheGroup.setLastUpdateTime(CWDateUtil.string2DateTime(cursor.getString(cursor.getColumnIndex(CWCacheGroup.LAST_UPDATE_TIME))));
            return cacheGroup;
        }
    }

}
