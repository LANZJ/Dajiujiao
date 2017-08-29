package com.zjyeshi.dajiujiao.buyer.task.account.data;

import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

/**
 * 提现记录明细
 * Created by wuhk on 2016/7/4.
 */
public class CashLogInfo extends BaseData<CashLogInfo>{
    private String id;
    private String passportName;
    private String amount;
    private long creationTime;
    private String cashNumber;
    private int cashStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassportName() {
        return passportName;
    }

    public void setPassportName(String passportName) {
        this.passportName = passportName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public String getCashNumber() {
        return cashNumber;
    }

    public void setCashNumber(String cashNumber) {
        if (Validators.isEmpty(cashNumber)){
            this.cashNumber = "";
        }else{
            this.cashNumber = cashNumber;
        }
    }

    public int getCashStatus() {
        return cashStatus;
    }

    public void setCashStatus(int cashStatus) {
        this.cashStatus = cashStatus;
    }
}
