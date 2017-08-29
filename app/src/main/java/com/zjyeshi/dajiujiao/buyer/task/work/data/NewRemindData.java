package com.zjyeshi.dajiujiao.buyer.task.work.data;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 * 考勤等新消息变动提醒
 * Created by wuhk on 2016/7/8.
 */
public class NewRemindData extends BaseData<NewRemindData> {
    private boolean leaveStatusChange;//请假记录是否右变动
    private int leaveUnAuditCount;//待审批数量
    private int leaveChangeCount;//请假状态变动数量
    private boolean feeApplyChange;//费用申请状态有变动
    private int feeApplyChangeCount;//费用申请状态变动数量
    private boolean feeReimbursement;//费用报销状态是否有变动
    private int feeReimbursementCount;//费用报销状态变动数量
    private int feeUnAuditCount;//费用申请报销待审批的数量
    private int rebackPipeliningCount;//退单待处理数量
    private int friendApplyCount;//好友申请数量
    private int orderWaitingForReceived;//订单待收货数量
    private int orderCount;//销售订单数量
    private int unreadDailyCommentCount;
    private List<UnReadDaily> unreadDailyList;//未读日报

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

    public int getFeeUnAuditCount() {
        return feeUnAuditCount;
    }

    public void setFeeUnAuditCount(int feeUnAuditCount) {
        this.feeUnAuditCount = feeUnAuditCount;
    }

    public int getRebackPipeliningCount() {
        return rebackPipeliningCount;
    }

    public void setRebackPipeliningCount(int rebackPipeliningCount) {
        this.rebackPipeliningCount = rebackPipeliningCount;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
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

    public List<UnReadDaily> getUnreadDailyList() {
        return unreadDailyList;
    }

    public void setUnreadDailyList(List<UnReadDaily> unreadDailyList) {
        this.unreadDailyList = unreadDailyList;
    }

    public int getOrderWaitingForReceived() {
        return orderWaitingForReceived;
    }

    public void setOrderWaitingForReceived(int orderWaitingForReceived) {
        this.orderWaitingForReceived = orderWaitingForReceived;
    }

    public int getFriendApplyCount() {
        return friendApplyCount;
    }

    public void setFriendApplyCount(int friendApplyCount) {
        this.friendApplyCount = friendApplyCount;
    }

    public int getUnreadDailyCommentCount() {
        return unreadDailyCommentCount;
    }

    public void setUnreadDailyCommentCount(int unreadDailyCommentCount) {
        this.unreadDailyCommentCount = unreadDailyCommentCount;
    }

    public static class UnReadDaily{
        private String memberId;
        private int unreadCount;

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public int getUnreadCount() {
            return unreadCount;
        }

        public void setUnreadCount(int unreadCount) {
            this.unreadCount = unreadCount;
        }
    }
}
