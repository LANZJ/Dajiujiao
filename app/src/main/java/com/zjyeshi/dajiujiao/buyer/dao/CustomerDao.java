package com.zjyeshi.dajiujiao.buyer.dao;

import android.database.Cursor;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.db.BPBaseDao;
import com.xuan.bigapple.lib.db.callback.MultiRowMapper;
import com.xuan.bigapple.lib.db.callback.SingleRowMapper;
import com.xuan.bigapple.lib.db.sqlmarker.Deletor;
import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.task.data.seller.CustomerData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 老用户
 * Created by wuhk on 2015/12/4.
 */
public class CustomerDao extends BPBaseDao {
    private final String SQL_INSERT = "INSERT OR REPLACE INTO customer(id,owner_user_id,data) VALUES(?,?,?)";
    private final String SQL_FINDALL = "SELECT * FROM customer WHERE owner_user_id = ?";

    /**批量不重复插入
     *
     * @param customerList
     */
    public  void insertBatch(List<CustomerData.Customer> customerList){
        if (Validators.isEmpty(customerList)){
            return;
        }

        String ownerUserId = LoginedUser.getLoginedUser().getId();
        if (Validators.isEmpty(ownerUserId)){
            return;
        }

        for (CustomerData.Customer customer : customerList){
            bpUpdate(SQL_INSERT , new Object[]{customer.getId() , ownerUserId , JSON.toJSONString(customer)});
        }
    }

    /**查找全部
     *
     * @return
     */
    public  List<CustomerData.Customer> findAll(){
        String ownerUserId = LoginedUser.getLoginedUser().getId();
        if (Validators.isEmpty(ownerUserId)){
            return new ArrayList<CustomerData.Customer>();
        }

        return  bpQuery(SQL_FINDALL ,new String[]{ownerUserId}, new MMultiRowMapper());
    }



    /**
     * 删除所有数据
     */
    public void deleteAll(){
        String ownerUserId = LoginedUser.getLoginedUser().getId();
        if (Validators.isEmpty(ownerUserId)) {
            return;
        }
        bpDetele(Deletor.deleteFrom("customer").where("owner_user_id=?", ownerUserId));
    }


    /**
     * 返回多条结果集
     */
    public class MMultiRowMapper implements MultiRowMapper<CustomerData.Customer> {
        @Override
        public CustomerData.Customer mapRow(Cursor cs, int i) throws SQLException {
            String data = cs.getString(cs.getColumnIndex("data"));
            CustomerData.Customer customer = (CustomerData.Customer) JSON.parseObject(data, CustomerData.Customer.class);
            return customer;
        }
    }

    /**
     * 返回单条结果集
     */
    public class MSingleRowMapper implements SingleRowMapper<CustomerData.Customer> {
        @Override
        public CustomerData.Customer mapRow(Cursor cursor) throws SQLException {
            return new MMultiRowMapper().mapRow(cursor, 0);
        }
    }

}
