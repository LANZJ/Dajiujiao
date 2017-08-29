package com.zjyeshi.dajiujiao.buyer.dao;

import com.zjyeshi.dajiujiao.buyer.circle.db.CircleDao;
import com.zjyeshi.dajiujiao.buyer.circle.db.UserCircleDao;

/**
 * Dao生产工厂类
 *
 * Created by wuhk on 2015/10/29.
 */
public class DaoFactory {
    private static final GoodsCarDao goodsCarDao = new GoodsCarDao();
    private static final MarketGoodsCarDao marketGoodsCarDao = new MarketGoodsCarDao();
    private static final CircleDao circleDao = new CircleDao();
    private static final AddressUserDao addressUserDao = new AddressUserDao();
    private static final AddressDao addressDao = new AddressDao();
    private static final AreaDao areaDao = new AreaDao();
    private static final ShopsDao shopsDao = new ShopsDao();
    private static final CustomerDao customerDao = new CustomerDao();
    private static final UnLoginShopDao unLoginShopDao = new UnLoginShopDao();
    private static final MyWineDao myWineDao = new MyWineDao();
    private static final ContactDao contactDao = new ContactDao();
    private static final UserCircleDao userCircleDao = new UserCircleDao();
    private static final PhoneContactDao phoneContactDao = new PhoneContactDao();
    private static final CodeAreaDao codeAreaDao = new CodeAreaDao();

    public static GoodsCarDao getGoodsCarDao() {
        return goodsCarDao;
    }

    public static MarketGoodsCarDao getMarketGoodsCarDao() {
        return marketGoodsCarDao;
    }

    public static CircleDao getCircleDao() {
        return circleDao;
    }

    public static AddressUserDao getAddressUserDao() {
        return addressUserDao;
    }

    public static AddressDao getAddressDao() {
        return addressDao;
    }

    public static AreaDao getAreaDao() {
        return areaDao;
    }

    public static ShopsDao getShopsDao() {
        return shopsDao;
    }

    public static CustomerDao getCustomerDao() {
        return customerDao;
    }

    public static UnLoginShopDao getUnLoginShopDao() {
        return unLoginShopDao;
    }

    public static MyWineDao getMyWineDao() {
        return myWineDao;
    }

    public static ContactDao getContactDao() {
        return contactDao;
    }

    public static UserCircleDao getUserCircleDao() {
        return userCircleDao;
    }

    public static PhoneContactDao getPhoneContactDao() {
        return phoneContactDao;
    }

    public static CodeAreaDao getCodeAreaDao() {
        return codeAreaDao;
    }
}
