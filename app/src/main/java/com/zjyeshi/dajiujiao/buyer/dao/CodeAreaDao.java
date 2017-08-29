package com.zjyeshi.dajiujiao.buyer.dao;

import android.database.Cursor;
import android.database.SQLException;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.db.BPBaseDao;
import com.xuan.bigapple.lib.db.callback.MultiRowMapper;
import com.xuan.bigapple.lib.db.callback.SingleRowMapper;
import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.entity.my.BigArea;
import com.zjyeshi.dajiujiao.buyer.entity.my.CodeArea;

/**
 * Created by wuhk on 2017/3/28.
 */

public class CodeAreaDao extends BPBaseDao {
    private final String SQL_REPLACE= "INSERT or REPLACE INTO code_area(code , area) VALUES (?,?)";
    private final String SQL_FIND_BY_AREA = "SELECT * FROM code_area WHERE area = ?";


    /**插入数据
     *
     * @param codeArea
     */
    public void insert(CodeArea codeArea){
        if (null == codeArea) {
            return;
        }
        bpUpdate(SQL_REPLACE, new Object[]{codeArea.getCode() , codeArea.getArea()});

    }

    /**根据地区查找代码
     *
     * @param area
     * @return
     */
    public  CodeArea findByArea(String area){
        if (Validators.isEmpty(area)) {
            return null;
        }
        return bpQuery(SQL_FIND_BY_AREA, new String[]{area}, new MSingleRowMapper());
    }


    /**
     * 多条结果集
     */
    private class MMultiRowMapper implements MultiRowMapper<CodeArea> {
        @Override
        public CodeArea mapRow(Cursor cursor, int n) throws SQLException {
            CodeArea codeArea = new CodeArea();
            codeArea.setCode(cursor.getString(cursor.getColumnIndex("code")));
            codeArea.setArea(cursor.getString(cursor.getColumnIndex("area")));
            return codeArea;
        }
    }

    /**
     * 单条结果集
     */
    private class MSingleRowMapper implements SingleRowMapper<CodeArea> {
        @Override
        public CodeArea mapRow(Cursor cursor) throws SQLException {
            return new MMultiRowMapper().mapRow(cursor, 0);
        }
    }
}
