package com.zjyeshi.dajiujiao.buyer.utils;

import android.content.Context;
import android.view.View;

import com.xuan.bigdog.lib.dialog.DGAlertDialog;
import com.xuan.bigdog.lib.dialog.DGConfirmDialog;
import com.xuan.bigdog.lib.dialog.DGPromptDialog;
import com.zjyeshi.dajiujiao.buyer.entity.enums.work.LeaveTypeEnum;
import com.zjyeshi.dajiujiao.buyer.entity.my.Address;
import com.zjyeshi.dajiujiao.buyer.task.data.store.goods.Product;
import com.zjyeshi.dajiujiao.buyer.widgets.dialog.AddressDialog;
import com.zjyeshi.dajiujiao.buyer.widgets.dialog.CustomerDialog;
import com.zjyeshi.dajiujiao.buyer.widgets.dialog.LeaveTypeDialog;
import com.zjyeshi.dajiujiao.buyer.widgets.dialog.SelProductDialog;

import java.util.List;

/**
 * 对话框
 * Created by wuhk on 2015/12/8.
 */
public class DialogUtil {

    /**
     * 提示对话框
     *
     * @param context
     * @param message
     * @param left
     * @param leftListener
     * @param right
     * @param rightListener
     */
    public static void confirm(final Context context, String message, String left,
                                View.OnClickListener leftListener, String right, View.OnClickListener rightListener) {
        new DGConfirmDialog.Builder(context).setTitle("提示")
                .setMessage(message).setLeftBtnText(left)
                .setOnLeftBtnListener(leftListener).setRightBtnText(right).setOnRightBtnListener(rightListener).create().show();
    }

    public static void confirmSure(final Context context, String message, String right, View.OnClickListener rightListener) {
        new DGConfirmDialog.Builder(context).setTitle("提示")
                .setMessage(message).setLeftBtnText("取消")
                .setOnLeftBtnListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).setRightBtnText(right).setOnRightBtnListener(rightListener).create().show();
    }

    /**
     * 提示对话框
     *
     * @param context
     */
    public static void alert(final Context context) {
        new DGAlertDialog.Builder(context).setTitle("提示").setMessage("您的账号在别处登录，请重新登录")
                .setbuttonText("确定").setOnButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExtraUtil.logoutAndUnBindPush(context);
            }
        }).create().show();
    }

    /**
     * 编辑框dialog
     *
     * @param context
     * @param title
     * @param message
     * @param leftText
     * @param rightText
     * @param leftListener
     * @param rightListener
     */
    public static void editDialog(final Context context, String title, String message
            , String leftText, String rightText, DGPromptDialog.PromptDialogListener leftListener, DGPromptDialog.PromptDialogListener rightListener) {
        new DGPromptDialog.Builder(context).setTitle(title).setMessage(message)
                .setLeftBtnText(leftText).setOnLeftBtnListener(leftListener)
                .setRightBtnText(rightText).setOnRightBtnListener(rightListener).create().show();
    }


    /**
     * 打卡成功
     *
     * @param context
     * @param l
     * @param n
     */
    public static void normalClock(final Context context, String des, View.OnClickListener l, View.OnClickListener n) {
        CustomerDialog customerDialog = new CustomerDialog(context, true, des, l, n);
        customerDialog.show();
    }

    /**
     * 打卡失败
     *
     * @param context
     * @param l       确认打卡
     * @param n       报告
     */
    public static void lateClock(final Context context, String des, View.OnClickListener l, View.OnClickListener n) {
        CustomerDialog customerDialog = new CustomerDialog(context, false, des, l, n);
        customerDialog.show();
    }

    /**
     * 选择请假类型
     *
     * @param context
     * @param itemClickListener
     */
    public static void selectLeaveType(final Context context, LeaveTypeDialog.ItemClickListener itemClickListener , List<String> dataList) {
        LeaveTypeDialog leaveTypeDialog = new LeaveTypeDialog(context, itemClickListener , dataList);
        leaveTypeDialog.show();
    }

    /**
     * 选择地址
     *
     * @param context
     * @param itemClickListener
     */
    public static void selectAddressDialog(final Context context, List<Address> dataList, AddressDialog.ItemClickListener itemClickListener) {
        AddressDialog addressDialog = new AddressDialog(context, dataList, itemClickListener);
        addressDialog.show();
    }

    /**
     * 选择地址
     *
     * @param context
     * @param itemClickListener
     */
    public static void selectProductDialog(final Context context, List<Product> dataList, SelProductDialog.ItemClickListener itemClickListener) {
        SelProductDialog selProductDialog = new SelProductDialog(context, dataList, itemClickListener);
        selProductDialog.show();
    }
}
