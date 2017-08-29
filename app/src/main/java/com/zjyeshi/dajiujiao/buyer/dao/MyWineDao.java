package com.zjyeshi.dajiujiao.buyer.dao;

import android.database.Cursor;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.db.BPBaseDao;
import com.xuan.bigapple.lib.db.callback.MultiRowMapper;
import com.xuan.bigapple.lib.db.callback.SingleRowMapper;
import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.task.data.store.goods.GoodData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 我的红酒Dao
 * Created by wuhk on 2015/12/10.
 */
public class MyWineDao extends BPBaseDao{

    private final String SQL_INSERT_BATCH = "INSERT OR REPLACE INTO my_wine(id,owner_user_id,data) VALUES(?,?,?)";
    private final String SQL_FIND_ALL = "SELECT * FROM my_wine WHERE owner_user_id = ?";

    /**批量不重复插入红酒
     *
     * @param goodDataList
     */
    public void insertBatch(List<GoodData> goodDataList){
        if (Validators.isEmpty(goodDataList)){
            return;
        }
        String ownerUserId = LoginedUser.getLoginedUser().getId();
        if (Validators.isEmpty(ownerUserId)){
            return;
        }

        for (GoodData goodData : goodDataList){
            bpUpdate(SQL_INSERT_BATCH , new Object[]{goodData.getProduct().getId() , ownerUserId , JSON.toJSONString(goodData)});
        }
    }

    /**插入单条数据
     *
     * @param goodData
     */
    public void insert(GoodData goodData){
        if (null == goodData){
            return;
        }

        String ownerUserId = LoginedUser.getLoginedUser().getId();
        if (Validators.isEmpty(ownerUserId)){
            return;
        }
        bpUpdate(SQL_INSERT_BATCH , new Object[]{goodData.getProduct().getId() , ownerUserId , JSON.toJSONString(goodData)});
    }

    /**查找全部
     *
     * @return
     */
    public List<GoodData> findAll(){
        String ownerUserId = LoginedUser.getLoginedUser().getId();
        if (Validators.isEmpty(ownerUserId)){
            return new ArrayList<GoodData>();
        }

        return bpQuery(SQL_FIND_ALL , new String[]{ownerUserId} , new MMultiRowMapper());
    }

    /**
     * 返回多条结果集
     */
    public class MMultiRowMapper implements MultiRowMapper<GoodData> {
        @Override
        public GoodData mapRow(Cursor cs, int i) throws SQLException {
            String data = cs.getString(cs.getColumnIndex("data"));
            GoodData goodData = (GoodData) JSON.parseObject(data, GoodData.class);
            return goodData;
        }
    }

    /**
     * 返回单条结果集
     */
    public class MSingleRowMapper implements SingleRowMapper<GoodData> {
        @Override
        public GoodData mapRow(Cursor cursor) throws SQLException {
            return new MMultiRowMapper().mapRow(cursor, 0);
        }
    }
}
