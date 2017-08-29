package com.jopool.crow.imlib.db.core;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.jopool.crow.imlib.db.CWDBHelper;
import com.jopool.crow.imlib.utils.CWLogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 对原生数据库操作做了一层轻量级封装，主要屏蔽了显式的close操作，并且处理了多线程操作的问题，当然也可以使用原生的API。<br>
 * 应用层可对本实例保持单例，线程安全。
 *
 * @author xuan
 */
public class CWBaseDao {
    public static boolean DEBUG = false;

    /**
     * 子类小心使用该锁，如果要使用请确保不会死锁，不然后果不堪
     */
    protected final static ReentrantLock lock = new ReentrantLock();// 保证多线程访问数据库的安全，性能有所损失

    /**
     * 获取数据库，对应调用这个一次必须调用close关闭源
     *
     * @return
     */
    public SQLiteDatabase openSQLiteDatabase() {
        return CWDBHelper.getInstance().getWritableDatabase();
    }

    /**
     * 使用完后请Close数据库连接，dbHelper的close其实内部就是sqliteDatabase的close，
     * 并且源码内部会判断null和open的状态
     */
    public void closeSQLiteDatabase() {
        CWDBHelper.getInstance().close();
    }

    /**
     * Sql语句执行方法
     *
     * @param sql
     */
    protected void execSQL(String sql) {
        lock.lock();
        try {
            debugSql(sql, null);
            openSQLiteDatabase().execSQL(sql);
        } catch (Exception e) {
            CWLogUtil.e(e.getMessage(), e);
        } finally {
            closeSQLiteDatabase();
            lock.unlock();
        }
    }

    /**
     * Sql语句执行方法
     *
     * @param sql
     * @param bindArgs
     */
    protected void execSQL(String sql, Object[] bindArgs) {
        lock.lock();
        try {
            debugSql(sql, null);
            openSQLiteDatabase().execSQL(sql, bindArgs);
        } catch (Exception e) {
            CWLogUtil.e(e.getMessage(), e);
        } finally {
            closeSQLiteDatabase();
            lock.unlock();
        }
    }

    /**
     * 批量操作
     *
     * @param sql
     * @param bindArgsList
     */
    protected void execSQLBatch(String sql, List<Object[]> bindArgsList) {
        lock.lock();
        SQLiteDatabase sqliteDatabase = null;
        try {
            sqliteDatabase = openSQLiteDatabase();
            sqliteDatabase.beginTransaction();

            for (int i = 0, n = bindArgsList.size(); i < n; i++) {
                Object[] bindArgs = bindArgsList.get(i);
                debugSql(sql, bindArgs);
                sqliteDatabase.execSQL(sql, bindArgs);
            }

            sqliteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            CWLogUtil.e(e.getMessage(), e);
        } finally {
            if (null != sqliteDatabase) {
                sqliteDatabase.endTransaction();
            }
            closeSQLiteDatabase();
            lock.unlock();
        }
    }

    /**
     * 插入或者更新
     *
     * @param sql
     */
    protected void bpUpdate(String sql) {
        lock.lock();
        try {
            debugSql(sql, null);
            openSQLiteDatabase().execSQL(sql);
        } catch (Exception e) {
            CWLogUtil.e(e.getMessage(), e);
        } finally {
            closeSQLiteDatabase();
            lock.unlock();
        }
    }

    /**
     * 插入或者更新，带参
     *
     * @param sql
     * @param args
     */
    protected void bpUpdate(String sql, Object[] args) {
        if (null == args) {
            bpUpdate(sql);
        } else {
            lock.lock();
            try {
                debugSql(sql, args);
                openSQLiteDatabase().execSQL(sql, args);
            } catch (Exception e) {
                CWLogUtil.e(e.getMessage(), e);
            } finally {
                closeSQLiteDatabase();
                lock.unlock();
            }
        }
    }

    /**
     * 插入或者更新，批量
     *
     * @param sql
     * @param argsList
     */
    protected void bpUpdateBatch(String sql, List<Object[]> argsList) {
        if (null == argsList) {
            return;
        }

        lock.lock();
        SQLiteDatabase sqliteDatabase = null;
        try {
            sqliteDatabase = openSQLiteDatabase();
            sqliteDatabase.beginTransaction();
            for (Object[] args : argsList) {
                debugSql(sql, args);
                sqliteDatabase.execSQL(sql, args);
            }
            sqliteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            CWLogUtil.e(e.getMessage(), e);
        } finally {
            if (null != sqliteDatabase) {
                sqliteDatabase.endTransaction();
            }
            closeSQLiteDatabase();
            lock.unlock();
        }
    }

    /**
     * 查询，返回多条记录
     *
     * @param sql
     * @param args
     * @param multiRowMapper
     * @return
     */
    protected <T> List<T> bpQuery(String sql, String[] args,
                                  CWMultiRowMapper<T> multiRowMapper) {
        List<T> ret = new ArrayList<T>();

        lock.lock();
        debugSql(sql, args);
        Cursor cursor = null;
        try {
            cursor = openSQLiteDatabase().rawQuery(sql, args);
            int i = 0;
            while (cursor.moveToNext()) {
                T t = multiRowMapper.mapRow(cursor, i);
                ret.add(t);
                i++;
            }
        } catch (Exception e) {
            CWLogUtil.e(e.getMessage(), e);
        } finally {
            closeCursor(cursor);
            closeSQLiteDatabase();
            lock.unlock();
        }

        return ret;
    }

    /**
     * 查询，返回单条记录
     *
     * @param sql
     * @param args
     * @param singleRowMapper
     * @param <T>
     * @return
     */
    protected <T> T bpQuery(String sql, String[] args,
                            CWSingleRowMapper<T> singleRowMapper) {
        T ret = null;

        lock.lock();
        debugSql(sql, args);
        Cursor cursor = null;
        try {
            cursor = openSQLiteDatabase().rawQuery(sql, args);
            if (cursor.moveToNext()) {
                ret = singleRowMapper.mapRow(cursor);
            }
        } catch (Exception e) {
            CWLogUtil.e(e.getMessage(), e);
        } finally {
            closeCursor(cursor);
            closeSQLiteDatabase();
            lock.unlock();
        }

        return ret;
    }

    /**
     * 查询，返回MAP集合
     *
     * @param sql
     * @param args
     * @param mapRowMapper
     * @param <K>
     * @param <V>
     * @return
     */
    protected <K, V> Map<K, V> bpQuery(String sql, String[] args,
                                       CWMapRowMapper<K, V> mapRowMapper) {
        Map<K, V> ret = new HashMap<K, V>();

        lock.lock();
        debugSql(sql, args);
        Cursor cursor = null;
        try {
            cursor = openSQLiteDatabase().rawQuery(sql, args);
            int i = 0;
            while (cursor.moveToNext()) {
                K k = mapRowMapper.mapRowKey(cursor, i);
                V v = mapRowMapper.mapRowValue(cursor, i);
                ret.put(k, v);
                i++;
            }
        } catch (Exception e) {
            CWLogUtil.e(e.getMessage(), e);
        } finally {
            closeCursor(cursor);
            closeSQLiteDatabase();
            lock.unlock();
        }

        return ret;
    }

    /**
     * IN查询，返回LIST集合
     *
     * @param prefix
     * @param prefixArgs
     * @param inArgs
     * @param postfix
     * @param multiRowMapper
     * @param <T>
     * @return
     */
    protected <T> List<T> bpQueryForInSQL(String prefix, String[] prefixArgs,
                                          String[] inArgs, String postfix, CWMultiRowMapper<T> multiRowMapper) {
        if (null == prefixArgs) {
            prefixArgs = new String[0];
        }

        StringBuilder sql = new StringBuilder();
        sql.append(prefix).append(CWSqlUtil.getInSQL(inArgs.length));

        if (!TextUtils.isEmpty(postfix)) {
            sql.append(postfix);
        }

        String[] args = new String[inArgs.length + prefixArgs.length];

        System.arraycopy(prefixArgs, 0, args, 0, prefixArgs.length);
        System.arraycopy(inArgs, 0, args, prefixArgs.length, inArgs.length);

        return bpQuery(sql.toString(), args, multiRowMapper);
    }

    /**
     * IN查询，返回MAP集合
     *
     * @param prefix
     * @param prefixArgs
     * @param inArgs
     * @param postfix
     * @param mapRowMapper
     * @param <K>
     * @param <V>
     * @return
     */
    protected <K, V> Map<K, V> queryForInSQL(String prefix,
                                             String[] prefixArgs, String[] inArgs, String postfix,
                                             CWMapRowMapper<K, V> mapRowMapper) {
        if (null == prefixArgs) {
            prefixArgs = new String[0];
        }

        StringBuilder sql = new StringBuilder();
        sql.append(prefix).append(CWSqlUtil.getInSQL(inArgs.length));

        if (!TextUtils.isEmpty(postfix)) {
            sql.append(postfix);
        }

        String[] args = new String[inArgs.length + prefixArgs.length];

        System.arraycopy(prefixArgs, 0, args, 0, prefixArgs.length);
        System.arraycopy(inArgs, 0, args, prefixArgs.length, inArgs.length);

        return bpQuery(sql.toString(), args, mapRowMapper);
    }

    private void debugSql(String sql, Object[] args) {
        if (DEBUG) {
            CWLogUtil.d(CWSqlUtil.getSQL(sql, args));
        }
    }

    // 关闭cursor
    private void closeCursor(Cursor cursor) {
        if (null != cursor && !cursor.isClosed()) {
            cursor.close();
        }
    }

}
