package com.zjyeshi.dajiujiao.buyer.task.sales.data;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 * 优惠活动列表
 * Created by wuhk on 2017/4/26.
 */

public class SalesListData extends BaseData<SalesListData> {
    private List<Sales> list;

    public List<Sales> getList() {
        return list;
    }

    public void setList(List<Sales> list) {
        this.list = list;
    }

    public static class Sales{
        private String id;
        private String activityId;
        private int satisfyType;
        private int favouredType;
        private int formType;
        private String satisfyCondition;
        private String favouredPolicy;
        private String startTime;
        private String endTime;
        private String giftName;
        private String giveProductName;
        private Boolean superposition;

        public void  setactivityId(String activityId){
            this.activityId=activityId;
        }
       public String getActivityId(){
           return activityId;
       }

        public Boolean getSuperposition(){
            return superposition;
        }
        public void setSuperposition(Boolean superposition){
            this.superposition=superposition;
        }
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getSatisfyType() {
            return satisfyType;
        }

        public void setSatisfyType(int satisfyType) {
            this.satisfyType = satisfyType;
        }

        public int getFavouredType() {
            return favouredType;
        }

        public void setFavouredType(int favouredType) {
            this.favouredType = favouredType;
        }

        public int getFormType() {
            return formType;
        }

        public void setFormType(int formType) {
            this.formType = formType;
        }

        public String getSatisfyCondition() {
            return satisfyCondition;
        }

        public void setSatisfyCondition(String satisfyCondition) {
            this.satisfyCondition = satisfyCondition;
        }

        public String getFavouredPolicy() {
            return favouredPolicy;
        }

        public void setFavouredPolicy(String favouredPolicy) {
            this.favouredPolicy = favouredPolicy;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getGiftName() {
            return giftName;
        }

        public void setGiftName(String giftName) {
            this.giftName = giftName;
        }

        public String getGiveProductName() {
            return giveProductName;
        }

        public void setGiveProductName(String giveProductName) {
            this.giveProductName = giveProductName;
        }
    }
}

