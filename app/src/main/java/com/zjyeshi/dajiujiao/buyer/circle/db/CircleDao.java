package com.zjyeshi.dajiujiao.buyer.circle.db;

import android.database.Cursor;

import com.alibaba.fastjson.JSON;
import com.zjyeshi.dajiujiao.buyer.circle.task.data.CircleData;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.xuan.bigapple.lib.db.BPBaseDao;
import com.xuan.bigapple.lib.db.callback.MultiRowMapper;
import com.xuan.bigapple.lib.db.callback.SingleRowMapper;
import com.xuan.bigapple.lib.db.sqlmarker.Deletor;
import com.xuan.bigapple.lib.db.sqlmarker.Insertor;
import com.xuan.bigapple.lib.db.sqlmarker.Selector;
import com.xuan.bigapple.lib.utils.DateUtils;
import com.xuan.bigapple.lib.utils.Validators;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 圈子本地数据
 * <p/>
 * Created by xuan on 15/11/5.
 */
public class CircleDao extends BPBaseDao {
    private final String SQL_FIND_BY_ID = "SELECT * FROM circle WHERE owner_user_id = ? AND id = ?";
    private final String SQL_REPLACE= "INSERT or REPLACE INTO circle(owner_user_id , id , creationTime , state , data) VALUES (?,?,?,?,?)";
    private final String SQL_DELETE_BY_ID =  "DELETE FROM circle WHERE id = ? AND owner_user_id = ?";

    /**
     * 批量不重复的插入
     *
     * @param circleList
     */
    public void replaceIntoBatch(List<CircleData.Circle> circleList) {
        if (Validators.isEmpty(circleList)) {
            return;
        }

        String ownerUserId = LoginedUser.getLoginedUser().getId();
        if (Validators.isEmpty(ownerUserId)) {
            return;
        }

        List<Insertor> insertorList = new ArrayList<Insertor>();
        for (CircleData.Circle circle : circleList) {
            insertorList.add(Insertor.replaceInto("circle")
                    .value("id", circle.getId())
                    .value("owner_user_id", ownerUserId)
                    .value("creationTime", DateUtils.date2StringBySecond(circle.getCreationTime()))
                    .value("state", CircleStateEnum.SUCCESS.getValueStr())
                    .value("data", JSON.toJSONString(circle)));
        }

        bpInsertBatch(insertorList);
    }


    /**
     * 插入动态
     * @param circle
     */
    public void replace(CircleData.Circle circle){
        String ownerUserId = LoginedUser.getLoginedUser().getId();
        if (null == circle){
            return;
        }

        bpUpdate(SQL_REPLACE, new Object[]{ownerUserId, circle.getId(), DateUtils.date2StringBySecond(circle.getCreationTime()), circle.getState(), JSON.toJSONString(circle)});
    }

    /** 根据id删除动态
     *
     * @param id
     * @param ownerUserId
     */
    public void deleteById(String id , String ownerUserId) {
        if (Validators.isEmpty(id) || Validators.isEmpty(ownerUserId)) {
            return;
        }

        bpUpdate(SQL_DELETE_BY_ID, new String[]{id , ownerUserId});
    }

    /**
     * 查找所有
     */
    public List<CircleData.Circle> findAll(){
        String ownerUserId = LoginedUser.getLoginedUser().getId();
        if (Validators.isEmpty(ownerUserId)) {
            return new ArrayList<CircleData.Circle>();
        }

        return bpQuery(Selector.from("circle").where("owner_user_id=?", ownerUserId).orderByDesc("creationTime"), new MMultiRowMapper());
    }

    /**根据id，ownerUserId查找
     *
     * @param id
     * @return
     */
    public CircleData.Circle findById(String id){
        String ownerUserId = LoginedUser.getLoginedUser().getId();
        if(Validators.isEmpty(ownerUserId) || Validators.isEmpty(id)){
            return null;
        }
        return bpQuery(SQL_FIND_BY_ID , new String[]{ownerUserId, id}, new MSingleRowMapper());
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
    public class MSingleRowMapper implements SingleRowMapper<CircleData.Circle>{
        @Override
        public CircleData.Circle mapRow(Cursor cursor) throws SQLException {
            return new MMultiRowMapper().mapRow(cursor, 0);
        }
    }

    /**
     * 删除所有数据
     */
    public void deleteAll(){
        String ownerUserId = LoginedUser.getLoginedUser().getId();
        if (Validators.isEmpty(ownerUserId)) {
            return;
        }

        bpDetele(Deletor.deleteFrom("circle").where("owner_user_id=?", ownerUserId));
    }

}
