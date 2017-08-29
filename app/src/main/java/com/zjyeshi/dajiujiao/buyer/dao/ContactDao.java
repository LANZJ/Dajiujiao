package com.zjyeshi.dajiujiao.buyer.dao;

import android.database.Cursor;
import android.database.SQLException;

import com.xuan.bigapple.lib.db.BPBaseDao;
import com.xuan.bigapple.lib.db.callback.MultiRowMapper;
import com.xuan.bigapple.lib.db.callback.SingleRowMapper;
import com.xuan.bigapple.lib.db.sqlmarker.Deletor;
import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.task.data.my.FriendListData;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhk on 2016/8/16.
 */
public class ContactDao extends BPBaseDao {
    private final String SQL_INSERT = "INSERT OR REPLACE INTO contact(owner_user_id , id , member_id , name , avatar) VALUES (?,?,?,?,?)";
    private final String SQL_FIND_ALL = "SELECT * FROM contact WHERE owner_user_id = ? ";
    private final String SQL_DELETE_BY_ID =  "DELETE FROM contact WHERE member_id = ? AND owner_user_id = ? AND id = ?";

    /**插入联系人
     *
     * @param dataList
     */
    public void insertBatch(List<FriendListData.Friend> dataList) {
        if (Validators.isEmpty(dataList)) {
            return;
        }
        for (FriendListData.Friend friend : dataList){
            bpUpdate(SQL_INSERT, new Object[]{friend.getOwnerUserId(), friend.getId(), friend.getMemberId()
                    , friend.getName() , friend.getAvatar()});
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

        bpDetele(Deletor.deleteFrom("contact").where("owner_user_id=?", ownerUserId));
    }

    /**删除联系人
     *
     * @param memberId
     * @param id
     */
    public void deleteById(String memberId , String id) {
        String ownerUserId = LoginedUser.getLoginedUser().getId();
        if (Validators.isEmpty(id) || Validators.isEmpty(ownerUserId) || Validators.isEmpty(id)) {
            return;
        }

        bpUpdate(SQL_DELETE_BY_ID, new String[]{memberId, ownerUserId , id});
    }

    /**查找全部
     *
     * @return
     */
    public List<FriendListData.Friend> findAll(){
        String ownerUserId = LoginedUser.getLoginedUser().getId();
        if (Validators.isEmpty(ownerUserId)){
            return new ArrayList<FriendListData.Friend>();
        }

        return bpQuery(SQL_FIND_ALL , new String[]{ownerUserId} , new MMultiRowMapper());
    }
    /**
     * 多条结果集
     */
    private class MMultiRowMapper implements MultiRowMapper<FriendListData.Friend> {
        @Override
        public FriendListData.Friend mapRow(Cursor cursor, int n) throws SQLException {
            FriendListData.Friend friend = new FriendListData.Friend();
            friend.setOwnerUserId(cursor.getString(cursor.getColumnIndex("owner_user_id")));
            friend.setId(cursor.getString(cursor.getColumnIndex("id")));
            friend.setMemberId(cursor.getString(cursor.getColumnIndex("member_id")));
            friend.setName(cursor.getString(cursor.getColumnIndex("name")));
            friend.setAvatar(cursor.getString(cursor.getColumnIndex("avatar")));
            return friend;
        }
    }

    /**
     * 单条结果集
     */
    private class MSingleRowMapper implements SingleRowMapper<FriendListData.Friend> {
        @Override
        public FriendListData.Friend mapRow(Cursor cursor) throws SQLException {
            return new MMultiRowMapper().mapRow(cursor, 0);
        }
    }
}
