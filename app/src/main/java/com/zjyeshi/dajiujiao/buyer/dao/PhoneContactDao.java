package com.zjyeshi.dajiujiao.buyer.dao;

import android.database.Cursor;
import android.database.SQLException;

import com.xuan.bigapple.lib.db.BPBaseDao;
import com.xuan.bigapple.lib.db.callback.MultiRowMapper;
import com.xuan.bigapple.lib.db.callback.SingleRowMapper;
import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.entity.contact.PhoneUser;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;

import java.util.List;

/**
 * 手机通讯录Dao
 * Created by wuhk on 2016/9/10.
 */
public class PhoneContactDao extends BPBaseDao {

    private final String SQL_INSERT = "INSERT OR REPLACE INTO phone_contact(owner_user_id , phone_number , phone_name , phone_used) VALUES (?,?,?,?)";
    private final String SQL_FIND_ALL = "SELECT * FROM phone_contact WHERE owner_user_id = ?";
    private final String SQL_UPDATE_USED = "UPDATE phone_contact SET phone_used = ? WHERE owner_user_id = ? AND phone_number = ?";
    private final String SQL_SEARCH_NUMBER = "SELECT * FROM phone_contact WHERE phone_number LIKE ?";
    private final String SQL_SEARCH_NAME = "SELECT * FROM phone_contact WHERE phone_name LIKE ?";

    private final String SQL_FIND_BY_ID = "SELECT * FROM phone_contact WHERE  owner_user_id= ? AND phone_number = ?";

    /**批量插入数据
     *
     * @param phoneUserList
     */
    public void insertBatch(List<PhoneUser> phoneUserList){
        if (Validators.isEmpty(phoneUserList)){
            return;
        }

        for(PhoneUser phoneUser : phoneUserList){
            if (null == findById(phoneUser.getPhoneNumber())){
                bpUpdate(SQL_INSERT, new Object[]{phoneUser.getOwnerUserId(), phoneUser.getPhoneNumber(), phoneUser.getPhoneName(), phoneUser.getPhoneUsed()});
            }
        }
    }

    /**查找全部
     *
     * @return
     */
    public List<PhoneUser> findAll(){
        String ownerUserId = LoginedUser.getLoginedUser().getId();
        if (Validators.isEmpty(ownerUserId)) {
            return null;
        }
        return bpQuery(SQL_FIND_ALL, new String[]{ownerUserId}, new MMultiRowMapper());
    }

    /**根据ID查找
     *
     * @param phone
     * @return
     */
    public PhoneUser findById(String phone){
        String ownerUserId = LoginedUser.getLoginedUser().getId();
        if (Validators.isEmpty(ownerUserId)) {
            return null;
        }
        return bpQuery(SQL_FIND_BY_ID, new String[]{ownerUserId , phone}, new MSingleRowMapper());
    }

    /**根据内容模糊查询
     *
     * @param content
     * @return
     */
    public List<PhoneUser> searchUserByName(String content){
        List<PhoneUser> ret = bpQuery(SQL_SEARCH_NAME, new String[]{content} , new MMultiRowMapper());

        return ret;
    }

    public List<PhoneUser> searchUserByNumber(String content){
        List<PhoneUser> ret = bpQuery(SQL_SEARCH_NUMBER, new String[]{content} , new MMultiRowMapper());

        return ret;
    }

    /**更新数据
     *
     * @param used
     * @param phoneNumber
     */
    public void updateContacts(String used , String phoneNumber){
        String ownerUserId = LoginedUser.getLoginedUser().getId();
        if (Validators.isEmpty(ownerUserId)){
            return;
        }

        bpUpdate(SQL_UPDATE_USED , new Object[]{used , ownerUserId , phoneNumber});
    }

    /**
     * 多条结果集
     */
    private class MMultiRowMapper implements MultiRowMapper<PhoneUser> {
        @Override
        public PhoneUser mapRow(Cursor cursor, int n) throws SQLException {
            PhoneUser phoneUser = new PhoneUser();
            phoneUser.setOwnerUserId(cursor.getString(cursor.getColumnIndex("owner_user_id")));
            phoneUser.setPhoneName(cursor.getString(cursor.getColumnIndex("phone_name")));
            phoneUser.setPhoneNumber(cursor.getString(cursor.getColumnIndex("phone_number")));
            phoneUser.setPhoneUsed(cursor.getInt(cursor.getColumnIndex("phone_used")));

            return phoneUser;
        }
    }

    /**
     * 单条结果集
     */
    private class MSingleRowMapper implements SingleRowMapper<PhoneUser> {
        @Override
        public PhoneUser mapRow(Cursor cursor) throws SQLException {
            return new MMultiRowMapper().mapRow(cursor, 0);
        }
    }
}
