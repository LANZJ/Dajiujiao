package com.zjyeshi.dajiujiao.buyer.dao;

import android.database.Cursor;
import android.database.SQLException;

import com.alibaba.fastjson.JSON;
import com.zjyeshi.dajiujiao.buyer.entity.my.BigArea;
import com.xuan.bigapple.lib.db.BPBaseDao;
import com.xuan.bigapple.lib.db.callback.MultiRowMapper;
import com.xuan.bigapple.lib.db.callback.SingleRowMapper;
import com.xuan.bigapple.lib.utils.Validators;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhk on 2015/11/22.
 */
public class AreaDao extends BPBaseDao {
    private final String SQL_REPLACE= "INSERT or REPLACE INTO area(code , big_area) VALUES (?,?)";
    private final String SQL_FIND_BY_CODE = "SELECT * FROM area WHERE code = ?";
    private final String SQL_FIND_BY_AREA_LIKE = "SELECT * FROM area WHERE big_area LIKE ?";

    /**批量插入地区
     *
     * @param bigAreaList
     */
    public void insertBatch(List<BigArea> bigAreaList) {
        if (Validators.isEmpty(bigAreaList)) {
            return;
        }
        List<Object[]> insertList = new ArrayList<Object[]>();
        for (BigArea area : bigAreaList) {
            insertList.add(new Object[] { area.getCode() , JSON.toJSONString(area)});
        }
        bpUpdateBatch(SQL_REPLACE, insertList);
    }

    /**根据code查找多条
     *
     * @param code
     * @return
     */
    public BigArea findByCode(String code){
        if (Validators.isEmpty(code)) {
            return null;
        }
        return bpQuery(SQL_FIND_BY_CODE, new String[]{code}, new MSingleRowMapper());
    }

    /**根据地区模糊查询
     *
     * @param area
     * @return
     */
    public BigArea findByAreaLike(String area){
        if (Validators.isEmpty(area)) {
            return null;
        }
        return bpQuery(SQL_FIND_BY_AREA_LIKE, new String[]{area}, new MSingleRowMapper());
    }
    /**
     * 多条结果集
     */
    private class MMultiRowMapper implements MultiRowMapper<BigArea> {
        @Override
        public BigArea mapRow(Cursor cursor, int n) throws SQLException {
            BigArea bigArea = new BigArea();
            bigArea.setCode(cursor.getString(cursor.getColumnIndex("code")));
            String data = cursor.getString(cursor.getColumnIndex("big_area"));
            bigArea.setList(((BigArea) JSON.parseObject(data, BigArea.class)).getList());
            return bigArea;
        }
    }

    /**
     * 单条结果集
     */
    private class MSingleRowMapper implements SingleRowMapper<BigArea> {
        @Override
        public BigArea mapRow(Cursor cursor) throws SQLException {
            return new MMultiRowMapper().mapRow(cursor, 0);
        }
    }


}
