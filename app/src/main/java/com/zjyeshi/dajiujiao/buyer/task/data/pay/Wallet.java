package com.zjyeshi.dajiujiao.buyer.task.data.pay;

import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

/**
 * Created by wuhk on 2016/6/30.
 */
public class Wallet extends BaseData<Wallet> {
    private String account;
    private String marketCost;


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getMarketCost() {
        return marketCost;
    }

    public void setMarketCost(String marketCost) {
        if (Validators.isEmpty(marketCost)){
            this.marketCost = "0";
        }else{
            this.marketCost = marketCost;
        }
    }
}
