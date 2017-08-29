package com.zjyeshi.dajiujiao.buyer.entity;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.utils.sharepreference.BPPreferences;
import com.zjyeshi.dajiujiao.buyer.task.work.data.NewRemindData;

import java.util.List;

/**
 * 新提醒
 * Created by wuhk on 2016/7/8.
 */
public class NewRemind {
    private static NewRemind newRemind;
    private boolean leaveStatusChange;//请假记录是否右变动
    private int leaveUnAuditCount;//待审批数量
    private int leaveChangeCount;//请假状态变动数量
    private boolean feeApplyChange;//费用申请状态有变动
    private int feeApplyChangeCount;//费用申请状态变动数量
    private boolean feeReimbursement;//费用报销状态是否有变动
    private int feeReimbursementCount;//费用报销状态变动数量
    private int feeUnAuditCount;//费用申请报销待审批的数量
    private int friendApplyCount;//好友申请数量
    private int rebackPipeliningCount;//退单待处理数量
    private int orderWaitingForReceived;//订单待收货数量
    private int orderCount;//销售订单数量
    private int unreadDailyCommentCount;//业务员日报未读评论数量
    private List<NewRemindData.UnReadDaily> unReadDailyList;
    /**
     * 获取提醒信息
     *
     * @return LoginedUser 对象永不为空
     */
    public static NewRemind getNewRemind() {
        if (null == newRemind) {
            // activity因系统内存不足被系统回收时 可能不存在已登录用户 ，需要恢复
            newRemind = getLastNewRemind();
        }
        return newRemind;
    }

    /** 设置提醒信息 ， 保存到本地
     *
     * @param newRemind
     */
    public static void setNewRemind(NewRemind newRemind) {
        NewRemind.newRemind = newRemind;
        saveToFile();
    }

    /**
     * 同步一次内存的数据到本地
     */
    public static void saveToFile() {
        setLastNewRemind(newRemind);
    }
    /**
     * 获取最近一次提醒的信息 （用JSON的格式保存在文件中）
     *
     * @return
     */
    public static NewRemind getLastNewRemind() {
        String temp = BPPreferences.instance().getString("new_remind", "{}");
        return JSON.parseObject(temp, NewRemind.class);
    }


    /**保存最近一次提醒的信息
     *
     * @param newRemind
     */
    public static void setLastNewRemind(NewRemind newRemind) {
        BPPreferences.instance().putString("new_remind", JSON.toJSONString(newRemind));
        NewRemind.newRemind = newRemind;
    }

    public boolean isLeaveStatusChange() {
        return leaveStatusChange;
    }

    public void setLeaveStatusChange(boolean leaveStatusChange) {
        this.leaveStatusChange = leaveStatusChange;
    }

    public boolean isFeeApplyChange() {
        return feeApplyChange;
    }

    public void setFeeApplyChange(boolean feeApplyChange) {
        this.feeApplyChange = feeApplyChange;
    }

    public boolean isFeeReimbursement() {
        return feeReimbursement;
    }

    public void setFeeReimbursement(boolean feeReimbursement) {
        this.feeReimbursement = feeReimbursement;
    }

    public int getLeaveUnAuditCount() {
        return leaveUnAuditCount;
    }

    public void setLeaveUnAuditCount(int leaveUnAuditCount) {
        this.leaveUnAuditCount = leaveUnAuditCount;
    }

    public int getLeaveChangeCount() {
        return leaveChangeCount;
    }

    public void setLeaveChangeCount(int leaveChangeCount) {
        this.leaveChangeCount = leaveChangeCount;
    }

    public int getFeeApplyChangeCount() {
        return feeApplyChangeCount;
    }

    public void setFeeApplyChangeCount(int feeApplyChangeCount) {
        this.feeApplyChangeCount = feeApplyChangeCount;
    }

    public int getFeeReimbursementCount() {
        return feeReimbursementCount;
    }

    public void setFeeReimbursementCount(int feeReimbursementCount) {
        this.feeReimbursementCount = feeReimbursementCount;
    }

    public int getFeeUnAuditCount() {
        return feeUnAuditCount;
    }

    public void setFeeUnAuditCount(int feeUnAuditCount) {
        this.feeUnAuditCount = feeUnAuditCount;
    }

    public List<NewRemindData.UnReadDaily> getUnReadDailyList() {
        return unReadDailyList;
    }

    public void setUnReadDailyList(List<NewRemindData.UnReadDaily> unReadDailyList) {
        this.unReadDailyList = unReadDailyList;
    }

    public int getOrderWaitingForReceived() {
        return orderWaitingForReceived;
    }

    public void setOrderWaitingForReceived(int orderWaitingForReceived) {
        this.orderWaitingForReceived = orderWaitingForReceived;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public int getFriendApplyCount() {
        return friendApplyCount;
    }

    public void setFriendApplyCount(int friendApplyCount) {
        this.friendApplyCount = friendApplyCount;
    }

    public int getRebackPipeliningCount() {
        return rebackPipeliningCount;
    }

    public void setRebackPipeliningCount(int rebackPipeliningCount) {
        this.rebackPipeliningCount = rebackPipeliningCount;
    }

    public int getUnreadDailyCommentCount() {
        return unreadDailyCommentCount;
    }

    public void setUnreadDailyCommentCount(int unreadDailyCommentCount) {
        this.unreadDailyCommentCount = unreadDailyCommentCount;
    }
}
