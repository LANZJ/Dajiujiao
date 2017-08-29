package com.zjyeshi.dajiujiao.buyer.dao;

import android.database.Cursor;
import android.database.SQLException;

import com.zjyeshi.dajiujiao.buyer.entity.GoodsCar;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.enums.SelectEnum;
import com.zjyeshi.dajiujiao.buyer.entity.store.CarShop;
import com.xuan.bigapple.lib.db.BPBaseDao;
import com.xuan.bigapple.lib.db.callback.MultiRowMapper;
import com.xuan.bigapple.lib.db.callback.SingleRowMapper;
import com.xuan.bigapple.lib.utils.Validators;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车Dao
 * Created by wuhk on 2015/10/29.
 */
public class GoodsCarDao extends BPBaseDao {
    private final String SQL_FIND_ALL_GOODS_BY_USERID = "SELECT * FROM goods_car WHERE owner_user_id = ?";
    private final String SQL_DELETE_BY_ID_AND_USERID =  "DELETE FROM goods_car WHERE good_id = ? AND owner_user_id = ?";
    private final String SQL_FIND_BY_SHOPID_AND_USERID =  "SELECT * FROM goods_car WHERE shop_id=? AND owner_user_id = ?";
    private final String SQL_FIND_BY_GOODID_AND_USERID =  "SELECT * FROM goods_car WHERE good_id=? AND owner_user_id = ?";
    private final String SQL_FIND_BY_STATUS_AND_USERID =  "SELECT * FROM goods_car WHERE status=? AND owner_user_id = ?";
    private final String SQL_REPLACE = "insert or replace INTO goods_car(shop_id,good_id,shop_name,good_name,good_icon,good_count,good_type,good_price,good_per_box,good_up_price,status,good_market_price,owner_user_id) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private final String SQL_FIND_SHOP_BY_USERID = "SELECT shop_id,shop_name FROM goods_car WHERE owner_user_id = ? GROUP BY shop_id";


    /**
     * 查找所有商品
     *
     * @return
     */
    public List<GoodsCar> findAllGoods() {
        String ownerUserId = LoginedUser.getLoginedUser().getId();
        return bpQuery(SQL_FIND_ALL_GOODS_BY_USERID,
                new String[] {ownerUserId}, new MMultiRowMapper());
    }

    /**
     * 查找所有店铺
     *
     * @return
     */
    public List<CarShop> findAllShopList() {
        String ownerUserId = LoginedUser.getLoginedUser().getId();
        return bpQuery(SQL_FIND_SHOP_BY_USERID, new String[] {ownerUserId},
                new MultiRowMapper<CarShop>() {
                    @Override
                    public CarShop mapRow(Cursor cursor, int arg1)
                            throws SQLException {
                        CarShop cs = new CarShop();
                        cs.setId(cursor.getString(cursor
                                .getColumnIndex("shop_id")));
                        cs.setName(cursor.getString(cursor
                                .getColumnIndex("shop_name")));
                        return cs;
                    }

                });
    }


    /**
     * 根据shopId找出该店铺下的所有商品
     *
     * @param shopId
     * @return
     */
    public List<GoodsCar> findGoodListByShopId(String shopId) {
        if (Validators.isEmpty(shopId)) {
            return new ArrayList<GoodsCar>();
        }

        String ownerUserId = LoginedUser.getLoginedUser().getId();
        return bpQuery(SQL_FIND_BY_SHOPID_AND_USERID, new String[] {shopId , ownerUserId}, new MMultiRowMapper());
    }


    /**根据status找出已经勾选的物品
     *
     * @return
     */
    public List<GoodsCar> findGoodListByStatus() {

        String ownerUserId = LoginedUser.getLoginedUser().getId();
        return bpQuery(SQL_FIND_BY_STATUS_AND_USERID, new String[] {String.valueOf(SelectEnum.SELECTED.getValue()) , ownerUserId}, new MMultiRowMapper());
    }

    /**
     * 插入商品
     *
     * @param gc
     */
    public void replace(GoodsCar gc) {
        if (null == gc) {
            return;
        }
        String ownerUserId = LoginedUser.getLoginedUser().getId();
        bpUpdate(
                SQL_REPLACE,
                new Object[] { gc.getShopId() , gc.getGoodId() ,gc.getShopName() ,gc.getGoodName()
                        ,gc.getGoodIcon() ,gc.getGoodCount() , gc.getGoodType(),gc.getGoodPrice() ,gc.getGoodBottole(),gc.getGoodUpPrice(),gc.getStatus(),gc.getGoodMarketPrice(),ownerUserId});

    }

    /**
     *  根据商品id查找商品信息
     *
     * @param goodId
     * @return
     */
    public GoodsCar findByGoodId(String goodId) {
        if ( Validators.isEmpty(goodId)) {
            return null;
        }
        String ownerUserId = LoginedUser.getLoginedUser().getId();
        return bpQuery(SQL_FIND_BY_GOODID_AND_USERID,
                new String[] { goodId , ownerUserId}, new MSingleRowMapper());
    }
    /**
     * 批量插入商品
     *
     * @param goodList
     */
    public void insertBatch(List<GoodsCar> goodList) {
        if (Validators.isEmpty(goodList)) {
            return;
        }

        String ownerUserId = LoginedUser.getLoginedUser().getId();
        List<Object[]> insertList = new ArrayList<Object[]>();
        for (GoodsCar gc : goodList) {
            insertList.add(new Object[] { gc.getShopId() , gc.getGoodId() ,gc.getShopName() ,gc.getGoodName()
            ,gc.getGoodIcon() ,gc.getGoodCount() , gc.getGoodType(),gc.getGoodPrice() , gc.getGoodBottole() , gc.getGoodUpPrice(), gc.getStatus() , gc.getGoodMarketPrice() , ownerUserId});
        }

        bpUpdateBatch(SQL_REPLACE, insertList);

    }

    /**
     * 根据商品id删除商品
     *
     * @param goodId
     */
    public void deleteById(String goodId) {
        if (Validators.isEmpty(goodId)) {
            return;
        }
        String ownerUserId = LoginedUser.getLoginedUser().getId();
        bpUpdate(SQL_DELETE_BY_ID_AND_USERID, new String[]{goodId , ownerUserId});
    }

    /**
     * 多条结果集
     *
     */
    private class MMultiRowMapper implements MultiRowMapper<GoodsCar> {
        @Override
        public GoodsCar mapRow(Cursor cursor, int n) throws SQLException {
            GoodsCar gc = new GoodsCar();
            gc.setShopId(cursor.getString(cursor.getColumnIndex("shop_id")));
            gc.setGoodId(cursor.getString(cursor.getColumnIndex("good_id")));
            gc.setShopName(cursor.getString(cursor.getColumnIndex("shop_name")));
            gc.setGoodName(cursor.getString(cursor.getColumnIndex("good_name")));
            gc.setGoodIcon(cursor.getString(cursor.getColumnIndex("good_icon")));
            gc.setGoodCount(cursor.getString(cursor.getColumnIndex("good_count")));
            gc.setGoodType(cursor.getString(cursor.getColumnIndex("good_type")));
            gc.setGoodPrice(cursor.getString(cursor.getColumnIndex("good_price")));
            gc.setGoodBottole(cursor.getString(cursor.getColumnIndex("good_per_box")));
            gc.setGoodUpPrice(cursor.getString(cursor.getColumnIndex("good_up_price")));
            gc.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
            gc.setGoodMarketPrice(cursor.getString(cursor.getColumnIndex("good_market_price")));
            return gc;
        }
    }

    /**
     * 单条结果集
     *
     */
    private class MSingleRowMapper implements SingleRowMapper<GoodsCar> {
        @Override
        public GoodsCar mapRow(Cursor cursor) throws SQLException {
            return new MMultiRowMapper().mapRow(cursor, 0);
        }
    }

}
