package com.zjyeshi.dajiujiao.buyer.dao;

import android.database.Cursor;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.db.sqlmarker.Deletor;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.task.data.store.homepage.ALLStoreData;
import com.xuan.bigapple.lib.db.BPBaseDao;
import com.xuan.bigapple.lib.db.callback.MultiRowMapper;
import com.xuan.bigapple.lib.db.callback.SingleRowMapper;
import com.xuan.bigapple.lib.utils.Validators;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 店铺列表Dao
 * Created by wuhk on 2015/12/3.
 */
public class ShopsDao extends BPBaseDao {
    private final String SQL_INSERT = "INSERT OR REPLACE INTO shops(id,owner_user_id,followed ,data) VALUES(?,?,?,?)";
    private final String SQL_FINDALL = "SELECT * FROM shops WHERE owner_user_id = ?";
    private final String SQL_FIND_BY_FOLLOWED = "SELECT * FROM shops WHERE owner_user_id = ? AND followed = ?";

    /**批量插入店铺
     *
     * @param allStoreDataList
     */
    public  void insertBatch(List<ALLStoreData> allStoreDataList){
        if (Validators.isEmpty(allStoreDataList)){
            return;
        }

        String ownerUserId = LoginedUser.getLoginedUser().getId();
        if (Validators.isEmpty(ownerUserId)){
            return;
        }

        for (ALLStoreData allStoreData : allStoreDataList){
            if (allStoreData.isFollowed()){
                bpUpdate(SQL_INSERT , new Object[]{allStoreData.getShop().getId() , ownerUserId , "1" , JSON.toJSONString(allStoreData)});
            }else{
                bpUpdate(SQL_INSERT , new Object[]{allStoreData.getShop().getId() , ownerUserId , "2" , JSON.toJSONString(allStoreData)});
            }
        }
    }

    /**查找全部
     *
     * @return
     */
    public List<ALLStoreData> findAll(){
        String ownerUserId = LoginedUser.getLoginedUser().getId();
        if (Validators.isEmpty(ownerUserId)){
            return new ArrayList<ALLStoreData>();
        }
        return bpQuery(SQL_FINDALL ,new String[]{ownerUserId} , new MMultiRowMapper());
    }

    /**查找收藏列表
     *
     * @return
     */
    public List<ALLStoreData> findByFollowed(){
        String ownerUserId = LoginedUser.getLoginedUser().getId();
        if (Validators.isEmpty(ownerUserId)){
            return  new ArrayList<ALLStoreData>();
        }
        return bpQuery(SQL_FIND_BY_FOLLOWED , new String[]{ownerUserId , "1"} , new MMultiRowMapper());
    }

    /**删除全部
     *
     */
    public void deleteAll(){
        String ownerUserId = LoginedUser.getLoginedUser().getId();
        if (Validators.isEmpty(ownerUserId)) {
            return;
        }
        bpDetele(Deletor.deleteFrom("shops").where("owner_user_id=?", ownerUserId));
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
