package com.zjyeshi.dajiujiao.buyer.task.data.my;

import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 * 优惠券
 * Created by wuhk on 2016/9/27.
 */
public class CouponListData extends BaseData<CouponListData>{

    public static final int ALL_CUSTOMER_USE = 1;
    public static final int BIND_CUSTOMER_USE = 2;

    private List<Coupon> list;

    public List<Coupon> getList() {
        return list;
    }

    public void setList(List<Coupon> list) {
        this.list = list;
    }

    public static class Coupon{
        private String id;
        private int type;//优惠类型
        private String fulfilMoney;//使用满额度
        private String minusMoney;//减免金额
        private String disCount;//折扣
        private String startTime;//时间期限
        private String endTime;
        private int payType;//支付方式优惠类型，1、线上支付，2、线下支付
        private boolean status;//店铺是否设置优惠券
        private int applicationType;//优惠类型， 1、全部用户 2、 绑定用户
        private int environmentType;//应用环境类型，1、全场 ，2、店铺 3、商品
        private String environmentName;//应用环境名称
        private boolean whetherUse;//是否可用

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getFulfilMoney() {
            return fulfilMoney;
        }

        public void setFulfilMoney(String fulfilMoney) {
            this.fulfilMoney = fulfilMoney;
        }

        public String getMinusMoney() {
            return minusMoney;
        }

        public void setMinusMoney(String minusMoney) {
            this.minusMoney = minusMoney;
        }

        public String getDisCount() {
            return disCount;
        }

        public void setDisCount(String disCount) {
            this.disCount = disCount;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public int getApplicationType() {
            return applicationType;
        }

        public void setApplicationType(int applicationType) {
            this.applicationType = applicationType;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            if (!Validators.isEmpty(startTime) && !startTime.equals("未设置")){
                this.startTime = startTime.substring(0 , 4) + "-" + startTime.substring(4 , 6) + "-" + startTime.substring(6 , 8);
            }else{
                this.startTime = startTime;
            }
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            if (!Validators.isEmpty(endTime) && !startTime.equals("未设置")){
                this.endTime = endTime.substring(0 , 4) + "-" + endTime.substring(4 , 6) + "-" + endTime.substring(6 , 8);
            }else{
                this.endTime = endTime;
            }
        }

        public int getPayType() {
            return payType;
        }

        public void setPayType(int payType) {
            this.payType = payType;
        }

        public int getEnvironmentType() {
            return environmentType;
        }

        public void setEnvironmentType(int environmentType) {
            this.environmentType = environmentType;
        }

        public String getEnvironmentName() {
            return environmentName;
        }

        public void setEnvironmentName(String environmentName) {
            this.environmentName = environmentName;
        }

        public boolean isWhetherUse() {
            return whetherUse;
        }

        public void setWhetherUse(boolean whetherUse) {
            this.whetherUse = whetherUse;
        }
    }
}
