package com.jopool.crow.imlib.db.dao;

import android.database.Cursor;

import com.jopool.crow.imlib.db.core.CWMultiRowMapper;
import com.jopool.crow.imlib.db.core.CWSingleRowMapper;
import com.jopool.crow.imlib.entity.CWCacheUser;
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
public class CWCacheUserDao extends CWBaseDao {
    //select
    private static final String SQL_FIND_BY_USERID = "SELECT * FROM cw_cache_user WHERE user_id=?";

    //insert
    private static final String SQL_INSERT_OR_REPLACE = "INSERT OR REPLACE INTO cw_cache_user(" +
            "user_id, user_name, user_logo, ext, creation_time, last_update_time) " +
            "VALUES(?,?,?,?,?,?)";

    //update

    //delete

    /**
     * 查找用户
     *
     * @param userId
     * @return
     */
    public CWCacheUser findByUserId(String userId) {
        if (CWValidator.isEmpty(userId)) {
            return null;
        }
        return bpQuery(SQL_FIND_BY_USERID, new String[]{userId},
                new MSingleRowMapper());
    }

    /**
     * 批量插入,如果存在就更新
     *
     * @param cacheUserList
     */
    public void insertOrReplaceBatch(List<CWCacheUser> cacheUserList) {
        if (CWValidator.isEmpty(cacheUserList)) {
            return;
        }
        List<Object[]> dataList = new ArrayList<Object[]>();
        for (CWCacheUser cacheUser : cacheUserList) {
            dataList.add(new Object[]{
                    cacheUser.getUserId(),
                    cacheUser.getUserName(),
                    cacheUser.getUserLogo(),
                    cacheUser.getExt(),
                    CWDateUtil.date2StringBySecond(cacheUser.getCreationTime()),
                    CWDateUtil.date2StringBySecond(cacheUser.getLastUpdateTime())});
        }
        bpUpdateBatch(SQL_INSERT_OR_REPLACE, dataList);
    }

    /**
     * 单条结果集
     *
     * @author xuan
     */
    private static class MSingleRowMapper implements
            CWSingleRowMapper<CWCacheUser> {
        @Override
        public CWCacheUser mapRow(Cursor cursor) throws SQLException {
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
            CWMultiRowMapper<CWCacheUser> {
        @Override
        public CWCacheUser mapRow(Cursor cursor, int n) throws SQLException {
            CWCacheUser cacheUser = new CWCacheUser();
            cacheUser.setUserId(cursor.getString(cursor.getColumnIndex(CWCacheUser.USER_ID)));
            cacheUser.setUserName(cursor.getString(cursor.getColumnIndex(CWCacheUser.USER_NAME)));
            cacheUser.setUserLogo(cursor.getString(cursor.getColumnIndex(CWCacheUser.USER_LOGO)));
            cacheUser.setExt(cursor.getString(cursor.getColumnIndex(CWCacheUser.EXT)));
            cacheUser.setCreationTime(CWDateUtil.string2DateTime(cursor.getString(cursor.getColumnIndex(CWCacheUser.CREATION_TIME))));
            cacheUser.setLastUpdateTime(CWDateUtil.string2DateTime(cursor.getString(cursor.getColumnIndex(CWCacheUser.LAST_UPDATE_TIME))));
            return cacheUser;
        }
    }

}
