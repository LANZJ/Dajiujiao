package com.zjyeshi.dajiujiao.buyer.task.work.data;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 * 打卡记录
 * Created by wuhk on 2016/6/20.
 */
public class TimeCardRecordListData extends BaseData<TimeCardRecordListData> {
    private String leaveDays;//请假天数
    private String notPunchDays;//未打卡天数
    private String punchDays;//打卡天数
    private String lateDays;//迟到天数
    private String leaveEarlyDays;//早退天数
    private List<CardRecord> list;

    public String getLeaveDays() {
        return leaveDays;
    }

    public void setLeaveDays(String leaveDays) {
        this.leaveDays = leaveDays;
    }

    public String getNotPunchDays() {
        return notPunchDays;
    }

    public void setNotPunchDays(String notPunchDays) {
        this.notPunchDays = notPunchDays;
    }

    public String getLateDays() {
        return lateDays;
    }

    public void setLateDays(String lateDays) {
        this.lateDays = lateDays;
    }

    public String getLeaveEarlyDays() {
        return leaveEarlyDays;
    }

    public void setLeaveEarlyDays(String leaveEarlyDays) {
        this.leaveEarlyDays = leaveEarlyDays;
    }

    public String getPunchDays() {
        return punchDays;
    }

    public void setPunchDays(String punchDays) {
        this.punchDays = punchDays;
    }

    public List<CardRecord> getList() {
        return list;
    }

    public void setList(List<CardRecord> list) {
        this.list = list;
    }

    public static class CardRecord{
        private String id;
        private String address;
        private String pics;
        private long creationTime;
        private int type;
        private int clockProperty;
        private String LngE5;
        private String latE5;
        private String remark;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public long getCreationTime() {
            return creationTime;
        }

        public void setCreationTime(long creationTime) {
            this.creationTime = creationTime;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getClockProperty() {
            return clockProperty;
        }

        public void setClockProperty(int clockProperty) {
            this.clockProperty = clockProperty;
        }

        public String getLngE5() {
            return LngE5;
        }

        public void setLngE5(String lngE5) {
            LngE5 = lngE5;
        }

        public String getLatE5() {
            return latE5;
        }

        public void setLatE5(String latE5) {
            this.latE5 = latE5;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getPics() {
            return pics;
        }

        public void setPics(String pics) {
            this.pics = pics;
        }
    }
}
