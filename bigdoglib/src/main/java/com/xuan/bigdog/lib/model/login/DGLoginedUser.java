package com.xuan.bigdog.lib.model.login;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigapple.lib.utils.sharepreference.BPPreferences;
import com.xuan.bigdog.lib.utils.DGLogUtil;

import org.json.JSONObject;

/**
 * 登录成功后的用户
 *
 * Created by xuan on 16/3/7.
 */
public class DGLoginedUser<T> {
    private static DGLoginedUser instance;

    private String userName;//手机号｜用户名｜邮箱等
    private String password;//密码
    private String ext;//扩展信息
    private boolean logined;//是否登录

    private T userInfo;//业务用户数据

    /**
     * 保存到文件
     */
    public void saveToFile(){
        //基本登录信息保存
        try{
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("userName", userName);
            jsonObj.put("password", password);
            jsonObj.put("ext", ext);
            jsonObj.put("logined", logined);
            String jsonStr = jsonObj.toString();
            DGLogUtil.d("登录基本信息保存:" + jsonStr);
            BPPreferences.instance().putString("DG.DGLoginedUser.KEY", jsonStr);
        }catch (Exception e){
            DGLogUtil.e(e);
        }

        //用户信息保存
        String userInfoStr = "{}";
        if(null != userInfo){
            userInfoStr = JSON.toJSONString(userInfo);
        }
        DGLogUtil.d("登录用户信息保存:" + userInfoStr);
        BPPreferences.instance().putString("DG.DGLoginedUser.INFO.KEY", userInfoStr);
        DGLoginedUser.instance = this;
    }

    /**
     * 从文件中获取
     *
     * @return
     */
    public static DGLoginedUser getFromFile(Class clazz) {
        String loginUserStr = BPPreferences.instance().getString("DG.DGLoginedUser.KEY", "{}");
        String userInfoStr = BPPreferences.instance().getString("DG.DGLoginedUser.INFO.KEY", "{}");
        DGLogUtil.d("登录基本信息获取:" + loginUserStr);
        DGLogUtil.d("登录用户信息获取:" + userInfoStr);

        if(Validators.isEmpty(loginUserStr)){
            loginUserStr = "{}";
        }
        if(Validators.isEmpty(userInfoStr)){
            userInfoStr = "{}";
        }

        DGLoginedUser loginedUser = JSON.parseObject(loginUserStr, DGLoginedUser.class);
        loginedUser.setUserInfo(JSON.parseObject(userInfoStr, clazz));
        return loginedUser;
    }

    /**
     * 获取登录后的用户
     *
     * @return
     */
    public static DGLoginedUser getLoginedUser(Class clazz){
        if (null == instance) {
            // activity因系统内存不足被系统回收时 可能不存在已登录用户 ，需要恢复
            DGLoginedUser.instance = DGLoginedUser.getFromFile(clazz);
        }
        return DGLoginedUser.instance;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public boolean isLogined() {
        return logined;
    }

    public void setLogined(boolean logined) {
        this.logined = logined;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public T getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(T userInfo) {
        this.userInfo = userInfo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
