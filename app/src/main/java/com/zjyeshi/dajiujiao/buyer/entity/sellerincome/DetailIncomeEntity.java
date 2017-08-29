package com.zjyeshi.dajiujiao.buyer.entity.sellerincome;

import com.zjyeshi.dajiujiao.buyer.circle.itementity.BaseEntity;
import com.zjyeshi.dajiujiao.buyer.task.data.seller.IncomeData;

import java.util.List;

/**
 * 我的收入列表
 *
 * Created by wuhk on 2015/11/6.
 */
public class DetailIncomeEntity extends BaseEntity {
    private String time;
    private List<IncomeData.Income> detailList;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<IncomeData.Income> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<IncomeData.Income> detailList) {
        this.detailList = detailList;
    }
}
