package com.zjyeshi.dajiujiao.buyer.circle.db;

import android.database.Cursor;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.db.BPBaseDao;
import com.xuan.bigapple.lib.db.callback.MultiRowMapper;
import com.xuan.bigapple.lib.db.callback.SingleRowMapper;
import com.xuan.bigapple.lib.db.sqlmarker.Deletor;
import com.xuan.bigapple.lib.db.sqlmarker.Insertor;
import com.xuan.bigapple.lib.db.sqlmarker.Selector;
import com.xuan.bigapple.lib.utils.DateUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.circle.task.data.CircleData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhk on 2016/8/23.
 */
public class UserCircleDao extends BPBaseDao {
    private final String SQL_FIND_BY_ID = "SELECT * FROM user_circle WHERE user_id = ? AND id = ?";
    private final String SQL_REPLACE= "INSERT or REPLACE INTO user_circle(user_id , id , creationTime , data) VALUES (?,?,?,?)";
    private final String SQL_DELETE_BY_ID =  "DELETE FROM user_circle WHERE id = ? AND user_id = ?";

    /**
     * 批量不重复的插入
     *
     * @param circleList
     */
    public void replaceIntoBatch(List<CircleData.Circle> circleList) {
        if (Validators.isEmpty(circleList)) {
            return;
        }

        List<Insertor> insertorList = new ArrayList<Insertor>();
        for (CircleData.Circle circle : circleList) {
            insertorList.add(Insertor.replaceInto("user_circle")
                    .value("id", circle.getId())
                    .value("user_id", circle.getMember().getId())
                    .value("creationTime", DateUtils.date2StringBySecond(circle.getCreationTime()))
                    .value("data", JSON.toJSONString(circle)));
        }

        bpInsertBatch(insertorList);
    }

    /**根据id，userId查找
     *
     * @param id
     * @return
     */
    public CircleData.Circle findById(String id , String userId){
        if(Validators.isEmpty(userId) || Validators.isEmpty(id)){
            return null;
        }
        return bpQuery(SQL_FIND_BY_ID , new String[]{userId, id}, new MSingleRowMapper());
    }

    /**
     * 插入动态
     * @param circle
     */
    public void replace(CircleData.Circle circle){
        if (null == circle){
            return;
        }

        bpUpdate(SQL_REPLACE, new Object[]{circle.getMember().getId(), circle.getId(), DateUtils.date2StringBySecond(circle.getCreationTime()) ,  JSON.toJSONString(circle)});
    }

    /** 根据id删除动态
     *
     * @param id
     * @param userId
     */
    public void deleteById(String id , String userId) {
        if (Validators.isEmpty(id) || Validators.isEmpty(userId)) {
            return;
        }

        bpUpdate(SQL_DELETE_BY_ID, new String[]{id , userId});
    }

    /**
     * 根据用户Id删除该用户的动态
     */
    public void deleteAll(String userId){
        bpDetele(Deletor.deleteFrom("user_circle").where("user_id=?", userId));
    }

    /**
     * 查找某个用户的所有动态
     */
    public List<CircleData.Circle> findAll(String userId){
        return bpQuery(Selector.from("user_circle").where("user_id=?", userId).orderByDesc("creationTime"), new MMultiRowMapper());
    }

    /**返回多条结果集
     *
     */
    public class MMultiRowMapper implements MultiRowMapper<CircleData.Circle> {
        @Override
        public CircleData.Circle mapRow(Cursor cs, int i) throws SQLException {
            String data = cs.getString(cs.getColumnIndex("data"));
            CircleData.Circle circle = (CircleData.Circle)JSON.parseObject(data, CircleData.Circle.class);
            return circle;
        }
    }

    /**返回单条结果集
     *
     */
    public class MSingleRowMapper implements SingleRowMapper<CircleData.Circle> {
        @Override
        public CircleData.Circle mapRow(Cursor cursor) throws SQLException {
            return new MMultiRowMapper().mapRow(cursor, 0);
        }
    }
}
