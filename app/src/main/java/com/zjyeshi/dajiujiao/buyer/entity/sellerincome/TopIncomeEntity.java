package com.zjyeshi.dajiujiao.buyer.entity.sellerincome;

import com.zjyeshi.dajiujiao.buyer.circle.itementity.BaseEntity;

/**
 * 卖家我的收入头部布局
 *
 * Created by wuhk on 2015/11/6.
 */
public class TopIncomeEntity extends BaseEntity {
    private String time;
    private String amount;
    private String income;
    private String out;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getOut() {
        return out;
    }

    public void setOut(String out) {
        this.out = out;
    }
}
