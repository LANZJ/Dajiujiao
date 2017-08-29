package com.zjyeshi.dajiujiao.buyer.task.work.data;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 * 待我审批
 *
 * Created by zhum on 2016/6/23.
 */
public class WaitReviewListData extends BaseData<WaitReviewListData> {
    private List<WaitReview> list;

    public List<WaitReview> getList() {
        return list;
    }

    public void setList(List<WaitReview> list) {
        this.list = list;
    }
}
