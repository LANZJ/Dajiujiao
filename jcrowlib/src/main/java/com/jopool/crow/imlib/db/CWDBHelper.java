package com.jopool.crow.imlib.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.jopool.crow.imlib.utils.CWLogUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 数据库帮助类,使用前先初始化
 *
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2013-3-20 下午7:33:06 $
 */
public class CWDBHelper extends SQLiteOpenHelper {
    private static CWDBHelper instance;// 单例

    /**
     * 用于初始化或升级数据库的文件名格式
     */
    private static final String DB_INIT_OR_UPGRADE_FILENAME = "cw_db_${db.version}.sql";

    /**
     * 数据库版本号，程序初始化时必须初始化此值
     */
    private static int dbVersion = -1;

    /**
     * 数据库名，使用时可以根据自己项目定义名称
     */
    public static String dbName;

    private final Context context;

    /**
     * 初始化数据库，必须操作
     *
     * @param dbVersion    数据库版本号
     * @param dbName       数据库名称
     * @param application Application实例
     */
    public static void init(int dbVersion, String dbName, Context application) {
        CWDBHelper.dbVersion = dbVersion;
        CWDBHelper.dbName = dbName;
        instance = new CWDBHelper(application);
    }

    /**
     * 废弃的构造，请使用单例
     *
     * @param context
     */
    private CWDBHelper(Context context) {
        super(context, dbName, null, dbVersion);
        this.context = context;
    }

    /**
     * 获取DBHelper的单例，必须先调用init
     *
     * @return
     */
    public static CWDBHelper getInstance() {
        return instance;
    }

    /**
     * 数据库第一次被创建时被调用 。<br>
     * 触发时机：不在构造时发生，而是在调用getWritableDatabase或getReadableDatabase时被调用时。
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        if (dbVersion <= 0) {
            throw new RuntimeException(
                    "CWDBHelper.dbVersion<0，请调用init方法初始化");
        }

        CWLogUtil.d("开始初始化数据库...");

        // read and execute assets/cw_db_1.sql
        executeSqlFromFile(db,
                DB_INIT_OR_UPGRADE_FILENAME.replace("${db.version}", "1"));

        // 如果调用onCreate时版本号比1大，则升级(发生在多次升级后，用户第一次安装，或者用户卸载后重新安装)
        if (dbVersion > 1) {
            onUpgrade(db, 1, dbVersion);
        }
    }

    /**
     * 数据库的版本号表明要升级时被调用
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (dbVersion <= 0) {
            throw new RuntimeException(
                    "CWDBHelper.dbVersion<0，请调用init方法");
        }

        if (newVersion <= oldVersion) {
            return;
        }

        CWLogUtil.d("数据库更新:oldVersion[" + oldVersion
                + "]to newVersion[" + newVersion + "]");

        for (int i = oldVersion + 1; i <= newVersion; i++) {
            executeSqlFromFile(
                    db,
                    DB_INIT_OR_UPGRADE_FILENAME.replace("${db.version}",
                            String.valueOf(i)));
        }
    }

    /**
     * 从文件读取sql并执行
     *
     * @param db
     * @param fileName
     */
    private void executeSqlFromFile(SQLiteDatabase db, String fileName) {
        CWLogUtil.d("开始执行在assets中的数据库文件：" + fileName);

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    context.getAssets().open(fileName)));

            String line = null;
            StringBuilder sql = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("--")) {// 注释行
                    continue;
                }
                if (line.trim().equalsIgnoreCase("go")) {// 表示一句sql的结束
                    if (!TextUtils.isEmpty(sql.toString())) {
                        // 执行sql
                        db.execSQL(sql.toString());
                    }
                    sql = new StringBuilder();
                    continue;
                }
                sql.append(line);
            }
            if (!TextUtils.isEmpty(sql.toString())) {
                // 执行sql
                db.execSQL(sql.toString());
            }

        } catch (Exception e) {
            CWLogUtil.e("", e);
            throw new RuntimeException(e);
        }

        CWLogUtil.d("成功执行在assets中的数据库文件：" + fileName);
    }

}