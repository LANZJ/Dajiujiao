package com.zjyeshi.dajiujiao.buyer.entity;

import com.alibaba.fastjson.JSON;
import com.zjyeshi.dajiujiao.buyer.common.PreferenceConstants;
import com.zjyeshi.dajiujiao.buyer.entity.enums.MarketCostEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.UserEnum;
import com.zjyeshi.dajiujiao.buyer.receiver.ToLoginReceiver;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.utils.LogUtil;
import com.xuan.bigapple.lib.utils.sharepreference.BPPreferences;

/**
 * 登录成功的用户信息
 *
 * @author wuhk
 */
public class LoginedUser {
    private static LoginedUser loginedUser;

    public static final String FIRST_LOGIN = "user_first_login";
    public static final String ALREADY_LOGIN = "user_already_login";

    private boolean isLogined;// 表示是否处于登录状态
    private String phone;// 手机号
    private String password;// 密码

    private String token;// 登陆后返回的token
    private String id; // 用户ID
    private String name;//用户名
    private String pic;// 头像地址
    private String circleBackgroundPic;//朋友圈背景图
    private String jru;//是否为内勤


    private UserEnum userEnum;// 角色
    private int shopType;//业务员所属店铺类型
    private String shopName;//点名
    private String shopId;//业务员所属shopId;
    private boolean leaveAuth;//请假审批权限
    private boolean moneyAuth;//费用申请审批权限
    private boolean bxAuth;//报销审批权限
    private boolean deliverAuth;//确认发货权限
    private boolean maxLeavel;//最高级别
    private MarketCostEnum marketCostEnum;//市场支持费用类型；

    public static boolean checkLogined(){
        if(LoginedUser.getLoginedUser().isLogined()){
            return true;
        }else{
            //通知需要到登录界面
            ToLoginReceiver.notifyReceiver();
            ToastUtil.toast("请先登录");
            return false;
        }
    }

    /**
     * 获取登录用户信息
     *
     * @return LoginedUser 对象永不为空
     */
    public static LoginedUser getLoginedUser() {
        if (null == loginedUser) {
            // activity因系统内存不足被系统回收时 可能不存在已登录用户 ，需要恢复
            loginedUser = getLastLoginedUserInfo();
            LoginedUser.loginedUser = loginedUser;
        }
        return loginedUser;
    }

    /**
     * 设置登录用户信息 ， 保存到本地
     *
     * @param user
     */
    public static void setLoginedUser(LoginedUser user) {
        loginedUser = user;
        saveToFile();
    }

    /**
     * 同步一次内存的数据到本地
     */
    public static void saveToFile() {
        loginedUser.setLastLoginedUserInfo(loginedUser);
    }

    /**
     * 获取最近一次登录的用户信息 （用JSON的格式保存在文件中）
     *
     * @return
     */
    public static LoginedUser getLastLoginedUserInfo() {
        String temp = BPPreferences.instance().getString(PreferenceConstants.LAST_LOGIN_USER_INFO, "{}");
        return JSON.parseObject(temp, LoginedUser.class);
    }

    /**
     * 保存最近一次用户登录的信息
     *
     * @param logineduser
     */
    public static void setLastLoginedUserInfo(
            LoginedUser logineduser) {
        BPPreferences.instance().putString(PreferenceConstants.LAST_LOGIN_USER_INFO, JSON.toJSONString(logineduser));
        LoginedUser.loginedUser = logineduser;
    }

    public boolean isLogined() {
        return isLogined;
    }

    public void setLogined(boolean isLogined) {
        //boolean类型fastjson必须要这种set格式哦
        this.isLogined = isLogined;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void  setjur(String jru){
        this.jru=jru;
    }
    public String getjur(){
        return jru;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public UserEnum getUserEnum() {
        return userEnum;
    }

    public void setUserEnum(UserEnum userEnum) {
        this.userEnum = userEnum;
    }

    public int getShopType() {
        return shopType;
    }

    public void setShopType(int shopType) {
        this.shopType = shopType;
    }

    public boolean isLeaveAuth() {
        return leaveAuth;
    }

    public void setLeaveAuth(boolean leaveAuth) {
        this.leaveAuth = leaveAuth;
    }

    public boolean isMoneyAuth() {
        return moneyAuth;
    }

    public void setMoneyAuth(boolean moneyAuth) {
        this.moneyAuth = moneyAuth;
    }

    public boolean isBxAuth() {
        return bxAuth;
    }

    public void setBxAuth(boolean bxAuth) {
        this.bxAuth = bxAuth;
    }

    public boolean isMaxLeavel() {
        return maxLeavel;
    }

    public void setMaxLeavel(boolean maxLeavel) {
        this.maxLeavel = maxLeavel;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getCircleBackgroundPic() {
        return circleBackgroundPic;
    }

    public void setCircleBackgroundPic(String circleBackgroundPic) {
        this.circleBackgroundPic = circleBackgroundPic;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public boolean isDeliverAuth() {
        return deliverAuth;
    }

    public void setDeliverAuth(boolean deliverAuth) {
        this.deliverAuth = deliverAuth;
    }

    public MarketCostEnum getMarketCostEnum() {
        return marketCostEnum;
    }

    public void setMarketCostEnum(MarketCostEnum marketCostEnum) {
        this.marketCostEnum = marketCostEnum;
    }

    /**
     * 打印用户信息
     */
    public void displayLog() {
        LogUtil.debug(JSON.toJSONString(this));
    }

}
