package com.jopool.crow.imlib.entity;

import com.jopool.crow.imlib.utils.CWLogUtil;
import com.jopool.crow.imlib.utils.CWPreferences;

import org.json.JSONObject;

/**
 * 用户信息
 *
 * @author xuan
 */
public class CWUser {
    public static final String PREFERE_KEY_DGUSER = "prefere.key.dguser";
    private static CWUser user;

    private static final String KEY_TOKEN = "token";
    private static final String KEY_USERID = "userId";
    private static final String KEY_NAME = "name";
    private static final String KEY_URL = "url";

    private String token;
    private String userId;
    private String name;
    private String url;

    public static String getConnectUserId() {
        CWUser user = getUser();
        if (null != user) {
            return user.getUserId();
        } else {
            return "";
        }
    }

    /**
     * 获取登录用户信息
     *
     * @return LoginedUser 对象永不为空
     */
    public static CWUser getUser() {
        if (null == user) {
            // activity因系统内存不足被系统回收时 可能不存在已登录用户 ，需要恢复
            user = getUserFromFile();
        }
        return user;
    }

    /**
     * 设置登录用户信息 ， 保存到本地
     *
     * @param user
     */
    public static void setUser(CWUser user) {
        CWUser.user = user;
        saveUserToFile(user);
    }

    /**
     * 从文件中读取
     *
     * @return
     */
    public static CWUser getUserFromFile() {
        CWPreferences preferences = CWPreferences.instance();
        String temp = (String) preferences.getSystemProperties(
                PREFERE_KEY_DGUSER, "{}", CWPreferences.Types.STRING);

        CWUser user = new CWUser();
        try {
            JSONObject obj = new JSONObject(temp);
            user.setName(obj.optString(KEY_NAME));
            user.setToken(obj.optString(KEY_TOKEN));
            user.setUrl(obj.optString(KEY_URL));
            user.setUserId(obj.optString(KEY_USERID));
        } catch (Exception e) {
            CWLogUtil.e(e);
        }
        return user;
    }

    /**
     * 保存到文件
     *
     * @param user
     */
    public static void saveUserToFile(CWUser user) {
        CWPreferences preferences = CWPreferences.instance();
        JSONObject obj = new JSONObject();
        try {
            obj.put(KEY_TOKEN, user.getToken());
            obj.put(KEY_USERID, user.getUserId());
            obj.put(KEY_NAME, user.getName());
            obj.put(KEY_URL, user.getUrl());

            preferences.saveSystemProperties(PREFERE_KEY_DGUSER,
                    obj.toString(), CWPreferences.Types.STRING);
        } catch (Exception e) {
            CWLogUtil.e(e);
        }
        CWUser.user = user;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
