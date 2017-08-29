package com.xuan.bigappleui.lib.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;

import com.xuan.bigappleui.lib.dialog.core.BUAlertDialog;
import com.xuan.bigappleui.lib.dialog.core.BUConfirmDialog;
import com.xuan.bigappleui.lib.dialog.core.BUProgressDialog;
import com.xuan.bigappleui.lib.dialog.core.BUProgressDialogSmall;
import com.xuan.bigappleui.lib.dialog.core.BUPromptDialog;
import com.xuan.bigappleui.lib.dialog.core.BUSingleSelectDialog;

import java.util.List;

/**
 * 这一层面的封装说方便用户使用,并且屏蔽底层实现
 *
 * Created by xuan on 16/9/14.
 */
public class BUDialog {
    public static BUDialog instance;

    public static BUDialog getInstance(){
        if(null == instance){
            instance = new BUDialog();
        }

        return instance;
    }

    //----------------------------alert----------------------------
    public BUAlertDialog alert(Context context, String title, String message, String btnText, final View.OnClickListener l) {
        BUAlertDialog alert = new BUAlertDialog.Builder(
                context).setTitle(title)
                .setMessage(message).setbuttonText(btnText)
                .setOnButtonListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (null != l) {
                            l.onClick(view);
                        }
                    }
                }).create();
        alert.show();
        return alert;
    }

    public BUAlertDialog alert(Context context, String message) {
        return alert(context, "温馨提示", message, "确认", null);
    }

    //----------------------------confirm----------------------------
    public BUConfirmDialog confirm(Context context, String title, String message, String leftBtnText, final View.OnClickListener leftL, String rightBtnText, final View.OnClickListener rightL) {
        BUConfirmDialog confirm = new BUConfirmDialog.Builder(
                context)
                .setTitle(title)
                .setMessage(message)
                .setLeftBtnText(leftBtnText)
                .setOnLeftBtnListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (null != leftL) {
                            leftL.onClick(view);
                        }
                    }
                }).setRightBtnText(rightBtnText)
                .setOnRightBtnListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (null != rightL) {
                            rightL.onClick(view);
                        }
                    }
                }).create();
        confirm.show();
        return confirm;
    }

    public BUConfirmDialog confirm(Context context, String message, final View.OnClickListener rightL) {
        return confirm(context, "温馨提示", message, "取消", null, "确定", rightL);
    }

    //----------------------------prompt----------------------------
    public BUPromptDialog prompt(Context context, String title, String message, String leftBtnText, final BUPromptDialog.PromptDialogListener leftL, String rightBtnText, final BUPromptDialog.PromptDialogListener rightL) {
        BUPromptDialog prompt = new BUPromptDialog.Builder(
                context)
                .setTitle(title)
                .setMessage(message)
                .setLeftBtnText(leftBtnText)
                .setOnLeftBtnListener(new BUPromptDialog.PromptDialogListener() {
                    @Override
                    public void onClick(View view, String inputText) {
                        if (null != leftL) {
                            leftL.onClick(view, inputText);
                        }
                    }
                }).setRightBtnText(rightBtnText)
                .setOnRightBtnListener(new BUPromptDialog.PromptDialogListener() {
                    @Override
                    public void onClick(View view, String inputText) {
                        if (null != rightL) {
                            rightL.onClick(view, inputText);
                        }
                    }
                }).create();
        prompt.show();
        return prompt;
    }

    public BUPromptDialog prompt(Context context, String message, BUPromptDialog.PromptDialogListener leftL) {
        return prompt(context, "温馨提示", message, "取消", null, "确定", leftL);
    }

    //----------------------------select----------------------------
    public BUSingleSelectDialog select(Context context, List<BUSingleSelectDialog.Item> itemList, BUSingleSelectDialog.ItemOnClickListener l){
        BUSingleSelectDialog select = new BUSingleSelectDialog.Builder(context).setItems(itemList).setItemOnClickListener(l).create();
        select.show();
        return select;
    }

    //----------------------------progress----------------------------
    public ProgressDialog getProgressDialogFull(Context context, String  message){
        return BUProgressDialog.createInstance(context, message);
    }

    public ProgressDialog getProgressDialogFull(Context context){
        return BUProgressDialog.createInstance(context);
    }

    public ProgressDialog getProgressDialogSmall(Context context){
        return BUProgressDialogSmall.createInstance(context);
    }

}
