package com.zjyeshi.dajiujiao.buyer.utils;

import com.xuan.bigapple.lib.utils.ToastUtils;
import com.xuan.bigapple.lib.utils.Validators;

/**
 * toast
 *
 * Created by xuan on 15/9/16.
 */
public class ToastUtil {
    public static void toast(String message){
        if(Validators.isEmpty(message)){
            return;
        }

        ToastUtils.displayTextShort(message);
    }
}
