package com.jopool.crow.imlib.utils;

import com.alibaba.fastjson.JSON;
import com.jopool.crow.CWChatConfig;
import com.jopool.crow.imlib.entity.CWUser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 设置工具类
 *
 * Created by xuan on 15/11/27.
 */
public class CWSettingUtil {
    private static final String CW_RECEIVE_MESSAGE_RING = "CW_RECEIVE_MESSAGE_RING";
    private static final String CW_RECEIVE_MESSAGE_VIBRATE = "CW_RECEIVE_MESSAGE_VIBRATE";

    /**
     * 判断收到消息是否响铃
     *
     * @return
     */
    public static boolean isReceiveMessageRing() {
        return CWPreferences.instance().getBoolean(CW_RECEIVE_MESSAGE_RING, false);
    }

    /**
     * 设置收到消息响铃
     */
    public static void openReceiveMessageRing() {
        CWPreferences.instance().putBoolean(CW_RECEIVE_MESSAGE_RING, true);
    }

    /**
     * 设置收到消息不响铃
     */
    public static void closeReceiveMessageRing() {
        CWPreferences.instance().putBoolean(CW_RECEIVE_MESSAGE_RING, false);
    }

    /**
     * 判断收到消息是否振动
     *
     * @return
     */
    public static boolean isReceiveMessageVibrate() {
        return CWPreferences.instance().getBoolean(CW_RECEIVE_MESSAGE_VIBRATE, false);
    }

    /**
     * 设置收到消息振动
     */
    public static void openReceiveMessageVibrate() {
        CWPreferences.instance().putBoolean(CW_RECEIVE_MESSAGE_VIBRATE, true);
    }

    /**
     * 设置收到消息不振动
     */
    public static void closeReceiveMessageVibrate() {
        CWPreferences.instance().putBoolean(CW_RECEIVE_MESSAGE_VIBRATE, false);
    }

    /**
     * 把toId加入到免打扰名单中去
     *
     * @param toId
     */
    public synchronized static void addToIdToUnDisturbList(String toId) {
        String ownerUserId = CWUser.getConnectUserId();
        if(CWValidator.isEmpty(toId) || CWValidator.isEmpty(ownerUserId)){
            CWLogUtil.e("toId|ownerUserId不能为空");
            return;
        }

        String notDisturbStr = CWPreferences.instance().getString(CWChatConfig.CW_PREFERENCES_NOT_DISTURB_LIST, "{}");
        DoNotDisturb notDisturb = JSON.parseObject(notDisturbStr, DoNotDisturb.class);
        if (CWValidator.isEmpty(notDisturb.getList())) {
            notDisturb.setList(new ArrayList<String>());
        }

        notDisturb.getList().add(ownerUserId + "," + toId);
        CWPreferences.instance().putString(CWChatConfig.CW_PREFERENCES_NOT_DISTURB_LIST, JSON.toJSONString(notDisturb));
    }

    /**
     * 把toId移除免打扰名单
     *
     * @param toId
     */
    public synchronized static void removeToIdFromUnDisturbList(String toId){
        String ownerUserId = CWUser.getConnectUserId();
        if(CWValidator.isEmpty(toId) || CWValidator.isEmpty(ownerUserId)){
            CWLogUtil.e("toId|ownerUserId不能为空");
            return;
        }

        String notDisturbStr = CWPreferences.instance().getString(CWChatConfig.CW_PREFERENCES_NOT_DISTURB_LIST, "{}");
        DoNotDisturb notDisturb = JSON.parseObject(notDisturbStr, DoNotDisturb.class);
        if (CWValidator.isEmpty(notDisturb.getList())) {
            return;
        }

        Iterator<String> it = notDisturb.getList().iterator();
        while (it.hasNext()) {
            String temp = it.next();
            if(toId.equals(temp)){
                //移除当前的对象
                it.remove();
                break;
            }
        }

        CWPreferences.instance().putString(CWChatConfig.CW_PREFERENCES_NOT_DISTURB_LIST, JSON.toJSONString(notDisturb));
    }

    /**
     * 判断toId是否在免打扰名单中
     *
     * @param toId
     * @return
     */
    public synchronized static boolean isToIdInUnDisturbList(String toId){
        String ownerUserId = CWUser.getConnectUserId();
        if(CWValidator.isEmpty(toId) || CWValidator.isEmpty(ownerUserId)){
            CWLogUtil.e("toId|ownerUserId不能为空");
            return false;
        }

        String notDisturbStr = CWPreferences.instance().getString(CWChatConfig.CW_PREFERENCES_NOT_DISTURB_LIST, "{}");
        if (notDisturbStr.equals("{}")){
            return false;
        }else {
            DoNotDisturb notDisturb = JSON.parseObject(notDisturbStr, DoNotDisturb.class);
            if (CWValidator.isEmpty(notDisturb.getList())) {
                return false;
            }

            Iterator<String> it = notDisturb.getList().iterator();
            while (it.hasNext()) {
                String temp = it.next();
                if(toId.equals(temp)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 免打扰名单
     */
    public static class DoNotDisturb {
        private List<String> list;

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }
    }

}
