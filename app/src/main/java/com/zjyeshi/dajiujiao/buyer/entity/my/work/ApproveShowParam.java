package com.zjyeshi.dajiujiao.buyer.entity.my.work;

/**
 * Created by wuhk on 2016/9/20.
 */
public class ApproveShowParam {
    /**
     * 我自己进入的列表待审批的
     * 显示两个操作，提醒和评论
     *
     */
    public static final String SHOW_REMIND_AND_COMMENT = "show_remind_and_show";

    /**
     * 已经审批过的
     * 只显示评论
     *
     */
    public static final String SHOW_ONLY_COMMENT = "show_only_comment";

    /**
     * Boos待审批的
     * 显示四个操作
     * 通过，拒绝，下一级，评论
     */
    public static final String SHOW_ALL = "show_all";
}
