package com.zjyeshi.dajiujiao.buyer.utils;

import com.xuan.bigapple.lib.utils.Validators;

/**
 * Created by wuhk on 2016/7/1.
 */
public abstract class NumberUtil {
    public static float toFloat(String floatStr) {
        if (Validators.isEmpty(floatStr)) {
            return 0;
        }
        try {
            return Float.parseFloat(floatStr);
        }catch (Exception e){
            return 0;
        }
    }
}
