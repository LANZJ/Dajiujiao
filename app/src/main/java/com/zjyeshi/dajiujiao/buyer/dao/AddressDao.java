package com.zjyeshi.dajiujiao.buyer.dao;

import android.database.Cursor;
import android.database.SQLException;

import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.my.Address;
import com.xuan.bigapple.lib.db.BPBaseDao;
import com.xuan.bigapple.lib.db.callback.MultiRowMapper;
import com.xuan.bigapple.lib.db.callback.SingleRowMapper;
import com.xuan.bigapple.lib.utils.Validators;

import java.util.ArrayList;
import java.util.List;

/**
 * 地址Dao
 * Created by wuhk on 2015/11/12.
 */
public class AddressDao extends BPBaseDao {
    private final String SQL_FIND_ALL = "SELECT * FROM address WHERE owner_user_id = ?";
    private final String SQL_REPLACE= "INSERT or REPLACE INTO address(owner_user_id , id , receiver , phone , area , selected,detail_address) VALUES (?,?,?,?,?,?,?)";
    private final String SQL_INSERT= "INSERT INTO address(owner_user_id , id , receiver , phone , area ,selected ,detail_address) VALUES (?,?,?,?,?,?,?)";
    private final String SQL_FIND_BY_ID = "SELECT * FROM address WHERE owner_user_id = ? AND id = ?";
    private final String SQL_DELETE_BY_ID =  "DELETE FROM address WHERE id = ? AND owner_user_id = ?";
    private final String SQL_FIND_BY_SELECTED =  "SELECT * FROM address WHERE selected = ? AND owner_user_id = ?";


    /**
     * 查找所有地址
     *
     * @param ownerUserId
     * @return
     */
    public List<Address> findAll(String ownerUserId) {
        if (Validators.isEmpty(ownerUserId)) {
            return new ArrayList<Address>();
        }
        return bpQuery(SQL_FIND_ALL, new String[] { ownerUserId },
                new MMultiRowMapper());
    }


    /**
     * 插入地址
     * @param address
     */
    public void replace(Address address){
        if (null == address){
            return;
        }

        bpUpdate(SQL_REPLACE, new Object[]{address.getOwnerUserId(), address.getId(), address.getName(), address.getPhone(), address.getArea(), address.getSelected(), address.getAddress()});
    }

    /**
     * 批量插入地址
     * @param addressList
     * @param ownerUserId
     */
    public void insertBatch(List<Address> addressList, String ownerUserId) {
        if (Validators.isEmpty(addressList) || Validators.isEmpty(ownerUserId)) {
            return;
        }

        List<Object[]> insertList = new ArrayList<Object[]>();
        for (Address ar : addressList) {
            insertList.add(new Object[] { ownerUserId, ar.getId(),
                    ar.getName() , ar.getPhone(), ar.getArea() ,ar.getSelected() ,ar.getAddress()});
        }

        bpUpdateBatch(SQL_REPLACE, insertList);

    }

    /** 根据id删除地址
     *
     * @param id
     * @param ownerUserId
     */
    public void deleteById(String id , String ownerUserId) {
        if (Validators.isEmpty(id) || Validators.isEmpty(ownerUserId)) {
            return;
        }

        bpUpdate(SQL_DELETE_BY_ID, new String[]{id, ownerUserId});
    }

    /** 根据id查找地址
     *
     * @param ownerUserId
     * @param id
     * @return
     */
    public Address findAddressById(String ownerUserId, String id) {
        if (Validators.isEmpty(ownerUserId) || Validators.isEmpty(id)) {
            return null;
        }
        return bpQuery(SQL_FIND_BY_ID, new String[]{ownerUserId, id}, new MSingleRowMapper());
    }

    /**查找选中的地址
     *
     * @return
     */
    public Address findAddressBySelected() {
        String ownerUserId = LoginedUser.getLoginedUser().getId();
        if (Validators.isEmpty(ownerUserId)) {
            return null;
        }
        return bpQuery(SQL_FIND_BY_SELECTED, new String[]{ "1" , ownerUserId}, new MSingleRowMapper());
    }
    /**
     * 多条结果集
     */
    private class MMultiRowMapper implements MultiRowMapper<Address> {
        @Override
        public Address mapRow(Cursor cursor, int n) throws SQLException {
            Address address = new Address();
            address.setOwnerUserId(cursor.getString(cursor.getColumnIndex("owner_user_id")));
            address.setId(cursor.getString(cursor.getColumnIndex("id")));
            address.setName(cursor.getString(cursor.getColumnIndex("receiver")));
            address.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            address.setArea(cursor.getString(cursor.getColumnIndex("area")));
            address.setSelected(cursor.getInt(cursor.getColumnIndex("selected")));
            address.setAddress(cursor.getString(cursor.getColumnIndex("detail_address")));
            return address;
        }
    }

    /**
     * 单条结果集
     */
    private class MSingleRowMapper implements SingleRowMapper<Address> {
        @Override
        public Address mapRow(Cursor cursor) throws SQLException {
            return new MMultiRowMapper().mapRow(cursor, 0);
        }
    }
}
