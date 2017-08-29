package com.zjyeshi.dajiujiao.buyer.task.account.data;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 * 提现账户列表
 * Created by wuhk on 2016/7/4.
 */
public class CashAccountList extends BaseData<CashAccountList> {
    private List<CashAccount> list;

    public List<CashAccount> getList() {
        return list;
    }

    public void setList(List<CashAccount> list) {
        this.list = list;
    }

    public static class CashAccount{
        private int type;
        private Alipay alipay;
        private Bank bank;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public Alipay getAlipay() {
            return alipay;
        }

        public void setAlipay(Alipay alipay) {
            this.alipay = alipay;
        }

        public Bank getBank() {
            return bank;
        }

        public void setBank(Bank bank) {
            this.bank = bank;
        }

        public static class Alipay{
            private String id;
            private String accountName;
            private String name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getAccountName() {
                return accountName;
            }

            public void setAccountName(String accountName) {
                this.accountName = accountName;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }


        public static class Bank{
            private String id;
            private String carNumber;
            private String bankName;
            private String name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getCarNumber() {
                return carNumber;
            }

            public void setCarNumber(String carNumber) {
                this.carNumber = carNumber;
            }

            public String getBankName() {
                return bankName;
            }

            public void setBankName(String bankName) {
                this.bankName = bankName;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }


    }

}
