package com.zjyeshi.dajiujiao.buyer.dao;

import android.database.Cursor;
import android.database.SQLException;

import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.contact.AddressUser;
import com.xuan.bigapple.lib.db.BPBaseDao;
import com.xuan.bigapple.lib.db.callback.MultiRowMapper;
import com.xuan.bigapple.lib.db.callback.SingleRowMapper;
import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.entity.enums.UserEnum;
import com.zjyeshi.dajiujiao.buyer.task.data.UserData;

import java.util.List;

/**
 * 联系人Dao
 * Created by wuhk on 2015/11/10.
 */
public class AddressUserDao extends BPBaseDao {
    private final String SQL_INSERT = "INSERT OR REPLACE INTO address_user(owner_user_id , user_id , name , avatar , priority) VALUES (?,?,?,?,?)";
    private final String SQL_FIND_BY_ID = "SELECT * FROM address_user WHERE owner_user_id = ? AND user_id = ?";

    /**
     * 插入联系人
     *
     * @param addressUser
     */
    public void replaceOrInsert(AddressUser addressUser) {
        if (null == addressUser) {
            return;
        }
        bpUpdate(SQL_INSERT, new Object[]{addressUser.getOwnerUserId(), addressUser.getUserId(), addressUser.getName(), addressUser.getAvatar(), addressUser.getPriority()});
    }

    /**
     * 插入人员,设置人员优先级
     *
     * @param memberId
     * @param result
     */
    public void insert(String memberId, Result<UserData> result) {
        AddressUser addressUser = new AddressUser();
        addressUser.setOwnerUserId(LoginedUser.getLoginedUser().getId());
        addressUser.setUserId(memberId);
        //设置该人员优先级，在会话中会用到
        addressUser.setPriority(getUserConversationPriority(result.getValue().getType()));

        if (Validators.isEmpty(result.getValue().getShopName())) {
            //全国客服
            if (!Validators.isEmpty(result.getValue().getName())){
                if (result.getValue().getName().contains("[WholeCountry]")){
                    addressUser.setPriority(20);
                    addressUser.setName(result.getValue().getName().split("\\[")[0]);
                }else if (result.getValue().getName().contains("[region]")){
                    //地区客服
                    addressUser.setPriority(19);
                    addressUser.setName(result.getValue().getName().split("\\[")[0]);
                }else{
                    addressUser.setName(result.getValue().getName());
                }
            }else{
                addressUser.setName(" ");
            }
            addressUser.setAvatar(result.getValue().getPic());
        } else {
            if (!Validators.isEmpty(result.getValue().getName())){
                addressUser.setName(result.getValue().getName() + "(" + result.getValue().getShopName() + ")");
            }else{
                addressUser.setName(" ");
            }
            addressUser.setAvatar(result.getValue().getShopPic());
        }

        bpUpdate(SQL_INSERT, new Object[]{addressUser.getOwnerUserId(), addressUser.getUserId(), addressUser.getName(), addressUser.getAvatar(), addressUser.getPriority()});
    }

    /**
     * 批量插入联系人
     *
     * @param addressUserList
     */
    public void insertBatch(List<AddressUser> addressUserList) {
        if (Validators.isEmpty(addressUserList)) {
            return;
        }
        for (AddressUser addressUser : addressUserList) {
            bpUpdate(SQL_INSERT, new Object[]{addressUser.getOwnerUserId(), addressUser.getUserId(), addressUser.getName(), addressUser.getAvatar(), addressUser.getPriority()});
        }
    }

    /**
     * 根据userId查找联系人
     *
     * @param ownerUserId
     * @param userId
     * @return
     */
    public AddressUser findUserById(String ownerUserId, String userId) {
        if (Validators.isEmpty(ownerUserId) || Validators.isEmpty(userId)) {
            return null;
        }
        return bpQuery(SQL_FIND_BY_ID, new String[]{ownerUserId, userId}, new MSingleRowMapper());
    }

    /**
     * 多条结果集
     */
    private class MMultiRowMapper implements MultiRowMapper<AddressUser> {
        @Override
        public AddressUser mapRow(Cursor cursor, int n) throws SQLException {
            AddressUser au = new AddressUser();
            au.setOwnerUserId(cursor.getString(cursor.getColumnIndex("owner_user_id")));
            au.setUserId(cursor.getString(cursor.getColumnIndex("user_id")));
            au.setName(cursor.getString(cursor.getColumnIndex("name")));
            au.setAvatar(cursor.getString(cursor.getColumnIndex("avatar")));
            au.setPriority(cursor.getInt(cursor.getColumnIndex("priority")));
            return au;
        }
    }

    /**
     * 单条结果集
     */
    private class MSingleRowMapper implements SingleRowMapper<AddressUser> {
        @Override
        public AddressUser mapRow(Cursor cursor) throws SQLException {
            return new MMultiRowMapper().mapRow(cursor, 0);
        }
    }


    /**
     * 设置聊天用户优先级排序
     *
     * @param userType
     * @return
     */
    public static int getUserConversationPriority(int userType) {
        int priority = 0;
        UserEnum userEnum = UserEnum.valueOf(userType);
        if (userEnum.equals(UserEnum.AGENT)) {
            //总代理
            priority = 9;
        } else if (userEnum.equals(UserEnum.DEALER)) {
            //经销商
            priority = 8;
        } else if (userEnum.equals(UserEnum.TERMINAL)) {
            //终端
            priority = 7;
        } else if (userEnum.equals(UserEnum.SALESMAN)) {
            //业务员 , 暂无处理

        } else if (userEnum.equals(UserEnum.CUSTOMER)) {
            //消费者 , 暂无处理

        } else {

        }
        return priority;
    }
}
