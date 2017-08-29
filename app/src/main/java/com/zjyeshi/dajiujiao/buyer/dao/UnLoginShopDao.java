package com.zjyeshi.dajiujiao.buyer.dao;

import android.database.Cursor;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.db.BPBaseDao;
import com.xuan.bigapple.lib.db.callback.MultiRowMapper;
import com.xuan.bigapple.lib.db.callback.SingleRowMapper;
import com.xuan.bigapple.lib.db.sqlmarker.Deletor;
import com.xuan.bigapple.lib.db.sqlmarker.Selector;
import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.task.data.store.homepage.ALLStoreData;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by wuhk on 2015/12/4.
 */
public class UnLoginShopDao extends BPBaseDao {
    private final String SQL_INSERT = "INSERT OR REPLACE INTO un_login_shop(id,data) VALUES(?,?)";

    /**批量不重复插入店铺
     *
     * @param allStoreDataList
     */
    public  void insertBatch(List<ALLStoreData> allStoreDataList){
        if (Validators.isEmpty(allStoreDataList)){
            return;
        }

        for (ALLStoreData allStoreData : allStoreDataList){
            bpUpdate(SQL_INSERT , new Object[]{allStoreData.getShop().getId() , JSON.toJSONString(allStoreData)});
        }
    }

    /**查找全部
     *
     * @return
     */
    public List<ALLStoreData> findAll(){
        return bpQuery(Selector.from("un_login_shop") , new MMultiRowMapper());
    }

    /**删除全部
     *
     */
    public void deleteAll(){
        bpDetele(Deletor.deleteFrom("un_login_shop"));
    }

    /**
     * 返回多条结果集
     */
    public class MMultiRowMapper implements MultiRowMapper<ALLStoreData> {
        @Override
        public ALLStoreData mapRow(Cursor cs, int i) throws SQLException {
            String data = cs.getString(cs.getColumnIndex("data"));
            ALLStoreData allStoreData = (ALLStoreData) JSON.parseObject(data, ALLStoreData.class);
            return allStoreData;
        }
    }

    /**
     * 返回单条结果集
     */
    public class MSingleRowMapper implements SingleRowMapper<ALLStoreData> {
        @Override
        public ALLStoreData mapRow(Cursor cursor) throws SQLException {
            return new MMultiRowMapper().mapRow(cursor, 0);
        }
    }
}
